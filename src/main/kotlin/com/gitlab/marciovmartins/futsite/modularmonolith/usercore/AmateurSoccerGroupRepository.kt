package com.gitlab.marciovmartins.futsite.modularmonolith.usercore

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository(value = "userCoreAmateurSoccerGroupRepository")
interface AmateurSoccerGroupRepository : JpaRepository<AmateurSoccerGroup, UUID>