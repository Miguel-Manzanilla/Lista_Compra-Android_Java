package es.riberadeltajo.practica4miguelmanzanillaocana;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.ByteArrayInputStream;

import es.riberadeltajo.practica4miguelmanzanillaocana.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISO_CONTACTOS = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    static Handler handler;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mercadona);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Arrays.adaptador=new MyContactoRecyclerViewAdapter(Arrays.misContactos,getApplicationContext());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_nueva_lista, R.id.nav_consultar_listas, R.id.nav_compartir, R.id.nav_crear)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        inicializarBD();
        Arrays.rellenarListas();
        Cursor c = Arrays.db.rawQuery("SELECT * FROM Misproductos order by CantidadTotal desc", null);
        if (c.getCount() == 0) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            handler = new Handler(Looper.getMainLooper());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    realizaConexion();
                }
            });
        } else {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            handler = new Handler(Looper.getMainLooper());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    rellenarRecycler(c);
                }
            });
        }
        if (checkSelfPermission("android.permission.READ_CONTACTS") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, PERMISO_CONTACTOS);

        } else {
            cargaContactos("");
        }
    }

    public static void rellenarRecycler(Cursor c) {
        Arrays.MisProductos.clear();
        while (c.moveToNext()) {
            byte[] byteArray = c.getBlob(2);
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            float precio = Float.parseFloat(c.getString(3));
            producto p = new producto(c.getString(0), c.getString(1), bm, precio, 0 );
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Arrays.MisProductos.add(p);
                    Arrays.MiAdaptador.notifyDataSetChanged();
                }
            });
        }
        c.close();
    }




    private void realizaConexion() {
        ConnectivityManager conManager = (ConnectivityManager)
                getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                //Establecer la conexión
                URL url = new URL("https://fp.cloud.riberadeltajo.es/listacompra/listaproductos.json");
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setReadTimeout(10000);
                conexion.setConnectTimeout(5000);
                conexion.setRequestMethod("GET");
                conexion.setDoInput(true);
                conexion.connect();
                if (conexion.getResponseCode() == 200) {
                    String cadena = leer(conexion.getInputStream());
                    byte[] bytes = cadena.getBytes();
                    InputStream is = new ByteArrayInputStream(bytes);
                    Log.d("Ribera del Tajo", "HEMOS RECIBIDO" + cadena);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    String json = new String(buffer, "UTF-8");
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray couchList = jsonObject.getJSONArray("productos");
                    for (int i = 0; i < couchList.length(); i++) {
                        JSONObject jsonCouch = couchList.getJSONObject(i);
                        String img = jsonCouch.getString("imagen");
                        Bitmap photo = Arrays.GenerarImagen(img);
                        String nombre = jsonCouch.getString("nombre");
                        String desc = jsonCouch.getString("descripcion");
                        String precio = jsonCouch.getString("precio");
                        producto p = new producto(nombre, desc, photo, Float.parseFloat(precio), 0);
                        byte[] imageBytes = Arrays.convertirBitmapABytes(photo);
                        String consulta = "INSERT INTO Misproductos VALUES('" + nombre + "','" + desc + "', ? ,'" + precio + "',0);";
                        Arrays.db.execSQL(consulta, new Object[]{imageBytes});
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Arrays.MisProductos.add(p);
                                Arrays.MiAdaptador.notifyDataSetChanged();
                            }
                        });

                    }


                }
                conexion.getInputStream().close();

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public String leer(InputStream is) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return bo.toString();
    }


    private void inicializarBD() {
        Arrays.db = openOrCreateDatabase("MyApplication", MODE_PRIVATE, null);
        Arrays.db.execSQL("CREATE TABLE IF NOT EXISTS Misproductos(" +
                "Nombre VARCHAR(100) PRIMARY KEY," +
                "Descripcion VARCHAR(100)," +
                "Imagen BLOB," +
                "Precio VARCHAR(100)," +
                "CantidadTotal  NUMBER(5));");
        Arrays.db.execSQL("CREATE TABLE IF NOT EXISTS misListas(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre VARCHAR(100)," +
                "Fecha DATE DEFAULT CURRENT_DATE);");
        Arrays.db.execSQL("CREATE TABLE IF NOT EXISTS listas_productos (" +
                "NombreProducto VARCHAR(100)," +
                "idListas NUMBER(5)," +
                "Cantidad VARCHAR(100)," +
                "PRIMARY KEY (NombreProducto, idListas)," +
                "FOREIGN KEY (NombreProducto) REFERENCES Misproductos(Nombre)," +
                "FOREIGN KEY (idListas) REFERENCES misListas(id)" +
                ");");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISO_CONTACTOS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cargaContactos("");
            }
        }
    }

    private void cargaContactos(String texto) {
        Arrays.misContactos.clear();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                String projection[] = {ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER};
                String selection = ContactsContract.Contacts.DISPLAY_NAME + " like ?";
                String selectionArgs[] = {"%" + texto + "%"};

                ContentResolver miCr = getContentResolver();
                Cursor miCursor = miCr.query(ContactsContract.Contacts.CONTENT_URI, projection, selection, selectionArgs, null);

                if (miCursor.getCount() > 0) {
                    while (miCursor.moveToNext()) {
                        String number="";
                        Long id = Long.parseLong(miCursor.getString(0));
                        Cursor phones = miCr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone._ID + " = " + id, null, null);
                        while (phones.moveToNext()) {
                            number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        String nombre = miCursor.getString(1);
                        Arrays.misContactos.add(new Contacto(nombre, number, id));
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Arrays.misContactos.sort(Comparator.comparing(Contacto::getTieneTelefono).reversed());
                        //Trabajo de la interfaz de usuario aquí
                        Arrays.adaptador.notifyDataSetChanged();
                    }
                });
            }
        });


    }
}