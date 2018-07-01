package javaapplication116;


public class availReg {
    
    public int index;
    public int size;
    public int pos;

    public availReg(int index, int size, int pos) {
        this.index = index;
        this.size = size;
        this.pos = pos;
    }
    
    public String toString(){
        return "index: " + index + " size: " + size + " pos: " + pos + '\n';
    }
    
}
