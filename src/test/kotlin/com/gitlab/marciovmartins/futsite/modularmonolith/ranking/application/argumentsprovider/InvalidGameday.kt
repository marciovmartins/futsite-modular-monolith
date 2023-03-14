package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.argumentsprovider

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.GamedayDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.RankingFixture
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.Instant
import java.util.stream.Stream
import kotlin.reflect.KClass

object InvalidGameday : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            testDescription = "Gameday date year before 2020",
            gameday = RankingFixture.gamedayDTO().copy(
                date = Instant.parse("2019-12-31T23:59:59.999999Z")
            ),
            exception = Gameday.Date.InvalidDateException::class,
            properties = mapOf(
                Gameday.InvalidGamedayException::propertyName.name to "date",
                Gameday.InvalidGamedayException::propertyValue.name to Instant.parse("2019-12-31T23:59:59.999999Z"),
            ),
        ),
        argument(
            testDescription = "Gameday date in the future",
            gameday = RankingFixture.gamedayDTO().copy(
                date = Instant.parse("2030-01-01T00:00:00.000000Z")
            ),
            exception = Gameday.Date.InvalidDateException::class,
            properties = mapOf(
                Gameday.InvalidGamedayException::propertyName.name to "date",
                Gameday.InvalidGamedayException::propertyValue.name to Instant.parse("2030-01-01T00:00:00.000000Z"),
            ),
        ),
        argument(
            testDescription = "Gameday with empty matches",
            gameday = RankingFixture.gamedayDTO().copy(
                matches = emptyList(),
            ),
            exception = Gameday.EmptyMatchesException::class,
            properties = mapOf(
                Gameday.InvalidGamedayException::propertyName.name to "matches",
            ),
        ),
        argument(
            testDescription = "Gameday with more than 99 matches",
            gameday = RankingFixture.gamedayDTO().copy(
                matches = (1..100).map { RankingFixture.matchDTO() }
            ),
            exception = Gameday.TooManyMatchesException::class,
            properties = mapOf(
                Gameday.InvalidGamedayException::propertyName.name to "matches",
            ),
        ),
        argument(
            testDescription = "Match with empty players",
            gameday = RankingFixture.gamedayDTO().copy(
                matches = listOf(
                    RankingFixture.matchDTO(
                        players = emptySet(),
                    ),
                ),
            ),
            exception = Gameday.Match.EmptyPlayersException::class,
            properties = mapOf(
                Gameday.InvalidGamedayException::propertyName.name to "matches.players",
            ),
        ),
        argument(
            testDescription = "Match with one player only",
            gameday = RankingFixture.gamedayDTO().copy(
                matches = listOf(
                    RankingFixture.matchDTO(
                        players = setOf(RankingFixture.playerDTO(team = GamedayDTO.MatchDTO.Team.A)),
                    ),
                ),
            ),
            exception = Gameday.Match.InsufficientPlayersException::class,
            properties = mapOf(
                Gameday.InvalidGamedayException::propertyName.name to "matches.players",
            ),
        ),
        argument(
            testDescription = "Match with more than 44 players",
            gameday = RankingFixture.gamedayDTO().copy(
                matches = listOf(
                    RankingFixture.matchDTO(
                        players = (
                            IntRange(1, 22).map { RankingFixture.playerDTO(team = GamedayDTO.MatchDTO.Team.A) }
                                + IntRange(1, 22).map { RankingFixture.playerDTO(team = GamedayDTO.MatchDTO.Team.B) }
                                + RankingFixture.playerDTO(team = GamedayDTO.MatchDTO.Team.A)
                            ).toSet(),
                    ),
                ),
            ),
            exception = Gameday.Match.TooManyPlayersException::class,
            properties = mapOf(
                Gameday.InvalidGamedayException::propertyName.name to "matches.players",
                Gameday.Match.TooManyPlayersException::numberOfPlayers.name to 45,
            ),
        ),
    )
}

private fun argument(
    testDescription: String,
    gameday: GamedayDTO,
    exception: KClass<out Exception>,
    properties: Map<String, Any>,
) = Arguments.of(testDescription, gameday, exception, properties)
