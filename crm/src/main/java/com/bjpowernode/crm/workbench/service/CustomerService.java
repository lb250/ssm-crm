package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findCustomerByName(String name);
}
