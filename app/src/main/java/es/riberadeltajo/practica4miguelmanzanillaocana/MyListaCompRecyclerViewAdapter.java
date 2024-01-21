package es.riberadeltajo.practica4miguelmanzanillaocana;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.riberadeltajo.practica4miguelmanzanillaocana.databinding.FragmentListacompBinding;
import es.riberadeltajo.practica4miguelmanzanillaocana.ui.slideshow.SlideshowFragment;

import java.util.List;


public class MyListaCompRecyclerViewAdapter extends RecyclerView.Adapter<MyListaCompRecyclerViewAdapter.ViewHolder> {

    private final List<lista> mValues;

    public MyListaCompRecyclerViewAdapter(List<lista> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentListacompBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText((mValues.get(position).idLista)+"");
        holder.mContentView.setText(mValues.get(position).nombre);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public  final Button selec;

        public final TextView text;
        public lista mItem;


        public ViewHolder(FragmentListacompBinding binding) {
            super(binding.getRoot());
            mIdView = binding.idLista;
            mContentView = binding.NombreLista;
            selec=binding.btnVer;
            text=binding.listaSelect;

            selec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (text.getText().equals("LISTA SELECIONADA")){
                        text.setText("NO SELECIONADA");
                        SlideshowFragment.listaDeListas[Integer.parseInt(mIdView.getText().toString())]=0;
                        text.setTextColor(Color.RED);
                        selec.setText("SELECIONAR LISTA");
                        selec.setBackgroundColor(Color.parseColor("#4CAF50"));

                    }else {
                        text.setText("LISTA SELECIONADA");
                        SlideshowFragment.listaDeListas[Integer.parseInt(mIdView.getText().toString())]=Integer.parseInt(mIdView.getText().toString());
                        text.setTextColor(Color.BLACK);
                        selec.setText("CANCELAR");
                        selec.setBackgroundColor(Color.RED);

                    }

                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}