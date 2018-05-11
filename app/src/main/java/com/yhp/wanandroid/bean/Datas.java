package com.yhp.wanandroid.bean;

import java.util.List;

public class Datas {

    /**
     * apkLink :
     * author : l455202325
     * chapterId : 337
     * chapterName : 快应用
     * collect : false
     * courseId : 13
     * desc : 快应用API Demo 集合 QuickAPP
     * envelopePic : http://www.wanandroid.com/blogimgs/589f3b01-d9d5-41b0-aeff-604b900aacd1.png
     * fresh : false
     * id : 2879
     * link : http://www.wanandroid.com/blog/show/2120
     * niceDate : 2018-04-27
     * origin :
     * projectLink : https://github.com/l455202325/APIDemo
     * publishTime : 1524832705000
     * superChapterId : 294
     * superChapterName : 开源项目主Tab
     * tags : [{"name":"项目","url":"/project/list/1?cid=337"}]
     * title : 快应用API Demo 集合 QuickAPP APIDemo
     * type : 0
     * userId : -1
     * visible : 1
     * zan : 0
     */

    String apkLink;
    String author;
    int chapterId;
    String chapterName;
    boolean collect;
    int courseId;
    String desc;
    String envelopePic;
    boolean fresh;
    int id;
    String link;
    String niceDate;
    String origin;
    String projectLink;
    long publishTime;
    int superChapterId;
    String superChapterName;
    String title;
    int type;
    int userId;
    int visible;
    int zan;
    List<TagsBean> tags;

    public static class TagsBean {
        /**
         * name : 项目
         * url : /project/list/1?cid=337
         */

        String name;
        String url;
    }
}
