package com.yhp.wanandroid.bean;

import java.util.List;

public class HomeArticlesData {
    /**
     * curPage : 2
     * datas:{}
     * offset : 20
     * over : false
     * pageCount : 63
     * size : 20
     * total : 1259
     */

    public int curPage;
    public List<ArticleDatas> datas;
    public int offset;
    public boolean over;
    public int pageCount;
    public int size;
    public int total;

    @Override
    public String toString() {
        return "HomeArticlesData{" +
                "curPage=" + curPage +
                ", datas.size=" + datas.size() +
                ", offset=" + offset +
                ", over=" + over +
                ", pageCount=" + pageCount +
                ", size=" + size +
                ", total=" + total +
                '}';
    }
}
