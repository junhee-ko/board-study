package study.board.comment.controller

import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClient
import study.board.comment.service.request.CommentCreateRequest
import study.board.comment.service.response.CommentResponse

class CommentControllerTest {

    @Test
    fun create() {
        // given
        val restClient = RestClient.create("http://localhost:9001")

        val request = CommentCreateRequest(
            articleId = 1L,
            content = "",
            parentCommentId = null,
            writerId = 0
        )

        // when
        val response = restClient.post()
            .uri("/v1/comments")
            .body(request)
            .retrieve()
            .body(CommentResponse::class.java)

        // then
        println(response)
    }

    @Test
    fun read() {
        // given
        val restClient = RestClient.create("http://localhost:9001")

        // when
        val response = restClient.get()
            .uri("/v1/comments/{commentId}", "184944521889984512")
            .retrieve()
            .body(CommentResponse::class.java)

        // then
        println(response)
    }

    @Test
    fun delete() {
        // given
        val restClient = RestClient.create("http://localhost:9001")

        // when
        val response = restClient.delete()
            .uri("/v1/comments/{commentId}", "184944521889984512")
            .retrieve()

        // then
        println(response)
    }

}