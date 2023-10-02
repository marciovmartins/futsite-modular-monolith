package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroup
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroupRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.GamedayFixture.gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.GamedayFixture.gamedayAfterPeriod
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.GamedayFixture.gamedayBeforePeriod
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CalculateRankingControllerIT(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val amateurSoccerGroupRepository: AmateurSoccerGroupRepository,
    @Autowired private val gamedayRepository: GamedayRepository,
) {
    @LocalServerPort
    private lateinit var port: String

    @Test
    fun `happy path`() {
        // given
        val amateurSoccerGroupId = AmateurSoccerGroupId(UUID.randomUUID())
        val period = TestPeriodDTO(
            from = Instant.now().minus(3, ChronoUnit.DAYS),
            to = Instant.now().minus(1, ChronoUnit.DAYS),
        )

        amateurSoccerGroupRepository.save(
            AmateurSoccerGroup().apply {
                this.amateurSoccerGroupId = amateurSoccerGroupId.value
            }
        )

        gamedayRepository.saveAll(
            setOf(
                gamedayBeforePeriod(amateurSoccerGroupId),
                gameday(amateurSoccerGroupId, Instant.now().minus(2, ChronoUnit.DAYS)),
                gamedayAfterPeriod(amateurSoccerGroupId),
            )
        )

        val graphQLTester = HttpGraphQlTester.create(
            WebTestClient.bindToServer().baseUrl("http://localhost:${port}/graphql").build()
        )

        // when
        val document = """
            {
                calculateRanking(
                    amateurSoccerGroupId: "${amateurSoccerGroupId.value}"
                    period: {
                        from: "${period.from}"
                        to: "${period.to}"
                    }
                ) {
                    content {
                        period {
                            from
                            to
                        }
                        matches
                    }
                }
            }
        """.trimIndent()

        val actual = graphQLTester.document(document).execute()

        // then
        actual
            .path("calculateRanking.content.period.from").entity(Instant::class.java).isEqualTo(period.from)
            .path("calculateRanking.content.period.to").entity(Instant::class.java).isEqualTo(period.to)
            .path("calculateRanking.content.matches").entity(Int::class.java).isEqualTo(1)
    }

    private data class TestPeriodDTO(
        val from: Instant,
        val to: Instant,
    )
}
