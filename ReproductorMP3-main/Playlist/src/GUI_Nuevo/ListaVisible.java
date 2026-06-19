package GUI_Nuevo;

import Contenedores.Lista0DLinkedList;
import ReproductorNuevo.Cancion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class ListaVisible {
    private ListView<String> listViewPlaylist ;
    private ObservableList<String> itemsVisible;
    //con el contructor enlazamos la informacion de la ventan a la vista de la lista
    public ListaVisible( ListView<String> listViewPlaylist ){
        this.listViewPlaylist = listViewPlaylist;
        this.itemsVisible = FXCollections.observableArrayList();
        this.listViewPlaylist.setItems(itemsVisible);
    }
    //actualiza la lista recorriendo la playlis dada y la recorre mostrando sus datos
    public ListView<String> actualizarListView(Lista0DLinkedList lista){
        this.listViewPlaylist.getItems().clear();

        for(int i = 0; i < lista.tamanio(); i++) {
            Cancion cancion = (Cancion) lista.devolver(i);
            this.listViewPlaylist.getItems().add(cancion.toString());
            }
            return this.listViewPlaylist;
        }
}


