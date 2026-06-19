package GUI_Nuevo;

import java.io.File;

import Recursos.NodoDoble;
//import Reproductor.PlayList;
import ReproductorNuevo.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
//import javafx.scene.control.Label;
import javafx.scene.control.ListView;
//import javafx.scene.control.Slider;
//import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
//import javafx.scene.input.Dragboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
//import java.util.random.*;

public class VentanaAPP2 extends Application{
    //botones de Control
    private PanelControles panelControles;
    //botones de Ordenar
    private PanelOrdenar panelOrdenar;
    //clase del contenedor de la lista
    private PanelInfo panelInfo;
    //clase del contenedor
    //private ImagenContener contenedorImg;
    private ListaVisible list;
    private NodoDoble cancionActual;
    private boolean ordenado=false;
    //

    private Button button_addFiles;
    private ReproductorMP3 reproductor;
    private ListaCanciones playlist;
    private ListaCancionesOrd playlistOrd;
    private ListaCanciones playlistRandom;
    private LectorMP3 lector;
    //
    private String background;
    //
    private ListView<String> listViewPlayList;
    private ImageView portada;

    private boolean pausado=false;
    private boolean bucle=false;
    private boolean repetir=false;
    private boolean rand=false;
    private boolean Pausa=false;
    private int indiceActual;

    //
    private Map<String, ListaCanciones> mapaAlbumes;
    private TreeView<String> treeViewBiblioteca;
    private StackPane contenedorImage;
    private CargaArchivos carga;
    //colores 
    public static final String COLOR_BG = "#121212";      // Negro fondo
    public static final String COLOR_SIDEBAR = "#030303"; // Negro más claro
    public static final String COLOR_BOTON = "#42c63eb7"; // Dorado/Amarillo principal//efb810
    public static final String COLOR_TEXTO = "#FFFFFF";    // Blanco
    public void start(Stage stage) {
        //
        String background = "#cdf1be";
        String sidebar = "#191414";
        String green = "#efb810";
        String text = "#FFFFFF";
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + background);

        inicializarComponentes(stage);
        //habilitarDragAndDrop();
        Scene escena = new Scene(crearLayout(), 700, 500);
        
