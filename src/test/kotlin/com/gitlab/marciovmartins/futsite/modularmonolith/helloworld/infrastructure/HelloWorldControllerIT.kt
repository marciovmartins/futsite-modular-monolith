package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.application.GetHelloWorld
import com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.domain.IllegalPropertyException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.kotlin.extensions.Given
import io.restassured.module.mockmvc.kotlin.extensions.Then
import io.restassured.module.mockmvc.kotlin.extensions.When
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class HelloWorldControllerIT(
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

@WebMvcTest(controllers = [HelloWorldController::class])
internal class HelloWorldControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var getHelloWorld: GetHelloWorld

    @Test
    fun `get hello world with 400 bad request`() {
        // given
        val name = "H"

        every { getHelloWorld.with(name) } throws IllegalPropertyException(
            properties = mapOf(
                "propertyName" to "name",
                "propertyValue" to "",
            )
        )

        // when
        val response = Given {
            mockMvc(mockMvc)
            contentType(ContentType.JSON)
            queryParam("name", name)
        } When {
            get("/api/v1/helloworld")
        }

        // then
        response Then {
            statusCode(400)
            body("detail", equalTo("IllegalProperty"))
            body("type", equalTo("http://localhost/api/exception/illegal-property"))
            body("propertyName", equalTo("name"))
            body("propertyValue", equalTo(""))
        }
    }
}