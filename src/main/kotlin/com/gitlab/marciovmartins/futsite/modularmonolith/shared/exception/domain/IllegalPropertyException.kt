package com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.domain

class IllegalPropertyException(
    message: String,
    val properties: Map<String, Any> = emptyMap(),
    cause: Throwable? = null,
) : IllegalArgumentException(message, cause)