package study.board.article.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import snowflake.Snowflake
import study.board.article.entity.Article
import study.board.article.repository.ArticleRepository

@Service
class ArticleService(
    private val articleRepository: ArticleRepository
) {

    private val snowflake: Snowflake = Snowflake()

    @Transactional
    fun create(articleCreateRequest: ArticleCreateRequest): ArticleResponse {
        val article = articleRepository.save(
            Article.create(
                articleId = snowflake.nextId(),
                title = articleCreateRequest.title,
                content = articleCreateRequest.content,
                boardId = articleCreateRequest.boardId,
                writerId = articleCreateRequest.writerId
            )
        )

        return ArticleResponse.from(article)
    }

    @Transactional
    fun update(articleId: Long, articleUpdateRequest: ArticleUpdateRequest): ArticleResponse {
        println("-----------------")
        val article = articleRepository.findById(articleId).orElseThrow()
        article.update(title = articleUpdateRequest.title, content = articleUpdateRequest.content)

        println(article)

        return ArticleResponse.from(article)
    }

    fun read(articleId: Long): ArticleResponse {
        println("read")
        val article = articleRepository.findById(articleId).orElseThrow()

        return ArticleResponse.from(article)
    }

    @Transactional
    fun delete(articleId: Long) {
        articleRepository.deleteById(articleId)
    }
}