package es.riberadeltajo.practica4miguelmanzanillaocana;

import android.graphics.Bitmap;

import java.util.BitSet;

public class producto {
    public String Nombre;
    public String Descripcion;
    public Bitmap Imagen;
    public float Precio;
    public int Cantidad;

    public String getNombre() {
        return Nombre;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        this.Cantidad = cantidad;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Bitmap getImagen() {
        return Imagen;
    }

    public void setImagen(Bitmap imagen) {
        Imagen = imagen;
    }

    public float getPrecio() {
        return Precio;
    }

    public void setPrecio(float precio) {
        Precio = precio;
    }

    public producto( String nombre, String descripcion, Bitmap imagen, float precio,int cantidad) {
        Nombre = nombre;
        Descripcion = descripcion;
        Imagen = imagen;
        Precio = precio;
        Cantidad = cantidad;
    }

    public producto() {
    }
}
