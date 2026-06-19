package ReproductorNuevo;

import java.io.File;
import java.util.Map;

import Recursos.NodoDoble;
//import Reproductor.PlayList;
import javafx.collections.MapChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
//
// 1. Para poder usar Platform.runLater
import javafx.application.Platform;

// 2. Para que el reproductor reconozca tu panel de botones personalizados
import GUI_Nuevo.PanelControles;
public class ReproductorMP3 {
    private ImageView img;
    private Slider SliderVolumen,SliderProgreso;
    private Label TActual;
    private Label TTotal;
    private NodoDoble nodoActual;
    private Cancion cancionCargada;
    //private ListaCanciones playlist;
    private MediaPlayer mediaPlayer;
    private boolean reproduciendo;
    private boolean pausado;
    private boolean usuarioMovioSlider=false;
    private PanelControles panel=null;
    public ReproductorMP3() {
        this.nodoActual = null;
        //this.playlist = new ListaCanciones();
        this.mediaPlayer = null;
        //this.reproduciendo = false;
        //this.pausado = false;
    }

    public void reproducir(Cancion cancion) {
        //Cancion cancion = (Cancion) nodoActual.getNodoInfo();
        if(cancion != null) {
            if(this.mediaPlayer == null || !cancionCargada.equals(cancion)){//|| this.cancionCargada != cancion) {
                cargarCancionActual(cancion);
                this.cancionCargada=cancion;
           }
           if(this.mediaPlayer!= null){
            this.mediaPlayer.play();
            this.reproduciendo = true;
            this.pausado = false;
            System.out.println("Reproduciendo: " + cancion);
           }
        }
        else {
            System.out.println("No hay cancion para reproducir");
        }
    }
    public void reanudar(){
        if(this.mediaPlayer!=null){
            this.mediaPlayer.play();
            this.reproduciendo=true;
            this.pausado=false;
            System.out.println("reanudando la cancion");
        }
    }
    //modulo fundamental para el draganddrop

    //Carga el MediaPlayer de forma segura aislando fallos de lectura o metadatos.
    private void cargarCancionActual(Cancion cancionActual) {
            if(cancionActual != null ){
            if(this.mediaPlayer != null){
                this.mediaPlayer.stop();
                //this.mediaPlayer.dispose();
            }
            try {
                File archivo = new File(cancionActual.getRuta());
                Media media = new Media(archivo.toURI().toString());
                this.mediaPlayer = new MediaPlayer(media);
                // se agregan aqui los slider para que la configuracion este automatica al instanciar el mediaplayer y no haya necidad de instanciar de nv
                if(SliderVolumen!=null&&SliderProgreso!=null){
                    //sincroniza el vol
                    mediaPlayer.setVolume(SliderVolumen.getValue());

                    //Listener del vol (se acopla al nuevo objeto de forma segura)
                    SliderVolumen.valueProperty().addListener((obs,oldVal,newVal)->{
                        if(mediaPlayer!=null) mediaPlayer.setVolume(newVal.doubleValue());
                    });

                    //listener de tiempo de seg a seg
                    mediaPlayer.currentTimeProperty().addListener((obs,oldTime,newtime)->{
                        if(!usuarioMovioSlider){
                            double segActuales = newtime.toSeconds();
                            SliderProgreso.setValue(segActuales);
                            int mins = (int)segActuales/60;
                            int seg = (int)segActuales%60;
                            TActual.setText(String.format("%02d:%02d", mins,seg));
                        }
                    });

                    //OnReady carga su metadata a los label de tiempos
                    mediaPlayer.setOnReady(()->{
                        double segTotales=mediaPlayer.getTotalDuration().toSeconds();
                        SliderProgreso.setMax(segTotales);
                        SliderProgreso.setValue(0);
                        int mins = (int)segTotales/60;
                        int seg = (int)segTotales%60;
                        TTotal.setText(String.format("%02d:%02d", mins,seg));
                    });
                }

                //
                mediaPlayer.setOnEndOfMedia(()-> {
                    //SliderProgreso.setValue(0);//
                    //TActual.setText("00:00");//
                    //siguiente();
                    //cuando nos toque presentar dire esta solucion sin necesidad de tocar tanto el codigo
                    javafx.application.Platform.runLater(() -> {
                    // Le avisa a la ventana que simule la presión del botón Siguiente
                    panel.getBtnNext().fire(); 
                    });
                });
            }
            catch(Exception e) {
                System.out.println("Error al cargar la cancion: " + e.getMessage());
                this.mediaPlayer = null;
            }
        }
    }

    public void mostrar() {
        Cancion c1 = (Cancion) nodoActual.getNodoInfo();
        System.out.println("Cancion: " +  c1.toString());
    }

    public void pausar() {
        this.mediaPlayer.pause();
    }

