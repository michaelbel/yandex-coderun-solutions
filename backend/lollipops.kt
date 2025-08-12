import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

data class Node(var serverId: Int = -1, var lazy: Int = -1)

lateinit var tree: Array<Node>
lateinit var initialLocations: IntArray
const val NO_SERVER = -1
const val NO_OVERLAP = -2

fun push(v: Int, tl: Int, tr: Int) {
    if (tree[v].lazy != NO_SERVER && tl != tr) {
        val lazyVal = tree[v].lazy
        val leftChild = v * 2
        val rightChild = v * 2 + 1
        tree[leftChild].serverId = lazyVal
        tree[leftChild].lazy = lazyVal
        tree[rightChild].serverId = lazyVal
        tree[rightChild].lazy = lazyVal
        tree[v].lazy = NO_SERVER
    }
}

fun combineNodes(leftId: Int, rightId: Int): Int {
    return if (leftId != NO_SERVER && leftId == rightId) {
        leftId
    } else {
        NO_SERVER
    }
}

fun combineQueryResults(leftResult: Int, rightResult: Int): Int {
    return when {
        leftResult == NO_OVERLAP -> rightResult
        rightResult == NO_OVERLAP -> leftResult
        leftResult == rightResult -> leftResult
        else -> NO_SERVER
    }
}

fun build(v: Int, tl: Int, tr: Int) {
    if (tl == tr) {
        tree[v] = Node(serverId = initialLocations[tl], lazy = NO_SERVER)
    } else {
        val tm = tl + (tr - tl) / 2
        val leftChild = v * 2
        val rightChild = v * 2 + 1
        build(leftChild, tl, tm)
        build(rightChild, tm + 1, tr)
        tree[v] = Node(
            serverId = combineNodes(tree[leftChild].serverId, tree[rightChild].serverId),
            lazy = NO_SERVER
        )
    }
}

fun query(v: Int, tl: Int, tr: Int, l: Int, r: Int): Int {
    if (tr < l || tl > r || l > r) {
        return NO_OVERLAP
    }
    if (l <= tl && tr <= r) {
        return tree[v].serverId
    }
    push(v, tl, tr)
    val tm = tl + (tr - tl) / 2
    val leftChild = v * 2
    val rightChild = v * 2 + 1
    val leftResult = query(leftChild, tl, tm, l, r)
    val rightResult = query(rightChild, tm + 1, tr, l, r)
    return combineQueryResults(leftResult, rightResult)
}

fun update(v: Int, tl: Int, tr: Int, l: Int, r: Int, newServer: Int) {
    if (tr < l || tl > r || l > r) {
        return
    }
    if (l <= tl && tr <= r) {
        tree[v].serverId = newServer
        tree[v].lazy = newServer
    } else {
        push(v, tl, tr)
        val tm = tl + (tr - tl) / 2
        val leftChild = v * 2
        val rightChild = v * 2 + 1
        update(leftChild, tl, tm, l, r, newServer)
        update(rightChild, tm + 1, tr, l, r, newServer)
        tree[v].serverId = combineNodes(tree[leftChild].serverId, tree[rightChild].serverId)
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    var tokenizer = StringTokenizer(reader.readLine())
    val n = tokenizer.nextToken().toInt()
    val m = tokenizer.nextToken().toInt()
    val q = tokenizer.nextToken().toInt()
    initialLocations = IntArray(n + 1)
    tokenizer = StringTokenizer(reader.readLine())
    for (i in 1..n) {
        initialLocations[i] = tokenizer.nextToken().toInt()
    }
    tree = Array(4 * n + 4) { Node() }
    build(1, 1, n)
    for (i in 0 until q) {
        tokenizer = StringTokenizer(reader.readLine())
        val a = tokenizer.nextToken().toInt()
        val b = tokenizer.nextToken().toInt()
        val l = tokenizer.nextToken().toInt()
        val r = tokenizer.nextToken().toInt()
        val currentServerStatus = query(1, 1, n, l, r)
        if (currentServerStatus == a) {
            writer.write("1")
            writer.newLine()
            update(1, 1, n, l, r, b)
        } else {
            writer.write("0")
            writer.newLine()
        }
    }
    reader.close()
    writer.flush()
    writer.close()
}
