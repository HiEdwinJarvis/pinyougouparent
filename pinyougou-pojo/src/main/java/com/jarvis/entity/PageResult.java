package com.jarvis.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @CreateDate: 2019/6/3 9:02
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/6/3 9:02
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PageResult implements Serializable {
    private long total;
    private List rows;
    public PageResult(long total,List rows){
        this.total = total;
        this.rows = rows;
    }
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
