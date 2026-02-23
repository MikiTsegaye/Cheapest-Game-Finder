package datamodels;

import java.io.Serializable;

public class Customer extends AbstractModel implements  Serializable {
    private int customerId;
    private String email;

    public Customer(int customerId, String email) {
        this.customerId = customerId;
        this.email = email;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public Integer getId() {
        return customerId;
    }
}
