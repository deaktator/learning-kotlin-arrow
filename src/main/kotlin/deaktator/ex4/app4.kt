package deaktator.ex4

import arrow.Kind
import arrow.core.Option
import arrow.core.fix
import arrow.core.toOption
import arrow.instances.option.monad.monad
import arrow.typeclasses.Monad
import arrow.typeclasses.Semigroup
import arrow.instances.IntContext
import arrow.instances.option.applicative.applicative
import arrow.typeclasses.Applicative

fun main(args: Array<String>) {
    val a: Int? = 1
    val b: Int? = 2
    val oa = a.toOption()
    val ob = b.toOption()
    val x: Int? =
            Option
                    .monad()
                    .combineValues(IntContext, oa, ob)
                    .fix()
                    .orNull()

    val y: Int? =
            Option
                    .applicative()
                    .combineValues(IntContext, oa, ob)
                    .fix()
                    .orNull()

    println(x)
    println(y)
}

// NOTE: If this function is present, it will be called.
//       If it is omitted (commented out), the applicative version will be
//       called since Monads are Applicatives.
//
//       This shows that resolution works well.  The most specialized
//       implementation of combineValues will be found.
fun <M, A> Monad<M>.combineValues(s: Semigroup<A>, x: Kind<M, A>, y: Kind<M, A>): Kind<M, A> =
    binding {
        val a = x.bind()
        val b = y.bind()
        s.run { a.combine(b) }
    }

fun <M, A> Applicative<M>.combineValues(s: Semigroup<A>, x: Kind<M, A>, y: Kind<M, A>): Kind<M, A> =
    map(x, y){(a, b) -> s.run { a.combine(b) }}
