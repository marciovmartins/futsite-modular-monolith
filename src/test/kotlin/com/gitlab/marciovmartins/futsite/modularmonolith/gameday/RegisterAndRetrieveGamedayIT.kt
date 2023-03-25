package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.reflect.KClass


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class RegisterAndRetrieveGamedayIT {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var repositoryEntityLinks: RepositoryEntityLinks

    @LocalServerPort
    private lateinit var port: String

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun initServletRequestAttributes() {
        val httpServletRequest = MockHttpServletRequest().apply { serverPort = port.toInt() }
        RequestContextHolder.setRequestAttributes(ServletRequestAttributes(httpServletRequest))
    }

    @Test
    fun `register and retrieve minimum gameday`() {
        // given
        val gamedayId = UUID.randomUUID()
        val gamedayToRegisterDTO = TestPostGameDayDTO(
            gamedayId = gamedayId.toString(),
            amateurSoccerGroupId = UUID.randomUUID().toString(),
            date = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString(),
            matches = listOf(
                TestMatchDTO(
                    players = setOf(
                        TestPlayerStatisticDTO(
                            playerId = UUID.randomUUID().toString(),
                            team = "A",
                            goalsInFavor = 0,
                            goalsAgainst = 0,
                            yellowCards = 0,
                            blueCards = 0,
                            redCards = 0,
                        ),
                        TestPlayerStatisticDTO(
                            playerId = UUID.randomUUID().toString(),
                            team = "B",
                            goalsInFavor = 0,
                            goalsAgainst = 0,
                            yellowCards = 0,
                            blueCards = 0,
                            redCards = 0,
                        ),
                    ),
                ),
            ),
        )
        val expectedResponseGamedayDTO = gamedayToRegisterDTO.toTestRetrieveGameDayDTO(
            links = mapOf(
                "self" to LinkDTO(repositoryEntityLinks.linkToItemResource(Gameday::class.java, gamedayId).href),
                "gameday" to LinkDTO(repositoryEntityLinks.linkToItemResource(Gameday::class.java, gamedayId).href),
            )
        )

        // when
        val request = webTestClient
            .post().uri("/api/gameDays")
            .bodyValue(gamedayToRegisterDTO)
            .header("Content-Type", "application/json")
            .exchange()

        val response = webTestClient
            .get().uri("/api/gameDays/${gamedayToRegisterDTO.gamedayId}")
            .header("Content-Type", "application/json")
            .exchange()

        // then
        request
            .expectStatus().isCreated

        response
            .expectStatus().isOk
            .expectHeader().valueEquals("Content-Type", "application/hal+json")
            .expectBody()
            .consumeWith {
                val expectedJsonBody = objectMapper.writeValueAsString(expectedResponseGamedayDTO)
                val actualJsonBody = it.responseBody!!.decodeToString()
                assertEquals(expectedJsonBody, actualJsonBody, false)
            }
    }

    @Test
    fun `register and retrieve maximum gameday`() {
        TODO("need to be implemented")
//        // given
//        val gamedayToRegisterDTO = RankingDtoFixture.gamedayDTO(
//            matches = (1..99).map {
//                RankingDtoFixture.matchDTO(
//                    players = (
//                        (1..12).map { RankingDtoFixture.playerDTO(team = GamedayDTO.MatchDTO.Team.A) }
//                            + (1..12).map { RankingDtoFixture.playerDTO(team = GamedayDTO.MatchDTO.Team.B) }
//                        ).toSet()
//                )
//            }
//        )
//
//        // when
//        registerGameday.with(gamedayToRegisterDTO)
//        val gamedayDTO = retrieveGameday.by(gamedayToRegisterDTO.id)
//
//        // then
//        assertThat(gamedayDTO).isEqualTo(gamedayToRegisterDTO)
    }

    @ParameterizedTest(name = "{0}")
    fun `register game day with invalid information`(
        testDescription: String,
        gamedayDTO: GamedayDTO,
        expectedException: KClass<out Exception>,
        properties: Map<String, Any>
    ) {
        TODO("need to be implemented")
//        // when
//        val exception = assertThrows<Exception> { registerGameday.with(gamedayDTO) }
//
//        // then
//        assertThat(exception).isInstanceOf(expectedException.javaObjectType)
//        properties.forEach { assertThat(exception).hasFieldOrPropertyWithValue(it.key, it.value) }
    }

    data class TestPostGameDayDTO(
        val gamedayId: Any?,
        val amateurSoccerGroupId: Any?,
        val date: Any?,
        val matches: Any?,
    )

    data class TestRetrieveGameDayDTO(
        val amateurSoccerGroupId: Any?,
        val date: Any?,
        val matches: Any?,
        @JsonProperty("_links")
        val links: Map<String, LinkDTO>,
    )

    data class TestMatchDTO(
        val players: Any?,
    )

    data class TestPlayerStatisticDTO(
        val playerId: Any?,
        val team: Any?,
        val goalsInFavor: Any?,
        val goalsAgainst: Any?,
        val yellowCards: Any?,
        val blueCards: Any?,
        val redCards: Any?,
    )

    data class LinkDTO(
        val href: String,
    )

    fun TestPostGameDayDTO.toTestRetrieveGameDayDTO(
        links: Map<String, LinkDTO>,
    ) = TestRetrieveGameDayDTO(
        amateurSoccerGroupId = this.amateurSoccerGroupId,
        date = this.date,
        matches = this.matches,
        links = links
    )
}