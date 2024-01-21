package es.riberadeltajo.practica4miguelmanzanillaocana.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import es.riberadeltajo.practica4miguelmanzanillaocana.Arrays;
import es.riberadeltajo.practica4miguelmanzanillaocana.MainActivity;
import es.riberadeltajo.practica4miguelmanzanillaocana.databinding.FragmentHomeBinding;
import es.riberadeltajo.practica4miguelmanzanillaocana.producto;
import es.riberadeltajo.practica4miguelmanzanillaocana.ui.gallery.GalleryFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        if (Arrays.db != null) {
            Cursor c = Arrays.db.rawQuery("SELECT * FROM Misproductos", null);
            MainActivity.rellenarRecycler(c);
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.edLista.
                        getText().toString().equals("")) {
                    String nombre = binding.edLista.getText().toString();
                    Cursor c = Arrays.db.rawQuery("SELECT COALESCE(MAX(id),0)+1 FROM misListas", null);
                    if (c.getCount() == 0)
                        Log.d("Error", "No hay registros");
                    else {
                        while (c.moveToNext()) {
                            Toast.makeText(getContext(), "Lista Creada", Toast.LENGTH_SHORT).show();
                            Arrays.db.execSQL("INSERT INTO misListas (Nombre)VALUES('" + nombre + "');");
                            Arrays.rellenarListas();
                            for (producto p : Arrays.MisProductos) {
                                int cant = p.getCantidad();
                                if (cant > 0) {
                                    Arrays.db.execSQL("INSERT INTO listas_productos VALUES('" + p.getNombre() + "','" + c.getInt(0) + "','" + p.getCantidad() + "');");
                                    Arrays.db.execSQL("Update Misproductos set CantidadTotal=CantidadTotal+" + p.getCantidad() + " where  Nombre='" + p.getNombre() + "'");
                                }
                            }
                        }
                    }
                    c.close();
                } else {
                    Toast.makeText(getContext(), "TE FALTA EL NOMBRE", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}