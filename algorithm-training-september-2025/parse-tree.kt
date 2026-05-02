import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.max

private class Node(
    val op: Char?,   // null для переменной
    val ch: Char?,   // символ переменной
    val left: Node?,
    val right: Node?
)

private class Canvas(
    val w: Int,
    val h: Int,
    val data: Array<CharArray>,
    val rootCol: Int
)

private class Parser(private val s: String) {
    private var i = 0
    private val n = s.length

    fun parse(): Node = parseExpression()

    private fun parseExpression(): Node {
        var node = parseTerm()
        while (i < n && (s[i] == '+' || s[i] == '-')) {
            val op = s[i]
            i++
            val right = parseTerm()
            node = Node(op = op, ch = null, left = node, right = right)
        }
        return node
    }

    private fun parseTerm(): Node {
        var node = parsePower()
        while (i < n && (s[i] == '*' || s[i] == '/')) {
            val op = s[i]
            i++
            val right = parsePower()
            node = Node(op = op, ch = null, left = node, right = right)
        }
        return node
    }

    // Возведение в степень правоассоциативно
    private fun parsePower(): Node {
        var node = parseFactor()
        if (i < n && s[i] == '^') {
            val op = s[i]
            i++
            val right = parsePower()
            node = Node(op = op, ch = null, left = node, right = right)
        }
        return node
    }

    private fun parseFactor(): Node {
        return if (s[i] == '(') {
            i++
            val node = parseExpression()
            i++ // пропускаем ')'
            node
        } else {
            val ch = s[i]
            i++
            Node(op = null, ch = ch, left = null, right = null)
        }
    }
}

private fun buildCanvas(node: Node): Canvas {
    if (node.op == null) {
        val data = Array(1) { CharArray(1) { node.ch!! } }
        return Canvas(1, 1, data, 0)
    }

    val left = buildCanvas(node.left!!)
    val right = buildCanvas(node.right!!)

    val w1 = left.w
    val h1 = left.h
    val w2 = right.w
    val h2 = right.h

    val w = w1 + 5 + w2
    val h = max(h1, h2) + 2
    val data = Array(h) { CharArray(w) { ' ' } }

    val center = w1 + 2           // колонка символа операции
    val rootCol = center
    val leftRoot = left.rootCol
    val rightRoot = w1 + 5 + right.rootCol

    // Верхняя строка
    // Сначала скобки и операция
    data[0][center - 1] = '['
    data[0][center] = node.op!!
    data[0][center + 1] = ']'

    // Левая "рука"
    data[0][leftRoot] = '.'
    for (c in leftRoot + 1 until center - 1) {
        data[0][c] = '-'
    }

    // Правая "рука"
    data[0][rightRoot] = '.'
    for (c in center + 2 until rightRoot) {
        data[0][c] = '-'
    }

    // Вторая строка — вертикальные палки
    data[1][leftRoot] = '|'
    data[1][rightRoot] = '|'

    // Копируем поддеревья, начиная с третьей строки
    for (i in 0 until h1) {
        for (j in 0 until w1) {
            data[i + 2][j] = left.data[i][j]
        }
    }
    for (i in 0 until h2) {
        for (j in 0 until w2) {
            data[i + 2][j + w1 + 5] = right.data[i][j]
        }
    }

    return Canvas(w, h, data, rootCol)
}

private fun trimRightSpaces(row: CharArray): String {
    var last = row.size - 1
    while (last >= 0 && row[last] == ' ') last--
    return if (last < 0) "" else String(row, 0, last + 1)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val expr = reader.readLine().trim()
    val parser = Parser(expr)
    val root = parser.parse()
    val canvas = buildCanvas(root)

    for (r in 0 until canvas.h) {
        writer.write(trimRightSpaces(canvas.data[r]))
        writer.newLine()
    }

    writer.flush()
}
