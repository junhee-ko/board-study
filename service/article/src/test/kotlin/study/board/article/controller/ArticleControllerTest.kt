package study.board.article.controller

import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClient
import study.board.article.service.ArticlePageResponse
import study.board.article.service.ArticleResponse

class ArticleControllerTest {
    private val restClient = RestClient.create("http://localhost:9000")

    @Test
    fun createTest() {
        val articleResponse: ArticleResponse? = create(
            ArticleCreateRequest(
                title = "test-title",
                content = "test-content",
                boardId = 1L,
                writerId = 1L
            )
        )
        println(articleResponse)
    }

    private fun create(articleCreateRequest: ArticleCreateRequest): ArticleResponse? {
        return restClient.post()
            .uri("/v1/articles")
            .body(articleCreateRequest)
            .retrieve()
            .body(ArticleResponse::class.java)
    }

    @Test
    fun readTest() {
        val articleResponse = read(177728103452299264)
        println(articleResponse)
    }

    private fun read(articleId: Long): ArticleResponse? {
        return restClient.get()
            .uri("/v1/articles/{articleId}", articleId)
            .retrieve()
            .body(ArticleResponse::class.java)
    }

    @Test
    fun readAllTest() {
        val articlePageResponse = restClient.get()
            .uri("/v1/articles?boardId=1&pageSize=30&page=50000")
            .retrieve()
            .body(ArticlePageResponse::class.java)

        println("articlePageResponse.articlesCount: ${articlePageResponse?.articlesCount}")

        articlePageResponse?.articles?.forEach {
            println("articleId: ${it.articleId}")
        }
    }

    @Test
    fun updateTest() {
        update(
            articleId = 177728103452299264,
            articleUpdateRequest = ArticleUpdateRequest(
                title = "title v2",
                content = "content v2"
            )
        )

        val articleResponse = read(177728103452299264)
        println(articleResponse)
    }

    private fun update(articleId: Long, articleUpdateRequest: ArticleUpdateRequest) {
        restClient.put()
            .uri("/v1/articles/{articleId}", articleId)
            .body(articleUpdateRequest)
            .retrieve()
            .body(ArticleResponse::class.java)
    }

    @Test
    fun deleteTest() {
        delete(articleId = 177728103452299264)
    }

    private fun delete(articleId: Long) {
        restClient.delete()
            .uri("/v1/articles/{articleId}", articleId)
            .retrieve()
            .body(Void::class.java)
    }

    data class ArticleCreateRequest(
        val title: String,
        val content: String,
        val boardId: Long,
        val writerId: Long
    )

    data class ArticleUpdateRequest(
        val title: String,
        val content: String,
    )
}