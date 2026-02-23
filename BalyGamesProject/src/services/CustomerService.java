package services;

import dao.*;
import datamodels.*;

public class CustomerService {
    private IDao<Integer, Customer> customerDao;

    public CustomerService(IDao<Integer, Customer> customerDao) {
        this.customerDao = customerDao;
    }
    public Customer findCustomer(int customerId) {
        if (customerId > 0) {
            return customerDao.find(customerId);
        }
        return null;
    }
    public void  deleteCustomer(Customer customer)
    {
        customerDao.delete(customer.getCustomerId());
    }
    public void registerCustomer(Customer customer)
    {
        if(customer.getCustomerId() > 0 && customer.getEmail() != null)
        {
            customerDao.save(customer.getCustomerId(), customer);
        }
    }
}
