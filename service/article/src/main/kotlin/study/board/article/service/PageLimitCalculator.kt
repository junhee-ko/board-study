package study.board.article.service

/*
* 공식 = (((n – 1) / k) + 1) * m * k + 1
    • 현재 페이지(n)
        • n > 0
    • 페이지 당 게시글 개수(m)
    • 이동 가능한 페이지 개수(k)
    • ((n - 1) / k)의 나머지는 버림
• n=7, m=30, k=10
    • (((7 - 1) / 10) + 1) * 30 * 10 + 1 = 301
• n=12, m=30, k=10
    • (((12 - 1) / 10) + 1) * 30 * 10 + 1 = 601
* */
object PageLimitCalculator {

    fun calculatePageLimit(page: Long, pageSize: Long, movablePageCount: Long): Long =
        (((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount + 1
}