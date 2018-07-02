
package javaapplication116;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class archivoCruz {
    File file;
    ArrayList<campo> campos = new ArrayList();
    ArrayList<registro> registros = new ArrayList();

    String[] metaDatos;
    
    int indiceReg;
    int posReg = 0;

    Stack anteriores = new Stack();
    int posAnterior = 502;
    int posActual = 502;
    int posFinal;

    private final int orden = 6;

    ArbolB arbol = new ArbolB(orden);

    final int buffer = 10000;
    int bufferActual = 0;

    public archivoCruz(File file) {
        this.file = file;
    }

    public void cargarArchivo() {                                              //Carga el archivo a memoria
        try {
            RandomAccessFile rac = new RandomAccessFile(file, "rw");            //crea una instancia de RAF. Este solo se utiliza para conseguir el tamaño del archivo
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);                     //Crea una instancia del buffered reader
            br.mark((int) file.length());
            char[] line = new char[500];                                        //Arreglo de caracteres de 500 de largo, para leer el metadata de 500 caracteres
            br.read(line, 0, 500);                                              //lee con el buffered reader el metadata
            String linestr = new String(line);                                  //Asigna a el string linsstr el metadata

            String[] metaDataStr = linestr.split("&");                          //Arreglo de strings con todas las subdivisiones del metadata, "campos & numeroDeIndices & availList"
            String[] camposstr = metaDataStr[0].split(":");                     //Arreglo de strings con todos los campos "campo1 : campos2 : campo3 : ..."

            ArrayList<campo> tempCampos = new ArrayList();                      //arreglo de campos temporal

            for (int i = 0; i < camposstr.length; i++) {                        //Regorre al arreglo de campos en forma de string
                String[] camposCampo = camposstr[i].split(";");                 //Arreglo de los objetos del campo Nombre;tipo;tamaño;llave;
                String nombre = camposCampo[0];                                 //<-Nombre
                String tipo = camposCampo[1];                                   //<-Tipo
                String sz = camposCampo[2];                                     //<-Tamaño
                int largo = Integer.parseInt(sz);
                boolean llave = camposCampo[3].equals("true");                  //<-Llave
                campo tempCampo = new campo(tipo, nombre, largo, llave);        //crea el objeto campo
                tempCampos.add(tempCampo);                                      //Y lo agrega al arreglo temporal de objetos
            }
            campos = tempCampos;                                                //aplica el arreglo de campos temporal al permanente

            indiceReg = Integer.parseInt(metaDataStr[1]);                       //Agrega la cantidad de indices a memoria


            posActual = 502;                                                    //aplica la posicion inicial del registro (502)
            posAnterior = 502;                                                  //aplica la posicion inicial del anterior (502)
            posReg = (int) rac.length();                                        //aplica la posicion final del archivo (largo del archivo)

            cargarRegistrosArbol();                                             //llama el metodo para cargar todos los registros en el arbol
            cargarRegistros();                                                  //llama el metodo para cargar la primera pagina de registros

            rac.close();
            br.close();                                                         //cierra el buffered reader y el RAF
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
    
    public void cargarRegistros() throws FileNotFoundException, IOException {
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);                         //Crea el objeto Buffered Reader
        String tempLine;                                                        //String en el que se cargaran los strings del archivo
        int cont = 0;                                                           //Contador de cuantos registros ha leido
        int pos = posActual;                                                    //Posicion de la que se leera el registro
//        System.out.println(pos);
//        System.out.println(posReg);
        registros = new ArrayList();                                            //Reinicia el arrayList de registros
        br.mark((int) file.length());                                       //marca el inicio del archivo con limite de bytes de movimiento al final del archivo (nunca de acavara esa marca. Revisar la documentacion de BR para mas informacion)
        br.reset();                                                             //Mueve el iterador al inicio del archivo
        while (pos < posReg - 1 && cont < buffer) {                             //Mientras la posicion no se pase de la posicion final del archivo y el contador no se pase de la cantidad de buffer
//            System.out.println("CargarR-" + pos);
            br.skip(pos);                                                       //Reinicia el iterador al inicio del archivo
            ArrayList objetos = new ArrayList();                                                //0000Sstring/Iint/Cchar/Ddouble
            char[] sizeReg = new char[4];                                       //Arreglo de caracteres para leer el tamaño del registro
            br.read(sizeReg);                                                   //lee el tamaño del registro
            int size = 0;                                                       //Parsea el tamaño a entero
            try {
                size = Integer.parseInt(new String(sizeReg));
            } catch (Exception e) {
                pos++;
                br.reset();
                continue;
            }
            char[] registro = new char[size];                                   //Arreglo de caracteres del tamaño del registro
            br.read(registro);                                                  //Lee el registro
            tempLine = new String(registro);                                    //Castea el string del registro
//            System.out.println(tempLine);
            if (tempLine.charAt(0) != '*') {                                    //Revisa que el registro no este eliminado
                cont++;                                                         //Suma uno al contador de los registros leidos
                String[] datos = tempLine.split("/");                           //Arreglo de string de los campos (objetos)
                for (int i = 0; i < datos.length; i++) {                        //Recorre todos los arreglos campos
                    switch (datos[i].charAt(0)) {                               //Mira el primer caracter de el campo para determinar el tipo
                        case 'S':                                               //Si es 'S' es un string
                            //String, Char, Integer, Double, Float, Long, Short
                            objetos.add(datos[i].substring(1, datos[i].length()));  //agrega el string al arreglo de objetos
                            break;
                        case 'C':                                               //Si es 'C' es un string
                            Character c = datos[i].charAt(1);                   //Castea el caracter
                            objetos.add(c);                                     //Lo agrega el caracter al arreglo de objetos
                            break;
                        case 'I':                                               //Si es 'I' es un string
                            Integer Int = Integer.parseInt(datos[i].substring(1, datos[i].length()));   //Castea el string a entero
                            objetos.add(Int);                                   //Agrega el entero a el arreglo de objetos
                            break;
                        case 'D':                                               //Si es 'D' es un string
                            Double d = Double.parseDouble(datos[i].substring(1, datos[i].length()));    //Castea el string al hombre
                            objetos.add(d);                                     //Agrega el double al arreglo de objetos
                            break;
                        case 'F':                                               //Si es 'F' es un string
                            Float f = Float.parseFloat(datos[i].substring(1, datos[i].length()));   //Castea el string 
                            objetos.add(f);                                     //Agrega el objeto al arreglo de objetos
                            break;
                        case 'L':                                               //Si es 'L' es un string
                            Long l = Long.parseLong(datos[i].substring(1, datos[i].length()));
                            objetos.add(l);
                            break;
                        case 's':                                               //Si es 's' es un string
                            Short s = Short.parseShort(datos[i].substring(1, datos[i].length()));
                            objetos.add(s);
                            break;
                        default:
                            break;
                    }
                }
                Integer ind = null;
                for (int i = 0; i < campos.size(); i++) {
                    if (campos.get(i).isLlave()) {
                        ind = (int) objetos.get(i);
                    }
                }
                registro newReg = new registro(ind, campos, objetos);
                registros.add(newReg);
            }
            pos += size + 4;
            br.reset();
        }
        bufferActual = cont;
        posFinal = pos;
        br.close();
    }

    public void cargarRegistrosArbol() throws FileNotFoundException, IOException {
        arbol = new ArbolB(orden);
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        br.mark((int) file.length());
        String tempLine;
        int pos = 502;
        br.reset();
        int posLlave = 0;
        for (int i = 0; i < campos.size(); i++) {
            if (campos.get(i).isLlave()) {
                posLlave = i;
                break;
            }
        }
        while (pos < posReg - 1) {
            br.skip(pos);
            ArrayList objetos = new ArrayList();
            char[] sizeReg = new char[4];
            br.read(sizeReg);
//            System.out.println("Cargar-" + pos);
//            System.out.println(new String(sizeReg) + "-" + new String(sizeReg).length());
            int size = 0;
            try {
                size = Integer.parseInt(new String(sizeReg));
            } catch (Exception e) {
                pos++;
                br.reset();
                continue;
            }
            char[] registro = new char[size];
            br.read(registro);
            String reg = new String(registro);
            if (reg.charAt(0) != '*') {
                String[] datos = reg.split("/");
                Integer ind;
                ind = Integer.parseInt(datos[posLlave].substring(1));
                arbol.insert(ind, pos);
            }
            pos += reg.length() + 4;
            br.reset();
//            System.out.println("arbol :" + pos);
//            System.out.println("");
        }
        br.close();
    }
    
    public boolean siguiente() {
        try {
            if (bufferActual == buffer) {
                anteriores.push(posAnterior);
                posAnterior = posActual;
                posActual = posFinal;
                cargarRegistros();
                return true;
            }
            return false;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    public boolean anterior() {
        try {
            if (posAnterior != posActual) {
                posFinal = posActual;
                posActual = posAnterior;
                posAnterior = (int) anteriores.pop();
                cargarRegistros();
                return true;
            }
            return false;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }
    
    public registro buscar(int index){
        int pos = 0;
        if ((pos = arbol.buscarPos(index)) == -1) {
            return null;
        }
        while(anterior());
        
        do{
            if (posActual <= pos && pos < posFinal) {
                for (int i = 0; i < registros.size(); i++) {
                    if (registros.get(i).index == index) {
                        return registros.get(i);
                    }
                }
            }
        }while(siguiente());
        
        return null;
    }

    public boolean escribirArchivo(File arch) {                             //Escribe todos los datos necesarios en el archivo especificado

        try {
            //AQUI EMPIEZA LO DE ESCRIBIR EN CIERTA POSICION

            RandomAccessFile rac = new RandomAccessFile(arch, "rw");         //Inizialica el random access file

            String buffer = "";                                                 //String en el que se escribira temporalmente el contenido a escribir en el archivo

            for (campo campoActual : campos) {                                  //Recorre los campos
                buffer += campoActual.toStringFile();                           //Y agrega el .toString() de los mismos...
            }                                                                   //...El .toString() de los campos ya esta formateado para ser escritos directamente
            buffer += "&" + registros.size() + "&";
            buffer += "-1" + "/" + "-1" + "&";

            int spaces = 500 - buffer.length();                                 //Agrega espacios en blanco para completar los siguientes 500 bytes que conforma el metadata
            for (int i = 0; i < spaces; i++) {
                buffer += " ";
            }

//            System.out.println(buffer);
            byte[] bytes = buffer.getBytes();                                   //arreglo de bytes de el String a escribir (buffer)
            ByteBuffer bf = ByteBuffer.allocate(bytes.length);               //ByteBuffer de el arreglo de bytes (El canal de Random Access File utiliza ByteBuffer)
            bf.put(bytes);                                                      //Agrega el arreglo de bytes al ByteBuffer
            bf.flip();                                                          //Metodo flip lo alista para escribir
            rac.seek(0);                                                        //Mueve el RAF a la posicion inicial del archivo
            rac.getChannel().write(bf);                                         //Toma el canal del RAF y escribe el ByteBuffer en el.

            rac.close();                                                        //Cierra el RAF

            while (anterior());

            return escribirRegistros(arch, true);                            //Llama metodo de escribir registros y retorna si fue exitoso o no
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }
    
    public boolean escribirRegistros(File arch, boolean archLoc) throws IOException {
        try {
            RandomAccessFile rac = new RandomAccessFile(arch, "rw");
            
                for (int i = 0; i < registros.size(); i++) {
                    registro tempReg = registros.get(i);
                    if (tempReg.modif) {
                        int pos = arbol.buscarPos(tempReg.index);
                        String regStr = tempReg.toString();
                        byte[] bytes = regStr.getBytes();
                        ByteBuffer bf = ByteBuffer.allocate(bytes.length);
                        bf.put(bytes);
                        bf.flip();
                        /*
                        if (pos == -1) {
//                            System.out.println("indice = " + tempReg.index);
                            arbol.mostrarArbol(arbol.raiz, "", '.');
                            Thread.sleep(99999999);
                        }
                         */
                        rac.seek(pos);
                        rac.getChannel().write(bf);
//                        System.out.println("Wr:" + pos);
//                        System.out.println("");
                        if (archLoc) {
                            tempReg.modif = false;
                        }
                    }
                }
            
            rac.close();
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }
    
    public void cerrarArchivo() {
        file = null;

        campos = new ArrayList();
        metaDatos = null;

        registros = new ArrayList();
        indiceReg = 0;
        posReg = 0;
        posAnterior = 0;
        posActual = 0;
        posFinal = 0;
        anteriores = new Stack();

        arbol = new ArbolB(orden);
    }
}
