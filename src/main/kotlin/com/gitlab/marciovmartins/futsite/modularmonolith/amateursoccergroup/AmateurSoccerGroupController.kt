package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import org.springframework.data.repository.findByIdOrNull
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(value = ["/api/amateurSoccerGroups"])
class AmateurSoccerGroupController(
    private val amateurSoccerGroupRepository: AmateurSoccerGroupRepository,
) {
    @GetMapping
    fun showAll(): CollectionModel<AmateurSoccerGroupModel> {
        val allAmateurSoccerGroup = amateurSoccerGroupRepository.findAll()

        val allAmateurSoccerGroupModel = allAmateurSoccerGroup.map { createAmateurSoccerGroupModel(it) }

        val selfLink = linkTo(methodOn(AmateurSoccerGroupController::class.java).showAll()).withSelfRel()

        return CollectionModel.of(allAmateurSoccerGroupModel, selfLink)
    }

    @PostMapping
    fun create(@RequestBody amateurSoccerGroup: AmateurSoccerGroup): AmateurSoccerGroupModel {
        amateurSoccerGroupRepository.save(amateurSoccerGroup)
        return createAmateurSoccerGroupModel(amateurSoccerGroup)
    }

    @GetMapping
    @RequestMapping("/{amateurSoccerGroupId}")
    fun show(@PathVariable amateurSoccerGroupId: UUID): AmateurSoccerGroupModel? {
        val amateurSoccerGroup = amateurSoccerGroupRepository.findByIdOrNull(amateurSoccerGroupId)
            ?: return null

        return createAmateurSoccerGroupModel(amateurSoccerGroup, showCollection = true)
    }

    private fun createAmateurSoccerGroupModel(
        amateurSoccerGroup: AmateurSoccerGroup,
        showCollection: Boolean = false
    ): AmateurSoccerGroupModel {
        val model = AmateurSoccerGroupModel(amateurSoccerGroup)

        val selfLink = linkTo(
            methodOn(AmateurSoccerGroupController::class.java).show(amateurSoccerGroup.amateurSoccerGroupId!!)!!
        ).withSelfRel()
        model.add(selfLink)

        if (showCollection) {
            val collectionLink = linkTo(methodOn(AmateurSoccerGroupController::class.java).showAll())
                .withRel("allAmateurSoccerGroups")
            model.add(collectionLink)
        }

        return model
    }
}