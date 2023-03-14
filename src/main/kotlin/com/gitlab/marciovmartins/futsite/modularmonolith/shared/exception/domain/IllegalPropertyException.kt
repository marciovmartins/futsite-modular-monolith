package com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.domain

open class IllegalPropertyException(
    val properties: Map<String, Any?> = emptyMap(),
    cause: Throwable? = null,
) : IllegalArgumentException(null, cause)