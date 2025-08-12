import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, k) = reader.readLine().split(" ").map { it.toInt() }
    val siteGooseCount = mutableMapOf<String, Int>()
    repeat(n) {
        val site = reader.readLine()
        val message = reader.readLine()
        val words = message.split(" ")
        val gooseCount = words.count { it == "goose" }
        siteGooseCount[site] = siteGooseCount.getOrDefault(site, 0) + gooseCount
    }
    val filteredSites = siteGooseCount
        .filter { it.value >= k }
        .keys
        .sorted()
    writer.write(filteredSites.size.toString())
    writer.newLine()
    filteredSites.forEach { site ->
        writer.write(site)
        writer.newLine()
    }
    reader.close()
    writer.close()
}
