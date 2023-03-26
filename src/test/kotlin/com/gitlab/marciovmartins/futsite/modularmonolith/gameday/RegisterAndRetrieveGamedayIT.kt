package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import com.fasterxml.jackson.databind.ObjectMapper
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.argumentsprovider.InvalidGameDay
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
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
                        testPlayerStatisticDTO(team = "A"),
                        testPlayerStatisticDTO(team = "B"),
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
        // given
        val gamedayId = UUID.randomUUID()
        val gamedayToRegisterDTO = TestPostGameDayDTO(
            gamedayId = gamedayId.toString(),
            amateurSoccerGroupId = UUID.randomUUID().toString(),
            date = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString(),
            matches = listOf(
                TestMatchDTO(
                    players = (
                        (1..24).map { testPlayerStatisticDTO(team = "A") }
                            + (1..24).map { testPlayerStatisticDTO(team = "B") }
                        ).toSet()
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

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(InvalidGameDay::class)
    fun `register game day with invalid information`(
        testDescription: String,
        gamedayToRegisterDTO: TestPostGameDayDTO,
        expectedTitle: String,
        expectedDetail: String?,
        properties: Map<String, Any>
    ) {
        // when
        val response = webTestClient
            .post().uri("/api/gameDays")
            .bodyValue(gamedayToRegisterDTO)
            .header("Content-Type", "application/json")
            .exchange()

        // then
        response
            .expectStatus().isEqualTo(400)
            .expectHeader().valueEquals("Content-Type", "application/problem+json")

        val expectedBody = response.expectBody()
            .jsonPath("$.type").isEqualTo("about:blank")
            .jsonPath("$.title").isEqualTo(expectedTitle)
            .jsonPath("$.status").isEqualTo(400)
            .jsonPath("$.instance").isEqualTo("/api/gameDays")

        expectedDetail?.let { expectedBody.jsonPath("$.detail").isEqualTo(expectedDetail) }

        properties.forEach { expectedBody.jsonPath("$.${it.key}").isEqualTo(it.value.toString()) }
    }
}