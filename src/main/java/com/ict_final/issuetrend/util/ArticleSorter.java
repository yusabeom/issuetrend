package com.ict_final.issuetrend.util;

import com.ict_final.issuetrend.entity.Article;

import java.util.Comparator;
import java.util.List;

public class ArticleSorter {
    public static void sortArticlesByCreatedDate(List<Article> articles) {
        articles.sort(Comparator.comparing(Article::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
    }
}