package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroup
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroupRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.GamedayFixture.gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.GamedayFixture.gamedayAfterPeriod
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.GamedayFixture.gamedayBeforePeriod
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CalculateRankingControllerIT(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val amateurSoccerGroupRepository: AmateurSoccerGroupRepository,
    @Autowired private val gamedayRepository: GamedayRepository,
    @LocalServerPort private val port: String,
) {
    @Test
    fun `happy path`() {
        // given
        val amateurSoccerGroupId = AmateurSoccerGroupId(UUID.randomUUID())
        val period = TestPeriodDTO(
            from = Instant.now().minus(3, ChronoUnit.DAYS),
            to = Instant.now().minus(1, ChronoUnit.DAYS),
        )
        val playerId1 = UUID.randomUUID()
        val playerId2 = UUID.randomUUID()

        amateurSoccerGroupRepository.save(
            AmateurSoccerGroup(
                amateurSoccerGroupId.value, "CalculateRankingControllerIT ${Random.nextLong(1, 99999999)}",
            )
        )

        gamedayRepository.saveAll(
            setOf(
                gamedayBeforePeriod(amateurSoccerGroupId, playerId1, playerId2),
                gameday(
                    amateurSoccerGroupId,
                    Instant.now().minus(2, ChronoUnit.DAYS),
                    GamedayFixture.TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 1u, ownGoals = 0u),
                    GamedayFixture.TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 0u, ownGoals = 0u),
                ),
                gamedayAfterPeriod(amateurSoccerGroupId, playerId1, playerId2),
            )
        )

        val expectedPlayerStatistic1 = mapOf(
            "playerId" to playerId1.toString(),
            "matches" to 1,
            "victories" to 1,
            "draws" to 0,
            "defeats" to 0,
            "goalsInFavor" to 1,
            "ownGoals" to 0,
        )
        val expectedPlayerStatistic2 = mapOf(
            "playerId" to playerId2.toString(),
            "matches" to 1,
            "victories" to 0,
            "draws" to 0,
            "defeats" to 1,
            "goalsInFavor" to 0,
            "ownGoals" to 0,
        )

        // when
        val actual = webTestClient.post()
            .uri("/api/v1/amateurSoccerGroups/${amateurSoccerGroupId.value}/ranking")
            .bodyValue(period)
            .exchange()

        // then
        actual
            .expectStatus().is2xxSuccessful
            .expectBody()
            .jsonPath("$.period.from").isEqualTo(period.from.toString())
            .jsonPath("$.period.to").isEqualTo(period.to.toString())
            .jsonPath("$.matches").isEqualTo(1)
            .jsonPath("$.playerStatistics").value(
                containsInAnyOrder(expectedPlayerStatistic1, expectedPlayerStatistic2)
            )
            .jsonPath("$._links.self.href").isEqualTo(url(amateurSoccerGroupId, version = "/v1", path = "/ranking"))
            .jsonPath("$._links.get-amateur-soccer-group.href").isEqualTo(url(amateurSoccerGroupId))
    }

    private fun url(amateurSoccerGroupId: AmateurSoccerGroupId, version: String = "", path: String = ""): String {
        return "http://localhost:${port}/api$version/amateurSoccerGroups/${amateurSoccerGroupId.value}${path}"
    }

    private data class TestPeriodDTO(
        val from: Instant,
        val to: Instant,
    )
}

internal class CalculateRankingControllerTest {
    @Test
    fun `with 200 Ok`() {
        TODO("need to be implemented")
    }

    @Test
    fun `with 400 Bad Request`() {
        TODO("need to be implemented")
    }

    @Test
    fun `with 404 Not Found`() {
        TODO("need to be implemented")
    }

    @Test
    fun `with 500 Internal Error`() {
        TODO("need to be implemented")
    }
}