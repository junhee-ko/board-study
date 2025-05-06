package study.board.article.controller

import org.springframework.web.bind.annotation.*
import study.board.article.service.*

@RestController
class ArticleController(
    private val articleService: ArticleService
) {

    @GetMapping("/v1/articles/{articleId}")
    fun read(@PathVariable articleId: Long): ArticleResponse {
        return articleService.read(articleId)
    }

    @GetMapping("/v1/articles")
    fun readAll(
        @RequestParam("boardId") boardId: Long,
        @RequestParam("page") page: Long,
        @RequestParam("pageSize") pageSize: Long,
    ): ArticlePageResponse {
        return articleService.readAll(
            boardId = boardId,
            page = page,
            pageSize = pageSize
        )
    }

    @PostMapping("/v1/articles")
    fun create(@RequestBody articleCreateRequest: ArticleCreateRequest): ArticleResponse {
        return articleService.create(articleCreateRequest)
    }

    @PutMapping("/v1/articles/{articleId}")
    fun update(
        @PathVariable articleId: Long,
        @RequestBody articleUpdateRequest: ArticleUpdateRequest
    ): ArticleResponse {
        return articleService.update(articleId, articleUpdateRequest)
    }

    @DeleteMapping("/v1/articles/{articleId}")
    fun delete(@PathVariable articleId: Long) {
        println("----------")
        articleService.delete(articleId)
    }
}