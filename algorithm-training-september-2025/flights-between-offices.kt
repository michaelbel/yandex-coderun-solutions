import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

private fun parseTime(line: String, start: Int): Int {
    val h = (line[start] - '0') * 10 + (line[start + 1] - '0')
    val m = (line[start + 3] - '0') * 10 + (line[start + 4] - '0')
    return h * 60 + m
}

private fun busesForOffice(dep: IntArray, arr: IntArray): Int {
    if (dep.isEmpty()) return 0
    dep.sort()
    arr.sort()

    val nDep = dep.size
    val nArr = arr.size
    var iDep = 0
    var iArr = 0
    var current = 0
    var minCurrent = 0

    val inf = Int.MAX_VALUE

    while (iDep < nDep || iArr < nArr) {
        val tArr = if (iArr < nArr) arr[iArr] else inf
        val tDep = if (iDep < nDep) dep[iDep] else inf
        val t = min(tArr, tDep)

        // сначала учитываем прибытия в этот момент
        while (iArr < nArr && arr[iArr] == t) {
            current++
            iArr++
        }
        // затем отправления
        while (iDep < nDep && dep[iDep] == t) {
            current--
            if (current < minCurrent) minCurrent = current
            iDep++
        }
    }

    return -minCurrent
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().trim().toInt()

    val depA = IntArray(n)
    val arrB = IntArray(n)

    for (i in 0 until n) {
        val line = reader.readLine()
        depA[i] = parseTime(line, 0)      // HH:MM
        arrB[i] = parseTime(line, 6)      // HH:MM после '-'
    }

    val m = reader.readLine().trim().toInt()

    val depB = IntArray(m)
    val arrA = IntArray(m)

    for (i in 0 until m) {
        val line = reader.readLine()
        depB[i] = parseTime(line, 0)
        arrA[i] = parseTime(line, 6)
    }

    val busesA = busesForOffice(depA, arrA)
    val busesB = busesForOffice(depB, arrB)

    val result = busesA + busesB
    writer.write(result.toString())
    writer.newLine()
    writer.flush()
}
