package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import com.fasterxml.jackson.annotation.JsonRootName
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.util.UUID

@JsonRootName(value = "amateurSoccerGroup")
@Relation(collectionRelation = "amateurSoccerGroups")
open class AmateurSoccerGroupModel(
    val amateurSoccerGroupId: UUID,
    val name: String,
) : RepresentationModel<AmateurSoccerGroupModel>() {
    constructor(amateurSoccerGroup: AmateurSoccerGroup) : this(
        amateurSoccerGroupId = amateurSoccerGroup.amateurSoccerGroupId!!,
        name = amateurSoccerGroup.name
    )
}
