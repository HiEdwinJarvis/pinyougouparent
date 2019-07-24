package com.jarvis.pinyougou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jarvis.entity.PageResult;
import com.jarvis.pinyougou.dao.mapper.TbSpecificationMapper;
import com.jarvis.pinyougou.dao.mapper.TbSpecificationOptionMapper;
import com.jarvis.pinyougou.pojo.TbSpecification;
import com.jarvis.pinyougou.pojo.TbSpecificationExample;
import com.jarvis.pinyougou.pojo.TbSpecificationOption;
import com.jarvis.pinyougou.pojo.TbSpecificationOptionExample;
import com.jarvis.pinyougou.pojogroup.Specification;
import com.jarvis.pinyougou.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper optionMapper;
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		/**
		 * 增加规格
		 * */
		TbSpecification tbSpecification = specification.getSpecification();
		specificationMapper.insert(tbSpecification);
		List<TbSpecificationOption> list  = specification.getSpecificationOptionList();
		for(TbSpecificationOption option:list){
			/**
			 * 先给规格属性添加SpecId确定他是属于哪一个规格的
			 * 然后再逐个添加
			 * */
			option.setSpecId(tbSpecification.getId());
			optionMapper.insert(option);

		}

	}


	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){

		//保存修改的规格
		specificationMapper.updateByPrimaryKey(specification.getSpecification());

		/**
		 * 先删除原有的规格选项再重新添加一次
		 * */

		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(specification.getSpecification().getId());
		optionMapper.deleteByExample(example);
		for(TbSpecificationOption option : specification.getSpecificationOptionList()){
			option.setSpecId(specification.getSpecification().getId());
			optionMapper.insert(option);
		}



	}

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		Specification specification = new Specification();
		/**
		* 获取规格实体并添加到specification
		* */
		specification.setSpecification(specificationMapper.selectByPrimaryKey(id));

		/**
		 * 获取规格选项列表
		 * */
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		//创建查询条件
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> tbSpecificationOptions = optionMapper.selectByExample(example);
		specification.setSpecificationOptionList(tbSpecificationOptions);

		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			specificationMapper.deleteByPrimaryKey(id);

			/**
			 * 删除规格选项
			 * */
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);//制定删除的规格id为删除条件
			optionMapper.deleteByExample(example);

		}
	}


	@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbSpecificationExample example=new TbSpecificationExample();
		TbSpecificationExample.Criteria criteria = example.createCriteria();

		if(specification!=null){
			if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}

		}

		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		return specificationMapper.selectOptionList();
	}

}
