package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID


@EnableAutoConfiguration
@ContextConfiguration(classes = [GamedayController::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class RegisterAndRetrieveGamedayIT(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val objectMapper: ObjectMapper,
    @LocalServerPort private val port: String
) {
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
                "self" to LinkDTO(linkTo(methodOn(GamedayController::class.java).byId(gamedayId)).toString()),
                "gameday" to LinkDTO(linkTo(methodOn(GamedayController::class.java).byId(gamedayId)).toString()),
            )
        )

        // when
        val request = webTestClient
            .post().uri("/api/v1/gameDays")
            .bodyValue(gamedayToRegisterDTO)
            .header("Content-Type", "application/json")
            .exchange()

        val response = webTestClient
            .get().uri("/api/v1/gameDays/${gamedayToRegisterDTO.gamedayId}")
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
                assertEquals(expectedJsonBody, actualJsonBody, JSONCompareMode.LENIENT)
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
                        (1..22).map { testPlayerStatisticDTO(team = "A") }
                            + (1..22).map { testPlayerStatisticDTO(team = "B") }
                        ).toSet()
                ),
            ),
        )
        val expectedResponseGamedayDTO = gamedayToRegisterDTO.toTestRetrieveGameDayDTO(
            links = mapOf(
                "self" to LinkDTO(linkTo(methodOn(GamedayController::class.java).byId(gamedayId)).toString()),
                "gameday" to LinkDTO(linkTo(methodOn(GamedayController::class.java).byId(gamedayId)).toString()),
            )
        )

        // when
        val request = webTestClient
            .post().uri("/api/v1/gameDays")
            .bodyValue(gamedayToRegisterDTO)
            .header("Content-Type", "application/json")
            .exchange()

        val response = webTestClient
            .get().uri("/api/v1/gameDays/${gamedayToRegisterDTO.gamedayId}")
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
                assertEquals(expectedJsonBody, actualJsonBody, JSONCompareMode.LENIENT)
            }
    }
}