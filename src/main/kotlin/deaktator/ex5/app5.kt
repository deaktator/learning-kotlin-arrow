package deaktator.ex5

import arrow.Kind
import arrow.core.Either
import arrow.core.EitherPartialOf
import arrow.data.*
import arrow.instances.either.applicativeError.applicativeError
import arrow.instances.nonemptylist.semigroup.semigroup
import arrow.instances.validated.applicativeError.applicativeError
import arrow.typeclasses.ApplicativeError

fun main(args: Array<String>) {
    val fields = listOf(
            FormField("Invalid Email Domain Label", "nowhere.com"),
            FormField("Too Long Email Label", "nowheretoolong${(0..251).map { "g" }}"), //this accumulates N errors
            FormField("Valid Email Label", "getlost@nowhere.com")
    )

    // -------------------------------------------------------------------------------------------------
    //              accumulateErrors                                   failFast
    // -------------------------------------------------------------------------------------------------
    //
    //    [                                                 [
    //      Invalid(                                          Left(
    //        e=NonEmptyList(all=[                              a=NonEmptyList(all=[
    //          NotAnEmail(reasons=NonEmptyList(all=[             NotAnEmail(reasons=NonEmptyList(all=[
    //            DoesNotContain(value=@)                           DoesNotContain(value=@)
    //          ]))                                               ]))
    //        ])                                                ])
    //      ),                                                ),
    //      Invalid(                                          Left(
    //        e=NonEmptyList(all=[                              a=NonEmptyList(all=[
    //          NotAnEmail(reasons=NonEmptyList(all=[             NotAnEmail(reasons=NonEmptyList(all=[
    //            DoesNotContain(value=@),                          DoesNotContain(value=@)
    //            MaxLength(value=250)
    //          ]))                                               ]))
    //        ])                                                ])
    //      ),                                                ),
    //      Valid(a=Email(value=getlost@nowhere.com))         Right(b=Email(value=getlost@nowhere.com))
    //    ]                                                 ]

    val a = Rules accumulateErrors {
        fields.map { it.validateEmail() }
    }

    val b = Rules failFast {
        fields.map { it.validateEmail() }
    }
    println(a)
    println(b)
}

sealed class ValidationError(val msg: String) {
    data class DoesNotContain(val value: String) : ValidationError("Did not contain $value")
    data class MaxLength(val value: Int) : ValidationError("Exceeded length of $value")
    data class NotAnEmail(val reasons: Nel<ValidationError>) : ValidationError("Not a valid email")
}

data class FormField(val label: String, val value: String)
data class Email(val value: String)

sealed class Rules<F>(A: ApplicativeError<F, Nel<ValidationError>>) : ApplicativeError<F, Nel<ValidationError>> by A {

    private fun FormField.contains(needle: String): Kind<F, FormField> =
        if (value.contains(needle, false)) just(this)
        else raiseError(ValidationError.DoesNotContain(needle).nel())

    private fun FormField.maxLength(maxLength: Int): Kind<F, FormField> =
        if (value.length <= maxLength) just(this)
        else raiseError(ValidationError.MaxLength(maxLength).nel())

    fun FormField.validateEmail(): Kind<F, Email> =
        map(contains("@"), maxLength(250)) {
            Email(value)
        }.handleErrorWith { raiseError(ValidationError.NotAnEmail(it).nel()) }

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv   Code to swap between short circuiting   vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    object ErrorAccumulationStrategy :
        Rules<ValidatedPartialOf<Nel<ValidationError>>>(Validated.applicativeError(NonEmptyList.semigroup()))

    object FailFastStrategy :
        Rules<EitherPartialOf<Nel<ValidationError>>>(Either.applicativeError())

    companion object {
        infix fun <A> failFast(f: FailFastStrategy.() -> A): A = f(FailFastStrategy)
        infix fun <A> accumulateErrors(f: ErrorAccumulationStrategy.() -> A): A = f(ErrorAccumulationStrategy)
    }
}
