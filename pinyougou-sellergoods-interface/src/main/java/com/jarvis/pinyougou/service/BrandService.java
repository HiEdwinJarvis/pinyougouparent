package com.jarvis.pinyougou.service;

import com.jarvis.pinyougou.pojo.TbBrand;

import java.util.List;

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
}
