package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.Transaction;

public interface TranService {
    int saveTran(Transaction transaction);
    int updateTranForStage(Transaction transaction);
    Transaction findTransactionById(String id);
    Transaction findTransactionForUpdateById(String id);
}
