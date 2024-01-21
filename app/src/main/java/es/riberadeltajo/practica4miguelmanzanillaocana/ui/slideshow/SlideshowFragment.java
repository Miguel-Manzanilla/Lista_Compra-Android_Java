package es.riberadeltajo.practica4miguelmanzanillaocana.ui.slideshow;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.riberadeltajo.practica4miguelmanzanillaocana.Arrays;
import es.riberadeltajo.practica4miguelmanzanillaocana.MostrarContactos;
import es.riberadeltajo.practica4miguelmanzanillaocana.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    public static int[] listaDeListas=new int[Arrays.MisListas.size()+5];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    compartirListaCompra();
            }
        });
        Arrays.rellenarListas();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void compartirListaCompra() {
        for (int elemento : SlideshowFragment.listaDeListas) {
            if (elemento!=0){
                Arrays.listaCompra= Arrays.listaCompra+"\t*"+elemento+": \n";
                Cursor c=Arrays.db.rawQuery("SELECT p.Nombre,p.Descripcion,p.Precio,l.Cantidad from Misproductos   p JOIN listas_productos l ON p.Nombre=l.NombreProducto WHERE l.idListas="+(elemento-1), null);
                if(c.getCount()==0)
                    Arrays.listaCompra= Arrays.listaCompra+"\t\t"+" NO TIENE PRODUCTOS  \n";
                else{
                    while(c.moveToNext()){
                        Arrays.listaCompra= Arrays.listaCompra+"\t\t"+c.getString(0)+": "+c.getString(1)+", Cant: "+c.getString(2)+", Precio: "+c.getInt(3)+"€  \n";
                    }
                }
            }
        }
        if (Arrays.listaCompra.equals("Lista de compra:\n")){
            Toast.makeText(getContext(),"SELECCIONA UNA LISTA ",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(getContext(), MostrarContactos.class );
            startActivityForResult(intent,1);
        }

    }
}