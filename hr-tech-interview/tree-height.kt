import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Node(val value: Int) {
    var left: Node? = null
    var right: Node? = null
}

class BST {
    var root: Node? = null

    fun insert(value: Int) {
        root = insertRec(root, value)
    }

    private fun insertRec(node: Node?, value: Int): Node {
        if (node == null) return Node(value)
        when {
            value < node.value -> node.left = insertRec(node.left, value)
            value > node.value -> node.right = insertRec(node.right, value)
        }
        return node
    }

    fun height(): Int = heightRec(root)

    private fun heightRec(node: Node?): Int {
        if (node == null) return 0
        return 1 + maxOf(heightRec(node.left), heightRec(node.right))
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val bst = BST()
    val parts = reader.readLine().split(" ").map { it.toInt() }

    for (num in parts) {
        if (num == 0) break
        bst.insert(num)
    }

    writer.write("${bst.height()}\n")

    reader.close()
    writer.flush()
    writer.close()
}