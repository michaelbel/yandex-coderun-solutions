import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.ArrayList
import kotlin.collections.listOf
import kotlin.collections.sorted
import kotlin.text.StringBuilder
import kotlin.Pair

fun getRunLengthEncoding(s: String): List<Pair<Char, Int>> {
    if (s.isEmpty()) return emptyList()
    val result: MutableList<Pair<Char, Int>> = ArrayList()
    var currentChar = s[0]
    var currentCount = 0
    for (char in s) {
        if (char == currentChar) {
            currentCount++
        } else {
            result.add(Pair(currentChar, currentCount))
            currentChar = char
            currentCount = 1
        }
    }
    result.add(Pair(currentChar, currentCount))
    return result
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val s1 = reader.readLine()
    val s2 = reader.readLine()
    val s3 = reader.readLine()

    val rle1 = getRunLengthEncoding(s1)
    val rle2 = getRunLengthEncoding(s2)
    val rle3 = getRunLengthEncoding(s3)

    var possible = true
    if (rle1.size != rle2.size || rle1.size != rle3.size) {
        possible = false
    } else {
        for (i in rle1.indices) {
            if (rle1[i].first != rle2[i].first || rle1[i].first != rle3[i].first) {
                possible = false
                break
            }
        }
    }

    if (!possible) {
        writer.write("IMPOSSIBLE")
        writer.newLine()
    } else {
        val resultBuilder = StringBuilder()
        for (i in rle1.indices) {
            val char = rle1[i].first
            val count1 = rle1[i].second
            val count2 = rle2[i].second
            val count3 = rle3[i].second
            val counts = listOf(count1, count2, count3).sorted()
            val medianCount = counts[1]
            repeat(medianCount) {
                resultBuilder.append(char)
            }
        }
        writer.write(resultBuilder.toString())
        writer.newLine()
    }

    reader.close()
    writer.close()
}
