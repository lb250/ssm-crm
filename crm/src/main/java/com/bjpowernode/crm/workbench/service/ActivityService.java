package com.bjpowernode.crm.workbench.service;



import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {

     List<Activity> findActivitiesByPageAndNum(Map map);
     List<Activity> findAllActivity();
     List<Activity> findSpecificActivityByIds(String[] id);
     List<Activity> findAllActivityForDetailByClueId(String clueId);
     List<Activity> findAllActivityForAddByMap(Map map);
     List<Activity> findActivityListForConvertByName(String name);
     Activity selectById(String id);
     int saveActivity(Activity activity);
     int saveActivitiesByList(List<Activity> activityList);
     int queryAllActivityCount(Map map);
     int updateActivityById(Activity activity);
     int deleteActivitiesByIds(String[] id);

}
