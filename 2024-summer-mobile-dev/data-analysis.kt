import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val wordCount = mutableMapOf<String, Int>()
    repeat(n) {
        val query = reader.readLine().split(" ")
        when (query[0]) {
            "+" -> {
                val count = query[1].toInt()
                val word = query[2]
                wordCount[word] = wordCount.getOrDefault(word, 0) + count
            }
            "?" -> {
                val prefix = query[1]
                val candidates = wordCount.keys.filter { it.startsWith(prefix) }
                if (candidates.isEmpty()) {
                    writer.write(prefix)
                } else {
                    var maxCount = -1
                    var bestWord = ""
                    for (word in candidates) {
                        val count = wordCount[word]!!
                        if (count > maxCount || (count == maxCount && word < bestWord)) {
                            maxCount = count
                            bestWord = word
                        }
                    }
                    writer.write(bestWord)
                }
                writer.newLine()
            }
        }
    }
    reader.close()
    writer.close()
}
