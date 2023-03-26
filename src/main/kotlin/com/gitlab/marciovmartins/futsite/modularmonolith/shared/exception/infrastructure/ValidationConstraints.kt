package com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.Instant
import kotlin.reflect.KClass

annotation class Property(val key: String, val value: String)

@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [IsAfter.IsAfterConstraintValidator::class])
annotation class IsAfter(
    val instant: String,
    val message: String = "{com.gitlab.marciovmartins.futsite.modularmonolith.exception.IsAfter.message}",
    val properties: Array<Property> = [],
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
) {
    class IsAfterConstraintValidator : ConstraintValidator<IsAfter, Instant> {
        private lateinit var thisInstant: Instant
        override fun isValid(value: Instant?, context: ConstraintValidatorContext?): Boolean {
            if (value == null) return true
            return value.isAfter(thisInstant)
        }

        override fun initialize(constraintAnnotation: IsAfter) {
            thisInstant = Instant.parse(constraintAnnotation.instant)
        }
    }
}