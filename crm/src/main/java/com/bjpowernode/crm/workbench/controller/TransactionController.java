package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.commons.constant.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.util.DateUtils;
import com.bjpowernode.crm.commons.util.UUIDUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.pojo.*;
import com.bjpowernode.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
public class TransactionController {
   
    @Autowired
    UserService userService;
    @Autowired
    DictionaryValueService dictionaryValueService;
    @Autowired
    ContactsService contactsService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ActivityService activityService;
    @Autowired
    TranService transactionService;
    @RequestMapping("workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        List<DictionaryValue> stage = dictionaryValueService.findDicValueByCode("stage");
        List<DictionaryValue> transactionType = dictionaryValueService.findDicValueByCode("transactionType");
        List<DictionaryValue> source = dictionaryValueService.findDicValueByCode("source");
        request.setAttribute("stageList",stage);
        request.setAttribute("typeList",transactionType);
        request.setAttribute("sourceList",source);
        return "workbench/transaction/index";
    }
    @RequestMapping("workbench/transaction/save.do")
    public String save(HttpServletRequest request){
        List<User> users = userService.findUsers();
        List<DictionaryValue> appellation = dictionaryValueService.findDicValueByCode("appellation");
        List<DictionaryValue> stage = dictionaryValueService.findDicValueByCode("stage");
        List<DictionaryValue> transactionType = dictionaryValueService.findDicValueByCode("transactionType");
        List<DictionaryValue> source = dictionaryValueService.findDicValueByCode("source");
        request.setAttribute("userList",users);
        request.setAttribute("appellationList",appellation);
        request.setAttribute("stageList",stage);
        request.setAttribute("typeList",transactionType);
        request.setAttribute("sourceList",source);
        return "workbench/transaction/save";
    }
    @RequestMapping("workbench/transaction/searchCustomerList.do")
    public @ResponseBody Object searchCustomerList(String customerName){
        List<Customer> customerList = customerService.findCustomerByName(customerName);
        return customerList;
    }
    @RequestMapping("workbench/transaction/searchContactsList.do")
    public @ResponseBody Object searchContactsList(String contactsName){
        List<Contacts> contactsList = contactsService.findContactsListByName(contactsName);
        return contactsList;
    }
    @RequestMapping("workbench/transaction/searchActivityList.do")
    public @ResponseBody Object searchActivityList(String activityName){
        List<Activity> activityList = activityService.findActivityListForConvertByName(activityName);
        return activityList;
    }
    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    public @ResponseBody Object getPossibilityByStage(String stageValue){
        //解析properties配置文件，根据阶段获取可能性
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(stageValue);
        //返回响应信息
        return possibility;
    }
    @RequestMapping("/workbench/transaction/saveTransAction.do")
    public @ResponseBody Object getPossibilityByStage(Transaction transaction, HttpSession httpSession) {
        ReturnObject returnObject = new ReturnObject();
        User user=(User)httpSession.getAttribute(Contants.SESSION_USER);
        transaction.setId(UUIDUtils.getUUID());
        transaction.setCreateBy(user.getId());
        transaction.setCreateTime(DateUtils.formateDate(new Date()));
        int i = transactionService.saveTran(transaction);
        if(i>0)
        {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
        }
        return  returnObject;
    }
    @RequestMapping("/workbench/transaction/detail.do")
    public String detail(HttpServletRequest request,String id){
        Transaction transaction = transactionService.findTransactionById(id);
        List<DictionaryValue> stageList = dictionaryValueService.findDicValueByCode("stage");
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String stage=transaction.getStage();
        String possibility=bundle.getString(stage);
        transaction.setPossibility(possibility);
        request.setAttribute("transaction",transaction);
        request.setAttribute("stageList",stageList);
        return "workbench/transaction/detail";
    }
    @RequestMapping("workbench/transaction/updateTrans.do")
    public @ResponseBody Object updateTrans(String id,String stage){
        ReturnObject returnObject = new ReturnObject();
        Transaction transaction = transactionService.findTransactionForUpdateById(id);
        transaction.setStage(stage);
        int i = transactionService.updateTranForStage(transaction);
        if(i>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
        }
        return returnObject;
    }
}
