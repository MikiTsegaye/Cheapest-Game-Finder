
import dao.DaoFileImpl;
import dao.IDao;
import datamodels.Game;
import main.java.com.hit.algorithm.AStarFindPrice;
import main.java.com.hit.algorithm.IFindPrice;
import main.java.com.hit.algorithm.DijsktraFindPrice;
import services.GameService;
import services.PriceComparisonService;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- BalyGames Application Initializing ---");


        String filePath = "src/resources/games.txt";

        // 2. בניית ה-DAO (שכבת ה-Repository)
        IDao<String, Game> gameDao = new DaoFileImpl<>(filePath);


        IFindPrice dijkstraAlgo = new DijsktraFindPrice();
        IFindPrice AStarAlgo = new AStarFindPrice();


        GameService gameService = new GameService(gameDao);
        PriceComparisonService priceService = new PriceComparisonService(gameDao, dijkstraAlgo);

        Game game3= new Game("Call of duty","FPS");
        game3.addPrice("KSP",25.0);
        game3.addPrice("PSStore",33.3);
        game3.addPrice("netgames",99.99);


        Game game1 = new Game("Elden Ring", "RPG");
        game1.addPrice("Steam", 60.0);
        game1.addPrice("Epic", 45.0);

        Game game2 = new Game("Cyberpunk 2077", "Action");
        game2.addPrice("Steam", 50.0);
        game2.addPrice("GOG", 300.0);

        Game game4= new Game("FIFA 26","Sports");
        game4.addPrice("KSP",41.0);
        game4.addPrice("PSStore",55.3);
        game4.addPrice("netgames",40.99);

        Game game5 = new Game("The Witcher 3", "RPG");
        game5.addPrice("GOG", 15.0);
        game5.addPrice("Steam", 20.0);
        game5.addPrice("Epic", 12.5);

        Game game6 = new Game("Minecraft", "Sandbox");
        game6.addPrice("Microsoft Store", 26.95);
        game6.addPrice("Official Site", 29.99);

        Game game7 = new Game("Hitman","Sandbox");
        game7.addPrice("KSP",13.0);
        game7.addPrice("PSStore",22.0);
        game7.addPrice("netgames",50.0);

        // 6. שמירה לקובץ דרך ה-Service
        gameService.addNewGame(game1);
        gameService.addNewGame(game2);
        gameService.addNewGame(game3);
        gameService.addNewGame(game4);
        gameService.addNewGame(game5);
        gameService.addNewGame(game6);
        gameService.addNewGame(game7);
        System.out.println("Data saved to: " + filePath);



        System.out.println("\n--- Price Comparison Results ---");
        String result1 = priceService.getCheapestStore("Elden Ring");
        String result2 = priceService.getCheapestStore("Cyberpunk 2077");
        String result3 = priceService.getCheapestStore("Call of duty");
        String result4 = priceService.getCheapestStore("FIFA 26");

        System.out.println("Elden Ring: " + result1);
        System.out.println("Cyberpunk 2077: " + result2);
        System.out.println("Call of duty: " + result3);
        System.out.println("FIFA 26: " + result4);

      //System.out.println("\n--- All Games in System ---");
      //Map<String, Game> allGames = gameService.getAllGames();
      //allGames.forEach((name, g) -> System.out.println("- " + name + " (" + g.getGenre() + ")"));



        System.out.println("\n--- Application Finished ---");
    }
}