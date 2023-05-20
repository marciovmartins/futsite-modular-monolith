package com.gitlab.marciovmartins.futsite.modularmonolith.usercore

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroupController
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController(value = "userCoreAmateurSoccerGroupController")
@RequestMapping(value = ["/api/user-core/amateurSoccerGroups"])
class AmateurSoccerGroupController(
    private val amateurSoccerGroupRepository: AmateurSoccerGroupRepository,
) {
    @GetMapping("{amateurSoccerGroupId}")
    fun show(
        @PathVariable amateurSoccerGroupId: UUID,
    ): EntityModel<*>? {
        return amateurSoccerGroupRepository.findById(amateurSoccerGroupId).map {
            amateurSoccerGroupEntityModel(it)
        }.orElse(null)
    }

    private fun amateurSoccerGroupEntityModel(amateurSoccerGroup: AmateurSoccerGroup): EntityModel<AmateurSoccerGroup> {
        return EntityModel.of(
            amateurSoccerGroup,
            linkTo(methodOn(AmateurSoccerGroupController::class.java).show(amateurSoccerGroup.amateurSoccerGroupId)!!)
                .withSelfRel()
        )
    }
}