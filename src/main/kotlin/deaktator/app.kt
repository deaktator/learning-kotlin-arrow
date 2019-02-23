package deaktator

fun main() {
    (1 .. 10000).map { x -> cnt(x) }.forEach { x -> println(x) }
}

fun cnt(x: Int): Int = fh(x).second

tailrec fun fh(x: Int, n: Int = 1): Pair<Int, Int> =
    if (x == 1) Pair(x, n)
    else if (x % 2 == 0) fh(x/2, n+1)
    else fh(3*x + 1, n + 1)
