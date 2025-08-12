import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.io.RandomAccessFile
import java.io.IOException

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = PrintWriter(BufferedWriter(OutputStreamWriter(System.`out`)))
    try {
        val fileName = reader.readLine() ?: ""
        RandomAccessFile(fileName, "r").use { raf ->
            val fileLen = raf.length()
            if (fileLen == 0L) {
                writer.println("OK")
                return
            }
            val visited = ByteArray(((fileLen + 7) shr 3).toInt())
            var offset = 0
            var walkedBytes = 0L
            while (true) {
                if (offset < 0 || offset > fileLen - 4) {
                    writer.println("not a ring buffer")
                    return
                }
                val byteIndex = offset shr 3
                val bitMask = 1 shl (offset and 7)
                if (visited[byteIndex].toInt() and bitMask != 0) {
                    if (offset != 0) {
                        writer.println("not a ring buffer")
                        return
                    }
                    break
                }
                visited[byteIndex] = (visited[byteIndex].toInt() or bitMask).toByte()
                raf.seek(offset.toLong())
                val headerBE = raf.readInt()
                val size = Integer.reverseBytes(headerBE)
                if (size < 0 || size.toLong() > fileLen - 8) {
                    writer.println("not a ring buffer")
                    return
                }
                walkedBytes += 4 + size + 4
                val pointerFieldPos = offset + 4 + size
                if (pointerFieldPos > fileLen - 4) {
                    writer.println("not a ring buffer")
                    return
                }
                raf.seek(pointerFieldPos.toLong())
                val pointerBE = raf.readInt()
                val nextOffset = Integer.reverseBytes(pointerBE)
                offset = nextOffset
            }
            if (walkedBytes != fileLen) {
                writer.println("data loss")
            } else {
                writer.println("OK")
            }
        }
    } catch (e: IOException) {
        PrintWriter(System.out).apply {
            println("not a ring buffer")
            flush()
        }
        return
    } finally {
        writer.flush()
    }
}
