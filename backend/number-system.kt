import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val s = reader.readLine().replace(" ", "")
    val parts = s.split("=")
    if (parts.size != 2) {
        writer.write("-1")
        writer.flush()
        return
    }
    fun parseExpression(expr: String, factor: Int): List<Pair<String, Int>> {
        val tokens = mutableListOf<Pair<String, Int>>()
        var sign = 1
        var current = StringBuilder()
        var i = 0
        while (i < expr.length) {
            val ch = expr[i]
            if (ch == '+' || ch == '-') {
                if (current.isNotEmpty()) {
                    tokens.add(Pair(current.toString(), sign * factor))
                    current = StringBuilder()
                }
                sign = if (ch == '+') 1 else -1
            } else {
                current.append(ch)
            }
            i++
        }
        if (current.isNotEmpty()) {
            tokens.add(Pair(current.toString(), sign * factor))
        }
        return tokens
    }
    val leftTokens = parseExpression(parts[0], 1)
    val rightTokens = parseExpression(parts[1], -1)
    val tokens = leftTokens + rightTokens
    var bMin = 2
    var maxDegree = 0
    for ((num, _) in tokens) {
        if (num.isEmpty()) continue
        var localMax = 0
        for (ch in num) {
            val d = if (ch in '0'..'9') ch - '0' else ch - 'A' + 10
            if (d > localMax) localMax = d
        }
        bMin = max(bMin, localMax + 1)
        maxDegree = max(maxDegree, num.length - 1)
    }
    val poly = IntArray(maxDegree + 1) { 0 }
    for ((num, sign) in tokens) {
        val len = num.length
        for (j in 0 until len) {
            val ch = num[j]
            val d = if (ch in '0'..'9') ch - '0' else ch - 'A' + 10
            poly[len - 1 - j] += sign * d
        }
    }
    var allZero = true
    for (coef in poly) {
        if (coef != 0) { allZero = false; break }
    }
    if (allZero) {
        writer.write(bMin.toString())
        writer.flush()
        return
    }
    var k = 0
    while (k < poly.size && poly[k] == 0) {
        k++
    }
    if (k == poly.size) {
        writer.write(bMin.toString())
        writer.flush()
        return
    }
    val Q0 = poly[k]
    val absQ0 = abs(Q0)
    val divisors = mutableSetOf<Int>()
    var i = 1
    while (i * i <= absQ0) {
        if (absQ0 % i == 0) {
            divisors.add(i)
            divisors.add(absQ0 / i)
        }
        i++
    }
    val candidates = divisors.filter { it >= bMin }.sorted()
    fun evaluate(b: Int): BigInteger {
        var res = BigInteger.valueOf(poly[poly.size - 1].toLong())
        for (i in poly.size - 2 downTo 0) {
            res = res.multiply(BigInteger.valueOf(b.toLong())).add(BigInteger.valueOf(poly[i].toLong()))
        }
        return res
    }
    for (cand in candidates) {
        if (evaluate(cand) == BigInteger.ZERO) {
            writer.write(cand.toString())
            writer.flush()
            return
        }
    }
    writer.write("-1")
    writer.flush()
}
