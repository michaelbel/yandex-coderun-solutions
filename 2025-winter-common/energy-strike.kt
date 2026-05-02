import java.io.BufferedInputStream
import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sqrt

private const val EPS = 1e-12
private const val SCALE = 1_000_000_000L

private class FastScanner {
    private val input = BufferedInputStream(System.`in`, 1 shl 16)
    private val buffer = ByteArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun readByte(): Int {
        if (ptr >= len) {
            len = input.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].toInt()
    }

    private fun skipSpaces(): Int {
        var c: Int
        do {
            c = readByte()
        } while (c >= 0 && c <= 32)
        return c
    }

    fun nextInt(): Int {
        var c = skipSpaces()
        var sign = 1
        if (c == '-'.code) {
            sign = -1
            c = readByte()
        }
        var v = 0
        while (c > 32) {
            v = v * 10 + (c - '0'.code)
            c = readByte()
        }
        return v * sign
    }

    fun nextDouble(): Double {
        var c = skipSpaces()
        var sign = 1.0
        if (c == '-'.code) {
            sign = -1.0
            c = readByte()
        }

        var intPart = 0L
        while (c > 32 && c != '.'.code && c != 'e'.code && c != 'E'.code) {
            intPart = intPart * 10 + (c - '0'.code)
            c = readByte()
        }

        var fracPart = 0L
        var fracDiv = 1.0
        if (c == '.'.code) {
            c = readByte()
            while (c > 32 && c != 'e'.code && c != 'E'.code) {
                fracPart = fracPart * 10 + (c - '0'.code)
                fracDiv *= 10.0
                c = readByte()
            }
        }

        var value = (intPart.toDouble() + fracPart.toDouble() / fracDiv) * sign

        if (c == 'e'.code || c == 'E'.code) {
            c = readByte()
            var expSign = 1
            if (c == '-'.code) {
                expSign = -1
                c = readByte()
            } else if (c == '+'.code) {
                c = readByte()
            }
            var exp = 0
            while (c > 32) {
                exp = exp * 10 + (c - '0'.code)
                c = readByte()
            }
            value *= pow10(exp * expSign)
        }

        return value
    }

    private fun pow10(e: Int): Double {
        if (e == 0) return 1.0
        var exp = e
        var base = 10.0
        if (exp < 0) {
            exp = -exp
            base = 0.1
        }
        var res = 1.0
        var p = base
        var k = exp
        while (k > 0) {
            if ((k and 1) != 0) res *= p
            p *= p
            k = k shr 1
        }
        return res
    }
}

private fun roundToInt(x: Double): Int {
    return if (x >= 0.0) floor(x + 0.5).toInt() else -floor(-x + 0.5).toInt()
}

private fun distSq(ax: Double, ay: Double, bx: Double, by: Double): Double {
    val dx = ax - bx
    val dy = ay - by
    return dx * dx + dy * dy
}

private fun appendFixed9(sb: StringBuilder, v: Double) {
    var x = (v * SCALE + if (v >= 0) 0.5 else -0.5).toLong()
    if (x == 0L) {
        sb.append("0.000000000")
        return
    }
    if (x < 0) {
        sb.append('-')
        x = -x
    }
    val intPart = x / SCALE
    val frac = (x % SCALE).toInt()
    sb.append(intPart)
    sb.append('.')
    var f = frac
    var div = 100_000_000
    while (div > 0) {
        sb.append((f / div) % 10)
        div /= 10
    }
}

fun main() {
    val fs = FastScanner()
    val t = fs.nextInt()
    val sb = StringBuilder(t * 64)

    val sqrt3 = sqrt(3.0)
    val xShifts = DoubleArray(10) { it * 0.2 }               // [0, 2) step 0.2
    val yShifts = DoubleArray(10) { it * (sqrt3 / 10.0) }    // [0, sqrt3) step sqrt3/10

    val px = DoubleArray(10)
    val py = DoubleArray(10)

    val centerRow = IntArray(10)
    val centerCol = IntArray(10)
    val centerX = DoubleArray(10)
    val centerY = DoubleArray(10)

    for (caseIdx in 0 until t) {
        val n = fs.nextInt()
        for (i in 0 until n) {
            px[i] = fs.nextDouble()
            py[i] = fs.nextDouble()
        }

        var found = false
        var outCnt = 0

        loop@ for (si in 0 until 10) {
            val x0 = xShifts[si]
            for (sj in 0 until 10) {
                val y0 = yShifts[sj]
                outCnt = 0
                var ok = true

                for (p in 0 until n) {
                    val x = px[p]
                    val y = py[p]

                    val rBase = roundToInt((y - y0) / sqrt3)
                    var got = false
                    var bestR = 0
                    var bestC = 0
                    var bestCx = 0.0
                    var bestCy = 0.0

                    var rr = rBase - 1
                    while (rr <= rBase + 1 && !got) {
                        val cy = y0 + rr * sqrt3
                        val baseX = x0 + if ((rr and 1) == 0) 0.0 else 1.0
                        val cBase = roundToInt((x - baseX) / 2.0)

                        var cc = cBase - 1
                        while (cc <= cBase + 1) {
                            val cx = baseX + cc * 2.0
                            if (distSq(x, y, cx, cy) <= 1.0 + EPS) {
                                got = true
                                bestR = rr
                                bestC = cc
                                bestCx = cx
                                bestCy = cy
                                break
                            }
                            cc++
                        }
                        rr++
                    }

                    if (!got) {
                        ok = false
                        break
                    }

                    var exists = false
                    for (k in 0 until outCnt) {
                        if (centerRow[k] == bestR && centerCol[k] == bestC) {
                            exists = true
                            break
                        }
                    }
                    if (!exists) {
                        centerRow[outCnt] = bestR
                        centerCol[outCnt] = bestC
                        centerX[outCnt] = bestCx
                        centerY[outCnt] = bestCy
                        outCnt++
                        if (outCnt > n) {
                            ok = false
                            break
                        }
                    }
                }

                if (ok) {
                    found = true
                    break@loop
                }
            }
        }

        if (!found) {
            sb.append("NO\n")
        } else {
            sb.append("YES\n")
            sb.append(outCnt).append('\n')
            for (i in 0 until outCnt) {
                appendFixed9(sb, centerX[i])
                sb.append(' ')
                appendFixed9(sb, centerY[i])
                sb.append('\n')
            }
        }
    }

    print(sb.toString())
}
