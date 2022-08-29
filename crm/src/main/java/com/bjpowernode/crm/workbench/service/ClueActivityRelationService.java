package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.pojo.ClueActivityRelation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationService {
    int saveClueActivityRelationForAdd(List<ClueActivityRelation> clueActivityRelation);
    int removeClueActivityRelationByMap(Map map);
}
