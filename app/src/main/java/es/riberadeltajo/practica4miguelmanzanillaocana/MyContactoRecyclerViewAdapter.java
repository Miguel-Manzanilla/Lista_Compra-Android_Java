package es.riberadeltajo.practica4miguelmanzanillaocana;

import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;

import android.widget.TextView;

import es.riberadeltajo.practica4miguelmanzanillaocana.databinding.FragmentContactoBinding;

import java.util.List;


public class MyContactoRecyclerViewAdapter extends RecyclerView.Adapter<MyContactoRecyclerViewAdapter.ViewHolder> {

    private final List<Contacto> mValues;
    private Context context;

    public MyContactoRecyclerViewAdapter(List<Contacto> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentContactoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).Telefono + "");
        holder.mContentView.setText(mValues.get(position).nombre);

        //holder.mFoto.setImageResource();

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Contacto mItem;
        public final ImageButton btn;

        public ViewHolder(FragmentContactoBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            btn = binding.imageButton2;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String phoneNumber = mContentView.getText().toString().replaceAll("[^0-9]", "");
                    System.out.println(phoneNumber);
                    String message = Arrays.listaCompra; // Puedes personalizar el mensaje
                    String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}