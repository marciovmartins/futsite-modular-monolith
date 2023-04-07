package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.PlayerId

internal fun Gameday.toDTO() = GamedayDTO(
    gamedayId = this.gamedayId!!,
    amateurSoccerGroupId = this.amateurSoccerGroupId,
    date = this.date,
    matches = this.matches.map { it.toDTO() }
)

internal fun GamedayDTO.toDomain() = Gameday(
    gamedayId = this.gamedayId,
    amateurSoccerGroupId = this.amateurSoccerGroupId,
    date = this.date,
    matches = this.matches.map { it.toDomain() }
)

private fun Gameday.Match.toDTO() = GamedayDTO.Match(
    players = this.players.map { it.toDTO() }.toSet(),
)

private fun Gameday.Match.PlayerStatistic.toDTO() = GamedayDTO.Match.PlayerStatistic(
    playerId = this.playerId.value,
    team = GamedayDTO.Match.Team.valueOf(this.team.name),
    goalsInFavor = this.goalsInFavor,
    ownGoals = this.goalsAgainst,
    yellowCards = this.yellowCards,
    blueCards = this.blueCards,
    redCards = this.redCards,
)

private fun GamedayDTO.Match.toDomain() = Gameday.Match(
    players = this.players.map { it.toDomain() }.toSet()
)

private fun GamedayDTO.Match.PlayerStatistic.toDomain() = Gameday.Match.PlayerStatistic(
    playerId = PlayerId(this.playerId),
    team = Gameday.Match.Team.valueOf(this.team.name),
    goalsInFavor = this.goalsInFavor,
    goalsAgainst = this.ownGoals,
    yellowCards = this.yellowCards,
    blueCards = this.blueCards,
    redCards = this.redCards,
)