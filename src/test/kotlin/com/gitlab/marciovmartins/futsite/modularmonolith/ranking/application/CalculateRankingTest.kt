package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.argumentsprovider.GamedaySuccessfullyArgumentsProvider
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.UUID


/**
 * TODO: Look for some table/spreadsheet way to test several different possibilities provided by the business. CSV? JBehave?
 */
@ExtendWith(MockKExtension::class)
internal class CalculateRankingTest(
) {
    @MockK
    private lateinit var gamedayRepository: GamedayRepository

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(GamedaySuccessfullyArgumentsProvider::class)
    fun successfully(
        testDescription: String,
        amateurSoccerGroupId: UUID,
        period: RankingDTO.Period,
        gamedays: MutableMap<UUID, Gameday>,
        expectedRanking: RankingDTO,
    ) {
        //given
        val calculateRanking = CalculateRanking(gamedayRepository)

        every {
            gamedayRepository.findByAmateurSoccerGroupIdAndDateAfterAndDateBefore(
                amateurSoccerGroupId,
                period.from,
                period.to
            )
        } returns gamedays.values.toSet()

        // when
        val actualRanking = calculateRanking.with(amateurSoccerGroupId, period)

        // then
        assertThat(actualRanking).isEqualTo(expectedRanking)
    }
}