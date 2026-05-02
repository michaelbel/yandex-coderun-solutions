import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private const val MOD = 1_000_000_007
private val reader = BufferedReader(InputStreamReader(System.`in`))
private val writer = BufferedWriter(OutputStreamWriter(System.out))

private fun readInt(): Int {
    var c = reader.read()
    while (c <= ' '.code) {
        c = reader.read()
    }
    var neg = false
    if (c == '-'.code) {
        neg = true
        c = reader.read()
    }
    var x = 0
    while (c in '0'.code..'9'.code) {
        x = x * 10 + (c - '0'.code)
        c = reader.read()
    }
    return if (neg) -x else x
}

private fun modPow(x: Long, exp: Int): Int {
    var base = x % MOD
    var e = exp
    var result = 1L
    while (e > 0) {
        if (e and 1 == 1) result = (result * base) % MOD
        base = (base * base) % MOD
        e = e shr 1
    }
    return result.toInt()
}

fun main() {
    val n = readInt()
    val a = IntArray(n + 1)
    for (i in 1..n) {
        a[i] = readInt()
    }

    val pref = IntArray(n + 1)
    val invPref = IntArray(n + 1)

    pref[0] = 1
    for (i in 1..n) {
        pref[i] = (pref[i - 1].toLong() * a[i] % MOD).toInt()
    }
    invPref[n] = modPow(pref[n].toLong(), MOD - 2)
    for (i in n downTo 1) {
        invPref[i - 1] = (invPref[i].toLong() * a[i] % MOD).toInt()
    }

    val q = readInt()
    repeat(q) {
        when (readInt()) {
            0 -> {
                val l = readInt()
                val r = readInt()
                for (i in l..r) {
                    a[i]++
                }
                for (i in l..n) {
                    pref[i] = (pref[i - 1].toLong() * a[i] % MOD).toInt()
                }
                invPref[n] = modPow(pref[n].toLong(), MOD - 2)
                for (i in n downTo l) {
                    invPref[i - 1] = (invPref[i].toLong() * a[i] % MOD).toInt()
                }
            }
            1 -> {
                val l = readInt()
                val r = readInt()
                val ans = (pref[r].toLong() * invPref[l - 1] % MOD).toInt()
                writer.write(ans.toString())
                writer.newLine()
            }
        }
    }

    writer.flush()
}
