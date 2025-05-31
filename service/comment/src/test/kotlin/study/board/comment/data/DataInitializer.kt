package study.board.comment.data

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.support.TransactionTemplate
import snowflake.Snowflake
import study.board.comment.entity.Comment
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SpringBootTest
@TestPropertySource(properties = ["spring.jpa.show-sql=false"])
class DataInitializer {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    private lateinit var snowflake: Snowflake
    private lateinit var countDownLatch: CountDownLatch
    private lateinit var executorService: ExecutorService

    @BeforeEach
    fun setUp() {
        snowflake = Snowflake()
        countDownLatch = CountDownLatch(EXECUTE_COUNT)
        executorService = Executors.newFixedThreadPool(30)
    }

    @AfterEach
    fun tearDown() {
        executorService.shutdown()
    }

    @Test
    fun init() {
        repeat(EXECUTE_COUNT) {
            executorService.submit {
                insert()
                countDownLatch.countDown()
                println("remaining count: ${countDownLatch.count}")
            }
        }

        countDownLatch.await()
    }

    private fun insert() {
        transactionTemplate.executeWithoutResult {
            var prevComment: Comment? = null

            repeat(BULK_INSERT_SIZE) { i ->
                val comment = Comment.create(
                    commentId = snowflake.nextId(),
                    content = "content",
                    parentCommentId = if (i % 2 == 0) null else prevComment?.commentId,
                    articleId = 0,
                    writerId = 0
                )
                prevComment = comment

                entityManager.persist(comment)
            }
        }
    }

    companion object {
        private const val BULK_INSERT_SIZE = 2000
        private const val EXECUTE_COUNT = 6000
    }
}