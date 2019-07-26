package com.jarvis.pinyougou.search.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jarvis.pinyougou.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description:
 * @CreateDate: 2019/7/26 1:24
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/7/26 1:24
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public Map<String, Object> search(@RequestBody Map searchMap ){
        return  itemSearchService.search(searchMap);
    }

}
