package javaapplication116;

public class campo {
    
    private int size = 0;
    private String tipo;
    private String nombre;
    private boolean llave;

    public campo(String tipo, String nombre, boolean llave) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.llave = llave;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isLlave() {
        return llave;
    }

    public void setLlave(boolean llave) {
        this.llave = llave;
    }

    
    
}
