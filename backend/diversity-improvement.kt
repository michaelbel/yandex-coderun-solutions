import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.PriorityQueue
import java.util.ArrayDeque

data class Product(val id: Int, val category: Int)

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val productsInput = ArrayList<Product>(n)
    val catCounts = HashMap<Int, Int>()
    val catProducts = HashMap<Int, ArrayDeque<Int>>()
    for (i in 0 until n) {
        val parts = reader.readLine().split(" ")
        val prodId = parts[0].toInt()
        val cat = parts[1].toInt()
        productsInput.add(Product(prodId, cat))
        catCounts[cat] = (catCounts[cat] ?: 0) + 1
        if (!catProducts.containsKey(cat)) {
            catProducts[cat] = ArrayDeque()
        }
        catProducts[cat]!!.add(prodId)
    }
    if (catCounts.values.all { it == 1 }) {
        for (p in productsInput) {
            writer.write("${p.id} ")
        }
        writer.flush()
        reader.close()
        writer.close()
        return
    }
    var maxFreq = 0
    for (cnt in catCounts.values) {
        if (cnt > maxFreq) maxFreq = cnt
    }
    var countMax = 0
    for (cnt in catCounts.values) {
        if (cnt == maxFreq) countMax++
    }
    val d = (n - countMax) / (maxFreq - 1)
    data class Task(val cat: Int, var remaining: Int, var nextAvailable: Int)
    val available = PriorityQueue<Task> { a, b -> b.remaining - a.remaining }
    val waiting = PriorityQueue<Task> { a, b -> a.nextAvailable - b.nextAvailable }
    for ((cat, cnt) in catCounts) {
        available.add(Task(cat, cnt, 0))
    }
    val result = IntArray(n)
    for (i in 0 until n) {
        while (waiting.isNotEmpty() && waiting.peek().nextAvailable <= i) {
            available.add(waiting.poll())
        }
        val task = available.poll()
        val prodId = catProducts[task.cat]!!.removeFirst()
        result[i] = prodId
        task.remaining--
        if (task.remaining > 0) {
            task.nextAvailable = i + d
            waiting.add(task)
        }
    }
    for (id in result) {
        writer.write("$id ")
    }
    writer.flush()
    reader.close()
    writer.close()
}
