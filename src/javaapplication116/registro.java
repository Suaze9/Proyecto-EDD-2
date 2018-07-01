package javaapplication116;

import java.util.ArrayList;

public class registro {
    
    public int index;
    public ArrayList<campo> campos;
    public ArrayList objetos;
    public boolean modif = true;

    public registro(int index, ArrayList<campo> campos) {
        this.index = index;
        this.campos = campos;
        this.objetos = new ArrayList();
    }
    
    public registro(int index, ArrayList<campo> campos, ArrayList objetos) {
        this.index = index;
        this.campos = campos;
        this.objetos = objetos;
    }

    @Override
    public String toString() {
        String salida = "";
        for (int i = 0; i < objetos.size(); i++) {
            if (objetos.get(i).getClass().equals(String.class)) {          //String, Char, Integer, Double, Float, Long, Short
                salida += "S" ;
            }else if (objetos.get(i).getClass().equals(Character.class)) {
                salida += "C" ;
            }else if (objetos.get(i).getClass().equals(Integer.class)) {
                salida += "I" ;
            }else if (objetos.get(i).getClass().equals(Double.class)) {
                salida += "D" ;
            }else if (objetos.get(i).getClass().equals(Float.class)) {
                salida += "F" ;
            }else if (objetos.get(i).getClass().equals(Long.class)) {
                salida += "L" ;
            }else if (objetos.get(i).getClass().equals(Short.class)) {
                salida += "s" ;
            }
            salida += objetos.get(i).toString() + "/";
        }
        
        String size = "" + salida.length();
        int j = 4 - size.length();
        for (int i = 0; i < j; i++) {
            size = "0" + size;
        }
        salida = size + salida;
        return salida;
    }
    
    
    
}
