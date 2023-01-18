package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.FakeGamedayRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    fun `register minimum gameday`() {
        // given
        val gamedayToCreateDTO = RankingFixture.gamedayToCreateDTO()

        // when
        registerGameday.with(gamedayToCreateDTO)
        val gamedayDTO = retrieveGameday.by(gamedayToCreateDTO.id)

        // then
        assertThat(gamedayDTO).isEqualTo(gamedayToCreateDTO)
    }
}