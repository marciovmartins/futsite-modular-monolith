package com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.Instant
import kotlin.reflect.KClass

annotation class Property(val key: String, val value: String)

@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [PastOrPresent.PastOrPresentConstraintValidator::class])
annotation class PastOrPresent(
    val from: String,
    val title: String = "",
    val message: String = "{com.gitlab.marciovmartins.futsite.modularmonolith.exception.PastOrPresent.message}",
    val properties: Array<Property> = [],
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
) {
    class PastOrPresentConstraintValidator : ConstraintValidator<PastOrPresent, Instant> {
        private lateinit var begin: Instant
        override fun isValid(value: Instant?, context: ConstraintValidatorContext?): Boolean {
            if (value == null) return true

            //TODO: get the clock system
            return value.isBefore(Instant.now())
                && value.isAfter(begin)
        }

        override fun initialize(constraintAnnotation: PastOrPresent) {
            begin = Instant.parse(constraintAnnotation.from)
        }
    }

}