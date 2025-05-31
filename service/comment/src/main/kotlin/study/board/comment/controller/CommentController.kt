package study.board.comment.controller

import org.springframework.web.bind.annotation.*
import study.board.comment.service.CommentService
import study.board.comment.service.request.CommentCreateRequest
import study.board.comment.service.response.CommentResponse

@RestController
@RequestMapping("/v1/comments")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping
    fun create(@RequestBody request: CommentCreateRequest): CommentResponse {
        return commentService.create(request)
    }

    @GetMapping("/{commentId}")
    fun read(@PathVariable("commentId") commentId: Long): CommentResponse {
        return commentService.read(commentId)
    }

    @DeleteMapping("/{commentId}")
    fun delete(@PathVariable("commentId") commentId: Long) {
        commentService.delete(commentId)
    }
}