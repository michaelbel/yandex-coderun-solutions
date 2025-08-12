import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Locale
import kotlin.math.abs

fun solveLinearSystem(a: Array<DoubleArray>, b: DoubleArray): DoubleArray? {
    val n = b.size
    val augmentedMatrix = Array(n) { DoubleArray(n + 1) }
    for (i in 0 until n) {
        System.arraycopy(a[i], 0, augmentedMatrix[i], 0, n)
        augmentedMatrix[i][n] = b[i]
    }

    for (i in 0 until n) {
        var pivot = i
        for (k in i + 1 until n) {
            if (abs(augmentedMatrix[k][i]) > abs(augmentedMatrix[pivot][i])) {
                pivot = k
            }
        }
        val tempRow = augmentedMatrix[i]
        augmentedMatrix[i] = augmentedMatrix[pivot]
        augmentedMatrix[pivot] = tempRow

        if (abs(augmentedMatrix[i][i]) < 1e-12) return null

        for (k in i + 1 until n) {
            val factor = augmentedMatrix[k][i] / augmentedMatrix[i][i]
            augmentedMatrix[k][i] = 0.0
            for (j in i + 1..n) {
                augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j]
            }
        }
    }

    val x = DoubleArray(n)
    for (i in n - 1 downTo 0) {
        var sum = 0.0
        for (j in i + 1 until n) {
            sum += augmentedMatrix[i][j] * x[j]
        }
        if (abs(augmentedMatrix[i][i]) < 1e-12) return null
        x[i] = (augmentedMatrix[i][n] - sum) / augmentedMatrix[i][i]
    }

    return x
}

fun main() {
    Locale.setDefault(Locale.US)
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val b = reader.readLine().toInt()
    val n = 2 * b - 1

    val probs = DoubleArray(19)
    for (k in -9..9) {
        probs[k + 9] = (10.0 - abs(k)) / 100.0
    }

    val matrixM = Array(n) { DoubleArray(n) }
    val vectorB = DoubleArray(n) { 1.0 }

    for (i in 0 until n) {
        for (j in 0 until n) {
            val k = j - i
            if (i == j) {
                matrixM[i][i] = 1.0 - probs[9]
            } else {
                if (k in -9..9) {
                    matrixM[i][j] = -probs[k + 9]
                } else {
                    matrixM[i][j] = 0.0
                }
            }
        }
    }

    val solutionE = solveLinearSystem(matrixM, vectorB)

    if (solutionE != null) {
        val targetIndex = b - 1
        if (targetIndex in 0 until n) {
            val expectedValue = solutionE[targetIndex]
            writer.write(String.format(Locale.US, "%.10f", expectedValue))
            writer.newLine()
        } else {
            writer.write("Error: Calculated target index is out of bounds.")
            writer.newLine()
        }
    } else {
        writer.write("Error: The linear system could not be solved.")
        writer.newLine()
    }

    writer.flush()
    reader.close()
    writer.close()
}

