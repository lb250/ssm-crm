package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.pojo.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    ActivityRemarkMapper activityRemarkMapper;
    @Override
    public List<ActivityRemark> findARemarkListByAid(String id) {
        return activityRemarkMapper.findActivityRemarkListByAid(id);
    }

    @Override
    public int saveActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.insertRemark(activityRemark);
    }

    @Override
    public int editActivityRemarkByAR(ActivityRemark activityRemark) {
        return activityRemarkMapper.updateARemarkByRemark(activityRemark);
    }

    @Override
    public int removeActivityRemarkById(String id) {
        return activityRemarkMapper.deleteARemarkById(id);
    }
}
