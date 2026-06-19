package GUI_Nuevo;

import java.io.File;
import java.util.Map;

import ReproductorNuevo.*;
import ReproductorNuevo.LectorMP3;
import ReproductorNuevo.ListaCanciones;
import ReproductorNuevo.ListaCancionesOrd;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Dragboard;

public class CargaArchivos {
    //referencia a los componentes visuales del constructor principal
    private ListView<String> listViewPlaylist;
    private TreeView<String> treeViewBiblioteca;
    
    private ListaCanciones playlist;
    private ListaCancionesOrd playlistOrd;
    private Map<String,ListaCanciones> mapaAlbunes;
    private LectorMP3 lectoR;
    private VentanaAPP2 ventana;
    public CargaArchivos(ListView<String> ListaVisible, TreeView<String> treeBiblioteca , ListaCanciones canciones, 
        ListaCancionesOrd cancionesOrd, Map<String,ListaCanciones> mapaAlbumes, LectorMP3 lector,VentanaAPP2 ventana){
        this.listViewPlaylist=ListaVisible;
        this.treeViewBiblioteca=treeBiblioteca;
        this.playlist=canciones;
        this.playlistOrd=cancionesOrd;
        this.mapaAlbunes=mapaAlbumes;
        this.lectoR=lector;
        this.ventana=ventana;
    }
    //Configura Drag and Drop   ---> Sirve para arrastrar archivos a la ventana y agregarlos a la playlist
    public void habilitarDragAndDrop() {
        //evento 1: Detectar cuando se arrastra un archivo sobre el ListView
        this.listViewPlaylist.setOnDragOver(event -> {
            if (event.getGestureSource() != listViewPlaylist && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });
        //evento 2: Detectar cuando se suelta el archivo sobre el ListView
        this.listViewPlaylist.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean complete = false; 
            //int indice = -1;
            if(db.hasFiles()) {
                complete = true;
                //Recorre todos los archivos soltados...
                for(File archivo: db.getFiles()) {
                    //Verifica que sean archivos mp3
                    if(archivo.getName().toLowerCase().endsWith(".mp3")) {
                        //Cancion cancion = lectoR.leer(archivo);//
                        Cancion cancion= lectoR.leer(archivo);
                        if (cancion != null) { //Si se pudo leer la cancion, se agrega a la playlist
                            //modificamos la UI y las estructurals de datos pasadas por referencia
                            listViewPlaylist.getItems().add(cancion.toString());
                            this.playlist.insertar(cancion, playlist.tamanio());
                            this.playlistOrd.insertar(cancion);
                            //clasificarCancionesPorAlbum(cancion);
                            System.out.println("Cancion agregada por Drag and Drop: "+ cancion);
                        }
                        else{
                            System.out.println("No se puede leer el archivo: " + archivo.getName());
                        }
                    }
                }
                //le avisamos a la ventana que procese la lista recien carga
                if(this.ventana!=null){
                    this.ventana.cargarPlayList(this.playlist);
                }
                event.setDropCompleted(complete);
                event.consume();
            }
        });
    }
    /*
    private void clasificarCancionPorAlbum(Cancion cancion){
        if(cancion==null){
            System.out.println("ERROR: LA CANCION ES NULA");
            return;
        }
        String album = cancion.getAlbum();
        if(album==null||album.isEmpty()){
            album="Desconocido";
        }
        if(!mapaAlbunes.containsKey(album)){
            //incializamos el albun con el titulo de la pimeraCancion encontrada
            mapaAlbunes.put(album,new ListaCanciones());
        }
        //descomenta esto cuadno definas el metodo en tu clase playlist
        mapaAlbunes.get(album).agregarCancion(cancion);
        construirArbolAlbumes();
    }*/
/* 
    public void construirArbolAlbumes() {
        TreeItem<String> raiz = new TreeItem<>("Biblioteca");
        raiz.setExpanded(true);

        for (String album : mapaAlbunes.keySet()) {
            TreeItem<String> nodoAlbum = new TreeItem<>(album);
            ListaCanciones listaAlbum = mapaAlbunes.get(album);

            for (int i = 0; i < listaAlbum.tamanio(); i++) {
                Cancion cancion = (Cancion) listaAlbum.devolver(i);
                if (cancion != null) {
                    TreeItem<String> nodoCancion = new TreeItem<>(cancion.getTitulo());
                    nodoAlbum.getChildren().add(nodoCancion);
                }
            }
            raiz.getChildren().add(nodoAlbum);
        }
        treeViewBiblioteca.setRoot(raiz);
    }*/
}
