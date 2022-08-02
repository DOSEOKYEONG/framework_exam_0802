package com.ll.exam.article.controller;

import com.ll.exam.annotation.Autowired;
import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.GetMapping;
import com.ll.exam.article.service.ArticleService;
import org.reflections.Reflections;

@Controller
public class ArticleController {

    public ArticleController() {
        articleService = new ArticleService();

    }

    @Autowired
    ArticleService articleService;

    @GetMapping("usr/list/free")
    void showList() {

    }

    public ArticleService getArticleServiceForTest() {
        return articleService;
    }
}
