package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.mapper.DictionaryValueMapper;
import com.bjpowernode.crm.workbench.pojo.DictionaryValue;
import com.bjpowernode.crm.workbench.service.DictionaryValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DictionaryValueServiceImpl implements DictionaryValueService {
    @Autowired
    DictionaryValueMapper dictionaryValueMapper;

    @Override
    public List<DictionaryValue> findDicValueByCode(String code) {
        return dictionaryValueMapper.findDictionaryValueByCode(code);
    }
}
