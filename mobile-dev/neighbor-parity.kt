import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque

fun isEven(n: Int): Boolean {
    return n % 2 == 0
}

fun calculateSwaps(n: Int, initialA: List<Int>, startParity: Int): Pair<Int, List<Int>> {
    val currentA = initialA.toMutableList()
    val mismatchedOddIndices = ArrayDeque<Int>()
    val mismatchedEvenIndices = ArrayDeque<Int>()
    var swaps = 0
    for (i in 0 until n) {
        val requiredParity = (startParity + i) % 2
        val currentElement = currentA[i]
        val currentParity = if (isEven(currentElement)) 0 else 1
        if (currentParity != requiredParity) {
            if (currentParity == 0) {
                if (mismatchedOddIndices.isNotEmpty()) {
                    val oddIdx = mismatchedOddIndices.pop()
                    val temp = currentA[i]
                    currentA[i] = currentA[oddIdx]
                    currentA[oddIdx] = temp
                    swaps++
                } else {
                    mismatchedEvenIndices.push(i)
                }
            } else {
                if (mismatchedEvenIndices.isNotEmpty()) {
                    val evenIdx = mismatchedEvenIndices.pop()
                    val temp = currentA[i]
                    currentA[i] = currentA[evenIdx]
                    currentA[evenIdx] = temp
                    swaps++
                } else {
                    mismatchedOddIndices.push(i)
                }
            }
        }
    }
    val k = swaps * 2
    return Pair(k, currentA)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val a = reader.readLine().split(" ").map { it.toInt() }
    var evenCount = 0
    var oddCount = 0
    for (x in a) {
        if (isEven(x)) {
            evenCount++
        } else {
            oddCount++
        }
    }
    if (kotlin.math.abs(evenCount - oddCount) > 1) {
        writer.write("-1")
        writer.newLine()
    } else {
        var resultK: Int
        var resultB: List<Int>
        if (evenCount > oddCount) {
            val (k, b) = calculateSwaps(n, a, 0)
            resultK = k
            resultB = b
        } else if (oddCount > evenCount) {
            val (k, b) = calculateSwaps(n, a, 1)
            resultK = k
            resultB = b
        } else {
            val (k0, b0) = calculateSwaps(n, a, 0)
            val (k1, b1) = calculateSwaps(n, a, 1)
            if (k0 <= k1) {
                resultK = k0
                resultB = b0
            } else {
                resultK = k1
                resultB = b1
            }
        }
        writer.write(resultK.toString())
        writer.newLine()
        writer.write(resultB.joinToString(" "))
        writer.newLine()
    }
    reader.close()
    writer.close()
}
