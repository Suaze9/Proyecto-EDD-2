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

    public ArbolB(int order) {
        this.raiz = new NodoB(order, true);
        this.orden = order;
    }

    public void insert(int key, int regPos) {
        NodoB r = raiz;
        if (r.getPaginasLlenas() == 2 * orden - 1) {
            NodoB s = new NodoB(orden, false);
            raiz = s;
            s.hijo[0] = r;
            s.split(0, r);
            s.insertNonFull(key, regPos);
        } else {
            r.insertNonFull(key, regPos);
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

    public String getArbol(NodoB nodo, String cadena, char caracter, String devolver) {
        for (int i = 0; i < nodo.getPaginasLlenas(); i++) {
            devolver += nodo.pagina[i] + " ";
            //System.out.print(nodo.pagina[i] + " ");
        }
        if (!nodo.esHoja()) {
            cadena += caracter;
            for (int i = 0; i <= nodo.getPaginasLlenas(); i++) {
                if (nodo.getChildAt(i) != null) {
                    //System.out.println("");
                    devolver += '\n';
                    //System.out.print(cadena);
                    devolver+= cadena;
                    //System.out.print(i + 1 + ")");
                    devolver += (i+1)+")";
                    getArbol(nodo.hijo[i], cadena, caracter,devolver);
                }

            }
        }
        return devolver;
    }

    public int buscarPos(int llave) {
        NodoB nodo = buscar(raiz, llave);
        if (nodo != null) {
            System.out.println("Llave = " + llave);
            System.out.println("Paginas Llenas = " + nodo.paginasLlenas);
            for (int i = 0; i < nodo.paginasLlenas; i++) {
                System.out.print(nodo.pagina[i] + " ");
                if (nodo.pagina[i] == llave) {
                    return nodo.posiciones[i];
                }
            }
            System.out.println("");
        }
        return -1;
    }

    public NodoB buscar(NodoB nodo, int llave) {
        int[] llaves = nodo.pagina;
        for (int i = 0; i < nodo.paginasLlenas; i++) {
            if (llave == llaves[i]) {
                //System.out.println("llave = " + llave);
                //System.out.println("llave[" + i + "] = " + llave);
                return nodo;
            }
        }
        if (!nodo.esHoja()) {
            if (llave < llaves[0]) {
                return buscar(nodo.hijo[0], llave);
            }
            for (int i = 1; i < nodo.paginasLlenas; i++) {
                if (llaves[i - 1] < llave && llave < llaves[i]) {
                    return buscar(nodo.hijo[i], llave);
                }
            }
            return buscar(nodo.hijo[nodo.paginasLlenas], llave);
        }
        return null;
    }
    
    //////////////////////////
    //Metodos para borrar/////
    //////////////////////////
    
    public void delete(int key) {
        delete(raiz, key);
    }

    private void delete(NodoB node, int key) {
        //Arrays.sort(node.pagina);
        //System.out.println(Arrays.toString(node.pagina));
        if (node.esHoja) {
            int i;
            if ((i = node.binarySearch(key)) != -1) {
                node.remove(i, 0);
            }
        } else {
            int i;
            if ((i = node.binarySearch(key)) != -1) {
                NodoB hijoIzq = node.hijo[i];
                NodoB hijoDer = node.hijo[i + 1];
                //si el predecesor tiene al menos t llaves
                if (hijoIzq.paginasLlenas >= orden) {
                    NodoB predecessor = hijoIzq;
                    NodoB eraser = predecessor;
                    while (predecessor.esHoja == false && predecessor.hijo[node.paginasLlenas - 1]!=null ) {
                        eraser = predecessor;
                        predecessor = predecessor.hijo[node.paginasLlenas - 1];
                    }
                    node.pagina[i] = predecessor.pagina[predecessor.paginasLlenas - 1];
                    node.posiciones[i] = predecessor.posiciones[predecessor.paginasLlenas-1];
                    delete(eraser, node.pagina[i]);
                } else if (hijoDer.paginasLlenas >= orden) {
                    //si el sucesor tiene al menos t llaves
                    NodoB successor = hijoDer;
                    NodoB eraser = successor;
                    while (successor.esHoja == false) {
                        eraser = successor;
                        successor = successor.hijo[0];
                    }
                    node.pagina[i] = successor.pagina[0];
                    node.posiciones[i] = successor.posiciones[0];
                    delete(eraser, node.pagina[i]);
                } else { //si ninguno cumple las condiciones
                    int median = merge(hijoIzq, hijoDer);
                    moveKey(node, i, 1, hijoIzq, median);
                    delete(hijoIzq, key);
                }
            } else {
                i = node.subTreeRootNodeIndex(key);
                NodoB child = node.hijo[i];
                if (child.paginasLlenas == orden - 1) {
                    NodoB hermanoIzq = (i - 1 >= 0) ? node.hijo[i - 1] : null;
                    NodoB hermanoDer = (i + 1 <= node.paginasLlenas) ? node.hijo[i + 1] : null;
                    if (hermanoIzq != null && hermanoIzq.paginasLlenas >= orden) {
                        child.correrUno();
                        child.pagina[0] = node.pagina[i - 1];
                        child.posiciones[0] = node.posiciones[i-1];
                        if (child.esHoja == false) {
                            child.hijo[0] = hermanoIzq.hijo[hermanoIzq.getPaginasLlenas()];
                        }
                        child.paginasLlenas++;
                        //mover una pagina del hermano de la izquiera al subarbol
                        node.pagina[i - 1] = hermanoIzq.pagina[hermanoIzq.paginasLlenas - 1];
                        node.posiciones[i - 1] = hermanoIzq.posiciones[hermanoIzq.paginasLlenas - 1];
                        //borrar la key junto con el hermano de la derecha
                        hermanoIzq.remove(hermanoIzq.paginasLlenas - 1, 1);
                    } else if (hermanoDer != null && hermanoDer.paginasLlenas >= orden) {
                        child.pagina[child.paginasLlenas] = node.pagina[i];
                        child.posiciones[child.paginasLlenas] = node.posiciones[i];
                        if (child.esHoja == false) {
                            child.hijo[child.paginasLlenas + 1] = hermanoDer.hijo[0];
                        }
                        child.paginasLlenas++;
                        //mover una paginas del hermano de la derecha al subarbol
                        node.pagina[i] = hermanoDer.pagina[0];
                        node.posiciones[i] = hermanoDer.posiciones[0];
                        //borrar la key junto con el hermano de la izq
                        hermanoDer.remove(0, 0);
                    } else {
                        if (hermanoIzq != null) {
                            int median = merge(child, hermanoIzq);
                            moveKey(node, i - 1, 0, child, median);
                        } else if (hermanoDer != null) {
                            int median = merge(child, hermanoDer);
                            moveKey(node, i, 1, child, median);
                        }
                    }
                }
                delete(child, key);
            }
        }
    }

    public int merge(NodoB destiny, NodoB source) {
//        bubbleSort(destiny);
//        bubbleSort(source);
//        bubbleSort(raiz);
        int median = 0;
        if (source.pagina[0] < destiny.pagina[destiny.getPaginasLlenas() - 1]) {
            int i;
            //correr todos los elementos de destiny
            if (destiny.esHoja == false) {
                destiny.hijo[source.paginasLlenas + destiny.paginasLlenas + 1] = destiny.hijo[destiny.paginasLlenas];
            }
            for (i = destiny.paginasLlenas; i > 0; i--) {
                destiny.pagina[source.paginasLlenas + i] = destiny.pagina[i - 1];
                destiny.posiciones[source.paginasLlenas + i] = destiny.posiciones[i - 1];
                if (destiny.esHoja == false) {
                    destiny.hijo[source.paginasLlenas + i] = destiny.hijo[i - 1];
                }
            }
            //borrar el median
            median = source.paginasLlenas;
            destiny.pagina[median] = 0;
            destiny.posiciones[median] = 0;
            //copiar los elementos de source a destiny
            for (i = 0; i < source.paginasLlenas; i++) {
                destiny.pagina[i] = source.pagina[i];
                destiny.posiciones[i] = source.posiciones[i];
                if (source.esHoja == false) {
                    destiny.hijo[i] = source.hijo[i];
                }
            }
            if (source.esHoja == false) {
                destiny.hijo[i] = source.hijo[i];
            }
        } else {
            //borrar el median
            median = destiny.getPaginasLlenas();
            destiny.pagina[median] = 0;
            destiny.posiciones[median] = 0;
            //copiar de source a destiny
            int offset = median + 1;
            int i;
            for (i = 0; i < source.paginasLlenas; i++) {
                destiny.pagina[offset + i] = source.pagina[i];
                destiny.posiciones[offset + i] = source.posiciones[i];
                if (source.esHoja == false) {
                    destiny.hijo[offset + i] = source.hijo[i];
                }
            }
            if (source.esHoja == false) {
                destiny.hijo[offset + i] = source.hijo[i];
            }
        }
        destiny.paginasLlenas += source.paginasLlenas;

        return median;
    }

    public void moveKey(NodoB source, int sourceIndex, int childIndex, NodoB destiny, int median) {
        destiny.pagina[median] = source.pagina[sourceIndex];
        destiny.posiciones[median] = source.posiciones[median];
        destiny.paginasLlenas++;
        source.remove(sourceIndex, childIndex);
        if (source == raiz && source.paginasLlenas == 0) {
            raiz = destiny;
        }
    }

}