    public void detener() {
        if(this.mediaPlayer!=null){
        this.mediaPlayer.stop();
        this.reproduciendo = false;
        this.pausar();
        System.out.println("Detenido");
    }
    }

    public void siguiente() {
       if(this.nodoActual != null && this.nodoActual.getNextNodo() != null){
            this.nodoActual=this.nodoActual.getNextNodo();
            reproducir((Cancion)this.nodoActual.getNodoInfo());
       }else
            System.out.println("no hay cancion siguiente");
    }

    public void anterior() {
       if(this.nodoActual   != null && this.nodoActual.getPrevNodo() !=null){
            this.nodoActual=this.nodoActual.getPrevNodo();
             reproducir((Cancion)this.nodoActual.getNodoInfo());
       }else
        System.out.println("no hay cancion cancion previa");
    }

    //control de sliders 
    // MÉTODO ÚNICO: Se ejecuta UNA sola vez al arrancar la Ventana APP
    public void setControlesInterfaz(Slider volumen, Slider progreso, Label tActual, Label tTotal, ImageView img,PanelControles panel) {
        this.SliderVolumen = volumen;
        this.SliderProgreso = progreso;
        this.TActual = tActual;
        this.TTotal = tTotal;
       // CargaPortada((Cancion)nodoActual.getNodoInfo(),img);
        this.img = img;
        //pasamos la comunicacion la interfaz del panel de contorl de botones
        this.panel=panel;

        this.SliderVolumen.valueProperty().addListener((obs,oldVal,newVal)->{
            if(mediaPlayer!=null){
                mediaPlayer.setVolume(newVal.doubleValue());
            }
        });
        /*metodo para detectar el click para arrastar el slider */
        this.SliderProgreso.setOnMousePressed(e -> {
            usuarioMovioSlider = true;
        });
        /*una vez suelta el progreso se actualiza en esa direccion */
        this.SliderProgreso.setOnMouseClicked(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(SliderProgreso.getValue()));
            }
            usuarioMovioSlider = false;
        });
        
        this.SliderProgreso.valueChangingProperty().addListener((obs,wasChanging,isChanging)->{
            if(isChanging){
                usuarioMovioSlider=true;
            }else {
                if(mediaPlayer!=null){
                mediaPlayer.seek(Duration.seconds(SliderProgreso.getValue()));
            }
            usuarioMovioSlider=false;
        }
        });

        this.SliderProgreso.valueProperty().addListener((obs,oldVal,newVal)->{
            if(usuarioMovioSlider){
                double segActuales = newVal.doubleValue();
                int mins=(int)segActuales/60;
                int seg=(int)segActuales%60;
                TActual.setText(String.format("%02d:%02d",mins,seg));
            }
        });
    }
    
    //captura de portada 
    public void CargaPortada(Cancion cancionActual, ImageView img) {
        this.img = img;
        if (this.img == null) return;
        if (cancionActual == null) {
            cargarPortadaPorDefecto();
            return;
        }
        cargarPortadaPorDefecto();
        //this.img.setImage(null); // Reseteamos visualmente mientras carga
        try{
        String source = cancionActual.getRuta();
        Media mediaPortada = new Media(new File(source).toURI().toString());
        
        mediaPortada.getMetadata().addListener((MapChangeListener<String,Object>) change -> {
            if (change.wasAdded()) {
                String clave = change.getKey();
                Object value = change.getValueAdded();
                if("image".equals(clave) && value instanceof Image) {
                    this.img.setImage((Image)value);
                }
            }
        });
    }catch(Exception e){
        System.out.println("error al procesar el metadato");
        cargarPortadaPorDefecto();
    }
    }
    // Método centralizado para restaurar el vinilo predefinido
    // Método centralizado para restaurar el vinilo predefinido
    public void cargarPortadaPorDefecto() {
        try {
            // 1. Cargamos el flujo del archivo de forma segura desde el paquete actual
            var stream = getClass().getResourceAsStream("music-plate-clipart-md.png");
            if (stream != null) {
                // 2. Cargamos la imagen pasándole dimensiones por defecto (300x300) y activando el suavizado.
                // Esto evita que los bindings de VentanaAPP dividan por cero al arrancar.
                Image imgDefault = new Image(stream, 300, 300, true, true);
                
                if (this.img != null) {
                    this.img.setImage(imgDefault);
                    System.out.println("img por defecto.");
                }
            } else {
                System.out.println("No se encontró el archivo 'defaul.png' en el paquete ReproductorNuevo.");
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el vinilo por defecto: " + e.getMessage());
            if (this.img != null) this.img.setImage(null);
        }
    }
    //para que sepa donde esta cuando quiera pasar al siguiente cancion
    public void setNodoActual(NodoDoble nodo) {
    this.nodoActual = nodo;
    }
}