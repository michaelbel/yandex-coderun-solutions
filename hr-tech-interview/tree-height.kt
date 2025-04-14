import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

// Класс узла бинарного дерева поиска (BST)
class Node(val value: Int) {
    var left: Node? = null
    var right: Node? = null
}

// Класс бинарного дерева поиска (BST)
class BST {
    var root: Node? = null // Корневой узел дерева

    // Метод вставки нового элемента в дерево
    fun insert(value: Int) {
        root = insertRec(root, value)
    }

    // Рекурсивный метод вставки значения в правильное место в дереве
    private fun insertRec(node: Node?, value: Int): Node {
        if (node == null) return Node(value) // Создаем новый узел, если достигли пустого места
        when {
            value < node.value -> node.left = insertRec(node.left, value) // Вставляем в левое поддерево
            value > node.value -> node.right = insertRec(node.right, value) // Вставляем в правое поддерево
        }
        return node
    }

    // Метод вычисления высоты дерева
    fun height(): Int = heightRec(root)

    // Рекурсивный метод вычисления высоты дерева
    private fun heightRec(node: Node?): Int {
        if (node == null) return 0 // Высота пустого узла равна 0
        return 1 + maxOf(heightRec(node.left), heightRec(node.right)) // Максимальная высота поддеревьев + 1
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val bst = BST() // Создаем экземпляр бинарного дерева поиска
    val parts = reader.readLine().split(" ").map { it.toInt() } // Читаем входные данные

    // Вставляем числа в дерево, прерываемся при встрече 0
    for (num in parts) {
        if (num == 0) break
        bst.insert(num)
    }

    writer.write("${bst.height()}\n") // Выводим высоту дерева

    reader.close()
    writer.flush()
    writer.close()
}