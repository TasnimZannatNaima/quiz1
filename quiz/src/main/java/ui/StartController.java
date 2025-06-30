package ui;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class StartController {
    @FXML private TextField nameField;
    @FXML private Button startBtn;
    @FXML
    private void onStart() throws Exception {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter your name.");
            alert.showAndWait();
            return;
        }
        QuizController.playerName = name;

        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/quiz.fxml")));
    }
}
