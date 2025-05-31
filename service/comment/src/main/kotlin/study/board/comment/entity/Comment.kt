package study.board.comment.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Comment(
    @Id val commentId: Long,
    val content: String,
    val parentCommentId: Long,
    val articleId: Long,
    val writerId: Long,
    var deleted: Boolean,
    val createdAt: LocalDateTime,
) {

    fun isRoot(): Boolean =
        parentCommentId == commentId

    fun delete() {
        this.deleted = true
    }

    companion object {
        fun create(
            commentId: Long,
            content: String,
            parentCommentId: Long? = null,
            articleId: Long,
            writerId: Long,
        ): Comment {
            return Comment(
                commentId = commentId,
                content = content,
                parentCommentId = parentCommentId ?: commentId,
                articleId = articleId,
                writerId = writerId,
                deleted = false,
                createdAt = LocalDateTime.now()
            )
        }
    }
}