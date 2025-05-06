package study.board.article.controller

import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.client.RestClient
import study.board.article.service.response.ArticlePageResponse
import study.board.article.service.response.ArticleResponse

class ArticleControllerTest {
    private val restClient = RestClient.create("http://localhost:9000")

    @Test
    fun createTest() {
        val articleResponse = restClient.post()
            .uri("/v1/articles")
            .body(
                ArticleCreateRequest(
                    title = "test-title",
                    content = "test-content",
                    boardId = 1L,
                    writerId = 1L
                )
            )
            .retrieve()
            .body(ArticleResponse::class.java)

        println(articleResponse)
    }

    @Test
    fun readTest() {
        val articleResponse = restClient.get()
            .uri("/v1/articles/{articleId}", 178062166243102720)
            .retrieve()
            .body(ArticleResponse::class.java)

        println(articleResponse)
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
    fun readAllInfiniteScroll() {
        val articles1: List<ArticleResponse>? = restClient.get()
            .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5")
            .retrieve()
            .body(object : ParameterizedTypeReference<List<ArticleResponse>>() {})

        println("first page")
        articles1?.forEach {
            println("articleId: ${it.articleId}")
        }

        val lastArticleId = articles1?.last()?.articleId
        val articles2: List<ArticleResponse>? = restClient.get()
            .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5&lastArticleId=$lastArticleId")
            .retrieve()
            .body(object : ParameterizedTypeReference<List<ArticleResponse>>() {})

        println("second page")
        articles2?.forEach {
            println("articleId: ${it.articleId}")
        }
    }

    @Test
    fun updateTest() {
        restClient.put()
            .uri("/v1/articles/{articleId}", 178062166243102720)
            .body(
                ArticleUpdateRequest(
                    title = "title v2",
                    content = "content v2"
                )
            )
            .retrieve()
            .body(ArticleResponse::class.java)

        val articleResponse = restClient.get()
            .uri("/v1/articles/{articleId}", 178062166243102720)
            .retrieve()
            .body(ArticleResponse::class.java)

        println(articleResponse)
    }

    @Test
    fun deleteTest() {
        restClient.delete()
            .uri("/v1/articles/{articleId}", 178062166243102720)
            .retrieve()
            .body(Void::class.java)
    }

    private data class ArticleCreateRequest(
        val title: String,
        val content: String,
        val boardId: Long,
        val writerId: Long
    )

    private data class ArticleUpdateRequest(
        val title: String,
        val content: String,
    )
}