package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.commons.constant.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.util.DateUtils;
import com.bjpowernode.crm.commons.util.ExcelDownUtils;
import com.bjpowernode.crm.commons.util.UUIDUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.pojo.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class ActivityController {
    @Autowired
    ActivityService activityService;
    @Autowired
    UserService userService;
    @Autowired
    ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.findUsers();
        request.setAttribute("userList",users);
        return "workbench/activity/index";
    }
    @RequestMapping("/workbench/activity/saveActivity.do")
    public @ResponseBody Object saveActivity(Activity activity, HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user=(User)session.getAttribute(Contants.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateBy(user.getId());
        activity.setCreateTime(DateUtils.formateDate(new Date()));
        int flag=activityService.saveActivity(activity);
        if(flag>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("添加失败");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/queryActivities.do")
    public @ResponseBody Object queryActivities(int pageNo,int pageNum,String name,String owner,String startDate,String endDate)
    {
        ReturnObject returnObject = new ReturnObject();
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> Remap = new HashMap<>();
        map.put("begin",(pageNo-1)*pageNum);
        map.put("num",pageNum);
        map.put("owner",owner);
        map.put("name",name);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        List<Activity> activities=activityService.findActivitiesByPageAndNum(map);
        int count=activityService.queryAllActivityCount(map);
        Remap.put("activitiesList",activities);
        Remap.put("count",count);
        return Remap;
    }
    @RequestMapping("/workbench/activity/editActivity.do")
    public @ResponseBody Object editActivity(Activity activity){
        ReturnObject returnObject = new ReturnObject();
        int t=activityService.updateActivityById(activity);
        if(t>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("修改失败");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/removeActivityList.do")
    public @ResponseBody Object removeActivityList(String[] id){
        ReturnObject returnObject = new ReturnObject();
        int t=activityService.deleteActivitiesByIds(id);
        if(t>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws IOException {
        List<Activity> activities = activityService.findAllActivity();
        ExcelDownUtils.down(response,activities);
    }
    @RequestMapping("/workbench/activity/exportActivity.do")
    public void exportActivity(String id[],HttpServletResponse response) throws IOException {
        List<Activity> activities=activityService.findSpecificActivityByIds(id);
        ExcelDownUtils.down(response,activities);
    }
    @RequestMapping("/workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile, HttpSession session) throws IOException {
        ArrayList<Activity> arrayList = new ArrayList<>();
        ReturnObject returnObject = new ReturnObject();
        User user=(User) session.getAttribute(Contants.SESSION_USER);
        System.out.println(activityFile);
        InputStream is = activityFile.getInputStream();
        HSSFWorkbook wb=new HSSFWorkbook(is);
        //根据wb获取HSSFSheet对象，封装了一页的所有信息
        HSSFSheet sheet=wb.getSheetAt(0);//页的下标，下标从0开始，依次增加
        //根据sheet获取HSSFRow对象，封装了一行的所有信息
        HSSFRow row=null;
        HSSFCell cell=null;
        Activity activity=null;
        for(int i=1;i<=sheet.getLastRowNum();i++){
            row=sheet.getRow(i);
            activity=new Activity();
            for(int j=0;j<row.getLastCellNum();j++){
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateBy(user.getId());
                activity.setCreateTime(DateUtils.formateDate(new Date()));
                cell=row.getCell(j);
                if(j==0){
                    activity.setName(cell.getStringCellValue());
                }else if(j==1){
                    activity.setStartDate(cell.getStringCellValue());
                }else if(j==2){
                    activity.setEndDate(cell.getStringCellValue());
                }else if(j==3){
                    activity.setCost(cell.getStringCellValue());
                }else {
                    activity.setDescription(cell.getStringCellValue());
                }
                System.out.println(activity);
            }
            arrayList.add(activity);
        }
        int t=activityService.saveActivitiesByList(arrayList);
        if(t>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(t);
        }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("导入失败");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/detail.do")
    public String detail(String id,HttpServletRequest request){
        Activity activity = activityService.selectById(id);
        List<ActivityRemark> remarkList = activityRemarkService.findARemarkListByAid(id);
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        return "workbench/activity/detail";
    }
    @RequestMapping("/workbench/activity/saveActivityRemark.do")
    public @ResponseBody Object saveActivityRemark(ActivityRemark activityRemark,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user=(User)session.getAttribute(Contants.SESSION_USER);
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateBy(user.getId());
        activityRemark.setCreateTime(DateUtils.formateDate(new Date()));
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_FAIL);
        int t=activityRemarkService.saveActivityRemark(activityRemark);
        if(t>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityRemark);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("插入失败");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/editActivityRemark.do")
    public @ResponseBody Object editActivityRemark(ActivityRemark activityRemark,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user=(User)session.getAttribute(Contants.SESSION_USER);
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_TRUE);
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditTime(DateUtils.formateDate(new Date()));
        int t=activityRemarkService.editActivityRemarkByAR(activityRemark);
        if(t>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityRemark);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/deleteActivityRemark.do")
    public @ResponseBody Object deleteActivityRemark(String id){
        ReturnObject returnObject = new ReturnObject();
        int t=activityRemarkService.removeActivityRemarkById(id);
        if(t>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }
}
