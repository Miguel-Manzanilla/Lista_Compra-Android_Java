package es.riberadeltajo.practica4miguelmanzanillaocana;

public class listas_productos {
    public int idLista;
    public int idProducto;

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public listas_productos(int idLista, int idProducto) {
        this.idLista = idLista;
        this.idProducto = idProducto;
    }

    public listas_productos() {
    }
}
