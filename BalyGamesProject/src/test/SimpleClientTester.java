package test;
import com.google.gson.Gson;
import server.Request;
import server.Response;
import datamodels.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class SimpleClientTester {
    public static void main(String[] args) {
        Game marioWithPrices = new Game("Super Mario", "Platformer");
        marioWithPrices.addPrice("BUG",40.90);
        marioWithPrices.addPrice("Steam", 49.99);

        // שלב 1: בדיקת הוספת משחק (game/add)
        System.out.println("--- Test 1: Adding a Game ---");
        sendRequest("game/add", new Game("Super Mario", "Platformer"));

        // שלב 2: בדיקת שליפת משחק (game/get)
        System.out.println("\n--- Test 2: Getting the Game ---");
        // נשלח אובייקט עם השם שאנחנו מחפשים
        Game gameToFind = new Game("Super Mario", "");
        sendRequest("game/get", gameToFind);

        // שלב 3: עדכון מחירים (game/update)
        System.out.println("\n--- Test 3: Updating the Game ---");
        sendRequest("game/update", marioWithPrices);

        // שלב 2: בדיקת מציאת מחיר זול (game/price)
        System.out.println("\n--- Test 4: Getting Cheapest Price ---");
        sendRequest("game/price", new Game("Super Mario", ""));
    }

    private static void sendRequest(String action, Game gameData) {
        // הגדרת החיבור לשרת (חייב להיות תואם לפורט ב-ServerDriver)
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner reader = new Scanner(socket.getInputStream())) {

            // 1. בניית הבקשה
            Map<String, String> headers = new HashMap<>();
            headers.put("action", action);

            Request<Game> request = new Request<>(headers, gameData);

            // 2. המרה ל-JSON ושליחה
            Gson gson = new Gson();
            String jsonRequest = gson.toJson(request);
            System.out.println("Client sending: " + jsonRequest);
            writer.println(jsonRequest);

            // 3. קבלת התשובה מהשרת
            if (reader.hasNext()) {
                String jsonResponse = reader.nextLine();
                System.out.println("Server returned: " + jsonResponse);

                // המרה חזרה לאובייקט (רק בשביל לראות שזה עובד)
                Response response = gson.fromJson(jsonResponse, Response.class);
                System.out.println("Status: " + response.getStatus());
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Is the server running??");
        }
    }
}
