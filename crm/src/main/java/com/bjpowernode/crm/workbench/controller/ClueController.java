package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.commons.constant.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.util.DateUtils;
import com.bjpowernode.crm.commons.util.UUIDUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.pojo.Clue;
import com.bjpowernode.crm.workbench.pojo.ClueActivityRelation;
import com.bjpowernode.crm.workbench.pojo.DictionaryValue;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.DictionaryValueService;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    ClueService clueService;
    @Autowired
    DictionaryValueService dictionaryValueService;
    @Autowired
    UserService userService;
    @Autowired
    ActivityService activityService;
    @Autowired
    ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.findUsers();
        List<DictionaryValue> appellationList = dictionaryValueService.findDicValueByCode("appellation");
        List<DictionaryValue> clueStateList=dictionaryValueService.findDicValueByCode("clueState");
        List<DictionaryValue> sourceList=dictionaryValueService.findDicValueByCode("source");
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/clue/index";
    }
    @RequestMapping("workbench/clue/queryClue.do")
    public @ResponseBody Object queryClue(Integer pageNo,Integer pageNum,String fullname,String company,String phone,String mphone,String state,String source,String owner){
        Map<String, Object> map = new HashMap<>();
        map.put("begin",(pageNo-1)*pageNum);
        map.put("num",pageNum);
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("source",source);
        map.put("owner",owner);
        List<Clue> clueList = clueService.findAllClueByPage(map);
        int count=clueService.selectAllClueCount(map);
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("count",count);
        return retMap;
    }
    @RequestMapping("workbench/clue/saveClue.do")
    public @ResponseBody Object saveClue(Clue clue, HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user=(User)session.getAttribute(Contants.SESSION_USER);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formateDate(new Date()));
        clue.setOwner(user.getId());
        int t=clueService.saveClueByCl(clue);
        if(t>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("添加失败");
        }
        return returnObject;
    }
    @RequestMapping("workbench/clue/detail.do")
    public String detail(String id,HttpServletRequest request){
        Clue clue = clueService.findClueForDetailById(id);
        List<Activity> activityList = activityService.findAllActivityForDetailByClueId(id);
        request.setAttribute("clue",clue);
        request.setAttribute("activityList",activityList);
        return "workbench/clue/detail";
    }
    @RequestMapping("workbench/clue/searchActivity.do")
    public @ResponseBody Object searchActivity(String clueId,String name){
        HashMap<String, Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("name",name);
        List<Activity> activityList = activityService.findAllActivityForAddByMap(map);
       return activityList;
    }
    @RequestMapping("workbench/clue/addActivityClueRelation.do")
    public @ResponseBody Object addActivityClueRelation(String[] id,String clueId){
        ReturnObject returnObject = new ReturnObject();
        ArrayList<ClueActivityRelation> list = new ArrayList<>();
        ClueActivityRelation clueActivityRelation;
        for(int i=0;i<id.length;i++){
            clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtils.getUUID());
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setActivityId(id[i]);
            list.add(clueActivityRelation);
        }
        int t=clueActivityRelationService.saveClueActivityRelationForAdd(list);
        if(t>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            List<Activity> activityList = activityService.findSpecificActivityByIds(id);
            returnObject.setRetData(activityList);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("添加失败");
        }
        return  returnObject;
    }
    @RequestMapping("workbench/clue/deleteActivityClueRelation.do")
    public @ResponseBody Object  deleteActivityClueRelation(String activityId,String clueId){
        ReturnObject returnObject = new ReturnObject();
        HashMap<String, Object> map = new HashMap<>();
        map.put("activityId",activityId);
        map.put("clueId",clueId);
        int i = clueActivityRelationService.removeClueActivityRelationByMap(map);
        if(i>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }
        return  returnObject;

    }
    @RequestMapping("workbench/clue/convert.do")
    public String convert(String clueId,HttpServletRequest request){
        Clue clue = clueService.findClueForDetailById(clueId);
        List<DictionaryValue> stateList = dictionaryValueService.findDicValueByCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stateList);
        return "workbench/clue/convert";
    }
    @RequestMapping("workbench/clue/selectActivityListForConvert.do")
    public @ResponseBody Object selectActivityListForConvert(String name){
        List<Activity> activityList = activityService.findActivityListForConvertByName(name);
        return activityList;
    }
    @RequestMapping("workbench/clue/convertClueToOther.do")
    public @ResponseBody Object convertClueToOther(HttpSession httpSession,String clueId,String isCreateTran,String money,String expectedDate,String name,String stage,String activityId){
        ReturnObject returnObject = new ReturnObject();
        User user=(User)httpSession.getAttribute(Contants.SESSION_USER);
        Map<String, Object> map = new HashMap<>();
        map.put(Contants.SESSION_USER,user);
        map.put("clueId",clueId);
        map.put("isCreateTran",isCreateTran);
        map.put("money",money);
        map.put("expectedDate",expectedDate);
        map.put("name",name);
        map.put("stage",stage);
        map.put("activityId",activityId);
        Boolean clue = clueService.convertInformationByClue(map);
        if(clue){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("转换失败");
        }
        return returnObject;
    }
}
