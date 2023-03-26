package com.gitlab.marciovmartins.futsite.modularmonolith.gameday.argumentsprovider

import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.TestMatchDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.TestPostGameDayDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.TestProblemDetailsDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.testMatchDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.testPlayerStatisticDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import java.util.stream.Stream

object InvalidGameDay : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            testDescription = "Gameday id not provided",
            gameday = defaultGameday().copy(
                gamedayId = null
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "gamedayId"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Amateur soccer group id not provided",
            gameday = defaultGameday().copy(
                amateurSoccerGroupId = null
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "amateurSoccerGroupId"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Date not provided",
            gameday = defaultGameday().copy(
                date = null
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "date"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Date year before 2020",
            gameday = defaultGameday().copy(
                date = Instant.parse("2019-12-31T23:59:59.999999Z")
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "The game day date should not be before 2020-01-01T00:00:00Z",
                    properties = mapOf(
                        "propertyName" to "date",
                        "propertyValue" to Instant.parse("2019-12-31T23:59:59.999999Z").toString(),
                        "minimumAllowedDate" to Instant.parse("2020-01-01T00:00:00.000000Z").toString(),
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Date in the future",
            gameday = defaultGameday().copy(
                date = Instant.now().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS)
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "The game day date should not be in the future",
                    properties = mapOf(
                        "propertyName" to "date",
                        "propertyValue" to Instant.now().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS).toString(),
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Matches not provided",
            gameday = defaultGameday().copy(
                matches = null
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "matches"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Gameday with empty matches",
            gameday = defaultGameday().copy(
                matches = emptyList<Any>(),
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Empty Matches",
                    properties = mapOf(
                        "propertyName" to "matches",
                        "minimumNumberOfMatches" to 1,
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Gameday with more than 99 matches",
            gameday = defaultGameday().copy(
                matches = (1 until 100).map { testMatchDTO() }
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "The maximum allowed matches are 99",
                    properties = mapOf(
                        "propertyName" to "matches",
                        "propertyValue" to 100,
                        "allowedNumberOfMatches" to 99,
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match players not provided",
            gameday = defaultGameday().copy(
                matches = listOf(
                    testMatchDTO(players = null)
                )
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "matches.players"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match with empty players",
            gameday = defaultGameday().copy(
                matches = listOf(
                    TestMatchDTO(
                        players = emptySet<Any>(),
                    ),
                ),
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Insufficient Number Players",
                    properties = mapOf(
                        "propertyName" to "matches.players",
                        "numberOfPlayers" to 0,
                        "minimumNumberOfPlayers" to 2,
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match with one player only",
            gameday = defaultGameday().copy(
                matches = listOf(
                    TestMatchDTO(
                        players = setOf(testPlayerStatisticDTO(team = "A")),
                    ),
                ),
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Insufficient Number Players",
                    properties = mapOf(
                        "propertyName" to "matches.players",
                        "numberOfPlayers" to 1,
                        "minimumNumberOfPlayers" to 2,
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match with more than 44 players",
            gameday = defaultGameday().copy(
                matches = listOf(
                    TestMatchDTO(
                        players = (
                            IntRange(1, 22).map { testPlayerStatisticDTO(team = "A") }
                                + IntRange(1, 22).map { testPlayerStatisticDTO(team = "B") }
                                + testPlayerStatisticDTO(team = "A")
                            ).toSet(),
                    ),
                ),
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Maximum number of players is 44",
                    properties = mapOf(
                        "propertyName" to "matches.players",
                        "propertyValue" to 45,
                        "maximumNumberOfPlayers" to 44,
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match with only team A players",
            gameday = defaultGameday().copy(
                matches = listOf(
                    TestMatchDTO(
                        players = (IntRange(1, 22).map { testPlayerStatisticDTO(team = "A") }).toSet(),
                    ),
                ),
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "must have at least one player statistic for team B",
                    properties = mapOf(
                        "propertyName" to "matches.players",
                        "missingTeam" to "B",
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match with only team B players",
            gameday = defaultGameday().copy(
                matches = listOf(
                    TestMatchDTO(
                        players = (IntRange(1, 22).map { testPlayerStatisticDTO(team = "B") }).toSet(),
                    ),
                ),
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "must have at least one player statistic for team A",
                    properties = mapOf(
                        "propertyName" to "matches.players",
                        "missingTeam" to "A",
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match player id not provided",
            gameday = defaultGameday().copy(
                matches = listOf(
                    testMatchDTO(
                        players = setOf(
                            testPlayerStatisticDTO(team = "A", playerId = null),
                            testPlayerStatisticDTO(team = "B")
                        )
                    )
                )
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "matches.players.playerId"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match player team not provided",
            gameday = defaultGameday().copy(
                matches = listOf(
                    testMatchDTO(
                        players = setOf(
                            testPlayerStatisticDTO(team = null),
                            testPlayerStatisticDTO(team = "B")
                        )
                    )
                )
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "matches.players.team"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match player goals in favor not provided",
            gameday = defaultGameday().copy(
                matches = listOf(
                    testMatchDTO(
                        players = setOf(
                            testPlayerStatisticDTO(team = "A", goalsInFavor = null),
                            testPlayerStatisticDTO(team = "B")
                        )
                    )
                )
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "matches.players.goalsInFavor"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match player goals against not provided",
            gameday = defaultGameday().copy(
                matches = listOf(
                    testMatchDTO(
                        players = setOf(
                            testPlayerStatisticDTO(team = "A", goalsAgainst = null),
                            testPlayerStatisticDTO(team = "B")
                        )
                    )
                )
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "matches.players.goalsAgainst"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match player yellow cards not provided",
            gameday = defaultGameday().copy(
                matches = listOf(
                    testMatchDTO(
                        players = setOf(
                            testPlayerStatisticDTO(team = "A", yellowCards = null),
                            testPlayerStatisticDTO(team = "B")
                        )
                    )
                )
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "matches.players.yellowCards"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match player blue cards not provided",
            gameday = defaultGameday().copy(
                matches = listOf(
                    testMatchDTO(
                        players = setOf(
                            testPlayerStatisticDTO(team = "A", blueCards = null),
                            testPlayerStatisticDTO(team = "B")
                        )
                    )
                )
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "matches.players.blueCards"
                    ),
                ),
            ),
        ),
        argument(
            testDescription = "Match player red cards not provided",
            gameday = defaultGameday().copy(
                matches = listOf(
                    testMatchDTO(
                        players = setOf(
                            testPlayerStatisticDTO(team = "A", redCards = null),
                            testPlayerStatisticDTO(team = "B")
                        )
                    )
                )
            ),
            problems = listOf(
                TestProblemDetailsDTO(
                    reason = "Missing Parameter",
                    properties = mapOf(
                        "propertyName" to "matches.players.redCards"
                    ),
                ),
            ),
        ),
    )

    private fun argument(
        testDescription: String,
        gameday: TestPostGameDayDTO,
        problems: List<TestProblemDetailsDTO>,
    ) = Arguments.of(
        testDescription, gameday, problems
    )

    private fun defaultGameday() = TestPostGameDayDTO(
        gamedayId = UUID.randomUUID(),
        amateurSoccerGroupId = UUID.randomUUID().toString(),
        date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
        matches = listOf(
            TestMatchDTO(
                players = setOf(
                    testPlayerStatisticDTO(team = "A"),
                    testPlayerStatisticDTO(team = "B"),
                )
            ),
        ),
    )
}
