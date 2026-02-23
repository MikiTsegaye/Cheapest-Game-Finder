package server;

import controller.GameController;
import dao.DaoFileImpl;
import dao.IDao;
import datamodels.Game;
import main.java.com.hit.algorithm.DijsktraFindPrice;
import main.java.com.hit.algorithm.IFindPrice;
import services.GameService;
import services.PriceComparisonService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private int port;
    private boolean serverUp = true;
    private GameController controller;

    public Server(int port) {
        this.port = port;
        // אתחול שכבת הנתונים והשירותים
        IDao<String, Game> dao = new DaoFileImpl<>("src/resources/games.txt");
        GameService service = new GameService(dao);
        IFindPrice algo = new DijsktraFindPrice();
        PriceComparisonService priceService = new PriceComparisonService(dao, algo);

        // יצירת ה-Controller שמנהל את ה-execute
        this.controller = new GameController(service, priceService);
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (serverUp) {
                Socket clientSocket = serverSocket.accept();
                // טיפול בכל בקשה ב-Thread נפרד
                new Thread(new HandleRequest(clientSocket, controller)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}