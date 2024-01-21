package es.riberadeltajo.practica4miguelmanzanillaocana;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.riberadeltajo.practica4miguelmanzanillaocana.databinding.FragmentListaBinding;

import java.util.ArrayList;
import java.util.List;


public class MyListaRecyclerViewAdapter extends RecyclerView.Adapter<MyListaRecyclerViewAdapter.ViewHolder> {

    private final List<lista> misListas;

    public MyListaRecyclerViewAdapter(List<lista> items) {
        misListas = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentListaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.miLista = misListas.get(position);
        holder.IdLista.setText((misListas.get(position).idLista) + "");
        holder.NomLista.setText(misListas.get(position).nombre);
    }

    @Override
    public int getItemCount() {
        return misListas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView IdLista;
        public final TextView NomLista;
        public lista miLista;
        public final Button btnver;


        public ViewHolder(FragmentListaBinding binding) {
            super(binding.getRoot());
            IdLista = binding.idLista;
            NomLista = binding.NombreLista;
            btnver = binding.btnVer;

            btnver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf((Integer.parseInt((String) IdLista.getText()) - 1));
                    System.out.println(id);
                    Context context = v.getContext();
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View vista = inflater.inflate(R.layout.dialog_ver_prod, null);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("PRODUCTOS DE LA LISTA: " + NomLista.getText());
                    dialog.setView(R.layout.dialog_ver_prod);
                    RecyclerView r = vista.findViewById(R.id.recycler);
                    dialog.setPositiveButton("CERRAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton(" GUARDAR CAMBIOS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Arrays.db.execSQL();
                            Toast.makeText(context.getApplicationContext(), "Guardado Correctamente", Toast.LENGTH_SHORT).show();
                            guardarCambios(id);
                            dialog.dismiss();
                        }
                    });
                    rellenarRecyclerConCantidades(id);
                    MyproductoRecyclerViewAdapter mp = new MyproductoRecyclerViewAdapter(Arrays.MisProductos);
                    r.setLayoutManager(new LinearLayoutManager(v.getContext()));
                    r.setAdapter(mp);
                    dialog.setView(vista);
                    AlertDialog dialogMostrar = dialog.create();
                    dialogMostrar.show();
                }

            });

        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "IdLista=" + IdLista +
                    ", Nombre_Lista=" + NomLista +
                    ", miLista=" + miLista +
                    '}';
        }
    }

    private void guardarCambios(String id) {
        int idConver=Integer.parseInt(id)+1;
        Cursor c = Arrays.db.rawQuery("SELECT p.Nombre,lp.Cantidad FROM misproductos p JOIN listas_productos lp ON lp.NombreProducto=p.Nombre WHERE lp.idListas="+idConver,null);
        while (c.moveToNext()){
            for (producto p: Arrays.MisProductos) {
                if (p.getNombre().equals(c.getString(0)) && p.getCantidad()!=(Integer.parseInt(c.getString(1)))){
                    Arrays.db.execSQL("Update listas_productos set Cantidad="+ p.getCantidad() + " where  NombreProducto='" + p.getNombre() + "' and idListas="+idConver);
                    if (p.getCantidad()>Integer.parseInt(c.getString(1))){
                        Arrays.db.execSQL("Update Misproductos set CantidadTotal=CantidadTotal+" + p.getCantidad() + " where  Nombre='" + p.getNombre() + "'");
                    }else{
                        int resta=p.getCantidad()-Integer.parseInt(c.getString(1));
                        Arrays.db.execSQL("Update Misproductos set CantidadTotal=CantidadTotal" + resta + " where  Nombre='" + p.getNombre() + "'");
                    }

                }
            }
        }
    }
    public static void rellenarRecyclerConCantidades(String id) {
        int idConver=Integer.parseInt(id)+1;
        Arrays.MisProductos.clear();
        Cursor c = Arrays.db.rawQuery("SELECT p.Nombre,p.Descripcion,p.Imagen,p.Precio,lp.Cantidad FROM misproductos p JOIN listas_productos lp ON lp.NombreProducto=p.Nombre WHERE lp.idListas="+idConver,null);
        while (c.moveToNext()) {
            byte[] byteArray = c.getBlob(2);
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            float precio = Float.parseFloat(c.getString(3));
            producto p = new producto(c.getString(0), c.getString(1), bm, precio, Integer.parseInt(c.getString(4)));
            Arrays.MisProductos.add(p);
        }
        Arrays.MiAdaptador.notifyDataSetChanged();
    }
}