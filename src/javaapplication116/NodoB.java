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
public class NodoB {
    
    public int orden;
    public int[] pagina;
    public int paginasLlenas;
    public NodoB[] hijo;
    public boolean esHoja;

    public NodoB() {

    }

    public NodoB(int order, boolean leaf) {
        this.orden = order;
        pagina = new int[2 * order - 1];
        hijo = new NodoB[2 * order];
        esHoja = leaf;
        paginasLlenas = 0;
    }

    public int get(int index) {
        try {
            return pagina[index];
        } catch (Exception e) {
            System.out.println("¡Error, indice invalido!");
            return 0;
        }
    }

    public NodoB getChildAt(int index) {
        try {
            return hijo[index];
        } catch (Exception e) {
            System.out.println("¡Error, indice invalido!");
            return null;
        }
    }

    public int getOrden() {
        return orden;
    }

    public int getPaginasLlenas() {
        return paginasLlenas;
    }

    public boolean esHoja() {
        return esHoja;
    }

    public void split(int i, NodoB y) {
        NodoB z = new NodoB(y.orden, y.esHoja);
        z.paginasLlenas = orden - 1;
        //copia la mitad de la derecha
        for (int j = 0; j < orden - 1; j++) {
            z.pagina[j] = y.pagina[j + orden];
        }
        if (y.esHoja() == false) {
            //si no es hoja, reasignamos nodos hijos
            for (int j = 0; j < orden; j++) {
                z.hijo[j] = y.hijo[j + orden];
            }
            for (int j = orden; j < y.paginasLlenas; j++) {
                y.hijo[j] = null;
            }
        }
        for (int j = orden; j < y.paginasLlenas; j++) {
            y.pagina[j] = 0;
        }
        y.paginasLlenas = orden - 1;
        for (int j = this.getPaginasLlenas(); j >= i + 1; j--) {
            //cambia los hijos de x
            this.hijo[j + 1] = this.hijo[j];
        }
        hijo[i + 1] = z;
        for (int j = this.getPaginasLlenas() - 1; j >= i; j--) {
            //cambia las llaves de x
            this.pagina[j + 1] = this.pagina[j];
        }
        //empuja el valor a la raiz
        this.pagina[i] = y.pagina[orden - 1];
        y.pagina[orden - 1] = 0;
        this.paginasLlenas++;
    }

    public void insertNonFull(int key) {
        int i = this.getPaginasLlenas() - 1;
        if (this.esHoja() == true) {
            //correr todo hacia la derecha
            while (i >= 0 && key < this.pagina[i]) {
                this.pagina[i + 1] = this.pagina[i];
                i--;
            }
            this.pagina[i + 1] = key;
            this.paginasLlenas++;
        } else {
            //encontrar hijo donde "key" pertenece
            int j = i;
            while (j >= 0 && key < this.pagina[j]) {
                j--;
            }
            if (this.hijo[j + 1].paginasLlenas == orden * 2 - 1) {
                //esta lleno, tiene que hacer split
                split(j + 1, this.hijo[j + 1]);
                if (key > this.pagina[j + 1]) {
                    j++;
                }
            }
            hijo[j + 1].insertNonFull(key);
        }
    }

    public NodoB search(int k) {
        int i = 0;
        while (i < this.paginasLlenas && k > pagina[i]) {
            i++;
        }
        if (i < this.paginasLlenas && pagina[i] == k) {
            return this;
        }
        if (esHoja == true) {
            return null;
        }
        return hijo[i].search(k);
    }
    
}
