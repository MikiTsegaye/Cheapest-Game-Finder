package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import datamodels.Game;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DaoFileImpl<ID extends Serializable, T extends Serializable> implements IDao<ID, T> {
    private final String FILE_PATH;
    private Map<ID, T> dataBase;
    private final Gson gson = new Gson();

    public DaoFileImpl(String filePath) {
        this.FILE_PATH = filePath;
        loadFromFile(); // טעינה ראשונית של הנתונים
    }

    private void loadFromFile() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            // הכרח את Gson להשתמש במחלקות הספציפיות שלך
            Type type = new TypeToken<HashMap<String, Game>>() {}.getType();
            Map<String, Game> tempMap = gson.fromJson(reader, type);

            if (tempMap != null) {
                this.dataBase = (Map<ID, T>) tempMap;
            } else {
                this.dataBase = new HashMap<>();
            }
        } catch (IOException e) {
            this.dataBase = new HashMap<>();
        }
    }

    private synchronized void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(dataBase, writer); // שמירה בפורמט JSON קריא
        } catch (IOException e) {
            System.err.println("[SERVER ERROR] Failed to save data: " + e.getMessage());
        }
    }

    @Override
    public boolean save(ID id, T object) {
        dataBase.put(id, object);
        saveToFile();
        return true;
    }

    @Override
    public T find(ID id) {
        loadFromFile(); // ריענון מהקובץ כדי להבטיח נתונים עדכניים
        return dataBase.get(id);
    }

    @Override
    public boolean delete(ID id) {
        if (dataBase.containsKey(id)) {
            dataBase.remove(id);
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public Map<ID, T> findAll() {
        loadFromFile(); // הבטחה שהרשימה עבור ה-ComboBox תהיה עדכנית
        return new HashMap<>(dataBase);
    }

    @Override
    public boolean update(ID id, T value) {
        if (dataBase.containsKey(id)) {
            dataBase.put(id, value);
            saveToFile();
            return true;
        }
        return false;
    }
}