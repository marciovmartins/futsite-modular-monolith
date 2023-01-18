package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain

interface GamedayRepository {
    fun persist(gameday: Gameday)
    fun findBy(gamedayId: Gameday.GamedayId): Gameday
}
