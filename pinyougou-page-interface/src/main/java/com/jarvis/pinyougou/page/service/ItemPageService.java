package com.jarvis.pinyougou.page.service;

/**
 * @Description:
 * @CreateDate: 2019/8/4 12:39
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/8/4 12:39
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface ItemPageService {


    /**
     * 生成商品详情页
     * */
    public boolean genItemHtml(Long goodsId);

    public boolean deleteItemHtml(Long[] goodsIds);
}
