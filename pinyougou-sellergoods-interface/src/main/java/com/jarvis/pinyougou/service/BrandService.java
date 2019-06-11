package com.jarvis.pinyougou.service;

import com.jarvis.entity.PageResult;
import com.jarvis.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @CreateDate: 2019/6/2 14:47
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/6/2 14:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface BrandService {
    List<TbBrand> findAll();
    /*
    * 分页
    * */
    public PageResult findPage(int pageNum,int pageSize);

    /*
    * 添加
    * */
    public void add(TbBrand brand);

    public TbBrand findOne(Long id);


    public void update(TbBrand brand);


    public void delete(Long[] ids);

    /*
    * 查找
    * */
    public PageResult findPage(TbBrand brand, int pageNum,int pageSize);

    //查找所有品牌，在下拉列表中显示，id与name以map的方式在list中返回
    public List<Map> selectOptionList();
}
