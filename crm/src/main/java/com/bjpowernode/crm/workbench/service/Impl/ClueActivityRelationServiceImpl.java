package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.pojo.ClueActivityRelation;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int saveClueActivityRelationForAdd(List<ClueActivityRelation> clueActivityRelation) {
        return clueActivityRelationMapper.saveClueActivityRelation(clueActivityRelation);
    }

    @Override
    public int removeClueActivityRelationByMap(Map map) {
        return clueActivityRelationMapper.deleteClueActivityRelationById(map);
    }

}
