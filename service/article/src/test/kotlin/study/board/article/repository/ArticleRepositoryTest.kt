package study.board.article.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import study.board.article.entity.Article

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private lateinit var articleRepository: ArticleRepository


    @Test
    fun findAllTest() {
        val articles = articleRepository.findAllArticles(
            boardId = 1,
            offset = 1499970,
            limit = 30
        )

        articles.forEach {
            println(it)
        }
    }

    @Test
    fun countArticlesTest() {
        val count = articleRepository.countArticles(
            boardId = 1,
            limit = 10000
        )
        println(count)
    }

    @Test
    fun findAllArticlesInfiniteScrollTest() {
        val articles: List<Article> = articleRepository.findAllArticlesInfiniteScroll(
            boardId = 1,
            limit = 30
        )
        articles.forEach {
            println(it.articleId)
        }

        val lastArticleId = articles.last().articleId
        val articles2: List<Article> = articleRepository.findAllArticlesInfiniteScroll(
            boardId = 1,
            limit = 30,
            lastArticleId = lastArticleId
        )
        articles2.forEach {
            println(it.articleId)
        }
    }
}