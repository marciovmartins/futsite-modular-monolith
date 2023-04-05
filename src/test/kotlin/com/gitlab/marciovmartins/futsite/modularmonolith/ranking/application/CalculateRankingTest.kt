package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.FakeGamedayRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID


internal class CalculateRankingTest {
    @Test
    fun `without any gameday registered`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val period = RankingDTO.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now()
        )

        val expectedRanking = RankingDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            period = period,
            playerStatistics = emptySet(),
        )

        val calculateRanking = CalculateRanking(FakeGamedayRepository())

        // when
        val actualRanking = calculateRanking.with(amateurSoccerGroupId, period)

        // then
        assertThat(actualRanking).isEqualTo(expectedRanking)
    }

    @Test
    fun `with one gameday registered`() {
        TODO("need to be implemented")
    }

    @Test
    fun `with many gameday registered`() {
        TODO("need to be implemented")
    }
}