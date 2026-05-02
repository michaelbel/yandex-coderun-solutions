import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val aList = reader.readLine().split(' ').map { it.toInt() }
    val m = reader.readLine().toInt()
    val bList = reader.readLine().split(' ').map { it.toInt() }

    val maxVal = maxOf(aList.maxOrNull() ?: 1, bList.maxOrNull() ?: 1)
    val primeLimit = Math.sqrt(maxVal.toDouble()).toInt()
    val isPrime = BooleanArray(primeLimit + 1) { true }
    if (primeLimit >= 0) isPrime[0] = false
    if (primeLimit >= 1) isPrime[1] = false
    val primes = mutableListOf<Int>()
    for (p in 2..primeLimit) {
        if (isPrime[p]) {
            primes.add(p)
            if (p.toLong() * p <= primeLimit) {
                for (j in p * p..primeLimit step p) {
                    isPrime[j] = false
                }
            }
        }
    }

    fun factorize(list: List<Int>, map: MutableMap<Int, Long>) {
        for (numInitial in list) {
            var x = numInitial
            for (p in primes) {
                if (p.toLong() * p > x) break
                if (x % p == 0) {
                    var cnt = 0L
                    while (x % p == 0) {
                        x /= p
                        cnt++
                    }
                    map[p] = map.getOrDefault(p, 0L) + cnt
                }
            }
            if (x > 1) {
                map[x] = map.getOrDefault(x, 0L) + 1L
            }
        }
    }

    val mapA = mutableMapOf<Int, Long>()
    val mapB = mutableMapOf<Int, Long>()
    factorize(aList, mapA)
    factorize(bList, mapB)

    val gcdFactors = mutableMapOf<Int, Long>()
    for ((p, cntA) in mapA) {
        val cntB = mapB[p] ?: continue
        val cnt = if (cntA < cntB) cntA else cntB
        if (cnt > 0) gcdFactors[p] = cnt
    }

    val MOD = 1_000_000_000L
    var resultMod = 1L
    for ((p, cnt) in gcdFactors) {
        resultMod = (resultMod * powMod(p.toLong(), cnt, MOD)) % MOD
    }

    val LIMIT = MOD
    var resSmall = 1L
    var smallFlag = true
    loop@
    for ((p, cnt) in gcdFactors) {
        val pLong = p.toLong()
        for (i in 1..cnt) {
            if (resSmall > LIMIT / pLong) {
                smallFlag = false
                break@loop
            }
            resSmall *= pLong
        }
    }

    if (smallFlag) {
        writer.write(resSmall.toString())
    } else {
        writer.write(resultMod.toString().padStart(9, '0'))
    }
    writer.newLine()
    writer.flush()
}

fun powMod(base: Long, exp: Long, mod: Long): Long {
    var result = 1L
    var b = base % mod
    var e = exp
    while (e > 0) {
        if ((e and 1L) == 1L) {
            result = (result * b) % mod
        }
        b = (b * b) % mod
        e = e shr 1
    }
    return result
}
