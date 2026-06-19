package GUI_Nuevo;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class PanelOrdenar extends HBox {
    private Button btnTitulo, btnArtista, btnYear;

    public PanelOrdenar() {
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);

        this.btnTitulo = new Button("Ordenar Título");
        this.btnArtista = new Button("Ordenar Artista");
        this.btnYear = new Button("Ordenar Año");
        //estilo de color
        String estiloOrdenar = "-fx-background-color: " + VentanaAPP2.COLOR_BOTON + ";" +
                           "-fx-text-fill: black;" +
                           "-fx-font-weight: bold;";
        btnTitulo.setStyle(estiloOrdenar);
        btnArtista.setStyle(estiloOrdenar);
        btnYear.setStyle(estiloOrdenar);

        this.getChildren().addAll(btnTitulo, btnArtista, btnYear);
    }

    public Button getBtnTitulo() { return btnTitulo; }
    public Button getBtnArtista() { return btnArtista; }
    public Button getBtnYear() { return btnYear; }
}
