package es.riberadeltajo.practica4miguelmanzanillaocana;

public class lista {
    public int idLista;
    public String nombre;

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public lista(int idLista, String nombre) {
        this.idLista = idLista;
        this.nombre = nombre;
    }

    public lista() {
    }
}
