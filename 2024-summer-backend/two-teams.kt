import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем входные данные
    val a = reader.readLine().toInt() // задачи первой команды
    val b = reader.readLine().toInt() // задачи второй команды
    val n = reader.readLine().toInt() // максимум задач на студента

    // Минимальное количество студентов для каждой команды
    val minTeam1 = (a + n - 1) / n  // округляем вверх: ceil(a/n)
    val minTeam2 = (b + n - 1) / n  // округляем вверх: ceil(b/n)
    
    // Максимальное количество студентов для каждой команды
    val maxTeam1 = a  // каждый решает минимум 1 задачу
    val maxTeam2 = b  // каждый решает минимум 1 задачу
    
    // Проверяем, может ли в первой команде быть больше студентов
    // Это возможно, если максимум первой команды больше минимума второй
    val result = if (maxTeam1 > minTeam2) "Yes" else "No"
    
    writer.write(result)
    writer.newLine()

    reader.close()
    writer.close()
}
