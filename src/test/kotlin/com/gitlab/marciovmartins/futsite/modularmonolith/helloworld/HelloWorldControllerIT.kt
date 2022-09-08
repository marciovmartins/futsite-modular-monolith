package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloWorldControllerIT(
    @Autowired private val restTemplate: TestRestTemplate,
) {
    @Test
    fun `get hello world message v1`() {
        // given
        val name = "Marcio"
        val expected = TestHelloWorldDTO(
            message = "Hello world, $name!",
            name = name
        )

        // when
        val actual = restTemplate.getForObject("/api/v1/helloworld?name=$name", TestHelloWorldDTO::class.java)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    data class TestHelloWorldDTO(val message: String, val name: String)
}