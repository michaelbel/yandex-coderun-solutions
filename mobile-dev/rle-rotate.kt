import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.math.BigInteger

data class Segment(val length: BigInteger, val char: Char)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val firstLine = reader.readLine().split(" ")
    val kStr = firstLine[1]
    val encodedS = reader.readLine() ?: ""
    val segments = mutableListOf<Segment>()
    var totalLength = BigInteger.ZERO
    var i = 0
    while (i < encodedS.length) {
        val numStr = StringBuilder()
        while (i < encodedS.length && encodedS[i].isDigit()) {
            numStr.append(encodedS[i])
            i++
        }
        if (i < encodedS.length && encodedS[i].isLetter()) {
            val char = encodedS[i]
            val length = if (numStr.isEmpty()) BigInteger.ONE else BigInteger(numStr.toString())
            segments.add(Segment(length, char))
            totalLength = totalLength.add(length)
            i++
        } else {
            break
        }
    }
    if (totalLength == BigInteger.ZERO) {
        writer.write("")
        writer.newLine()
        reader.close()
        writer.close()
        return
    }
    val k = BigInteger(kStr)
    val effectiveK = k.mod(totalLength)
    if (effectiveK == BigInteger.ZERO) {
        val outputBuilder = StringBuilder()
        for (seg in segments) {
            if (seg.length == BigInteger.ONE) {
                outputBuilder.append(seg.char)
            } else if (seg.length > BigInteger.ONE) {
                outputBuilder.append(seg.length.toString())
                outputBuilder.append(seg.char)
            }
        }
        writer.write(outputBuilder.toString())
        writer.newLine()
        reader.close()
        writer.close()
        return
    }
    var splitIndex = -1
    var currentSum = BigInteger.ZERO
    var splitOffset = BigInteger.ZERO
    for (idx in segments.indices) {
        val segLength = segments[idx].length
        if (currentSum.add(segLength) >= effectiveK) {
            splitIndex = idx
            splitOffset = effectiveK.subtract(currentSum)
            break
        }
        currentSum = currentSum.add(segLength)
    }
    val rearrangedSegments = mutableListOf<Segment>()
    val splitSegment = segments[splitIndex]
    val secondPartLength = splitSegment.length.subtract(splitOffset)
    if (secondPartLength > BigInteger.ZERO) {
        rearrangedSegments.add(Segment(secondPartLength, splitSegment.char))
    }
    for (j in splitIndex + 1 until segments.size) {
        rearrangedSegments.add(segments[j])
    }
    for (j in 0 until splitIndex) {
        rearrangedSegments.add(segments[j])
    }
    rearrangedSegments.add(Segment(splitOffset, splitSegment.char))
    val mergedSegments = mutableListOf<Segment>()
    if (rearrangedSegments.isNotEmpty()) {
        mergedSegments.add(rearrangedSegments[0])
        for (j in 1 until rearrangedSegments.size) {
            val currentSeg = rearrangedSegments[j]
            val lastMergedSeg = mergedSegments.last()
            if (currentSeg.char == lastMergedSeg.char) {
                val updatedLength = lastMergedSeg.length.add(currentSeg.length)
                mergedSegments[mergedSegments.size - 1] = Segment(updatedLength, lastMergedSeg.char)
            } else {
                mergedSegments.add(currentSeg)
            }
        }
    }
    val outputBuilder = StringBuilder()
    for (seg in mergedSegments) {
        if (seg.length == BigInteger.ONE) {
            outputBuilder.append(seg.char)
        } else if (seg.length > BigInteger.ONE) {
            outputBuilder.append(seg.length.toString())
            outputBuilder.append(seg.char)
        }
    }
    writer.write(outputBuilder.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
