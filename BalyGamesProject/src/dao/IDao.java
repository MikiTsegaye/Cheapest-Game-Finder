package dao;

import java.io.Serializable;
import java.util.Map;

public interface IDao <ID extends Serializable, T extends Serializable> {
    boolean save(ID id,T entity);
    T find(ID id);
    boolean delete(ID id);
    Map<ID, T> findAll();
    boolean update(ID id, T entity);
}
