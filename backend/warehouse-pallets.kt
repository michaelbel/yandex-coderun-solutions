import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import kotlin.math.min
import kotlin.math.max
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.sortWith
import kotlin.Comparator

data class Pallet(val minDim: Int, val maxDim: Int)

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val pallets: MutableList<Pallet> = ArrayList(n)
    for (i in 0 until n) {
        val st = StringTokenizer(reader.readLine())
        val w = st.nextToken().toInt()
        val h = st.nextToken().toInt()
        pallets.add(Pallet(min(w, h), max(w, h)))
    }
    pallets.sortWith(Comparator { p1, p2 ->
        if (p1.minDim != p2.minDim) {
            p1.minDim.compareTo(p2.minDim)
        } else {
            p2.maxDim.compareTo(p1.maxDim)
        }
    })
    var countUnstackable = 0
    var maxDimSeenSoFar = 0
    for (i in n - 1 downTo 0) {
        val currentPallet = pallets[i]
        if (currentPallet.maxDim >= maxDimSeenSoFar) {
            countUnstackable++
        }
        maxDimSeenSoFar = max(maxDimSeenSoFar, currentPallet.maxDim)
    }
    writer.write(countUnstackable.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
