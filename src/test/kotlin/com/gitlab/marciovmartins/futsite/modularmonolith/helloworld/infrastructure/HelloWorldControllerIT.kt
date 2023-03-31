package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.infrastructure

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class HelloWorldControllerIT(
    @Autowired private val webTestClient: WebTestClient
) {
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