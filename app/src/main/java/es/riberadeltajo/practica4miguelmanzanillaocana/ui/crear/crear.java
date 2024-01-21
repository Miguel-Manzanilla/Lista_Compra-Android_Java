package es.riberadeltajo.practica4miguelmanzanillaocana.ui.crear;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import es.riberadeltajo.practica4miguelmanzanillaocana.Arrays;
import es.riberadeltajo.practica4miguelmanzanillaocana.MainActivity;
import es.riberadeltajo.practica4miguelmanzanillaocana.R;
import es.riberadeltajo.practica4miguelmanzanillaocana.databinding.FragmentCrearBinding;
import es.riberadeltajo.practica4miguelmanzanillaocana.databinding.FragmentGalleryBinding;
import es.riberadeltajo.practica4miguelmanzanillaocana.producto;

public class crear extends Fragment {

    private CrearViewModel mViewModel;
    private FragmentCrearBinding binding;
    private int captura=1;
    Bitmap foto=null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CrearViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,captura);
            }
        });

        binding.btnCrearArti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre= binding.edNombre.getText().toString();
                String desc=binding.edDesc.getText().toString();
                String precio2=binding.edPrecio.getText().toString();

                if (!nombre.equals("") && !desc.equals("") && !precio2.equals("") && foto != null) {
                    float precio = Float.parseFloat(String.valueOf(binding.edPrecio.getText()));
                    byte[] imageBytes = Arrays.convertirBitmapABytes(foto);
                    String consulta="INSERT INTO Misproductos VALUES('" + nombre + "','" + desc + "', ? ,'" + precio + "',0);";
                    Arrays.db.execSQL(consulta,new Object[]{imageBytes});
                    Cursor c = Arrays.db.rawQuery("SELECT * FROM Misproductos", null);
                    Toast.makeText(getContext(), "PRODUCTO CREADO", Toast.LENGTH_SHORT).show();
                    MainActivity.rellenarRecycler(c);
                } else {
                    Toast.makeText(getContext(), "TE FALTA ALGUN VALOR PARA CREARLO", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==captura){
            foto= (Bitmap) data.getExtras().get("data");
            binding.imgFondo.setImageBitmap(foto);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}