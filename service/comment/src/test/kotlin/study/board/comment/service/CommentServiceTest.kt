package study.board.comment.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import study.board.comment.entity.Comment
import study.board.comment.repository.CommentRepository
import java.time.LocalDateTime
import java.util.*

class CommentServiceTest {

    private lateinit var commentService: CommentService
    private lateinit var commentRepository: CommentRepository

    @BeforeEach
    fun setUp() {
        commentRepository = mockk(relaxed = true)
        commentService = CommentService(commentRepository)
    }

    // 부모
    // 삭제 대상 댓글
    // 자식 댓글
    @Test
    fun `삭제하고자 하는 댓글이 자식이 있으면, 해당 댓글을 삭제 마킹한다`() {
        // given
        val articleId = 1L
        val commentId = 2L

        val comment = Comment(
            commentId = commentId,
            content = "this is child comment",
            parentCommentId = commentId,
            articleId = articleId,
            writerId = 0,
            deleted = false,
            createdAt = LocalDateTime.now()
        )

        every { commentRepository.findById(commentId) } returns Optional.of(comment)
        every {
            commentRepository.countBy(
                articleId = articleId,
                parentCommentId = commentId,
                limit = 2
            )
        } returns 2L

        // when
        commentService.delete(commentId)

        // then
        assertThat(comment.deleted).isTrue()
    }

    // 부모 (삭제 마킹 X)
    // 삭제 대상 댓글
    @Test
    fun `삭제하고자 하는 댓글이 자식 댓글이 없으면서 부모 댓글이 아직 삭제 마킹이 안되어 있으면, 해당 댓글만 삭제한다`() {
        // given
        val articleId = 1L
        val commentId = 2L
        val parentCommentId = 3L

        val comment = Comment(
            commentId = commentId,
            content = "this is child comment",
            parentCommentId = parentCommentId,
            articleId = articleId,
            writerId = 0,
            deleted = false,
            createdAt = LocalDateTime.now()
        )
        every { commentRepository.findById(commentId) } returns Optional.of(comment)
        every {
            commentRepository.countBy(
                articleId = articleId,
                parentCommentId = commentId,
                limit = 2
            )
        } returns 1L

        val parentComment = Comment(
            commentId = parentCommentId,
            content = "this is parent comment",
            parentCommentId = parentCommentId,
            articleId = articleId,
            writerId = 0,
            deleted = false,
            createdAt = LocalDateTime.now()
        )
        every { commentRepository.findById(parentCommentId) } returns Optional.of(parentComment)

        // when
        commentService.delete(commentId)

        // then
        verify(exactly = 1) { commentRepository.delete(comment) }
        verify(exactly = 0) { commentRepository.delete(parentComment) }
    }

    // 부모 (삭제 마킹 O)
    // 삭제 대상 댓글
    @Test
    fun `삭제하고자 하는 댓글이 자식 댓글이 없으면서 부모 댓글이 삭제 마킹이 되어 있으면, 해당 댓글과 부모 댓글을 모두 삭제한다`() {
        // given
        val articleId = 1L
        val commentId = 2L
        val parentCommentId = 3L

        val comment = Comment(
            commentId = commentId,
            content = "this is child comment",
            parentCommentId = parentCommentId,
            articleId = articleId,
            writerId = 0,
            deleted = false,
            createdAt = LocalDateTime.now()
        )
        every { commentRepository.findById(commentId) } returns Optional.of(comment)
        every {
            commentRepository.countBy(
                articleId = articleId,
                parentCommentId = commentId,
                limit = 2
            )
        } returns 1L

        val parentComment = Comment(
            commentId = parentCommentId,
            content = "this is parent comment",
            parentCommentId = parentCommentId,
            articleId = articleId,
            writerId = 0,
            deleted = true,
            createdAt = LocalDateTime.now()
        )
        every { commentRepository.findById(parentCommentId) } returns Optional.of(parentComment)
        every {
            commentRepository.countBy(
                articleId = articleId,
                parentCommentId = parentCommentId,
                limit = 2
            )
        } returns 1L

        // when
        commentService.delete(commentId)

        // then
        verify(exactly = 1) { commentRepository.delete(comment) }
        verify(exactly = 1) { commentRepository.delete(parentComment) }
    }
}