package com.jarvis.portal.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jarvis.pinyougou.content.service.ContentService;
import com.jarvis.pinyougou.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @CreateDate: 2019/7/24 23:01
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/7/24 23:01
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    /**
     * 根据广告分类ID查询广告列表
     * @param categoryId
     * @return
     */
    @RequestMapping("/findByCategoryId")
    public List<TbContent> findByCategoryId(Long categoryId) {
        return contentService.findByCategoryId(categoryId);
    }

}
