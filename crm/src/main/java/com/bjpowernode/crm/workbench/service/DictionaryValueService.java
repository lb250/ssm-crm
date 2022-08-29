package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.DictionaryValue;

import java.util.List;

public interface DictionaryValueService {
    List<DictionaryValue> findDicValueByCode(String code);
}
