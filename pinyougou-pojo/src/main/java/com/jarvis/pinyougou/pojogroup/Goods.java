package com.jarvis.pinyougou.pojogroup;

import com.jarvis.pinyougou.pojo.TbGoods;
import com.jarvis.pinyougou.pojo.TbGoodsDesc;
import com.jarvis.pinyougou.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:做一个包装类用于接收商品添加时的信息
 * @CreateDate: 2019/6/19 15:32
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/6/19 15:32
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Goods implements Serializable {
    private TbGoods goods;//商品SPU
    private TbGoodsDesc goodsDesc;//商品扩展
    private List<TbItem> itemList;//商品SKU列表

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }
}
