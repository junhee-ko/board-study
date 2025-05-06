package study.board.article.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import snowflake.Snowflake
import study.board.article.entity.Article
import study.board.article.repository.ArticleRepository
import study.board.article.service.request.ArticleCreateRequest
import study.board.article.service.request.ArticleUpdateRequest
import study.board.article.service.response.ArticlePageResponse
import study.board.article.service.response.ArticleResponse

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
        val article = articleRepository.findById(articleId).orElseThrow()
        article.update(
            title = articleUpdateRequest.title,
            content = articleUpdateRequest.content
        )

        return ArticleResponse.from(article)
    }

    fun read(articleId: Long): ArticleResponse {
        val article = articleRepository.findById(articleId).orElseThrow()

        return ArticleResponse.from(article)
    }

    fun readAll(boardId: Long, page: Long, pageSize: Long): ArticlePageResponse {
        val articles: List<Article> = articleRepository.findAllArticles(
            boardId = boardId,
            offset = (page - 1) * pageSize,
            limit = pageSize
        )

        val pageLimit = PageLimitCalculator.calculatePageLimit(
            page = page,
            pageSize = pageSize,
            movablePageCount = 10L
        )
        val count = articleRepository.countArticles(
            boardId = boardId,
            limit = pageLimit
        )

        return ArticlePageResponse(
            articles = articles,
            articlesCount = count
        )
    }

    @Transactional
    fun delete(articleId: Long) {
        articleRepository.deleteById(articleId)
    }
}