import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val number = reader.readLine().toInt()
    fun isLucky(num: Int): Boolean {
        val digits = num.toString().map { it - '0' }
        val leftSum = digits[0] + digits[1] + digits[2]
        val rightSum = digits[3] + digits[4] + digits[5]
        return leftSum == rightSum
    }
    var nextNumber = number + 1
    while (nextNumber <= 999999) {
        if (isLucky(nextNumber)) {
            writer.write(nextNumber.toString())
            break
        }
        nextNumber++
    }
    reader.close()
    writer.close()
}
