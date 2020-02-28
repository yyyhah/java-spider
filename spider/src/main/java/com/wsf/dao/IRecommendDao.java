package com.wsf.dao;

/**
 * 对数据库中的recommend表进行操作
 */
public interface IRecommendDao {

    /**
     * 插入一条数据
     */
    void doInsert();

    /**
     * 分页查询数据
     */
    void doFindAll();
}
