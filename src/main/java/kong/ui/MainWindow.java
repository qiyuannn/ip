package kong.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import kong.Kong;

/**
 * Controls the main chat window and passes user commands to Kong.
 */
public class MainWindow extends AnchorPane {
    private static final String WELCOME_MESSAGE = "Hello, I'm Kong!\nWhat can I do for you?";

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private final Image userImage = loadImage("/images/DaUser.png");
    private final Image kongImage = loadImage("/images/DaKong.png");
    private Kong kong;

    /** Keeps the latest dialog visible whenever the conversation grows. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the application logic used by this controller and displays Kong's greeting.
     *
     * @param kong application logic for processing commands
     */
    public void setKong(Kong kong) {
        assert kong != null : "The main window requires an application instance";
        this.kong = kong;
        dialogContainer.getChildren().add(DialogBox.getKongDialog(WELCOME_MESSAGE, kongImage));
    }

    /** Sends the current text to Kong and adds both sides of the exchange to the chat. */
    @FXML
    private void handleUserInput() {
        assert kong != null : "The application must be injected before input is handled";
        String input = userInput.getText();
        String response = kong.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getKongDialog(response, kongImage));
        userInput.clear();

        if (kong.isExitRequested()) {
            closeAfterGoodbye();
        }
    }

    /** Gives the goodbye message time to appear before closing the application. */
    private void closeAfterGoodbye() {
        userInput.setDisable(true);
        sendButton.setDisable(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> Platform.exit());
        pause.play();
    }

    /** Loads an avatar bundled with the application. */
    private static Image loadImage(String resourcePath) {
        return new Image(MainWindow.class.getResourceAsStream(resourcePath));
    }
}
