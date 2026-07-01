package GUI_Nuevo;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class PanelControles extends HBox {
    private Button btnPrev,btnPlay,btnNext,btnStop,btnAleatorio,btnBucle,btnEliminar;
    public PanelControles(){
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);

        this.btnPrev = new Button("<<");
        this.btnPlay = new Button("PLAY ▶");
        this.btnNext = new Button(">>");
        this.btnStop = new Button("STOP ⏹");
        this.btnAleatorio = new Button("RANDOM 🔀");
        this.btnBucle = new Button("Bucle 🔁");
        this.btnEliminar=new Button("Eliminar");
        
    // Aplicamos el estilo del profesor a cada botón
    String estiloBotones = "-fx-background-color: " + VentanaAPP2.COLOR_BOTON + ";" +
                           "-fx-text-fill: black;" +
                           "-fx-font-weight: bold;" +
                           "-fx-background-radius: 20;"; // Los hace un toque redondeados y modernos

    btnPrev.setStyle(estiloBotones);
    btnPlay.setStyle(estiloBotones);
    btnStop.setStyle(estiloBotones);
    btnAleatorio.setStyle(estiloBotones);
    btnNext.setStyle(estiloBotones);
    btnEliminar.setStyle(estiloBotones);
    btnBucle.setStyle(estiloBotones);
        this.getChildren().addAll(btnEliminar,btnPrev,btnPlay,btnNext,btnStop,btnAleatorio,btnBucle);
    }
    // Getters para que la Ventana principal les asigne las acciones (Eventos)
    public Button getBtnPrev() { return btnPrev; }
    public Button getBtnPlay() { return btnPlay; }
    public Button getBtnStop() { return btnStop; }
    public Button getBtnAleatorio() { return btnAleatorio; }
    public Button getBtnNext() { return btnNext; }
    public Button getBtnbucle() {return btnBucle;}
    public Button getBtnEliminar(){return btnEliminar;}
}
