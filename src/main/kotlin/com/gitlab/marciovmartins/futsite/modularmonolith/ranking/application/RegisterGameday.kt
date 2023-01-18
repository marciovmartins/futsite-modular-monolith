package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.GamedayRepository

class RegisterGameday(
    private val gamedayRepository: GamedayRepository
) {
    fun with(gamedayToCreateDTO: GamedayDTO) {
        val gameday = GamedayMapper.from(gamedayToCreateDTO)
        gamedayRepository.persist(gameday)
    }
}
