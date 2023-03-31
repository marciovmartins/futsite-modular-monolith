package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.webmvc.RepositoryRestController
import java.util.UUID

@RepositoryRestController
@RepositoryRestResource(path = "gameDays")
interface GamedayRepository : JpaRepository<Gameday, UUID>
