package es.riberadeltajo.practica4miguelmanzanillaocana;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Arrays {
    public static ArrayList<producto> MisProductos = new ArrayList<producto>();
    public static ArrayList<lista> MisListas = new ArrayList<lista>();
    public static MyproductoRecyclerViewAdapter MiAdaptador;
    public static MyListaRecyclerViewAdapter MiAdaptadorLista;
    public static SQLiteDatabase db;
    public static MyListaCompRecyclerViewAdapter MiAdaptadorListaComp;
    public static String listaCompra = "Lista de compra:\n";

    public static ArrayList<Contacto> misContactos = new ArrayList<>();
    public static MyContactoRecyclerViewAdapter adaptador;


    public static Bitmap GenerarImagen(String img) throws IOException {
        URL url = new URL("https://fp.cloud.riberadeltajo.es/listacompra/images/" + img);
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setReadTimeout(10000); // Le damos un segundo para leer los datos-> aborta
        conexion.setConnectTimeout(5000); //le damos un segundo para conectar
        conexion.setRequestMethod("GET");
        conexion.setDoInput(true);
        conexion.connect();
        Bitmap b = null;
        if (conexion.getResponseCode() == 200) {
            b = BitmapFactory.decodeStream(conexion.getInputStream());
        }
        return b;
    }
    public static byte[] convertirBitmapABytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    public static void rellenarListas() {
        Arrays.MisListas.clear();
        Cursor c= Arrays.db.rawQuery("SELECT * FROM misListas", null);
        if(c.getCount()==0)
            Log.d("Error","No hay registros");
        else{
            while(c.moveToNext()) {
                Arrays.MisListas.add(new lista(c.getInt(0),c.getString(1)));
            }
            if (Arrays.MiAdaptadorLista != null) {
                Arrays.MiAdaptadorLista.notifyDataSetChanged();
            } else {
                Log.d("Error", "Adapter is null");
            }
        }
        c.close();
    }

}
