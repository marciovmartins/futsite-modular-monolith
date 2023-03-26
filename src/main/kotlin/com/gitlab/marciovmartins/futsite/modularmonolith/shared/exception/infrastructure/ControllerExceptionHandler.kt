package com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.domain.IllegalPropertyException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@ControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(IllegalPropertyException::class)
    fun handleIllegalArgumentException(ex: IllegalPropertyException): ProblemDetail {
        return ProblemDetail.forStatus(400).apply {
            this.type = typeUrl("illegal-property")
            this.title = ex::class.simpleName?.removeSuffix("Exception")
            this.detail = ex.message
            ex.properties.forEach { this.setProperty(it.key, it.value) }
        }
    }

    @ExceptionHandler(MissingParameterException::class)
    fun handleMissingPropertyException(ex: MissingParameterException): ProblemDetail {
        return ProblemDetail.forStatus(400).apply {
            this.type = typeUrl("illegal-parameters")
            this.title = "Your request parameters didn't validate"
            this.setProperty(
                "problems", listOf(
                    mapOf(
                        "reason" to "Missing Parameter",
                        "propertyName" to ex.propertyName,
                    )
                )
            )
        }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ProblemDetail {
        return ProblemDetail.forStatus(400).apply {
            this.type = typeUrl("illegal-parameters")
            this.title = "Your request parameters didn't validate"

            this.setProperty("problems", ex.constraintViolations.map {
                mutableMapOf<String, Any>(
                    "propertyName" to it.propertyPath.toString(),
                    "propertyValue" to it.invalidValue
                ).apply {
                    val attributes = it.constraintDescriptor.attributes

                    this["reason"] = it.message

                    val properties = attributes["properties"]
                    if (properties != null && properties is Array<*> && properties.isNotEmpty()) {
                        properties.forEach { property ->
                            if (property is Property) this[property.key] = property.value
                        }
                    }
                }.toMap()
            })
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ProblemDetail {
        return ProblemDetail.forStatus(400).apply {
            this.type = typeUrl("illegal-parameters")
            this.title = "Your request parameters didn't validate"
            when (ex.cause) {
                is MissingKotlinParameterException -> {
                    this.setProperty(
                        "problems", listOf(
                            mapOf(
                                "reason" to "Missing Parameter",
                                "propertyName" to (ex.cause as MissingKotlinParameterException).parameter.name,
                            )
                        )
                    )
                }
            }
        }
    }

    private fun typeUrl(exceptionType: String) = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .replacePath("/api/exception/$exceptionType")
        .build().toUri()
}