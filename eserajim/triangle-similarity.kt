import java.io.BufferedInputStream
import java.io.IOException
import java.util.HashSet

fun main() {
    val input = BufferedInputStream(System.`in`)
    val n = readInt(input)
    val set = HashSet<Tri>(n * 4 / 3 + 1)
    repeat(n) {
        var a = readInt(input)
        var b = readInt(input)
        var c = readInt(input)
        if (a > b) { val t = a; a = b; b = t }
        if (a > c) { val t = a; a = c; c = t }
        if (b > c) { val t = b; b = c; c = t }
        val g = gcd(gcd(a, b), c)
        a /= g; b /= g; c /= g
        set.add(Tri(a, b, c))
    }
    System.out.println(set.size)
}

@Throws(IOException::class)
private fun readInt(input: BufferedInputStream): Int {
    var c = input.read()
    while (c != -1 && c <= ' '.code) {
        c = input.read()
    }
    if (c == -1) throw IOException("Unexpected EOF")
    var neg = false
    if (c == '-'.code) {
        neg = true
        c = input.read()
    }
    var x = 0
    while (c >= '0'.code && c <= '9'.code) {
        x = x * 10 + (c - '0'.code)
        c = input.read()
    }
    return if (neg) -x else x
}

private fun gcd(a: Int, b: Int): Int {
    var x = a
    var y = b
    while (y != 0) {
        val t = x % y
        x = y
        y = t
    }
    return x
}

private data class Tri(val a: Int, val b: Int, val c: Int)
