package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
     List<Clue> findAllClueByPage(Map map);
     Clue findClueForDetailById(String id);
     Boolean convertInformationByClue(Map map);
     int selectAllClueCount(Map map);
     int saveClueByCl(Clue clue);
}
