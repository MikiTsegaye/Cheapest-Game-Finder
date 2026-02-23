package server;

import com.google.gson.Gson;
import controller.GameController;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import com.google.gson.internal.LinkedTreeMap;
import datamodels.Game;

public class HandleRequest implements Runnable {
    private Socket socket;
    private GameController controller;
    private final Gson gson = new Gson();

    public HandleRequest(Socket socket, GameController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try (Scanner reader = new Scanner(socket.getInputStream());
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            if (reader.hasNextLine()) {
                String jsonRequest = reader.nextLine();

                // לוג 1: הדפסת ה-JSON הגולמי שהגיע מהלקוח
                System.out.println("\n[SERVER LOG] Raw JSON received: " + jsonRequest);

                Request<Object> request = gson.fromJson(jsonRequest, Request.class);
                String action = request.getHeaders().get("action");
                Object body = request.getBody();

                // לוג 2: פירוט הפעולה והגוף שהתקבל
                System.out.println("[SERVER LOG] Action: " + action);
                System.out.println("[SERVER LOG] Body type before conversion: " + (body != null ? body.getClass().getSimpleName() : "null"));

                Game gameData = null;
                if (body instanceof LinkedTreeMap) {
                    String bodyJson = gson.toJson(body);
                    gameData = gson.fromJson(bodyJson, Game.class);
                    System.out.println("[SERVER LOG] Successfully converted LinkedTreeMap to Game object.");
                }

                Response response = controller.execute(action, gameData);

                // לוג 3: הדפסת התשובה שהשרת עומד לשלוח חזרה
                String jsonResponse = gson.toJson(response);
                System.out.println("[SERVER LOG] Sending response: " + jsonResponse);

                writer.println(jsonResponse);
            }
        } catch (Exception e) {
            System.err.println("[SERVER ERROR] Error handling request: " + e.getMessage());
            e.printStackTrace();
        }
    }
}