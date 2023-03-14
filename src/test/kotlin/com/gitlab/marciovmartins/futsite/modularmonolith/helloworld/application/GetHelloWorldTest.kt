package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.application

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class GetHelloWorldTest {
    @Test
    fun `get hello world with name`() {
        // given
        val getHelloWorld = GetHelloWorld()
        val name = "Marcio"
        val expected = HelloWorldDTO(
            message = "Hello world, Marcio!",
            name = name,
        )

        // when
        val actual = getHelloWorld.with(name)

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }

    @Test
    fun `get hello world without name`() {
        // given
        val getHelloWorld = GetHelloWorld()
        val nullName: String? = null
        val expected = HelloWorldDTO(
            message = "Hello world, John Doe!",
            name = "John Doe",
        )

        // when
        val actual = getHelloWorld.with(nullName)

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }

    @Test
    fun `get hello world with empty name should fail`() {
        // given
        val getHelloWorld = GetHelloWorld()
        val emptyName = ""

        // when
        val actualException = assertThrows<IllegalArgumentException> {
            getHelloWorld.with(emptyName)
        }

        // then
        assertThat(actualException).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(actualException.message).isEqualTo("Empty hello world name")
    }
}