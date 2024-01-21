package es.riberadeltajo.practica4miguelmanzanillaocana;

public class Contacto {
    String nombre;
    String Telefono;
    Long id;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTieneTelefono() {
        return Telefono;
    }

    public void setTieneTelefono(String tieneTelefono) {
        this.Telefono = tieneTelefono;
    }

    public Contacto(String nombre, String Telefono, Long id) {
        this.nombre = nombre;
        this.Telefono = Telefono;
        this.id = id;
    }
    public Contacto() {

    }
}
