package kong;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kong.ui.MainWindow;

/**
 * Starts Kong's JavaFX user interface.
 */
public class Main extends Application {
    private final Kong kong = new Kong("data/duke.txt");

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane mainWindow = loader.load();
            loader.<MainWindow>getController().setKong(kong);

            stage.setTitle("Kong");
            stage.setMinHeight(420);
            stage.setMinWidth(440);
            stage.setScene(new Scene(mainWindow));
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Kong's main window.", e);
        }
    }
}
