package ui;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EndController {
    @FXML private Label scoreLabel;
    @FXML private Button playAgainBtn, exitBtn;

    @FXML
    public void setScore(int score) {
        scoreLabel.setText("Your score: " + score);
    }

    @FXML
    private void onPlayAgain() throws Exception {
        Stage stage = (Stage) playAgainBtn.getScene().getWindow();
        stage.getScene().setRoot(javafx.fxml.FXMLLoader.load(getClass().getResource("/start.fxml")));
    }

    @FXML
    private void onExit() {
        ((Stage) exitBtn.getScene().getWindow()).close();
    }
}
