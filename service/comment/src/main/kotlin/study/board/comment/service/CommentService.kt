package study.board.comment.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import snowflake.Snowflake
import study.board.comment.entity.Comment
import study.board.comment.repository.CommentRepository
import study.board.comment.service.request.CommentCreateRequest
import study.board.comment.service.response.CommentResponse

@Service
class CommentService(
    private val commentRepository: CommentRepository,
) {

    private val snowflake: Snowflake = Snowflake()

    fun create(request: CommentCreateRequest): CommentResponse {
        val parentComment = findParent(request)
        val comment = commentRepository.save(
            Comment.create(
                commentId = snowflake.nextId(),
                content = request.content,
                parentCommentId = parentComment?.commentId,
                articleId = request.articleId,
                writerId = request.writerId
            )
        )

        return CommentResponse.from(comment)
    }

    private fun findParent(request: CommentCreateRequest): Comment? {
        val parentCommentId = request.parentCommentId ?: return null
        val parentComment = commentRepository.findByIdOrNull(parentCommentId) ?: return null

        if (parentComment.deleted) throw IllegalStateException("parent comment is deleted")
        if (parentComment.isRoot().not()) throw IllegalStateException("parent comment is not root")

        return parentComment
    }

    fun read(commentId: Long): CommentResponse {
        val comment = commentRepository.findById(commentId).orElseThrow()

        return CommentResponse.from(comment)
    }

    fun delete(commentId: Long) {
        val comment = commentRepository.findById(commentId).orElseThrow()
        if (comment.deleted) throw IllegalStateException("comment is already deleted")

        if (hasChildren(comment)) {
            comment.delete()
        } else {
            delete(comment)
        }
    }

    private fun hasChildren(comment: Comment): Boolean {
        return commentRepository.countBy(
            articleId = comment.articleId,
            parentCommentId = comment.commentId,
            limit = 2
        ) == 2L
    }

    private fun delete(comment: Comment) {
        commentRepository.delete(comment)

        if (comment.isRoot().not()) {
            val parentComment = commentRepository.findById(comment.parentCommentId).orElseThrow()
            if (parentComment.deleted && hasChildren(parentComment).not()) {
                delete(parentComment)
            }
        }
    }
}