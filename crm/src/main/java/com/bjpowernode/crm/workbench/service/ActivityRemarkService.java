package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> findARemarkListByAid(String id);
    int saveActivityRemark(ActivityRemark activityRemark);
    int editActivityRemarkByAR(ActivityRemark activityRemark);
    int removeActivityRemarkById(String id);
}
