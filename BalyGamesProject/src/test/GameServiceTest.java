package test;

import dao.DaoFileImpl;
import dao.IDao;
import datamodels.Customer;
import datamodels.Game;
import datamodels.Store;
import services.CustomerService;
import services.GameService;
import services.PriceComparisonService;
import main.java.com.hit.algorithm.IFindPrice;
import main.java.com.hit.algorithm.DijsktraFindPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.StoreService;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class GameServiceTest {
    private IDao<String, Game> gameDao;
    private IDao<Integer, Customer> customerDao;
    private IDao<String, Store> storeDao;
    private IFindPrice priceAlgo;
    private GameService gameService;
    private CustomerService customerService;
    private StoreService storeService;
    private PriceComparisonService priceComparisonService;
    private final String GAMES_PATH = "src/resources/games.txt";
    private final String CUSTOMERS_PATH = "src/resources/customers.txt";
    private final String STORES_PATH = "src/resources/stores.txt";

    @BeforeEach
    public void setUp() {

        gameDao = new DaoFileImpl<>(GAMES_PATH);
        storeDao = new DaoFileImpl<>(STORES_PATH);
        customerDao = new DaoFileImpl<>(CUSTOMERS_PATH);
        priceAlgo = new DijsktraFindPrice();
        gameService = new GameService(gameDao);
        customerService = new CustomerService(customerDao);
        storeService = new StoreService(storeDao);
        priceComparisonService = new PriceComparisonService(gameDao, priceAlgo);
    }

    @Test
    public void testFullSystemIntegration() {
        Game game = new Game("Elden Ring", "RPG");
        game.addPrice("Steam", 60.0);
        game.addPrice("Epic", 45.0);

        gameService.addNewGame(game);

        Game savedGame = gameService.findGame("Elden Ring");
        assertNotNull(savedGame, "Game should be saved and found in DAO");

        String cheapestResult = priceComparisonService.getCheapestStore("Elden Ring");
        assertNotNull(cheapestResult);

        assertTrue(cheapestResult.contains("Epic"), "Should identify Epic as cheapest store");
        assertTrue(cheapestResult.contains("45"), "Should return the correct minimum price");

        System.out.println("Integration Test Result: " + cheapestResult);
    }

    @Test
    public void testComparisonService() {
        String result = priceComparisonService.getCheapestStore("NonExistentGame");
        assertEquals("GAME NOT FOUND", result);
    }

    @Test
    public void testCustomerService(){
        Customer customer = new Customer(1,"Mi@gm.com");
        customerService.registerCustomer(customer);
        Customer savedCustomer = customerService.findCustomer(1);
        assertNotNull(savedCustomer, "Customer should be saved and found in DAO");
        customerService.deleteCustomer(customer);
        Customer deletedCustomer = customerService.findCustomer(1);
        assertNull(deletedCustomer, "Customer should not be found in DAO");
    }

    @Test
    public void testStoreService(){
        Store store = new Store(1,"Steam");
        storeService.addStore(store);
        Store addedStore = storeService.findStore(store.getStoreName());
        assertNotNull(addedStore, "Store should be saved and found in DAO");
        storeService.deleteStore(store);
        Store deletedStore = storeService.findStore(store.getStoreName());
        assertNull(deletedStore, "Store should not be found in DAO");
    }

    @Test
    public void testGameService(){
        Game game = new Game("Elden Ring", "RPG");
        gameService.addNewGame(game);
        Game savedGame = gameService.findGame("Elden Ring");
        assertNotNull(savedGame, "Game should be saved and found in DAO");
        gameService.deleteGame(game.getGameName());
        Game deletedGame = gameService.findGame("Elden Ring");
        assertNull(deletedGame, "Game should not be found in DAO");
    }
}