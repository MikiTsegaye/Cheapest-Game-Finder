package org.hit.client.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Game extends AbstractModel implements Serializable {
    private String genre;
    private String gameName;
    private Map<String, Double> storePrices;

    // בתוך Game.java (גם בלקוח וגם בשרת)
    public Game() {
        this.gameName = ""; // מניעת null ראשוני
        this.genre = "";
        this.storePrices = new HashMap<>();
    }
    public Game(String gameName, String genre)
    {
        this.gameName = gameName;
        this.genre = genre;
        this.storePrices = new HashMap<String, Double>();
    }

    public Map<String, Double> getStorePrices() {
        return storePrices;
    }
    public void setStorePrices(Map<String, Double> storePrices) {
        this.storePrices = storePrices;
    }
    public String getGenre()
    {
        return genre;
    }
    public void setGenre(String genre)
    {
        this.genre = genre;
    }
    public String getGameName()
    {
        return gameName;
    }
    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }
    public void addPrice(String storeName, Double price)
    {
        this.storePrices.put(storeName, price);
    }
    public void removePrice(String storeName)
    {
        this.storePrices.remove(storeName);
    }

    @Override
    public String getId() {
        return gameName;
    }
}

