package deaktator.ex2

import kotlin.Exception
import arrow.core.Either
import arrow.instances.either.applicative.applicative
import arrow.instances.either.monad.monad
import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    val e1 = Either.right(1)
    val ee1: Either<Exception, Int> = Either.left(IllegalArgumentException("failure 1."))
    val ee2: Either<Exception, Int> = Either.left(Exception("failure 2."))

    val res1 = Either.applicative<Exception>().map(e1, ee1, ee2) { (a, b, c) -> a + b + c }
    println(res1)

    // Monads are Applicatives so we can support those operations from Monad.
    val res2 = Either.monad<Exception>().map(e1, ee1, ee2) { (a, b, c) -> a + b + c }
    println(res2)

    val res3 = Either.monad<Exception>().binding {
        val a = e1.bind()
        val b = ee1.bind()
        val c = ee2.bind()
        a + b + c
    }

    println(res3)
}
