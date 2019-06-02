package com.jarvis.pinyougou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jarvis.pinyougou.dao.mapper.TbBrandMapper;
import com.jarvis.pinyougou.pojo.TbBrand;
import com.jarvis.pinyougou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description:品牌接口实现
 * @CreateDate: 2019/6/2 14:49
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/6/2 14:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;


    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }
}
