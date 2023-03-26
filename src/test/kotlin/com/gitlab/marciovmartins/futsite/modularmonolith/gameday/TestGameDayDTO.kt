package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class TestPostGameDayDTO(
    val gamedayId: Any?,
    val amateurSoccerGroupId: Any?,
    val date: Any?,
    val matches: Any?,
)

data class TestRetrieveGameDayDTO(
    val amateurSoccerGroupId: Any?,
    val date: Any?,
    val matches: Any?,
    @JsonProperty("_links")
    val links: Map<String, LinkDTO>,
)

data class TestMatchDTO(
    val players: Any?,
)

data class TestPlayerStatisticDTO(
    val playerId: Any?,
    val team: Any?,
    val goalsInFavor: Any?,
    val goalsAgainst: Any?,
    val yellowCards: Any?,
    val blueCards: Any?,
    val redCards: Any?,
)

data class LinkDTO(
    val href: String,
)

fun testMatchDTO(
    players: Any? = setOf(
        testPlayerStatisticDTO(team = "A"),
        testPlayerStatisticDTO(team = "B"),
    )
) = TestMatchDTO(players)

fun testPlayerStatisticDTO(
    team: Any?,
    playerId: Any? = UUID.randomUUID().toString(),
    goalsInFavor: Any? = 0,
    goalsAgainst: Any? = 0,
    yellowCards: Any? = 0,
    blueCards: Any? = 0,
    redCards: Any? = 0
) = TestPlayerStatisticDTO(
    playerId, team, goalsInFavor, goalsAgainst, yellowCards, blueCards, redCards,
)

fun TestPostGameDayDTO.toTestRetrieveGameDayDTO(
    links: Map<String, LinkDTO>,
) = TestRetrieveGameDayDTO(
    amateurSoccerGroupId = this.amateurSoccerGroupId,
    date = this.date,
    matches = this.matches,
    links = links
)