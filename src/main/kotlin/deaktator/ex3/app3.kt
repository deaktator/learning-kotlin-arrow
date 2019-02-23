package deaktator.ex3

import arrow.Kind
import arrow.core.Option
import arrow.core.some
import arrow.data.ListK
import arrow.data.k
import arrow.instances.listk.traverse.traverse
import arrow.instances.option.applicative.applicative
import arrow.typeclasses.Applicative
import arrow.typeclasses.Traverse



fun main(args: Array<String>) {
    val x = Blah.traverse1(
            ListK.traverse(),
            Option.applicative(),
            listOf(1.some(), 2.some()).k(),
            { a -> a }
    )
    println(x)

    val y = Blah.traverse2(
            ListK.traverse(),
            Option.applicative(),
            listOf(1.some(), 2.some()).k(),
            { a -> a }
    )
    println(y)
}

object Blah {
    fun <F, G, A, B> Traverse<F>.traverse_(
            g: Applicative<G>,
            fa: Kind<F, A>,
            fn: (A) -> Kind<G, B>): Kind<G, Kind<F, B>> =
        fa.traverse(g, fn)

    fun <F, G, A, B> traverse1(
            f: Traverse<F>,
            g: Applicative<G>,
            fa: Kind<F, A>,
            fn: (A) -> Kind<G, B>): Kind<G, Kind<F, B>> =
        f.traverse_(g, fa, fn)

    fun <F, G, A, B> traverse2(
            f: Traverse<F>,
            g: Applicative<G>,
            fa: Kind<F, A>,
            fn: (A) -> Kind<G, B>): Kind<G, Kind<F, B>> = f.run {
        fa.traverse(g, fn)
    }
}
