data class Answer(
    val sum: Int,
    val field: Array<String>
)

fun solution(n: Int): Answer {
    val sum = (n - 1) * (3 * n - 2)
    val minesRow = "x".repeat(n)
    val emptyRow = "-".repeat(n)
    val field = Array(n) { if (it % 2 == 0) minesRow else emptyRow }
    return Answer(sum, field)
}
