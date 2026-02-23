package services;

import dao.IDao;
import datamodels.Store;

import java.util.Map;

public class StoreService {
    private IDao<String, Store> storeDao;
    public StoreService(IDao<String, Store> storeDao) {
        this.storeDao = storeDao;
    }
    public Store findStore(String storeName) {
        Store store = storeDao.find(storeName);
        if (store != null) {
            return store;
        }
        return null;
    }
    public void addStore(Store store) {
        if(storeDao.find(store.getStoreName()) == null) {
            storeDao.save(store.getStoreName(), store);
        }
    }
    public void deleteStore(Store store) {
        if(storeDao.find(store.getStoreName()) != null) {
            storeDao.delete(store.getStoreName());
        }
    }
    public Map<String, Store> getAllStores() {
        return storeDao.findAll();
    }
}
