package deaktator.ex7

import kotlin.math.roundToLong

fun main() {
    val xs = listOf(1.0, 2.4, 2.5, 3.0, 4.0)

    println(xs.findAll0(2.5, 3.0, EqFunkyD))
    println(xs.findAll1(2.5, 3.0, EqFunkyD))
    println(xs.findAll2(2.5, 3.0, EqFunkyD))
    println(xs.findAll3(2.5, 3.0, EqFunkyD))
    println(xs.findAll4(2.5, 3.0, EqFunkyD))
    println(xs.findAll5(2.5, 3.0, EqFunkyD))
    println(xs.findAll6(2.5, 3.0, EqFunkyD))
    println(xs.findAll7(2.5, 3.0, EqFunkyD))
    println(xs.findAll8(2.5, 3.0, EqFunkyD))
    println(xs.findAll9(2.5, 3.0, EqFunkyD))

    println()

    println(xs.findAll0(2.5, 3.0, EqD))
    println(xs.findAll1(2.5, 3.0, EqD))
    println(xs.findAll2(2.5, 3.0, EqD))
    println(xs.findAll3(2.5, 3.0, EqD))
    println(xs.findAll4(2.5, 3.0, EqD))
    println(xs.findAll5(2.5, 3.0, EqD))
    println(xs.findAll6(2.5, 3.0, EqD))
    println(xs.findAll7(2.5, 3.0, EqD))
    println(xs.findAll8(2.5, 3.0, EqD))
    println(xs.findAll9(2.5, 3.0, EqD))
}

interface Eq<A> {
    infix fun A.amTheSameAs(a: A): Boolean
    infix fun A.areDifferentThan(a: A): Boolean = !amTheSameAs(a)
}

object EqFunkyD: Eq<Double> {
    override fun Double.amTheSameAs(a: Double) = a.toLong() == roundToLong()
}

object EqD: Eq<Double> {
    override fun Double.amTheSameAs(a: Double) = this == a
}

// Find all `A`s in the (receiver) list that are the same as `i` and
// different than `you`, according to the equivalence defined in `eq`.
fun <A> List<A>.findAll0(i: A, you: A, eq: Eq<A>): List<A> {
    return this.filter { a ->
        with(eq) {
            a.amTheSameAs(i) && a.areDifferentThan(you)
        }
    }
}

fun <A> List<A>.findAll1(i: A, you: A, eq: Eq<A>): List<A> {
    return filter { a ->
        with(eq) {
            a.amTheSameAs(i) && a.areDifferentThan(you)
        }
    }
}

fun <A> List<A>.findAll2(i: A, you: A, eq: Eq<A>): List<A> {
    return eq.run {
        filter { a ->
            a.amTheSameAs(i) && a.areDifferentThan(you)
        }
    }
}

fun <A> List<A>.findAll3(i: A, you: A, eq: Eq<A>): List<A> {
    return eq.run {
        filter { a ->
            a amTheSameAs i && a areDifferentThan you
        }
    }
}

fun <A> List<A>.findAll4(i: A, you: A, eq: Eq<A>): List<A> {
    return eq.run {
        filter { it ->
            it amTheSameAs i && it areDifferentThan you
        }
    }
}

fun <A> List<A>.findAll5(i: A, you: A, eq: Eq<A>): List<A> {
    return eq.run {
        filter {
            it amTheSameAs i && it areDifferentThan you
        }
    }
}

fun <A> List<A>.findAll6(i: A, you: A, eq: Eq<A>): List<A> =
    eq.run {
        filter {
            it amTheSameAs i && it areDifferentThan you
        }
    }

fun <A> List<A>.findAll7(i: A, you: A, eq: Eq<A>): List<A> = eq.run {
    filter {
        it amTheSameAs i && it areDifferentThan you
    }
}

// This is probably the most concise and best choice.
fun <A> List<A>.findAll8(i: A, you: A, eq: Eq<A>): List<A> = eq.run {
    filter { it amTheSameAs i && it areDifferentThan you }
}

// This is tricky.  This is not necessarily the same as the previous function because there
// are no laws associated with Eq.  Specifically, Eq doesn't impose a commutativity law, so
// arguments cannot necessarily be swapped in amTheSameAs and areDifferentThan with the same
// result.
//
// If Eq had a commutativity law, then it could be proven that this function is the same as
// the previous ones.
//
// All previous functions should be exactly the same.
fun <A> List<A>.findAll9(i: A, you: A, eq: Eq<A>): List<A> = eq.run {
    filter { i amTheSameAs it && you areDifferentThan it }
}
