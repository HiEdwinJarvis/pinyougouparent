package com.jarvis.pinyougou.manager.web.controller;

import com.jarvis.entity.PageResult;
import com.jarvis.entity.Result;
import com.jarvis.pinyougou.pojo.TbBrand;
import com.jarvis.pinyougou.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import java.util.List;
import java.util.Map;

/**
* 品牌Controller
*
* */
@RestController
@RequestMapping("/brand")
public class BrandController {

	@Reference
	private BrandService brandService;
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		return brandService.findAll();		
	}

	/**
	* 分页查询
	* */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int size){
		return brandService.findPage(page, size);
	}
	/**
	* 添加
	* */
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand){
		try {
			brandService.add(brand);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}

	}
	/**
	* 修改之前先查找到要修改的记录
	* */
	@RequestMapping("/findOne")
	public TbBrand findOne(Long id){
		return brandService.findOne(id);
	}
	/**
	 * 修改
	 * */
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand brand){
		try {
			brandService.update(brand);
			return new Result(true,"修改成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"修改失败");
		}
	}

	/**
	 * 品牌删除
	 * */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			brandService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	/**
	* 搜索
	* */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand brand,int page,int size){
		return brandService.findPage(brand, page, size);
	}

	/***
	 * 返回下拉列表中的品牌信息
	 * @return
	 */
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();

	}
}
