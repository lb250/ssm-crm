package com.bjpowernode.crm.workbench.service.Impl;


import com.bjpowernode.crm.workbench.mapper.TransactionMapper;
import com.bjpowernode.crm.workbench.pojo.Transaction;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TranService {
    @Autowired
    TransactionMapper transactionMapper;
    @Override
    public int saveTran(Transaction transaction) {
        return transactionMapper.insertTransByClue(transaction);
    }

    @Override
    public int updateTranForStage(Transaction transaction) {
        return transactionMapper.updateByPrimaryKey(transaction);
    }

    @Override
    public Transaction findTransactionById(String id) {
        return transactionMapper.selectTransactionForDetailById(id);
    }

    @Override
    public Transaction findTransactionForUpdateById(String id) {
        return transactionMapper.selectByPrimaryKey(id);
    }
}
