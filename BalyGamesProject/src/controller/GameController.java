package controller;

import datamodels.Game;
import server.Response;
import services.GameService;
import services.PriceComparisonService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController {
    private GameService gameService;
    private PriceComparisonService priceService;
    private final Gson gson = new Gson();

    public GameController(GameService gameService, PriceComparisonService priceService) {
        this.gameService = gameService;
        this.priceService = priceService;
    }

    public Response execute(String action, Object data) {
        // המרת המידע לאובייקט Game בצורה בטוחה יותר
        Game gameData = null;
        if (data instanceof LinkedTreeMap) {
            try {
                String json = gson.toJson(data);
                gameData = gson.fromJson(json, Game.class);
            } catch (Exception e) {
                System.err.println("[SERVER ERROR] Conversion failed: " + e.getMessage());
            }
        } else if (data instanceof Game) {
            gameData = (Game) data;
        }

        switch (action) {
            case "game/getAll":
                List<Game> gamesList = gameService.getAllGames();
                // הדפסת לוג לבדיקה - אם מודפס 0, הבעיה היא בטעינת הקובץ בשרת
                System.out.println("[SERVER LOG] getAllGames found: " + gamesList.size() + " games.");

                return new Response("Success", gamesList);


            case "game/update":
                if (gameData != null && gameData.getGameName() != null) {
                    gameService.addNewGame(gameData);
                    return new Response("Success", "Game '" + gameData.getGameName() + "' updated successfully");
                }
                return new Response("Error", "Invalid game data or missing name");

            case "game/delete":
                if (gameData != null && gameData.getGameName() != null) {
                    gameService.deleteGame(gameData.getGameName());
                    return new Response("Success", "Game deleted successfully");
                }
                return new Response("Error", "Missing game name for deletion");

            case "game/price":
                // בדיקה מפורטת של תוכן האובייקט שהתקבל
                if (gameData != null) {
                    String name = gameData.getGameName();
                    System.out.println("[SERVER LOG] Searching price for game: '" + name + "'");

                    if (name != null && !name.trim().isEmpty()) {
                        String priceResult = priceService.getCheapestStore(name) + "$.";

                        // בדיקת תוצאת האלגוריתם (Dijkstra/A*)
                        if (priceResult != null && !priceResult.equals("GAME NOT FOUND")) {
                            return new Response("Success", priceResult);
                        }
                        return new Response("Error", "No stores or prices found for: " + name);
                    }
                }
                // הודעה זו תוחזר אם gameName התקבל כ-null למרות שהאובייקט קיים
                return new Response("Error", "Game name missing from request");

            default:
                return new Response("Error", "Unknown action: " + action);
        }
    }
}