fun main() {
    val s = readln().trim()

    if (isCorrectEquation(s)) {
        println("Correct")
        return
    }

    val n = s.length
    for (i in 0 until n) {
        val ch = s[i]
        if (ch !in '0'..'9') continue
        val base = StringBuilder(s)
        base.deleteCharAt(i)
        for (j in 0..base.length) {
            if (j == i) continue
            val cand = StringBuilder(base).insert(j, ch).toString()
            if (isCorrectEquation(cand)) {
                println(cand)
                return
            }
        }
    }

    println("Impossible")
}

private fun isCorrectEquation(eq: String): Boolean {
    val parts = eq.split('=')
    if (parts.size != 2) return false
    val left = evalExpr(parts[0]) ?: return false
    val right = evalExpr(parts[1]) ?: return false
    return left == right
}

private fun evalExpr(expr: String): Long? {
    if (expr.isEmpty()) return null
    var i = 0
    fun readNumber(): Long? {
        if (i >= expr.length || expr[i] !in '0'..'9') return null
        val start = i
        while (i < expr.length && expr[i] in '0'..'9') i++
        val len = i - start
        if (len == 0 || len > 10) return null
        if (len > 1 && expr[start] == '0') return null
        return expr.substring(start, i).toLong()
    }

    var value = readNumber() ?: return null
    while (i < expr.length) {
        val op = expr[i]
        if (op != '+' && op != '-') return null
        i++
        val num = readNumber() ?: return null
        value = if (op == '+') value + num else value - num
    }
    return value
}
