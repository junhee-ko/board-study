package study.board.article.controller

import org.springframework.web.bind.annotation.*
import study.board.article.service.*
import study.board.article.service.request.ArticleCreateRequest
import study.board.article.service.request.ArticleUpdateRequest
import study.board.article.service.response.ArticlePageResponse
import study.board.article.service.response.ArticleResponse

@RestController
class ArticleController(
    private val articleService: ArticleService
) {

    @GetMapping("/v1/articles/{articleId}")
    fun read(@PathVariable articleId: Long): ArticleResponse =
        articleService.read(articleId)

    @GetMapping("/v1/articles")
    fun readAll(
        @RequestParam("boardId") boardId: Long,
        @RequestParam("page") page: Long,
        @RequestParam("pageSize") pageSize: Long,
    ): ArticlePageResponse =
        articleService.readAll(
            boardId = boardId,
            page = page,
            pageSize = pageSize
        )

    @GetMapping("/v1/articles/infinite-scroll")
    fun readAllInfiniteScroll(
        @RequestParam("boardId") boardId: Long,
        @RequestParam("pageSize") pageSize: Long,
        @RequestParam("lastArticleId", required = false) lastArticleId: Long?,
    ): List<ArticleResponse> =
        articleService.readAllInfiniteScroll(
            boardId = boardId,
            pageSize = pageSize,
            lastArticleId = lastArticleId
        )

    @PostMapping("/v1/articles")
    fun create(@RequestBody articleCreateRequest: ArticleCreateRequest): ArticleResponse =
        articleService.create(articleCreateRequest)

    @PutMapping("/v1/articles/{articleId}")
    fun update(
        @PathVariable articleId: Long,
        @RequestBody articleUpdateRequest: ArticleUpdateRequest
    ): ArticleResponse =
        articleService.update(articleId, articleUpdateRequest)

    @DeleteMapping("/v1/articles/{articleId}")
    fun delete(@PathVariable articleId: Long) =
        articleService.delete(articleId)
}