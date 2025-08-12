import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque
import java.util.HashSet
import java.util.ArrayList

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val deliveryIdList = reader.readLine().split(" ").map { it.toInt() }
    val parentIdList = reader.readLine().split(" ").map { it.toInt() }
    val k = reader.readLine().toInt()
    val undeliveredSet = HashSet<Int>()
    if (k > 0) {
        val undeliveredList = reader.readLine().split(" ").map { it.toInt() }
        undeliveredSet.addAll(undeliveredList)
    }
    val isShippable = BooleanArray(n + 1) { true }
    val queue = ArrayDeque<Int>()
    for (i in 1..n) {
        if (deliveryIdList[i - 1] in undeliveredSet) {
            if (isShippable[i]) {
                isShippable[i] = false
                queue.addLast(i)
            }
        }
    }
    while (queue.isNotEmpty()) {
        val currentItemIndex = queue.removeFirst()
        val parentItemIndex = parentIdList[currentItemIndex - 1]
        if (parentItemIndex != 0 && isShippable[parentItemIndex]) {
            isShippable[parentItemIndex] = false
            queue.addLast(parentItemIndex)
        }
    }
    val resultPallets = ArrayList<Int>()
    for (i in 1..n) {
        if (parentIdList[i - 1] == 0 && isShippable[i]) {
            resultPallets.add(i)
        }
    }
    writer.write(resultPallets.size.toString())
    writer.newLine()
    if (resultPallets.isNotEmpty()) {
        writer.write(resultPallets.joinToString(" "))
        writer.newLine()
    }
    reader.close()
    writer.flush()
    writer.close()
}
