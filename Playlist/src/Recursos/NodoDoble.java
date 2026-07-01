package Recursos;

public class NodoDoble {
    protected Object nodoInfo;
    protected NodoDoble prevNodo, nextNodo;

    public NodoDoble(Object nodoInfo) {
        this.nodoInfo = nodoInfo;
        this.prevNodo = null;
        this.nextNodo = null;
    }

    public NodoDoble (Object nodoInfo, NodoDoble prev, NodoDoble next) {
        this.nodoInfo = nodoInfo;
        this.prevNodo = prev;
        this.nextNodo = next;
    }

    public void setNodoInfo(Object valor ){
        this.nodoInfo = valor;
    }

    public void setPrevNodo(NodoDoble nodo) {
        this.prevNodo = nodo;
    }

    public void setNextNodo(NodoDoble nodo) {
        this.nextNodo = nodo;
    }

    public Object getNodoInfo() {
        return this.nodoInfo;
    }

    public NodoDoble getPrevNodo() {
        return this.prevNodo;
    }

    public NodoDoble getNextNodo() {
        return this.nextNodo;
    }

}
