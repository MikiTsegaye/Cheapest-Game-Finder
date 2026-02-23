package services;

import dao.IDao;
import datamodels.Game;
import main.java.com.hit.algorithm.IFindPrice;
import java.util.HashMap;
import java.util.Map;

public class PriceComparisonService {
    private IDao<String, Game> gameDao;
    private IFindPrice findAlgo;

    public PriceComparisonService(IDao<String, Game> gameDao, IFindPrice findAlgo) {
        this.gameDao = gameDao;
        this.findAlgo = findAlgo;
    }

    public String getCheapestStore(String gameName) {
        // טעינת הנתונים העדכניים מה-DAO
        Map<String, Game> allGames = gameDao.findAll();

        if (!allGames.containsKey(gameName)) {
            System.out.println("[SERVER LOG] Game '" + gameName + "' not found in database.");
            return "GAME NOT FOUND";
        }

        // בניית הגרף לאלגוריתם עם המרה בטוחה של מחירים
        Map<String, Map<String, Double>> graph = new HashMap<>();

        for (Game game : allGames.values()) {
            Map<String, Double> prices = new HashMap<>();
            Map<String, Double> rawPrices = game.getStorePrices();

            if (rawPrices != null) {
                // המרה מפורשת של כל ערך ל-Double כדי למנוע ClassCastException מ-LinkedTreeMap
                rawPrices.forEach((store, price) -> {
                    if (price != null) {
                        // המרת הערך בצורה בטוחה למספר עשרוני
                        prices.put(store, Double.valueOf(price.toString()));
                    }
                });
            }
            graph.put(game.getGameName(), prices);
        }

        // הרצת האלגוריתם (Dijkstra/A*)
        Map<String, Double> cheapest = findAlgo.cheapestPrice(graph, gameName);

        if (cheapest == null || cheapest.isEmpty()) {
            return "GAME NOT FOUND";
        }

        String cheapestStore = null;
        double minPrice = Double.MAX_VALUE;

        for (Map.Entry<String, Double> entry : cheapest.entrySet()) {
            String node = entry.getKey();
            double price = entry.getValue();

            // סינון תוצאות לא רלוונטיות או מחירים אינסופיים
            if (!node.equals(gameName) && price < minPrice && price != Double.MAX_VALUE) {
                minPrice = price;
                cheapestStore = node;
            }
        }

        if (cheapestStore == null) {
            return "GAME NOT FOUND";
        }

        return cheapestStore + " (" + minPrice + ")";
    }
}