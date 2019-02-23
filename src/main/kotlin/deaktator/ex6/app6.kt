package deaktator.ex6

import kotlin.math.roundToLong


fun main(args: Array<String>) {
    val a = 2.5
    val b = 3.0
    val same = a
    val diff = b

    val lst = listOf(1.0, a, b, 4.0)
    val filtered1 = lst.findWhere(same, diff, X.DoubleRoundEq)
    val filtered2 = lst.findWhere(same, diff, X.DoubleEq)

    println(filtered1)
    println(filtered2)

    val filtered3 = lst.findWhere(same, diff, X.DoubleRoundEq1)
    println(filtered3)
}

// Because Eq only provides extensions on A and not functions whose inputs are As, we need
// to "import" the extension functions to use them.  This is what the `run` call does.
// It calls the block and includes eq in the block's scope.  Note that the List<A> methods
// are also in scope.
fun <A> List<A>.findWhere(same: A, different: A, eq: Eq<A>): List<A> = eq.run {
     filter { it eqv same && it neqv different }
}

fun <A> List<A>.findWhere(same: A, different: A, eq: Eq1<A>): List<A> {
    return filter { a -> eq.eqv(a, same) && eq.neqv(a, different) }
}

interface Eq<A> {
    infix fun A.eqv(b: A): Boolean
    infix fun A.neqv(b: A) = !eqv(b)
}

interface Eq1<A> {
    fun eqv(a: A, b: A): Boolean
    fun neqv(a: A, b: A) = !eqv(a, b)
}

object X {

    object DoubleRoundEq1: Eq1<Double> {
        // Notice this is not an extension.
        override fun eqv(a: Double, b: Double) = a.toLong() == b.roundToLong()
    }

    object DoubleRoundEq: Eq<Double> {
        // Notice this is an extension on Double.
        override fun Double.eqv(b: Double) = toLong() == b.roundToLong()
    }

    object DoubleEq: Eq<Double> {
        override fun Double.eqv(b: Double) = this == b
    }
}