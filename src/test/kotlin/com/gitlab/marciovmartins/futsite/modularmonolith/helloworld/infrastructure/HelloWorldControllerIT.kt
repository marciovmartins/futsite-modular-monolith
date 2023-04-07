package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.application.GetHelloWorld
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@EnableAutoConfiguration
@ContextConfiguration(classes = [HelloWorldController::class, GetHelloWorld::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class HelloWorldControllerIT(
    @Autowired private val webTestClient: WebTestClient,
    @LocalServerPort private val port: String,
) {
    @BeforeEach
    fun initServletRequestAttributes() {
        val httpServletRequest = MockHttpServletRequest().apply { serverPort = port.toInt() }
        RequestContextHolder.setRequestAttributes(ServletRequestAttributes(httpServletRequest))
    }

    @Test
    fun `get hello world message v1`() {
        // given
        val name = "Marcio"

        // when
        val actual = webTestClient.get()
            .uri("/api/v1/helloworld?name=$name")
            .exchange()

        // then
        actual.expectBody()
            .jsonPath("$.message").isEqualTo("Hello world, $name!")
            .jsonPath("$.name", name)
    }
}