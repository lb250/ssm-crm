package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.pojo.Contacts;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    ContactsMapper contactsMapper;
    @Override
    public List<Contacts> findContactsListByName(String name) {
        return contactsMapper.selectContactsListForSearchByName(name);
    }
}
