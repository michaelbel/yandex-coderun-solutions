import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val words = reader.readLine().split(" ")
    val seen = HashSet<String>()
    val trie = TrieNode()
    var totalKeyPresses = 0L
    for (word in words) {
        if (!seen.contains(word)) {
            totalKeyPresses += word.length
            seen.add(word)
            var cur = trie
            for (ch in word) {
                val index = ch - 'a'
                if (cur.children[index] == null) {
                    cur.children[index] = TrieNode()
                }
                cur = cur.children[index]!!
                cur.count++
            }
        } else {
            var cur = trie
            var presses = word.length
            for (i in word.indices) {
                val index = word[i] - 'a'
                cur = cur.children[index]!!
                if (cur.count == 1) {
                    presses = i + 1
                    break
                }
            }
            totalKeyPresses += presses
        }
    }
    writer.write(totalKeyPresses.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}

class TrieNode {
    var count = 0
    val children = Array<TrieNode?>(26) { null }
}
