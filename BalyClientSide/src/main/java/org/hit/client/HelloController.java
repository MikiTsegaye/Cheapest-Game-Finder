package org.hit.client;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hit.client.model.Game;
import org.hit.client.model.Request;
import org.hit.client.model.Response;
import org.hit.client.network.Client;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloController {

    @FXML private ComboBox<String> gamesComboBox; // ה-fx:id ששמת ב-Scene Builder
    @FXML private Label priceResultLabel;

    @FXML private TextField adminGameName;
    @FXML private TextField adminGenre;
    @FXML private TextField storeNameField;
    @FXML private TextField priceField;
    @FXML private TextArea fullInventoryArea;
    private boolean isAlertShowing = false; // משתנה למניעת הצפת חלונות

    private final Client networkClient = new Client("localhost", 5000);

    // מתודה שפועלת אוטומטית כשהמסך נטען
    public void initialize() {
        gamesComboBox.setPromptText("Choose a game...");
        System.out.println("[CLIENT LOG] Controller Initialized. Loading games once...");
        loadGamesFromServer();
    }

    // לוגיקה לטעינת רשימת המשחקים מהשרת
    @FXML
    private void loadGamesFromServer() {
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "game/getAll");
        Request<String> request = new Request<>(headers, "");

        new Thread(() -> {
            Response response = networkClient.sendRequest(request);
            if (response != null && "Success".equals(response.getStatus())) {
                try {
                    // המרה בטוחה של ה-body (LinkedTreeMap) לרשימת שמות
                    com.google.gson.Gson gson = new com.google.gson.Gson();
                    String jsonBody = gson.toJson(response.getBody());
                    java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<List<Game>>(){}.getType();
                    List<Game> gamesList = gson.fromJson(jsonBody, listType);

                    if (gamesList != null) {
                        List<String> names = new ArrayList<>();
                        for (Game g : gamesList) {
                            if (g.getGameName() != null) {
                                names.add(g.getGameName());
                            } else {
                                // אם זה מודפס, שמות השדות ב-Game.java בלקוח ובשרת לא תואמים!
                                System.out.println("[CLIENT LOG] Warning: Game object received with null name.");
                            }
                        }
                        Platform.runLater(() -> gamesComboBox.setItems(FXCollections.observableArrayList(names)));
                    }
                } catch (Exception e) {
                    // במקרה של שגיאת פענוח, נדפיס ללוג ולא נקפיץ חלון שיכול ליצור לולאה
                    System.err.println("Parsing error: " + e.getMessage());
                }
            } else {
                // שים לב: הדפס רק לוג במקום להקפיץ Alert אם יש חשש ללולאה
                System.err.println("[CLIENT LOG] Failed to fetch games. Standing by...");
            }
        }).start();
    }

    // לחיצה על כפתור ה-Refresh החדש
    @FXML
    private void onRefreshClick() {
        loadGamesFromServer();
    }

    @FXML
    private void onRefreshInventory() {
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "game/getAll");
        Request<String> request = new Request<>(headers, "");

        new Thread(() -> {
            Response response = networkClient.sendRequest(request);
            if (response != null && "Success".equals(response.getStatus())) {
                // פענוח הרשימה
                com.google.gson.Gson gson = new com.google.gson.Gson();
                String jsonBody = gson.toJson(response.getBody());
                java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<List<Game>>(){}.getType();
                List<Game> gamesList = gson.fromJson(jsonBody, listType);

                StringBuilder sb = new StringBuilder();
                for (Game game : gamesList) {
                    sb.append("🎮 ").append(game.getGameName().toUpperCase()).append("\n");
                    if (game.getStorePrices() != null) {
                        game.getStorePrices().forEach((store, price) ->
                                sb.append("   - ").append(store).append(": $").append(price).append("\n"));
                    }
                    sb.append("----------------------------\n");
                }

                Platform.runLater(() -> fullInventoryArea.setText(sb.toString()));
            } else {
                Platform.runLater(() -> fullInventoryArea.setText("Error: Could not retrieve inventory."));
            }
        }).start();
    }

    @FXML
    private void onSearchClick() {
        String selectedGame = gamesComboBox.getValue();

        // לוג לבדיקה - וודא שמופיע שם המשחק בקונסול
        System.out.println("[CLIENT LOG] Searching for: " + selectedGame);

        if (selectedGame == null || selectedGame.isEmpty()) {
            showAlert("Selection Error", "Please select a game first.");
            return;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("action", "game/price");

        // יצירת אובייקט Game עם השם שנבחר
        Game gameToSearch = new Game(selectedGame, "");
        Request<Game> request = new Request<>(headers, gameToSearch);

        sendRequest(request, response -> {
            Platform.runLater(() -> {
                if (response != null && "Success".equals(response.getStatus())) {
                   showAlert("The cheapest store found is: " ,response.getBody() + "");
                } else {
                    // כאן מופיעה הודעת ה-Game name missing
                    priceResultLabel.setText("Error: " + (response != null ? response.getBody() : "No response"));
                }
            });
        });
    }

    private void sendRequest(Request<Game> request, java.util.function.Consumer<Response> callback) {
        new Thread(() -> {
            Response response = networkClient.sendRequest(request);
            if (response != null) {
                callback.accept(response);
            } else {
                Platform.runLater(() -> showAlert("Network Error", "Could not connect to server."));
            }
        }).start();
    }

    private void showAlert(String title, String content) {
        if (isAlertShowing) return; // אם כבר יש חלון פתוח, אל תפתח עוד אחד

        isAlertShowing = true;
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);

            // ברגע שהמשתמש סוגר את החלון, נאפשר פתיחת חלון חדש בעתיד
            alert.showAndWait();
            isAlertShowing = false;
        });
    }
}