package study.board.article.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PageLimitCalculatorTest {

    @Test
    fun page1() {
        // given

        // when
        val actual = PageLimitCalculator.calculatePageLimit(
            page = 1,
            pageSize = 30,
            movablePageCount = 10
        )

        //then
        assertThat(actual).isEqualTo(301)
    }

    @Test
    fun page7() {
        // given

        // when
        val actual = PageLimitCalculator.calculatePageLimit(
            page = 7,
            pageSize = 30,
            movablePageCount = 10
        )

        //then
        assertThat(actual).isEqualTo(301)
    }

    @Test
    fun page10() {
        // given

        // when
        val actual = PageLimitCalculator.calculatePageLimit(
            page = 7,
            pageSize = 30,
            movablePageCount = 10
        )

        //then
        assertThat(actual).isEqualTo(301)
    }

    @Test
    fun page11() {
        // given

        // when
        val actual = PageLimitCalculator.calculatePageLimit(
            page = 11,
            pageSize = 30,
            movablePageCount = 10
        )

        //then
        assertThat(actual).isEqualTo(601)
    }

    @Test
    fun page12() {
        // given

        // when
        val actual = PageLimitCalculator.calculatePageLimit(
            page = 12,
            pageSize = 30,
            movablePageCount = 10
        )

        //then
        assertThat(actual).isEqualTo(601)
    }
}