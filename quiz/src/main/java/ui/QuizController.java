package ui;

import db.DBConnection;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.util.Duration;
import model.Question;

import java.sql.*;
import java.util.*;

public class QuizController {

    @FXML
    private Label questionLabel, timerLabel;
    @FXML
    private RadioButton optA, optB, optC, optD;
    private ToggleGroup options;
    @FXML
    private Button nextBtn, exitBtn, restartBtn;

    private List<Question> questionList = new ArrayList<>();
    private Iterator<Question> iterator;
    private Question current;
    private int score = 0;
    public static String playerName;
    private Timeline timeline;
    private final int TIME_PER_Q = 15;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        // Initialize ToggleGroup programmatically and assign to radio buttons
        options = new ToggleGroup();
        optA.setToggleGroup(options);
        optB.setToggleGroup(options);
        optC.setToggleGroup(options);
        optD.setToggleGroup(options);

        loadRandomQuestions();
        try {
            loadNext();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        exitBtn.setOnAction(e -> ((Stage) exitBtn.getScene().getWindow()).close());
        restartBtn.setOnAction(e -> restartGame());
        nextBtn.setOnAction(e -> {
            checkAnswer();
            try {
                loadNext();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void loadRandomQuestions() {
        String sql = "SELECT * FROM questions ORDER BY RAND() LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                questionList.add(new Question(
                        rs.getInt("id"), rs.getString("question"),
                        rs.getString("option_a"), rs.getString("option_b"),
                        rs.getString("option_c"), rs.getString("option_d"),
                        rs.getString("correct_option").charAt(0)
                ));
            }
            System.out.println("Loaded " + questionList.size() + " questions from DB.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        iterator = questionList.iterator();
    }

    private void loadNext() throws Exception {
        if (timeline != null) timeline.stop();
        if (!iterator.hasNext()) {
            saveScore();
            showEnd();
            return;
        }
        current = iterator.next();
        questionLabel.setText(current.question);
        optA.setText(current.a);
        optB.setText(current.b);
        optC.setText(current.c);
        optD.setText(current.d);

        // Clear selection
        options.selectToggle(null);

        startTimer();
    }

    private void startTimer() {
        timerLabel.setText("Time: " + TIME_PER_Q);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            int t = Integer.parseInt(timerLabel.getText().split(": ")[1]);
            if (--t < 0) {
                checkAnswer();
                try {
                    loadNext();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                timerLabel.setText("Time: " + t);
            }
        }));
        timeline.setCycleCount(TIME_PER_Q + 1);
        timeline.play();
    }

    private void checkAnswer() {
        if (timeline != null) timeline.stop();
        Toggle selectedToggle = options.getSelectedToggle();
        if (selectedToggle != null) {
            char ans = switch (((RadioButton) selectedToggle).getId()) {
                case "optA" -> 'A';
                case "optB" -> 'B';
                case "optC" -> 'C';
                default -> 'D';
            };
            if (ans == current.correct) score++;
        }
    }

    private void saveScore() {
        String sql = "INSERT INTO scores (name, score) VALUES (?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, playerName);
            p.setInt(2, score);
            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showEnd() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/end.fxml"));
        Parent root = loader.load();
        EndController ec = loader.getController();
        ec.setScore(score);

        Platform.runLater(() -> {
            stage.getScene().setRoot(root);
        });
    }

    private void restartGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/start.fxml"));
            Stage currentStage = (Stage) restartBtn.getScene().getWindow();
            currentStage.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
