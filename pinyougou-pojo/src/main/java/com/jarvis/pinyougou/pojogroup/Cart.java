package com.jarvis.pinyougou.pojogroup;

import com.jarvis.pinyougou.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @CreateDate: 2019/8/11 21:33
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/11 21:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Cart implements Serializable {


    private String sellerId;//商家ID

    private String sellerName;//商家名称

    private List<TbOrderItem> orderItemList;


    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
