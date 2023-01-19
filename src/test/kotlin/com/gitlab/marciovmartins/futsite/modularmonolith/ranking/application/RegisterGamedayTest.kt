package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.argumentsprovider.InvalidGameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.FakeGamedayRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import kotlin.reflect.KClass

class RegisterGamedayTest {
    private lateinit var registerGameday: RegisterGameday
    private lateinit var retrieveGameday: RetrieveGameday
    private lateinit var gamedayRepository: GamedayRepository

    @BeforeEach
    fun setUp() {
        gamedayRepository = FakeGamedayRepository()
        registerGameday = RegisterGameday(gamedayRepository)
        retrieveGameday = RetrieveGameday(gamedayRepository)
    }

    @Test
    fun `register and retrieve minimum gameday`() {
        // given
        val gamedayToRegisterDTO = RankingFixture.gamedayDTO()

        // when
        registerGameday.with(gamedayToRegisterDTO)
        val gamedayDTO = retrieveGameday.by(gamedayToRegisterDTO.id)

        // then
        assertThat(gamedayDTO).isEqualTo(gamedayToRegisterDTO)
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(InvalidGameday::class)
    fun `register game day with invalid information`(
        testDescription: String,
        gamedayDTO: GamedayDTO,
        expectedException: KClass<out Exception>,
        properties: Map<String, Any>
    ) {
        // when
        val exception = assertThrows<Exception> { registerGameday.with(gamedayDTO) }

        // then
        assertThat(exception).isInstanceOf(expectedException.javaObjectType)
        properties.forEach { assertThat(exception).hasFieldOrPropertyWithValue(it.key, it.value) }
    }
}