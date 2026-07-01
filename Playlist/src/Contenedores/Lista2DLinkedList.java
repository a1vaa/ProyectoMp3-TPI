package Contenedores;

import Recursos.*;

public abstract class Lista2DLinkedList extends Lista0DLinkedList implements OperacionesCL4{

    public abstract boolean iguales(Object elemento1, Object elemento2);
	public abstract boolean esMenor(Object elemento1, Object elemento2);
	public abstract boolean esMayor(Object elemento1, Object elemento2);

    public void insertar(Object elemento) {
        if(estaVacia()) {
            this.frenteL = this.finalL = new NodoDoble(elemento);
        }
        else {
            /*::::::Hacemos una inserccion al frente::::::*/
            if(esMenor(elemento, this.frenteL.getNodoInfo())) {
                this.frenteL = new NodoDoble(elemento, null,this.frenteL);
                this.frenteL.getNextNodo().setPrevNodo(this.frenteL);
            }
            else {
                /*:::::Hacemos una inserccion al final::::::*/
                if (esMayor(elemento, this.finalL.getNodoInfo()) || iguales(elemento, this.finalL.getNodoInfo())) {	//insercion al final, si es igual no puede ponerse antes.
					//Completar!!! // SE inserta al final de la lista 27/4
                    this.finalL = new NodoDoble(elemento, this.finalL, null);
                    this.finalL.getPrevNodo().setNextNodo(this.finalL);
				}
                /*:::::Hacemos una inserccion en el medio::::::*/
                else{
                    NodoDoble aux = this.frenteL;
                    //boolean flag = true;

                    while (aux.getNextNodo() != null && (esMayor(elemento, aux.getNextNodo().getNodoInfo()) || iguales(elemento, aux.getNextNodo().getNodoInfo()))) {
                            aux = aux.getNextNodo();
                    }
                    
                    NodoDoble nodo = new NodoDoble(elemento, aux, aux.getNextNodo());
                    aux.getNextNodo().setPrevNodo(nodo); // Restableciendo las referencias....
                    aux.setNextNodo(nodo);
                }
            }
        }
        this.ultimo++;
    }

    public int buscar(Object elemento) {
        int posicion = -1; int contador = 0;
		Object unElemento;
		NodoDoble temp;
		
		temp = this.frenteL;
		while (temp != null && posicion == -1) {
			unElemento = temp.getNodoInfo();
			if (iguales(unElemento,elemento)) {
				posicion = contador;
			}else{
				temp = temp.getNextNodo();
				contador++;
			}
		}				
		return posicion;
    }


}
