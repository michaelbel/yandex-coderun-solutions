import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val s = reader.readLine()!!
    val n = s.length

    val prefix = Array(26) { IntArray(n + 1) }
    for (i in 0 until n) {
        val ci = s[i] - 'a'
        for (c in 0 until 26) {
            prefix[c][i + 1] = prefix[c][i]
        }
        prefix[ci][i + 1]++
    }

    val divisors = mutableListOf<Int>()
    var x = 1
    while (x * x <= n) {
        if (n % x == 0) {
            divisors.add(x)
            if (x != n / x) divisors.add(n / x)
        }
        x++
    }
    divisors.sortDescending()

    var answer = 1
    outer@ for (k in divisors) {
        if (k == 1) {
            answer = 1
            break
        }
        val len = n / k
        for (c in 0 until 26) {
            if (prefix[c][n] % k != 0) continue@outer
        }
        val baseCount = IntArray(26) { prefix[it][len] }
        for (j in 1 until k) {
            val start = j * len
            val end = start + len
            for (c in 0 until 26) {
                if (prefix[c][end] - prefix[c][start] != baseCount[c]) {
                    continue@outer
                }
            }
        }
        answer = k
        break@outer
    }

    writer.write(answer.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
