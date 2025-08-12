import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

fun contains(seq1: List<Int>, seq2: List<Int>): Boolean {
    if (seq2.isEmpty()) return true
    if (seq1.size < seq2.size) return false
    for (i in 0..(seq1.size - seq2.size)) {
        var match = true
        for (j in seq2.indices) {
            if (seq1[i + j] != seq2[j]) {
                match = false
                break
            }
        }
        if (match) return true
    }
    return false
}

fun findOverlap(seq1: List<Int>, seq2: List<Int>): Int {
    var maxOverlap = 0
    for (k in min(seq1.size, seq2.size) downTo 1) {
        var isOverlap = true
        for (i in 0 until k) {
            if (seq1[seq1.size - k + i] != seq2[i]) {
                isOverlap = false
                break
            }
        }
        if (isOverlap) {
            maxOverlap = k
            break
        }
    }
    return maxOverlap
}

fun merge(seq1: List<Int>, seq2: List<Int>): List<Int> {
    val overlapLength = findOverlap(seq1, seq2)
    return seq1 + seq2.subList(overlapLength, seq2.size)
}

fun readSequence(reader: BufferedReader): List<Int> {
    return reader.readLine().split(" ").map { it.toInt() }.drop(1)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val sequences = List(3) { readSequence(reader) }
    val indices = listOf(0, 1, 2)
    val activeIndices = mutableSetOf(0, 1, 2)
    for (i in indices) {
        for (j in indices) {
            if (i == j) continue
            if (activeIndices.contains(i) && activeIndices.contains(j)) {
                if (contains(sequences[i], sequences[j])) {
                    activeIndices.remove(j)
                }
            }
        }
    }
    val activeSequences = activeIndices.map { sequences[it] }
    var shortestSupersequence: List<Int> = emptyList()
    when (activeSequences.size) {
        0 -> {
            shortestSupersequence = emptyList()
        }
        1 -> {
            shortestSupersequence = activeSequences[0]
        }
        2 -> {
            val s1 = activeSequences[0]
            val s2 = activeSequences[1]
            val merged12 = merge(s1, s2)
            val merged21 = merge(s2, s1)
            shortestSupersequence = if (merged12.size <= merged21.size) merged12 else merged21
        }
        3 -> {
            val permutations = listOf(
                listOf(0, 1, 2), listOf(0, 2, 1), listOf(1, 0, 2),
                listOf(1, 2, 0), listOf(2, 0, 1), listOf(2, 1, 0)
            )
            var minLength = Int.MAX_VALUE
            for (p in permutations) {
                val s1 = activeSequences[p[0]]
                val s2 = activeSequences[p[1]]
                val s3 = activeSequences[p[2]]
                val merged12 = merge(s1, s2)
                val merged123 = merge(merged12, s3)
                if (merged123.size < minLength) {
                    minLength = merged123.size
                    shortestSupersequence = merged123
                }
            }
        }
    }
    writer.write(shortestSupersequence.size.toString())
    writer.newLine()
    writer.write(shortestSupersequence.joinToString(" "))
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
