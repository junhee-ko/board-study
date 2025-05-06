package study.board.article.service.response

import study.board.article.entity.Article

data class ArticlePageResponse(
    val articles: List<Article>,
    val articlesCount: Long
)
