package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.argumentsprovider.GamedaySuccessfullyArgumentsProvider
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.FakeGamedayRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.UUID


/**
 * TODO: Look for some table/spreadsheet way to test several different possibilities provided by the business. CSV? JBehave?
 */
internal class CalculateRankingTest(
) {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(GamedaySuccessfullyArgumentsProvider::class)
    fun successfully(
        testDescription: String,
        amateurSoccerGroupId: UUID,
        period: RankingDTO.Period,
        gamedays: Set<Gameday>,
        expectedRanking: RankingDTO,
    ) {
        //given
        val gamedayRepository = FakeGamedayRepository(gamedays)
        val calculateRanking = CalculateRanking(gamedayRepository)

        // when
        val actualRanking = calculateRanking.with(amateurSoccerGroupId, period)

        // then
        assertThat(actualRanking).isEqualTo(expectedRanking)
    }
}