package study.board.article.repository

import org.springframework.data.jpa.repository.JpaRepository
import study.board.article.entity.Article

interface ArticleRepository: JpaRepository<Article, Long> {
}