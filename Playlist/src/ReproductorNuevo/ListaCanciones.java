package ReproductorNuevo;

import Contenedores.*;

public class ListaCanciones extends Lista1DLinkedList{
    
    public boolean iguales(Object elemento1, Object elemento2) {
        Cancion c1 = (Cancion) elemento1;
        Cancion c2 = (Cancion) elemento2;

        if(c1.getTitulo().equals(c2.getTitulo()) && c1.getArtista().equals(c2.getArtista())) {
            return true;
        }
        else {
            return false;
        }
    }

    public void agregarCancion(Cancion cancion) {
        insertar(cancion,tamanio());
    }

}
