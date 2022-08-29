package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> findContactsListByName(String name);
}
