package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure.MissingParameterException
import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.stereotype.Repository
import java.util.UUID

@RepositoryRestController
interface GamedayRepository : JpaRepository<Gameday, UUID>, CustomizedGamedayRepository

interface CustomizedGamedayRepository {
    fun <S : Gameday?> save(entity: S): S
}

@Repository
class CustomizedGamedayRepositoryImpl(
    private val em: EntityManager,
) : CustomizedGamedayRepository {
    override fun <S : Gameday?> save(entity: S): S {
        if (entity?.gamedayId == null) throw MissingParameterException(propertyName = "gamedayId")
        em.persist(entity)
        return entity
    }
}