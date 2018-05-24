package javaapplication116;

public class campo {
    
    private String tipo;
    private String nombre;
    private int size;
    private boolean llave;

    public campo(String tipo, String nombre, int size, boolean llave) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.size = size;
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

    @Override
    public String toString() {
        return nombre;
    }
    
    public String toStringFile(){
        if (llave) {
            return nombre + ";" + tipo + ";" + size + ";" + "true" + ":";
        }else{
            return nombre + ";" + tipo + ";" + size + ";" + "false" + ":";
        }
    }
    
}
