import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().trim().toInt()

    // Sieve of Eratosthenes to mark primes up to n
    val isPrime = BooleanArray(n + 1)
    if (n >= 2) {
        for (i in 2..n) isPrime[i] = true
        var p = 2
        while (p * p <= n) {
            if (isPrime[p]) {
                var k = p * p
                while (k <= n) {
                    isPrime[k] = false
                    k += p
                }
            }
            p++
        }
    }

    // DP: win[i] = true, если игрок при i спичках имеет выигрышную стратегию
    val win = BooleanArray(n + 1)
    win[0] = false // нет ходов — проигрыш

    for (i in 1..n) {
        var canWin = false
        for (take in 1..3) {
            if (take > i) break
            val remain = i - take
            // Ход разрешён, если оставшееся число не простое
            if (remain >= 0 && (remain == 0 || !isPrime[remain])) {
                if (!win[remain]) {
                    canWin = true
                    break
                }
            }
        }
        win[i] = canWin
    }

    writer.write(if (win[n]) "1" else "2")
    writer.newLine()
    writer.flush()
}
