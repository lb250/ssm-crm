package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.commons.constant.Contants;
import com.bjpowernode.crm.commons.util.DateUtils;
import com.bjpowernode.crm.commons.util.UUIDUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.pojo.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ClueServiceImpl implements ClueService {
    @Autowired
    ClueMapper clueMapper;
    @Autowired
    ClueRemarkMapper clueRemarkMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    ContactsMapper contactsMapper;
    @Autowired
    CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    TransactionRemarkMapper transactionRemarkMapper;
    @Override
    public List<Clue> findAllClueByPage(Map map) {
        return clueMapper.selectAllClueByPage(map);
    }

    @Override
    public Clue findClueForDetailById(String id) {
        return clueMapper.selectClueByForDetailById(id);
    }

    @Override
    public Boolean convertInformationByClue(Map map) {
        try {
            Clue clue = clueMapper.selectClueByForDetailById((String) map.get("clueId"));
            User user=(User)map.get(Contants.SESSION_USER);
            String isCreateTran=(String) map.get("isCreateTran");
            Customer customer = new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(clue.getCompany());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formateDate(new Date()));
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());
            customerMapper.insertCustomerByClue(customer);
            Contacts contacts = new Contacts();
            contacts.setId(UUIDUtils.getUUID());
            contacts.setOwner(clue.getOwner());
            contacts.setCustomerId(customer.getId());
            contacts.setFullname(clue.getFullname());
            contacts.setAppellation(clue.getAppellation());
            contacts.setEmail(clue.getEmail());
            contacts.setMphone(clue.getMphone());
            contacts.setJob(clue.getJob());
            contacts.setCreateBy(user.getId());
            contacts.setCreateTime(DateUtils.formateDate(new Date()));
            contacts.setDescription(clue.getDescription());
            contacts.setContactSummary(clue.getContactSummary());
            contacts.setNextContactTime (clue.getNextContactTime());
            contacts.setAddress(clue.getAddress());
            contactsMapper.insert(contacts);
            List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkList(clue.getId());
            CustomerRemark customerRemark = null;
            ContactsRemark contactsRemark=null;
            ArrayList<CustomerRemark> customerRemarks = new ArrayList<>();
            ArrayList<ContactsRemark> contactsRemarks = new ArrayList<>();
            if(clueRemarkList!=null && clueRemarkList.size()>0)
            {
                for(ClueRemark clueRemark:clueRemarkList){
                    customerRemark=new CustomerRemark();
                    customerRemark.setId(UUIDUtils.getUUID());
                    customerRemark.setNoteContent(clueRemark.getNoteContent());
                    customerRemark.setCreateBy(clueRemark.getCreateBy());
                    customerRemark.setCreateTime(clueRemark.getCreateTime());
                    customerRemark.setEditBy(clueRemark.getEditBy());
                    customerRemark.setEditTime(clueRemark.getEditTime());
                    customerRemark.setEditFlag(clueRemark.getEditFlag());
                    customerRemark.setCustomerId(customer.getId());
                    customerRemarks.add(customerRemark);
                    contactsRemark=new ContactsRemark();
                    contactsRemark.setId(UUIDUtils.getUUID());
                    contactsRemark.setNoteContent(clueRemark.getNoteContent());
                    contactsRemark.setCreateBy(clueRemark.getCreateBy());
                    contactsRemark.setCreateTime(clueRemark.getCreateTime());
                    contactsRemark.setContactsId(contacts.getId());
                    contactsRemark.setEditBy(clueRemark.getEditBy());
                    contactsRemark.setEditTime(clueRemark.getEditTime());
                    contactsRemark.setEditFlag(clueRemark.getEditFlag());
                    contactsRemarks.add(contactsRemark);
                }
                customerRemarkMapper.insertCustomerRemarkByList(customerRemarks);
                contactsRemarkMapper.insertContactsRemarkByList(contactsRemarks);
            }
            List<ClueActivityRelation> clueActivityRelations = clueActivityRelationMapper.selectClueActivityRelationList(clue.getId());
            ContactsActivityRelation contactsActivityRelation=null;
            List<ContactsActivityRelation> contactsActivityRelations = new ArrayList<>();
            if(clueActivityRelations!=null&&clueActivityRelations.size()>0)
            {
                for(ClueActivityRelation clueActivityRelation:clueActivityRelations){
                    contactsActivityRelation=new ContactsActivityRelation();
                    contactsActivityRelation.setId(UUIDUtils.getUUID());
                    contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                    contactsActivityRelation.setContactsId(contacts.getId());
                    contactsActivityRelations.add(contactsActivityRelation);
                }
                contactsActivityRelationMapper.insertContactsActivityRelationByClue(contactsActivityRelations);
            }
            Transaction transaction = null;
            if(isCreateTran.equals("true")){
                transaction = new Transaction();
                transaction.setId(UUIDUtils.getUUID());
                transaction.setMoney((String) map.get("money"));
                transaction.setName((String)map.get("name"));
                transaction.setExpectedDate((String)map.get("expectedDate"));
                transaction.setStage((String)map.get("stage"));
                transaction.setSource(clue.getSource());
                transaction.setActivityId((String)map.get("activityId"));
                transaction.setContactsId(contacts.getId());
                transaction.setCustomerId(customer.getId());
                transaction.setCreateBy(user.getId());
                transaction.setCreateTime(DateUtils.formateDate(new Date()));
                transaction.setDescription(clue.getDescription());
                transaction.setContactSummary(clue.getContactSummary());
                transaction.setNextContactTime(clue.getNextContactTime());
                transaction.setOwner(clue.getOwner());
                transactionMapper.insertTransByClue(transaction);
            }
            TransactionRemark transactionRemark=null;
            ArrayList<TransactionRemark> transactionRemarks = new ArrayList<>();
            if(clueRemarkList!=null && clueRemarkList.size()>0)
            {
                for(ClueRemark clueRemark:clueRemarkList){
                    transactionRemark=new TransactionRemark();
                    transactionRemark.setId(UUIDUtils.getUUID());
                    transactionRemark.setCreateBy(clueRemark.getCreateBy());
                    transactionRemark.setEditBy(clueRemark.getEditBy());
                    transactionRemark.setNoteContent(clueRemark.getNoteContent());
                    transactionRemark.setCreateTime(clueRemark.getCreateTime());
                    transactionRemark.setEditTime(clueRemark.getEditTime());
                    transactionRemark.setEditFlag(clueRemark.getEditFlag());
                    transactionRemark.setTranId(transaction.getId());
                    transactionRemarks.add(transactionRemark);
                }
                transactionRemarkMapper.insertTransRemarkByList(transactionRemarks);
            }
            clueRemarkMapper.deleteClueRemarkByClueId(clue.getId());
            clueActivityRelationMapper.deleteClueActivityRelationByClueId(clue.getId());
            clueMapper.deleteByPrimaryKey(clue.getId());
        }catch (RuntimeException e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public int selectAllClueCount(Map map) {
        return clueMapper.selectAllClueCount(map);
    }

    @Override
    public int saveClueByCl(Clue clue) {
        return clueMapper.saveClueByCl(clue);
    }
}
