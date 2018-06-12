/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication116;

/**
 *
 * @author josue
 */
public class ArbolB {
    
    public int orden;
    public NodoB raiz;

    public ArbolB() {

    }

    public ArbolB(int order) {
        this.raiz = new NodoB(order, true);
        this.orden = order;
    }

    public void insert(int key) {
        NodoB r = raiz;
        if(r.getPaginasLlenas()==2*orden-1){
            NodoB s = new NodoB(orden,false);
            raiz = s;
            s.hijo[0] = r;
            s.split(0, r);
            s.insertNonFull(key);
        }else{
            r.insertNonFull(key);
        }
    }

    //muestra el arbol al usuario
    public void mostrarArbol(NodoB nodo, String cadena, char caracter) {
        for (int i = 0; i < nodo.getPaginasLlenas(); i++) {
            System.out.print(nodo.pagina[i] + " ");
        }
        if (!nodo.esHoja()) {
            cadena += caracter;
            for (int i = 0; i <= nodo.getPaginasLlenas(); i++) {
                if (nodo.getChildAt(i) != null) {
                    System.out.println("");
                    System.out.print(cadena);
                    System.out.print(i + 1 + ")");
                    mostrarArbol(nodo.hijo[i], cadena, caracter);
                }

            }
        }
    }
    
}
