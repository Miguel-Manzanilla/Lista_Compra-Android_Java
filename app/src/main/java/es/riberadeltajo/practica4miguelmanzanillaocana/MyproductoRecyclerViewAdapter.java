package es.riberadeltajo.practica4miguelmanzanillaocana;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import es.riberadeltajo.practica4miguelmanzanillaocana.databinding.FragmentProductoBinding;

import java.util.List;


public class MyproductoRecyclerViewAdapter extends RecyclerView.Adapter<MyproductoRecyclerViewAdapter.ViewHolder> {

    public static List<producto> mValues;

    public MyproductoRecyclerViewAdapter(List<producto> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentProductoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.Nombre.setText(mValues.get(position).Nombre);
        holder.editTextCantidad.setText(mValues.get(position).Cantidad+"");
        holder.Img.setImageBitmap(mValues.get(position).Imagen);
        holder.descripcion.setText(mValues.get(position).Descripcion);
        holder.precio.setText(mValues.get(position).Precio+"€");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView Nombre;
        public final ImageView Img;
        public final EditText editTextCantidad;
        public final TextView precio;
        public final TextView descripcion;
        public producto mItem;

        public ViewHolder(FragmentProductoBinding binding) {
            super(binding.getRoot());
            Img =binding.fotoportada;
            Nombre = binding.nombre;
            editTextCantidad =binding.edCantidad;
            precio=binding.precio;
            descripcion=binding.desc;

            editTextCantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals("")){
                        mValues.get(getAdapterPosition()).setCantidad(Integer.parseInt(s.toString()));
                    }

                }
            });
        }


    }
}