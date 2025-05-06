package study.board.article.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.time.LocalDateTime.*

@Entity
class Article(
    @Id val articleId: Long,
    var title: String,
    var content: String,
    val boardId: Long,
    val writerId: Long,
    val createdAt: LocalDateTime,
    var modifiedAt: LocalDateTime
) {

    fun update(title: String, content: String, now: LocalDateTime= now()) {
        this.title = title
        this.content = content
        this.modifiedAt = now
    }

    companion object {
        fun create(
            articleId: Long,
            title: String,
            content: String,
            boardId: Long,
            writerId: Long,
            now: LocalDateTime = now()
        ): Article {
            return Article(
                articleId = articleId,
                title = title,
                content = content,
                boardId = boardId,
                writerId = writerId,
                createdAt = now,
                modifiedAt = now
            )
        }
    }
}