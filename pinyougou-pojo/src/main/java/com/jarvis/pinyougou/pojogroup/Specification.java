package com.jarvis.pinyougou.pojogroup;

import com.jarvis.pinyougou.pojo.TbSpecification;
import com.jarvis.pinyougou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:包装类Specification，用于添加规格时，因为同时修改了两张表
 * 所有，做一个包装类用于将页面数据统一传到后端，然后再处理
 * @CreateDate: 2019/6/4 21:18
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/6/4 21:18
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Specification implements Serializable {

    private TbSpecification specification;

    private List<TbSpecificationOption> specificationOptionList;

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
