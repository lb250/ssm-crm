package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activityMapper;
    @Override
    public List<Activity> findActivitiesByPageAndNum(Map map) {
        return activityMapper.selectActivitiesByPageAndNum(map);
    }

    @Override
    public List<Activity> findAllActivity() {
        return activityMapper.selectAllActivities();
    }

    @Override
    public List<Activity> findSpecificActivityByIds(String[] id) {
        return activityMapper.selectActivitiesByIds(id);
    }

    @Override
    public List<Activity> findAllActivityForDetailByClueId(String clueId) {
        return activityMapper.selectAllActivityForDetailByClueId(clueId);
    }

    @Override
    public List<Activity> findAllActivityForAddByMap(Map map) {
        return activityMapper.selectAllActivityForAddByClueIdsAndLikeName(map);
    }

    @Override
    public List<Activity> findActivityListForConvertByName(String name) {
        return activityMapper.selectActivityListByName(name);
    }

    @Override
    public Activity selectById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int saveActivity(Activity activity) {
       return activityMapper.insertSelective(activity);
    }

    @Override
    public int saveActivitiesByList(List<Activity> activityList) {
        return activityMapper.insertActivitiesByList(activityList);
    }

    @Override
    public int queryAllActivityCount(Map map) {
        return activityMapper.selectActivitiesCount(map);
    }

    @Override
    public int updateActivityById(Activity activity) {
        return activityMapper.updateActivityById(activity);
    }

    @Override
    public int deleteActivitiesByIds(String[] id) {
        return activityMapper.deleteActivitiesByIds(id);
    }


}