        stage.setTitle("Reproductor MP3 de musica");
        stage.setScene(escena);
        CentrarImagenCentro(stage);
        stage.show();
        this.reproductor.cargarPortadaPorDefecto();
    }

    private void inicializarComponentes(Stage stage) {
         //clases de contenedores
        this.listViewPlayList = new ListView<String>();
        this.list=new ListaVisible(listViewPlayList);
        //funciones del reproductor
        this.reproductor = new ReproductorMP3();
        this.playlist = new ListaCanciones();
        this.playlistOrd = new ListaCancionesOrd(null);
        this.playlistRandom=new ListaCanciones();
        this.lector = new LectorMP3();
        //this.labelCancionActual = new Label("Canción actual: ");
        this.cancionActual=null;
        this.portada=new ImageView();//findViewById(R.id.miImageView);

        // Le damos un tamaño inicial para que no nazca en 0px mientras se arma el Layout
    this.portada.setFitWidth(150);
    this.portada.setFitHeight(150);
        this.panelControles=new PanelControles();
        this.panelOrdenar=new PanelOrdenar();
        this.panelInfo=new PanelInfo();

        this.button_addFiles = new Button("Add Files");
        this.reproductor.setControlesInterfaz(
        this.panelInfo.getSliderVolumen(),
        this.panelInfo.getSliderProgreso(),
        this.panelInfo.getTiempoActual(),
        this.panelInfo.getTiempoTotal(),
        this.portada,
        this.panelControles);
        // Hace que el vinilo aparezca de entrada apenas se abre la aplicación
        this.reproductor.cargarPortadaPorDefecto();
        
        this.contenedorImage=new StackPane(this.portada);
        this.treeViewBiblioteca=new TreeView<>();
        this.mapaAlbumes=new HashMap<>();
        
        this.carga=new CargaArchivos(this.listViewPlayList,
            this.treeViewBiblioteca,
            this.playlist, 
            this.playlistOrd, 
            this.mapaAlbumes, 
            this.lector,
            this); 
        this.carga.habilitarDragAndDrop();
        //this.carga.construirArbolAlbumes();
        registrarEventos(stage);
    }

    private VBox crearLayout(){
        HBox addFilesBox = new HBox();
        addFilesBox.setSpacing(10);
        addFilesBox.getChildren().add(button_addFiles);
        addFilesBox.setAlignment(Pos.TOP_LEFT);

        //confi de portada (evita que rompa el layout)
        this.portada.setPreserveRatio(false);//mantiene la proporciones de la image, no se deforma
        this.portada.setSmooth(true);//mejora la calidad del escalado
        //le pedimos al imageView que se adapte al contenedor, no a su tamaño real
        //el conetendor de la imagen (StackPane)
        StackPane contenedorImagen = new StackPane(this.portada);
        contenedorImagen.setAlignment(Pos.CENTER);
        contenedorImagen.setMinWidth(200);
        contenedorImagen.setMinHeight(210);
        contenedorImagen.setPrefSize(800, 800);
        contenedorImagen.setMaxSize(900, 900);
        //fuerza a la imagen a estar en el centro al estirar
        contenedorImagen.setMaxWidth(Double.MAX_VALUE);
        StackPane.setAlignment(this.portada, Pos.CENTER);
        //un binding que elije siempre el lado mas corto del contenedor
        javafx.beans.binding.NumberBinding dimensionMinima = javafx.beans.binding.Bindings.min(
        contenedorImagen.widthProperty(), 
        contenedorImagen.heightProperty()
        );

        contenedorImagen.setPickOnBounds(false);//ayuda con los clics?
        this.portada.setTranslateX(0);
        this.portada.setTranslateY(0);
        //vinculamos el ancho y alto de la imagen a esa dimension minima(menos un margen de 30px)
        this.portada.fitWidthProperty().bind(dimensionMinima.subtract(30));//contenedorImagen.widthProperty().subtract(30));
        this.portada.fitHeightProperty().bind(dimensionMinima.subtract(30));//contenedorImagen.heightProperty().subtract(30));
        // 3. El recorte (Clip) circular
        
        javafx.scene.shape.Circle clipCircular = new javafx.scene.shape.Circle();
        clipCircular.radiusProperty().bind(this.portada.fitWidthProperty().divide(2));
        // El radio es la mitad de la dimensión de la imagen
        clipCircular.centerXProperty().bind(this.portada.fitWidthProperty().divide(2));
        // Centramos el círculo en el ImageView
        clipCircular.centerYProperty().bind(this.portada.fitHeightProperty().divide(2));

        this.portada.setClip(clipCircular);
        //El contenedor central (HBox)
        HBox centroBox=new HBox(30);
        //centroBox.setAlignment(Pos.CENTER);
        centroBox.getChildren().addAll(contenedorImagen,listViewPlayList);
       //VBox.setVgrow(listViewPlayList,Priority.ALWAYS);
        //HBox.setHgrow(listViewPlayList,Priority.ALWAYS);
        //se asegura que el centro ocupe todo el ancho
        centroBox.setMaxWidth(Double.MAX_VALUE);
        centroBox.setFillHeight(false);//ayuda a mantener la alineacion vertical
        //StackPane contenedorImagen = new StackPane(portada);
        //StackPane.setAlignment(portada, Pos.CENTER_LEFT);//por defecto lo tiramos a la izquiera para que no pegue al borde 
        StackPane.setMargin(portada, new javafx.geometry.Insets(10,10,10,10)); //lo dejamos con 50px de margen izq
        
        listViewPlayList.setMaxWidth(Double.MAX_VALUE);
        listViewPlayList.setMinWidth(300);
        listViewPlayList.setPrefWidth(300);
        VBox.setVgrow(centroBox, Priority.ALWAYS);
        //HBox importateBox = new HBox(30,contenedorImagen,listViewPlayList);
        //addFilesBox.getChildren().add(importateBox);
        //playlistBox.setAlignment(Pos.CENTER);
        centroBox.setAlignment(Pos.CENTER);//mantiene a ambos elementos centrados
        HBox.setHgrow(contenedorImagen, Priority.ALWAYS);

       //COMO YA INSTANCIAMOS LAS CLASES DE PANELES CONTROLES Y ORDENAR SOLO HAY QUE LLAMARA A LOS OBJETOS
        VBox root = new VBox();
        root.setSpacing(20);
        root.getChildren().addAll(addFilesBox, centroBox,this.panelInfo, this.panelControles, this.panelOrdenar);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20,20,20,80));
        
    
        //colores 
        //fondo
        root.setStyle("-fx-background-color: " + COLOR_BG +";");
        // Estilo para el botón de "Add Files"
        button_addFiles.setStyle(
        "-fx-background-color: " +COLOR_BOTON + ";" +
        "-fx-text-fill: black;" +
        "-fx-font-weight: bold;"
        );
        //lista
        listViewPlayList.setStyle("-fx-control-inner-background: #191414; -fx-text-fill: white;");
        return root; 
    }
    //la la visualizacion de la playlist que se esta reproduciendo
    private void CargaListaView(){
       if (list != null) {
        if(rand)this.list.actualizarListView(playlistRandom);
        else{
            if (ordenado){//&& playlistOrd != null) {
                this.list.actualizarListView(playlistOrd); // Renderiza la ordenada
            } else {
                this.list.actualizarListView(playlist);    // Renderiza la común
            }
            }
        }
    }

    private void registrarEventos(Stage stage){
            button_addFiles.setOnAction(e ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos MP3", "*.mp3"));

            List<File> archivos = fileChooser.showOpenMultipleDialog(stage);

            if(archivos != null&& !archivos.isEmpty()){
                for(File archivo: archivos){
                    if(archivo!=null){
                    Cancion cancion = lector.leer(archivo);
                if(cancion != null){
                    this.playlist.insertar(cancion, playlist.tamanio());
                    if(ordenado) this.playlistOrd.insertar(cancion);
                        if(rand) this.playlistRandom.insertar(cancion,this.playlistRandom.tamanio());
                    System.out.println("Cancion agregada: " + cancion);
                    if(cancionActual==null){
                        cancionActual=playlist.getFrenteL();
                    }
                }
                else{
                    System.out.println("No se puede leer el archivo");
                    }
                }
                }
            }
            ordenado=false;
            rand=false;
            reproductor.CargaPortada(getCancionActual(), portada);
            CargaListaView();
            actualizarLabelCanciones();
            actualizarSeleccionVisual(getCancionActual());
        });

        this.panelControles.getBtnPrev().setOnAction(e->{
        System.out.println("PREVIO");
        //if(repetido) cancion = cancionActual ;
                Cancion cancion = getCancionPrevia();//actualizamos la cabecera
                if(bucle&& cancion==null){
                    //si se termino la playlist y el bucle esta activo se reinicia la playlist
                    if(rand) {
                        actualizarSeleccionVisual(cancion);
                        actualizarAleatorio();
                        CargaListaView();
                    }
                    else{
                        NodoDoble nodoFrente = ordenado? playlistOrd.getFinalL()
                        : playlist.getFinalL();
                            if(nodoFrente!=null){
                                this.cancionActual=nodoFrente;
                                cancion=(Cancion)cancionActual.getNodoInfo();
                            }
                        }
                    }
                if(cancion!=null){
                //lo de abajo no se hace para no volver a cargar el media player de nv, solo se debe enlazar una sola vez
                reproductor.CargaPortada(cancion, portada);
                actualizarLabelCanciones();
                reproductor.setNodoActual(cancionActual);
                reproductor.reproducir(cancion);
                //muestra en la lista
                actualizarSeleccionVisual(cancion);
                if(!pausado) {
                    this.panelInfo.getLabelCancionActual().setText("SE TERMINO LA PLAYLIST");
                    reproductor.detener();
                    //actualizarPLayPause();
                    }   
                }else{
                    this.panelInfo.getLabelCancionActual().setText("SE TERMINO LA PLAYLIST");
                    this.reproductor.detener();
                }
            });

        this.panelControles.getBtnPlay().setOnAction(e -> {
            //b=false;
            System.out.println("PLAY");
            Cancion cancion=getCancionActual();
            if(cancion!=null){//nose que tan necesario sea porque si ordenamos y solo actualizamos el nodo de cancionActual no es necesario controlar si es ordena o no
                if(pausado){
                    //reproductor.reanudar();
                    reproductor.pausar();
                    pausado=false;
                }else{
                    //si esta sonando se pausa
                    reproductor.reproducir(cancion);
                    pausado = true;
                }
                actualizarLabelCanciones();
                reproductor.CargaPortada(cancion, portada);
                actualizarPLayPause();
            }
        });

       // this.button_pause.setOnAction(e -> this.reproducto.pause());
        this.panelControles.getBtnStop().setOnAction(e -> {
            this.reproductor.detener();
            pausado = false;
           actualizarPLayPause();
        });

        this.panelControles.getBtnNext().setOnAction(e -> {
            System.out.println("SIGUIENTE");
            //Cancion cancion;
            //if(repetir)  cancion= (Cancion)cancionActual.getNodoInfo();
                Cancion cancion = getCancionSiguiente();//actualizamos la cabecera 
                if(bucle&& cancion==null){
                    if(rand) {
                        actualizarSeleccionVisual(cancion);
                        actualizarAleatorio();
                        CargaListaView();
                    }
                    else{
                    //si se termino la playlist y el bucle esta activo se reinicia la playlist
                        NodoDoble nodoFrente = ordenado? playlistOrd.getFrenteL()
                    : playlist.getFrenteL();
                    if(nodoFrente!=null){
                        this.cancionActual=nodoFrente;
                        cancion=(Cancion)cancionActual.getNodoInfo();
                    }
                    }
                }
                //this.panelControles.getBtnPlay();
                if(cancion!=null){
                    reproductor.setNodoActual(cancionActual);
                    reproductor.reproducir(cancion);
                    reproductor.CargaPortada(cancion, portada);
                    actualizarLabelCanciones();
                    //muestra en la lista
                    actualizarSeleccionVisual(cancion);
                if(!pausado) {
                    reproductor.detener();
                    this.panelInfo.getLabelCancionActual().setText("SE TERMINO LA PLAYLIST");
                }
                } else{//if para bucle
                    this.panelInfo.getLabelCancionActual().setText("SE TERMINO LA PLAYLIST");
                    reproductor.detener();
                }
            });

        this.panelOrdenar.getBtnTitulo().setOnAction(e->{
             if(!ordenado){
                activarOrden( "titulo");
                this.panelOrdenar.getBtnArtista().setText("Orden Artista");
                this.panelOrdenar.getBtnTitulo().setText("ON Orden Titulo");
                this.panelOrdenar.getBtnYear().setText("Orden Año");

        }else{
                desactivarOrden();
                this.panelOrdenar.getBtnTitulo().setText("Orden Titulo");
            }
            rand=false;
            this.panelControles.getBtnAleatorio().setText("🔀OFF");
            reproductor.CargaPortada(getCancionActual(), portada);
            reproductor.reproducir(getCancionActual());
            actualizarLabelCanciones();
           CargaListaView();
           actualizarSeleccionVisual(getCancionActual());
           System.out.println("/////ordenar por titulo////////////7");
           this.playlistOrd.mostrar();
        });

        this.panelOrdenar.getBtnArtista().setOnAction(e -> {
            if(!ordenado){
                activarOrden( "artista");
                this.panelOrdenar.getBtnArtista().setText("ON Orden Artista");
                this.panelOrdenar.getBtnTitulo().setText("Orden Titulo");
                this.panelOrdenar.getBtnYear().setText("Orden Año");
            }else{
                desactivarOrden();
                this.panelOrdenar.getBtnArtista().setText("Ordenar Artista");
            }
            rand=false;
            this.panelControles.getBtnAleatorio().setText("🔀OFF");;
            actualizarLabelCanciones();
            reproductor.CargaPortada(getCancionActual(), portada);
            reproductor.reproducir(getCancionActual());
            CargaListaView();
            actualizarSeleccionVisual(getCancionActual());
            System.out.println("//////ORDENAR POR ARTISTA///////////");
            this.playlistOrd.mostrar();
        });

        this.panelOrdenar.getBtnYear().setOnAction(e -> {
            if(!ordenado){
                activarOrden( "Anio");
                this.panelOrdenar.getBtnArtista().setText("Orden Artista");
                this.panelOrdenar.getBtnTitulo().setText("Orden Titulo");
                this.panelOrdenar.getBtnYear().setText("ON Orden Año");
            }else{
                desactivarOrden();
                this.panelOrdenar.getBtnYear().setText("Orden Año");
            }
            rand=false;
            this.panelControles.getBtnAleatorio().setText("🔀OFF");
            actualizarLabelCanciones();
            CargaListaView();
            actualizarSeleccionVisual(getCancionActual());
            reproductor.CargaPortada(getCancionActual(), portada);
            reproductor.reproducir(getCancionActual());
            System.out.println("////////ORDENAR POR ANIO////////////");
            this.playlistOrd.mostrar();
        });

        this.panelControles.getBtnAleatorio().setOnAction(e -> {
            if(!rand){
                this.panelControles.getBtnAleatorio().setText("🔀ON");
                actualizarAleatorio();
                CargaListaView();
                actualizarLabelCanciones();
                actualizarSeleccionVisual(getCancionActual());
            }else{
                this.rand=false;
                this.panelControles.getBtnAleatorio().setText("🔀OFF");
                if(!ordenado) this.cancionActual=this.playlist.getFrenteL();
                else this.cancionActual=this.playlistOrd.getFrenteL();
                CargaListaView();
                actualizarLabelCanciones();
                actualizarSeleccionVisual(getCancionActual());
                reproductor.CargaPortada(getCancionActual(), portada);
                reproductor.reproducir(getCancionActual());
            }
        });

        this.panelControles.getBtnbucle().setOnMouseClicked(e->{
            if(e.getClickCount()==1){
                if(!bucle){
                    this.bucle=true;
                    this.panelControles.getBtnbucle().setText("🔁ACTIVADO");
                    System.out.println("bucle Activado");
                }
                else{
                    this.bucle=false;
                    System.out.println("bucle Descativado");
                    this.panelControles.getBtnbucle().setText("BUCLE 🔁");
                }
            }
            if(e.getClickCount()==2)
                if(!bucle)    
                    this.bucle=true;
                else
                    this.bucle=false;     
        });
        //selecciona musica desde la listaview
        this.listViewPlayList.setOnMouseClicked(e -> {
            indiceActual = listViewPlayList.getSelectionModel().getSelectedIndex();
            if(indiceActual >=0){
            Cancion seleccionada = seleccionarCancion(indiceActual);
            if(seleccionada!=null){
            reproductor.setNodoActual(cancionActual);
            reproductor.reproducir(seleccionada);
            reproductor.CargaPortada(seleccionada, portada);
            actualizarLabelCanciones();
            }
            }
        });
        this.panelControles.getBtnEliminar().setOnMouseClicked(e->{
            reproductor.detener();
            eliminarSong();
            actualizarLabelCanciones();
            reproductor.CargaPortada(getCancionActual(), portada);
            CargaListaView();
            actualizarSeleccionVisual(getCancionActual());
        });
    }

    private Cancion getCancionActual(){
       Cancion cancion = null;
        if(this.playlist != null) {
            cancion = (Cancion)this.cancionActual.getNodoInfo();
        }

        return cancion;
    }   
    private Cancion getCancionSiguiente(){
       // Verificamos si tenemos una canción actual y si hay una siguiente en la estructura de datos
    if (this.cancionActual != null && this.cancionActual.getNextNodo() != null) {
        this.cancionActual = this.cancionActual.getNextNodo();
        return (Cancion) this.cancionActual.getNodoInfo();
    }
    // Si llegó al final, solo devolvemos null y dejamos que el botón maneje si reinicia o frena
    return null;
    }
    private Cancion getCancionPrevia(){
         // Verificamos si tenemos una canción actual y si hay una anterior en la estructura de datos
    if (this.cancionActual != null && this.cancionActual.getPrevNodo() != null) {
        this.cancionActual = this.cancionActual.getPrevNodo();
        return (Cancion) this.cancionActual.getNodoInfo();
    }
    // Si llegó al final, solo devolvemos null y dejamos que el botón maneje si reinicia o frena
    return null;
    }
    private Cancion seleccionarCancion(int indice){
        Cancion cancion = null;
        // Identificamos cuál es la lista de datos que está usando la pantalla actualmente
        NodoDoble tem = null;
        //Cancion cancion = null;
        
        if (rand) {
            tem = playlistRandom.getFrenteL();
            cancion = (Cancion) playlistRandom.devolver(indice);
        } else if (ordenado) {
            tem = playlistOrd.getFrenteL();
            cancion = (Cancion) playlistOrd.devolver(indice);
        } else {
            tem = playlist.getFrenteL();
            cancion = (Cancion) playlist.devolver(indice);
        }

        // Avanzamos el puntero del nodo recorriendo la LISTA CORRECTA activa
        for(int i = 0; i < indice && tem != null; i++){
            tem = tem.getNextNodo();
        }

        // Ahora sí, cancionActual tiene el nodo correcto con sus punteros next y prev bien mapeados
        this.cancionActual = tem; 

        return cancion; 
    }

    private void actualizarLabelCanciones(){
        Cancion cancion = (Cancion) getCancionActual();
        if(cancion != null) {
            this.panelInfo.getLabelCancionActual().setText("🎶"+ cancion.toString());
        }
        else {
            this.panelInfo.getLabelCancionActual().setText("Sin reproducción actual");
        }
    }
    private void actualizarAleatorio(){
        //copiamos la lista de canciones 
        if(playlistRandom!=null)
        this.playlistRandom.limpiar();
        System.out.println("empieza Aleatorio");
        ListaCanciones clonAux= new ListaCanciones();
        for(int i=0;i<this.playlist.tamanio();i++){
            clonAux.insertar((Cancion)this.playlist.devolver(i),clonAux.tamanio());
        }
        Random random= new Random();
        //vamos sacando de la lista auxiliar el azar hasta vaciarla
        while(clonAux.tamanio()>0){
            int indice=random.nextInt(clonAux.tamanio());
                //insertar en playlistrand 
            Cancion cancion = (Cancion)clonAux.devolver(indice);
                this.playlistRandom.insertar(cancion,this.playlistRandom.tamanio());
                //borramos del clon para que el rango se vaya achicango y jamas se repita
                clonAux.eliminar(indice);
            }
        this.rand=true;
        System.out.println("Lista Aleatoria ");
        for(int i=0;i<this.playlistRandom.tamanio();i++){
            Cancion c = (Cancion)this.playlistRandom.devolver(i);
            System.out.println("Cancion agregada "+c.toString());
        }
        this.cancionActual=this.playlistRandom.getFrenteL();
    }


    //metodo para aplicar el play y pausa en el mismo boton
    private void actualizarPLayPause(){
        //this.pausado=!this.pausado;
        if(pausado){
            this.panelControles.getBtnPlay().setText("PLAY ▶");
            //this.reproductor.pausar();
            System.out.println("pausado");
            //this.pausado=false;
        }else{
            //this.reproductor.pausar();
            this.panelControles.getBtnPlay().setText("PAUSE ⏸");
            //this.pausado=true;
        }
    }
    public void CentrarImagenCentro(Stage stage){
        //estara atento si la ventan se maximiza o cambia de tamaño
        stage.maximizedProperty().addListener((observable,oldValue,isMaximed)->{
            if(isMaximed){
                //si esta en pantalla completa va al centro y crece
                StackPane.setAlignment(this.portada, Pos.CENTER);
                StackPane.setMargin(this.portada, new javafx.geometry.Insets(0));//se quita el margen
                this.portada.setFitHeight(300);
                this.portada.setFitWidth(300);
            }else{
                //si vuelve a la ventan simple: vuelve a la izq y se achica
                StackPane.setAlignment(this.portada, Pos.CENTER_LEFT);
                StackPane.setMargin(this.portada, new javafx.geometry.Insets(10,10,10,10));
                this.portada.setFitHeight(300);
                this.portada.setFitWidth(300);
            }
        });
    }

     private void activarOrden(String criterio) {
        this.playlistOrd.setCriterio(criterio);
        this.playlistOrd.limpiar();

        NodoDoble aux = this.playlist.getFrenteL();
        while (aux != null) {
            this.playlistOrd.insertar(aux.getNodoInfo());
            aux = aux.getNextNodo();
        }

        this.ordenado = true;
        this.cancionActual = this.playlistOrd.getFrenteL();

        System.out.println("Lista ordenada: " );
        for(int i = 0; i < this.playlistOrd.tamanio(); i++) {
            Cancion c = (Cancion)this.playlistOrd.devolver(i);
            System.out.println("Cancion agregada: " + c.toString());
        }
    }
     private void desactivarOrden() {
        this.ordenado = false;
        this.cancionActual = this.playlist.getFrenteL();

        System.out.println("\nLista Desordenada: ");
        System.out.println("\n");
        for(int i = 0; i < this.playlistOrd.tamanio(); i++) {
            Cancion c = (Cancion)this.playlist.devolver(i);
            System.out.println("Cancion agregada: " + c.toString());
        }
    }

   public void cargarPlayList(ListaCanciones playlist) {
    this.playlist = playlist;

    if (this.playlist != null && this.playlist.tamanio() > 0) {
        this.indiceActual = 0;
        
        // Buscamos el nodo inicial de tu estructura
        this.cancionActual = this.playlist.getFrenteL(); 
        
        if (this.cancionActual != null) {
            Cancion primera = (Cancion) this.cancionActual.getNodoInfo();
            // Le pasamos al reproductor la primera canción para que esté lista
            //this.reproductor.reproducir(primera); 
            this.reproductor.CargaPortada(primera, this.portada);
            actualizarLabelCanciones();
            actualizarSeleccionVisual(primera);
        }
    } else {
        this.indiceActual = -1;
        this.cancionActual = null;
        this.reproductor.detener();
    }
}
    private void eliminarSong(){
                // 1. Control de seguridad: Si no hay nada seleccionado, nos vamos
        if (cancionActual == null) return; 
        // Extraemos el objeto Cancion real antes de perder las referencias
        Cancion cancionABorrar = (Cancion) cancionActual.getNodoInfo();
        // 2. Sincronización del puntero: Movemos la reproducción al tema siguiente o anterior
       // if(cancionActual.getNextNodo()!=null&&cancionActual.getPrevNodo()!=null){
        if (cancionActual.getNextNodo() != null) {
            this.cancionActual = cancionActual.getNextNodo();
        } else if (cancionActual.getPrevNodo() != null) {
            this.cancionActual = cancionActual.getPrevNodo();
        } else {
            this.cancionActual = null; // Si era la única que quedaba, la lista queda vacía
        }
        //}
        // 3. Eliminación en la Playlist Principal usando tu método buscar()
        int indiceComun = this.playlist.buscar(cancionABorrar); // ◀️ Le pasamos la Cancion, no el Nodo
        if (indiceComun != -1) {
            this.playlist.eliminar(indiceComun);
        }
        // 4. Eliminación en la Playlist Ordenada (si corresponde)
        if (ordenado && this.playlistOrd != null) {
            int indiceOrdenado = this.playlistOrd.buscar(cancionABorrar);
            if (indiceOrdenado != -1) {
                this.playlistOrd.eliminar(indiceOrdenado);
            }
        }
}

private void actualizarSeleccionVisual(Cancion cancionActualizada) {
    if (cancionActualizada == null || listViewPlayList == null) return;
    // 1. Buscamos en qué posición de la lista está la canción actual
    int indice = listViewPlayList.getItems().indexOf(cancionActualizada.toString());
    // 2. Si la encuentra en pantalla, movemos el foco visual
    if (indice != -1) {
        // Guarda el índice internamente para que tus otros métodos no se desincronicen
        this.indiceActual = indice; 
        // Selecciona el renglón (lo pinta de azul/gris)
        listViewPlayList.getSelectionModel().select(indice);
        // Hace scroll automático si la canción quedó muy abajo o muy arriba
        listViewPlayList.scrollTo(indice);
    }
}
}
    