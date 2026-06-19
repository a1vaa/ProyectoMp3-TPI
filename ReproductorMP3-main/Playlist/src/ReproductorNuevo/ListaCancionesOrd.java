package ReproductorNuevo;

import Contenedores.*;

public class ListaCancionesOrd extends Lista2DLinkedList{
    public enum ordenCriterio {
        TITULO, ARTISTA, ANIO
    }
    private ordenCriterio criterio;

    public void agregarCancion(Cancion cancion) {
        insertar(cancion);
    }
    
    public void setCriterio(String criterio){
        switch (criterio.toLowerCase()) {
            case "titulo": this.criterio = ordenCriterio.TITULO; break;
            case "artista": this.criterio= ordenCriterio.ARTISTA; break;
            case "Anio": this.criterio=ordenCriterio.ANIO; break;
            default:    this.criterio=ordenCriterio.TITULO;break;
        }
    } 
    public ListaCancionesOrd(ordenCriterio criterio) {
        super();
        this.criterio = criterio;
    }

    public boolean iguales(Object elementoL, Object elemento) {
        Cancion c1 = (Cancion) elementoL;
        Cancion c2 = (Cancion) elemento;

        String r1 = "";
        String r2 = "";

        switch (criterio) {
            case TITULO:
                r1 = c1.getTitulo();
                r2 = c2.getTitulo();
                break;
            case ARTISTA:
                r1 = c1.getArtista();
                r2 = c2.getArtista();
                break;
            case ANIO:
                return c1.getAnio()==c2.getAnio();
                //r1 = String.valueOf(c1.getAnio());
                //r2 = String.valueOf(c2.getAnio());
                //break;
        default:
                r1 = c1.getTitulo();
                r2 = c2.getTitulo();
                break;
        }
        return r1.compareToIgnoreCase(r2) == 0;
    }

    public boolean esMayor(Object elememto1, Object elemento2) {
        Cancion c1 = (Cancion) elememto1;
        Cancion c2 = (Cancion) elemento2;

        String r1 = "";
        String r2 = "";

        switch (this.criterio) {
            case TITULO:
                r1 = c1.getTitulo();
                r2 = c2.getTitulo();
                break;
            case ARTISTA:
                r1 = c1.getArtista();
                r2 = c2.getArtista();
                break;
            case ANIO:
                return c1.getAnio()>c2.getAnio();
                //r1 = String.valueOf(c1.getAnio());
                //r2 = String.valueOf(c2.getAnio());
                //break;
                default:
                    r1 = c1.getTitulo();
                    r2 = c2.getTitulo();
                break;
        }

        return r1.compareToIgnoreCase(r2) > 0;
    }

    public boolean esMenor(Object elemento1, Object elemento2) {
        Cancion c1 = (Cancion) elemento1;
        Cancion c2 = (Cancion) elemento2;

        String r1 = "";
        String r2 = "";

        switch (this.criterio) {
            case TITULO:
                r1 = c1.getTitulo();
                r2 = c2.getTitulo();
                break;
            case ARTISTA:
                r1 = c1.getArtista();
                r2 = c2.getArtista();
                break;
            case ANIO:
                return c1.getAnio()<c2.getAnio();
                //r1 = String.valueOf(c1.getAnio());
                //r2 = String.valueOf(c2.getAnio());
                //break;
            default:
                r1 = c1.getTitulo();
                r2 = c2.getTitulo();
                break;
        }

        return r1.compareToIgnoreCase(r2) < 0;
    }
}
