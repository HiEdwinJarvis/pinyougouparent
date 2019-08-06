package com.jarvis.pinyougou.manager.web.controller;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jarvis.entity.PageResult;
import com.jarvis.entity.Result;
//import com.jarvis.pinyougou.page.service.ItemPageService;
import com.jarvis.pinyougou.pojo.TbGoods;
import com.jarvis.pinyougou.pojo.TbItem;
import com.jarvis.pinyougou.pojogroup.Goods;
//import com.jarvis.pinyougou.search.service.ItemSearchService;
import com.jarvis.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	/*@Reference
	private ItemSearchService itemSearchService;*/

	/**
	 * activemq
	 * */

	@Autowired
	private Destination queueSolrDestination;//用于导入solr的消息

	@Autowired
	private JmsTemplate jmsTemplate;


	@Autowired
	private Destination topicPageDestination;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return goodsService.findPage(page, rows);
	}
	

	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */


	@Autowired
	private Destination queueSolrDeleteDestination;

	@Autowired
	private Destination topicPageDeleteDestination;//用于生成静态网页的消息
	@RequestMapping("/delete")
	public Result delete(final Long [] ids){
		try {
			goodsService.delete(ids);
			/**
			* 删除solr索引库内容
			* */
			//itemSearchService.deleteByGoodsIds(Arrays.asList(ids));

			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});

			//删除静态页面
			jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});

			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	

	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}



	//更改审核状态
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status){
		try {
			goodsService.updateStatus(ids, status);
			//按照SPU id查询sku列表
			if(status.equals("1")){
				//审核通过
				List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(ids,status);

				System.out.println("item的size"+itemList.size());
				if(itemList.size()>0){
					/**
					 * 将审核通过的商品导入索引库
					 * */
					//itemSearchService.importList(itemList);



					/**
					 *activemq
					 * */

					final String jsonString = JSON.toJSONString(itemList);
					jmsTemplate.send(queueSolrDestination,new MessageCreator(){


						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(jsonString);
						}
					});
				}else{
					System.out.println("没有明细数据");
				}

				//静态页生成
				/*for(Long goodsId:ids){
					itemPageService.genItemHtml(goodsId);
				}*/

				for(final Long goodsId:ids){

					jmsTemplate.send(topicPageDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(goodsId+"");
						}
					});
				}

			}
			return new Result(true, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "失败");
		}		
	}

	//@Reference
	//private ItemPageService itemPageService;

	/**
	 * 生成静态页面，测试
	 * */
	/*@RequestMapping("/genHtml")
	public void genHtml(Long goodsId){

		itemPageService.genItemHtml(goodsId);
	}*/


}
