package com.yhp.wanandroid.bean;

import java.util.List;

public class StarListData {

    /**
     * "curPage": 1,
     * "datas":[]
     * "offset": 0,
     *     "over": true,
     *     "pageCount": 1,
     *     "size": 20,
     *     "total": 10
     */

    public int curPage;
    public List<StarArticleDatas> datas;
    public int offset;
    public boolean over;
    public int pageCount;
    public int size;
    public int total;
}
