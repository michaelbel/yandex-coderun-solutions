import java.io.BufferedInputStream
import java.lang.StringBuilder
import java.util.TreeSet
import kotlin.math.max
import kotlin.math.min

private class FastScanner {
    private val input = BufferedInputStream(System.`in`)
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

    fun nextInt(): Int {
        var c: Int
        do {
            c = readByte()
        } while (c <= 32 && c >= 0)
        var sgn = 1
        var res = 0
        var ch = c
        if (ch == '-'.code) {
            sgn = -1
            ch = readByte()
        }
        while (ch > 32 && ch >= 0) {
            res = res * 10 + (ch - '0'.code)
            ch = readByte()
        }
        return res * sgn
    }

    fun nextString(): String {
        var c: Int
        do {
            c = readByte()
        } while (c <= 32 && c >= 0)
        val sb = StringBuilder()
        var ch = c
        while (ch > 32 && ch >= 0) {
            sb.append(ch.toChar())
            ch = readByte()
        }
        return sb.toString()
    }
}

fun main() {
    val fs = FastScanner()
    val n = fs.nextInt()
    val q = fs.nextInt()

    val digMin = LongArray(n + 1)
    val digMax = LongArray(n + 1)
    val verMin = LongArray(n + 1)
    val verMax = LongArray(n + 1)
    val horMin = LongArray(n + 1)
    val horMax = LongArray(n + 1)

    digMin[0] = 1
    digMax[0] = 1
    verMin[0] = 1
    verMax[0] = 1
    horMin[0] = 1
    horMax[0] = 1

    for (i in 1..n) {
        run {
            val aMin = horMin[i - 1]
            val aMax = horMax[i - 1]
            val bMin = digMin[i - 1]
            val bMax = digMax[i - 1]
            val cMin = verMin[i - 1]
            val cMax = verMax[i - 1]
            digMin[i] = aMin + bMin + cMin
            digMax[i] = aMax + bMax + cMax
        }
        run {
            var resMin = 0L
            var resMax = -1L

            val t1Min = horMin[i - 1] + horMin[i - 1]
            val t1Max = horMax[i - 1] + horMax[i - 1]
            resMin = t1Min
            resMax = t1Max

            val t2Min = verMin[i - 1] + verMin[i - 1] + digMin[i - 1] + digMin[i - 1]
            val t2Max = verMax[i - 1] + verMax[i - 1] + digMax[i - 1] + digMax[i - 1]
            resMin = min(resMin, t2Min)
            resMax = max(resMax, t2Max)

            horMin[i] = resMin
            horMax[i] = resMax
            verMin[i] = resMin
            verMax[i] = resMax
        }
    }

    val V = n * q + 1
    val go = IntArray(V * 4) { -1 }
    val cnt = IntArray(V)
    val nodeDigMin = LongArray(V)
    val nodeDigMax = LongArray(V)
    val nodeVerMin = LongArray(V)
    val nodeVerMax = LongArray(V)
    val nodeHorMin = LongArray(V)
    val nodeHorMax = LongArray(V)

    var vc = 0
    fun newVertex(h: Int): Int {
        val id = vc++
        val base = id * 4
        go[base] = -1
        go[base + 1] = -1
        go[base + 2] = -1
        go[base + 3] = -1
        nodeDigMin[id] = digMin[h]
        nodeDigMax[id] = digMax[h]
        nodeVerMin[id] = verMin[h]
        nodeVerMax[id] = verMax[h]
        nodeHorMin[id] = horMin[h]
        nodeHorMax[id] = horMax[h]
        cnt[id] = 0
        return id
    }

    newVertex(n)

    val near = BooleanArray(256 * 256)
    fun setNear(a: Char, b: Char) {
        near[a.code * 256 + b.code] = true
    }
    for (i in 0 until 4) {
        val j = (i + 1) and 3
        val a = ('a'.code + i).toChar()
        val b = ('a'.code + j).toChar()
        setNear(a, b)
        setNear(b, a)
    }

    val map = charArrayOf('a', 'b', 'd', 'c')
    val words = TreeSet<Long>()

    val masks = LongArray(q)
    for (i in 0 until q) {
        val s = fs.nextString()
        var msk = 0L
        for (j in 0 until n) {
            val ch = s[j]
            var d = 0
            if (ch == 'b' || ch == 'c') d = d or 1
            if (ch == 'c' || ch == 'd') d = d or 2
            msk = msk or (d.toLong() shl (2 * j))
        }
        masks[i] = msk

        var v = 0
        for (j in 0 until n) {
            val digit = ((msk ushr (2 * j)) and 3L).toInt()
            val base = v * 4
            var nx = go[base + digit]
            if (nx == -1) {
                nx = newVertex(n - j - 1)
                go[base + digit] = nx
            }
            v = nx
        }
    }

    fun calculate(v: Int, h: Int, corner: Int) {
        val hm1 = h - 1

        fun state(child: Int): LongArray {
            if (child == -1) {
                return longArrayOf(
                    digMin[hm1], digMax[hm1],
                    verMin[hm1], verMax[hm1],
                    horMin[hm1], horMax[hm1],
                    0L
                )
            }
            return longArrayOf(
                nodeDigMin[child], nodeDigMax[child],
                nodeVerMin[child], nodeVerMax[child],
                nodeHorMin[child], nodeHorMax[child],
                cnt[child].toLong()
            )
        }

        val base = v * 4
        val s0 = state(go[base + (corner xor 0)])
        val s1 = state(go[base + (corner xor 1)])
        val s2 = state(go[base + (corner xor 2)])
        val s3 = state(go[base + (corner xor 3)])

        val dig0Min = s0[0]; val dig0MaxV = s0[1]
        val ver0MinV = s0[2]; val ver0MaxV = s0[3]
        val hor0MinV = s0[4]; val hor0MaxV = s0[5]
        val cnt0 = s0[6].toInt()

        val dig1Min = s1[0]; val dig1MaxV = s1[1]
        val ver1MinV = s1[2]; val ver1MaxV = s1[3]
        val hor1MinV = s1[4]; val hor1MaxV = s1[5]
        val cnt1 = s1[6].toInt()

        val dig2MinV = s2[0]; val dig2MaxV = s2[1]
        val ver2MinV = s2[2]; val ver2MaxV = s2[3]
        val hor2MinV = s2[4]; val hor2MaxV = s2[5]
        val cnt2 = s2[6].toInt()

        val dig3MinV = s3[0]; val dig3MaxV = s3[1]
        val ver3MinV = s3[2]; val ver3MaxV = s3[3]
        val hor3MinV = s3[4]; val hor3MaxV = s3[5]
        val cnt3 = s3[6].toInt()

        cnt[v] = cnt0 + cnt1 + cnt2 + cnt3

        var dMin = 0L
        var dMax = -1L
        fun addToDig(aMin: Long, aMax: Long) {
            if (aMax < 0) return
            if (dMax < 0) {
                dMin = aMin
                dMax = aMax
            } else {
                dMin = min(dMin, aMin)
                dMax = max(dMax, aMax)
            }
        }

        if (cnt0 == 0) {
            if (hor2MaxV >= 0 && dig3MaxV >= 0 && ver1MaxV >= 0) {
                addToDig(hor2MinV + dig3MinV + ver1MinV, hor2MaxV + dig3MaxV + ver1MaxV)
            }
        }
        if (cnt3 == 0) {
            if (ver2MaxV >= 0 && dig0MaxV >= 0 && hor1MaxV >= 0) {
                addToDig(ver2MinV + dig0Min + hor1MinV, ver2MaxV + dig0MaxV + hor1MaxV)
            }
        }
        nodeDigMin[v] = dMin
        nodeDigMax[v] = dMax

        var hMinV = 0L
        var hMaxV = -1L
        fun addToHor(aMin: Long, aMax: Long) {
            if (aMax < 0) return
            if (hMaxV < 0) {
                hMinV = aMin
                hMaxV = aMax
            } else {
                hMinV = min(hMinV, aMin)
                hMaxV = max(hMaxV, aMax)
            }
        }
        if (ver0MaxV >= 0 && dig2MaxV >= 0 && dig3MaxV >= 0 && ver1MaxV >= 0) {
            addToHor(ver0MinV + dig2MinV + dig3MinV + ver1MinV, ver0MaxV + dig2MaxV + dig3MaxV + ver1MaxV)
        }
        if (cnt2 + cnt3 == 0) {
            if (hor0MaxV >= 0 && hor1MaxV >= 0) {
                addToHor(hor0MinV + hor1MinV, hor0MaxV + hor1MaxV)
            }
        }
        nodeHorMin[v] = hMinV
        nodeHorMax[v] = hMaxV

        var vMinV = 0L
        var vMaxV = -1L
        fun addToVer(aMin: Long, aMax: Long) {
            if (aMax < 0) return
            if (vMaxV < 0) {
                vMinV = aMin
                vMaxV = aMax
            } else {
                vMinV = min(vMinV, aMin)
                vMaxV = max(vMaxV, aMax)
            }
        }
        if (hor0MaxV >= 0 && dig1MaxV >= 0 && dig3MaxV >= 0 && hor2MaxV >= 0) {
            addToVer(hor0MinV + dig1Min + dig3MinV + hor2MinV, hor0MaxV + dig1MaxV + dig3MaxV + hor2MaxV)
        }
        if (cnt1 + cnt3 == 0) {
            if (ver0MaxV >= 0 && ver2MaxV >= 0) {
                addToVer(ver0MinV + ver2MinV, ver0MaxV + ver2MaxV)
            }
        }
        nodeVerMin[v] = vMinV
        nodeVerMax[v] = vMaxV
    }

    val pathNodes = IntArray(n + 1)
    val pathCorners = IntArray(n + 1)

    fun update(msk: Long) {
        var v = 0
        pathNodes[0] = 0
        pathCorners[0] = 0
        var cur = msk
        for (lvl in 0 until n) {
            val digit = (cur and 3L).toInt()
            val child = go[v * 4 + digit]
            pathNodes[lvl + 1] = child
            pathCorners[lvl + 1] = digit
            v = child
            cur = cur ushr 2
        }
        if (cnt[v] == 0) words.add(msk) else words.remove(msk)
        cnt[v] = cnt[v] xor 1

        for (lvl in n - 1 downTo 0) {
            val node = pathNodes[lvl]
            val h = n - lvl
            val corner = pathCorners[lvl]
            calculate(node, h, corner)
        }
    }

    fun getAnswer(): LongArray {
        if (cnt[0] == 0) {
            val mx = 4L * digMax[n - 1]
            return longArrayOf(2L, mx)
        }

        var resMin = 0L
        var resMax = -1L
        fun addToRes(aMin: Long, aMax: Long) {
            if (aMax < 0) return
            if (resMax < 0) {
                resMin = aMin
                resMax = aMax
            } else {
                resMin = min(resMin, aMin)
                resMax = max(resMax, aMax)
            }
        }

        var cycleLen2 = false
        if (cnt[0] <= 1) cycleLen2 = true
        if (cnt[0] == 2) {
            val it = words.iterator()
            val m1 = it.next()
            val m2 = it.next()
            val sArr = CharArray(n)
            val tArr = CharArray(n)
            run {
                var x = m1
                for (i in 0 until n) {
                    sArr[i] = map[(x and 3L).toInt()]
                    x = x ushr 2
                }
            }
            run {
                var x = m2
                for (i in 0 until n) {
                    tArr[i] = map[(x and 3L).toInt()]
                    x = x ushr 2
                }
            }
            val sLast = sArr[n - 1]
            val tLast = tArr[n - 1]
            val flag = near[sLast.code * 256 + tLast.code]
            if (flag) {
                var ok1 = true
                for (i in 0 until n - 1) {
                    if (sArr[i] != tArr[i]) {
                        ok1 = false
                        break
                    }
                }
                if (ok1) cycleLen2 = true

                var sSuf = 0
                while (sSuf < n && sArr[n - sSuf - 1] == sLast) sSuf++
                var tSuf = 0
                while (tSuf < n && tArr[n - tSuf - 1] == tLast) tSuf++

                if (!cycleLen2 && sSuf == tSuf && sSuf < n) {
                    val len = n - sSuf - 1
                    var ok2 = true
                    for (i in 0 until len) {
                        if (sArr[i] != tArr[i]) {
                            ok2 = false
                            break
                        }
                    }
                    if (ok2) {
                        if (sLast == tArr[n - sSuf - 1] && tLast == sArr[n - tSuf - 1]) {
                            cycleLen2 = true
                        }
                    }
                }
            }
        }
        if (cycleLen2) {
            addToRes(2L, 2L)
        }

        var v = 0
        var h = n
        while (h != 0) {
            val base = v * 4
            val v0 = go[base]
            val v1 = go[base + 1]
            val v2 = go[base + 2]
            val v3 = go[base + 3]
            val hm1 = h - 1

            val d0Min = if (v0 == -1) digMin[hm1] else nodeDigMin[v0]
            val d0Max = if (v0 == -1) digMax[hm1] else nodeDigMax[v0]
            val d1Min = if (v1 == -1) digMin[hm1] else nodeDigMin[v1]
            val d1Max = if (v1 == -1) digMax[hm1] else nodeDigMax[v1]
            val d2Min = if (v2 == -1) digMin[hm1] else nodeDigMin[v2]
            val d2Max = if (v2 == -1) digMax[hm1] else nodeDigMax[v2]
            val d3Min = if (v3 == -1) digMin[hm1] else nodeDigMin[v3]
            val d3Max = if (v3 == -1) digMax[hm1] else nodeDigMax[v3]

            if (d0Max > 0 && d1Max > 0 && d2Max > 0 && d3Max > 0) {
                addToRes(d0Min + d1Min + d2Min + d3Min, d0Max + d1Max + d2Max + d3Max)
            }

            h--
            val curCnt = cnt[v]
            if (v0 != -1 && cnt[v0] == curCnt) {
                v = v0
                continue
            }
            if (v1 != -1 && cnt[v1] == curCnt) {
                v = v1
                continue
            }
            if (v2 != -1 && cnt[v2] == curCnt) {
                v = v2
                continue
            }
            if (v3 != -1 && cnt[v3] == curCnt) {
                v = v3
                continue
            }
            break
        }

        return longArrayOf(resMin, resMax)
    }

    val out = StringBuilder()
    for (i in 0 until q) {
        val m = masks[i]
        update(m)
        val ans = getAnswer()
        val a = ans[0]
        val b = ans[1]
        if (a > b) {
            out.append("-1\n")
        } else {
            out.append(a).append(' ').append(b).append('\n')
        }
    }
    print(out.toString())
}
