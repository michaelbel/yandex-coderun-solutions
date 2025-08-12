import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun grundy(n: Int): Int {
    return n % 3
}

fun solve(a1: Int, a2: Int, a3: Int): Int {
    val g = grundy(a1) xor grundy(a2) xor grundy(a3)
    return if (g != 0) 1 else 0
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val t = reader.readLine().toInt()
    
    repeat(t) {
        val (a1, a2, a3) = reader.readLine().split(" ").map { it.toInt() }
        val result = solve(a1, a2, a3)
        writer.write(result.toString())
        writer.newLine()
    }
    
    reader.close()
    writer.close()
}
