package com.yhp.wanandroid.bean;

import java.util.List;

public class ArticleDatas {

    /**
     *                 "apkLink": "",
     *                 "audit": 1,
     *                 "author": "",
     *                 "canEdit": false,
     *                 "chapterId": 502,
     *                 "chapterName": "自助",
     *                 "collect": false,
     *                 "courseId": 13,
     *                 "desc": "",
     *                 "descMd": "",
     *                 "envelopePic": "",
     *                 "fresh": true,
     *                 "host": "",
     *                 "id": 17415,
     *                 "link": "https://juejin.cn/post/6932778923491065864",
     *                 "niceDate": "2小时前",
     *                 "niceShareDate": "2小时前",
     *                 "origin": "",
     *                 "prefix": "",
     *                 "projectLink": "",
     *                 "publishTime": 1614218071000,
     *                 "realSuperChapterId": 493,
     *                 "selfVisible": 0,
     *                 "shareDate": 1614218071000,
     *                 "shareUser": "renxhui",
     *                 "superChapterId": 494,
     *                 "superChapterName": "广场Tab",
     *                 "tags": [],
     *                 "title": "Gradle学习系列（一）：Groovy学习",
     *                 "type": 0,
     *                 "userId": 27931,
     *                 "visible": 1,
     *                 "zan": 0
     */

    public String apkLink;
    public int audit;
    public String author;
    public boolean canEdit;
    public int chapterId;
    public String chapterName;
    public boolean collect;
    public int courseId;
    public String desc;
    public String descMd;
    public String envelopePic;
    public boolean fresh;
    public String host;
    public int id;
    public String link;
    public String niceDate;
    public String niceShareDate;
    public String origin;
    public String prefix;
    public String projectLink;
    public long publishTime;
    public int realSuperChapterId;
    public int selfVisible;
    public long shareDate;
    public String shareUser;
    public int superChapterId;
    public String superChapterName;
    public String title;
    public int type;
    public int userId;
    public int visible;
    public int zan;
    public List<TagsBean> tags;

    public static class TagsBean {
        /**
         * name : 项目
         * url : /project/list/1?cid=337
         */

        public String name;
        public String url;
    }

    @Override
    public String toString() {
        return "ArticleDatas{" +
                "author='" + author + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", desc='" + desc + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
