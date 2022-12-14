package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jul 10 16:23:48 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jul 10 16:23:48 CST 2022
     */
    int insert(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jul 10 16:23:48 CST 2022
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jul 10 16:23:48 CST 2022
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jul 10 16:23:48 CST 2022
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jul 10 16:23:48 CST 2022
     */
    int updateByPrimaryKey(ActivityRemark record);
    /**
     * @param  "aid
     * 查找该活动对应的备注
     */
    List<ActivityRemark> findActivityRemarkListByAid(String id);
    int insertRemark(ActivityRemark activityRemark);
    /**
     * @param "activityRemark
     * 修改选择的备注
     */
    int updateARemarkByRemark(ActivityRemark activityRemark);
    /***
     * @param  "id
     * 删除选择的备注
     */
    int deleteARemarkById(String id);

}