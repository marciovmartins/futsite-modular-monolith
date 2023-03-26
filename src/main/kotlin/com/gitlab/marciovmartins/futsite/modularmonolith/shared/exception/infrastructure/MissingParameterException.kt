package com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure

class MissingParameterException(
    val propertyName: String,
    cause: Throwable? = null,
) : IllegalArgumentException(null, cause)