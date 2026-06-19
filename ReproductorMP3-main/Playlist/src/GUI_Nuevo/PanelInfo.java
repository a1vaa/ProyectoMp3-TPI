package GUI_Nuevo;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PanelInfo extends VBox {
    private Label labelCancionActual;
    private Slider sliderProgreso;
    private Slider sliderVolumen;
    private Label lblTiempoActual;
    private Label lblTiempoTotal;

    public PanelInfo(){
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-padding: 10;");

        //incializa el label principal
        this.labelCancionActual = new Label("Sin produccion Actual");
        this.labelCancionActual.setStyle(
        "-fx-text-fill: " + VentanaAPP2.COLOR_TEXTO + ";" +
        "-fx-font-size: 20px;" +
        "-fx-font-weight: bold;"
        );
        //arma la fila de Progreso(TIEMPO-BARRA-DURACION)
        HBox filaProgreso = new HBox(10);
        filaProgreso.setAlignment(Pos.CENTER);
        
        this.lblTiempoActual=new Label("00:00");
        this.lblTiempoActual.setStyle("-fx-text-fill: " + VentanaAPP2.COLOR_TEXTO + ";");
        this.sliderProgreso=new Slider(0,100,0);
        this.sliderProgreso.setMinWidth(300);//para que se estire en la pantalla
        this.lblTiempoTotal=new Label("00:00");
        this.lblTiempoTotal.setStyle("-fx-text-fill: " + VentanaAPP2.COLOR_TEXTO + ";");
        filaProgreso.getChildren().addAll(lblTiempoActual,sliderProgreso,lblTiempoTotal);

        HBox filaVolumen = new HBox(10);
        filaVolumen.setAlignment(Pos.CENTER); 
        Label lblVol = new Label("🔊 vol:");
        lblVol.setStyle("-fx-text-fill: "+ VentanaAPP2.COLOR_TEXTO+";");
        this.sliderVolumen=new Slider(0,1,0.2);//rango de 0.0 a 1.0 (50%por def)
        this.sliderVolumen.setMaxWidth(100);
        filaVolumen.getChildren().addAll(lblVol,sliderVolumen);

        this.getChildren().addAll(this.labelCancionActual,filaProgreso,filaVolumen);
    }

    public Label getLabelCancionActual(){return this.labelCancionActual;}
    public Slider getSliderProgreso(){return this.sliderProgreso;}
    public Slider getSliderVolumen(){return this.sliderVolumen;}
    public Label getTiempoActual(){return this.lblTiempoActual;}
    public Label getTiempoTotal(){return this.lblTiempoTotal;}
}
