package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.webmvc.RepositoryRestController
import java.util.UUID

@RepositoryRestController
interface GamedayRepository : JpaRepository<Gameday, UUID>
