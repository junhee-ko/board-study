package study.board.article.service

data class ArticleCreateRequest(
    val title: String,
    val content: String,
    val boardId: Long,
    val writerId: Long
)