package datamodels;

import java.io.Serializable;

public class Store extends AbstractModel implements Serializable {
    private final int storeId;
    private final String storeName;

    public  Store(int storeId, String storeName)
    {
        this.storeId = storeId;
        this.storeName = storeName;
    }
    public int getStoreId()
    {
        return storeId;
    }
    public String getStoreName()
    {
        return storeName;
    }

    @Override
    public Integer getId() {
        return storeId;
    }
}
