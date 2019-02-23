package deaktator.ex1

import arrow.Kind
import arrow.core.*
import arrow.data.ForListK
import arrow.data.ListK
import kotlin.String
import arrow.data.fix
import arrow.instances.list.traverse.sequence
import arrow.instances.option.applicative.applicative

import arrow.data.k
import arrow.instances.option.functor.map
import arrow.instances.option.monad.binding

import arrow.instances.IntContext
import arrow.instances.listk.foldable.fold

fun main(args: Array<String>) {
    // Raul Raja:
    //   ForXy is the unique witness for Xy from the paper.
    //   XyOf<A> is a type alias for Kind<ForXy, A>.
    //   fix() is an extension method on XyOf<A> returning an Xy<A>.
    //   k() is an extension method that lifts Xy<A> into a XyOf<A> (HKT version).

    // arrow.instances.option.applicative.applicative
    val oa = Option.applicative()

    // arrow.core.some, arrow.core.none
    val lst1 = listOf(1.some(), none(), 3.some())

    // sequence is enabled with arrow.instances.list.traverse.sequence
    // Not quite an Option<List<A>> because of Kind wrapping, but it "kind" of works ;-).
    val olst1K: Kind<ForOption, Kind<ForListK, Int>> = lst1.sequence(oa)

    // fix transforms from Kind<K, A> to K<A>.  Fixing the outer type gives:
    val okflki: Option<Kind<ForListK, Int>> = olst1K.fix()

    // Fixing the inner type gives the type we'd want if Kotlin had better machinery for
    // higher kinded types built in:
    val olki: Option<ListK<Int>> = okflki.map { it.fix() }


    val lst2 = listOf(Option(1), Option(2))
    val olst2 = lst2.sequence(oa)

    // arrow.core.fix: Kind<ForOption, A>  -> Option<A>
    // arrow.data.fix: Kind<ForListK, Int> -> ListK<Int>
    //      olst2 is a Kind            xs is a Kind
    //            arrow.core.fix          arrow.data.fix   arrow.core.getOrElse
    val s = olst2.fix()   .map { xs -> xs.fix().sum() }   .getOrElse { 0 }

    // arrow.instances.option.applicative.applicative allows us to omit first fix.
    // because ListK has a foldable instance, we can fold using a Monoid[Int], of
    // which IntContext is an instance.
    // .fold is enabled with arrow.instances.listk.foldable.fold
    val s1: Option<Int> = olst2   .map { xs -> xs.fold(IntContext) }
    val s2 = s1.getOrElse { 1 }

    println("fix: $s")
    println("fancy monoid fold: $s1")
    println("fancy monoid fold unwrapped: $s2")

    val lst123 = listOf(1, 2, 3)

    // .k() wraps a value in an Arrow (higher kinded) wrapper.
    // List<A> -> arrow.data.ListK<A>
    val lstK123: ListK<Int> = lst123.k()
    println(lstK123.map { i -> i + 0.5 })

    // This is useful:
    // monad comprehension via binding block.
    //
    // import arrow.instances.option.monad.monad to use Option.monad().binding
    // import arrow.instances.option.monad.binding to just using binding, but
    //   keep in mind you can only have one binding import in scope.

    val o1 = 1.some()
    val o2 = 2.some()

    val c = binding {
        val a = o1.bind()
        val b = o2.bind()
        a + b
    }
    println(c)
}
