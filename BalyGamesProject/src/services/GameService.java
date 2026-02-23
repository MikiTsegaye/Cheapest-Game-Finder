package services;

import dao.IDao;
import datamodels.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameService  {
    private IDao<String, Game> gameDao;
    public GameService(IDao<String, Game> gameDao) {
        this.gameDao = gameDao;
    }

    public void addNewGame(Game game)
    {
        if(!game.getGameName().isEmpty()) {
            gameDao.save(game.getGameName(), game);
        }
    }
    public Game findGame(String gameName) {
        if (gameDao.find(gameName) != null) {
            return gameDao.find(gameName);
        }
        return null;
    }


    public void deleteGame(String gameName)
    {
        if(gameDao.find(gameName) != null) {
            gameDao.delete(gameName);
        }
    }

    public List<Game> getAllGames()
    {
        Map<String, Game> allGames = gameDao.findAll();
        return new ArrayList<>(allGames.values());
    }
}
