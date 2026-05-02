import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val s = br.readLine()!!.trim()
    val n = s.length
    val L26 = (1 shl 26) - 1
    if (n > L26) {
        println("No solution")
        return
    }
    var K = 1
    while (((1 shl K) - 1) < n) {
        K++
    }
    var P = "a"
    for (j in 2..K) {
        val mid = (('a'.code + j - 1).toChar())
        P = P + mid + P
    }
    val P_chars = P.toCharArray()
    val Lk = P_chars.size
    val sChars = s.toCharArray()
    val fVal = IntArray(26)
    val gVal = IntArray(26)
    val stampF = IntArray(26)
    val stampG = IntArray(26)
    var curStamp = 0
    var answerPos = -1
    if (Lk >= n) {
        val lastStart = Lk - n
        outer@ for (p in 0..lastStart) {
            curStamp++
            val stamp = curStamp
            var ok = true
            for (j in 0 until n) {
                val cI = P_chars[p + j] - 'a'
                val dI = sChars[j] - 'A'
                if (stampF[cI] != stamp && stampG[dI] != stamp) {
                    stampF[cI] = stamp;  fVal[cI] = dI
                    stampG[dI] = stamp;  gVal[dI] = cI
                } else if (!(stampF[cI] == stamp && fVal[cI] == dI
                          && stampG[dI] == stamp && gVal[dI] == cI)) {
                    ok = false
                    break
                }
            }
            if (ok) {
                answerPos = p + 1
                break@outer
            }
        }
    }
    if (answerPos == -1 && K < 26) {
        val freq = IntArray(26)
        for (ch in sChars) freq[ch - 'A']++
        val uniques = mutableListOf<Int>()
        for (i in sChars.indices) {
            if (freq[sChars[i] - 'A'] == 1) uniques.add(i + 1)
        }
        uniques.sortDescending()
        suffix@ for (pos in uniques) {
            val i0 = pos - 1
            curStamp++
            val stamp = curStamp
            var ok = true
            if (i0 > 0) {
                val start = Lk - i0
                for (j in 0 until i0) {
                    val cI = P_chars[start + j] - 'a'
                    val dI = sChars[j] - 'A'
                    if (stampF[cI] != stamp && stampG[dI] != stamp) {
                        stampF[cI] = stamp;  fVal[cI] = dI
                        stampG[dI] = stamp;  gVal[dI] = cI
                    } else if (!(stampF[cI] == stamp && fVal[cI] == dI
                              && stampG[dI] == stamp && gVal[dI] == cI)) {
                        ok = false
                        break
                    }
                }
            }
            if (!ok) continue@suffix
            val cMid = K
            val dMid = sChars[i0] - 'A'
            if (stampF[cMid] != stamp && stampG[dMid] != stamp) {
                stampF[cMid] = stamp;  fVal[cMid] = dMid
                stampG[dMid] = stamp;  gVal[dMid] = cMid
            } else if (!(stampF[cMid] == stamp && fVal[cMid] == dMid
                      && stampG[dMid] == stamp && gVal[dMid] == cMid)) {
                continue@suffix
            }
            val prefLen = n - i0 - 1
            if (prefLen > 0) {
                for (j in 0 until prefLen) {
                    val cI = P_chars[j] - 'a'
                    val dI = sChars[i0 + 1 + j] - 'A'
                    if (stampF[cI] != stamp && stampG[dI] != stamp) {
                        stampF[cI] = stamp;  fVal[cI] = dI
                        stampG[dI] = stamp;  gVal[dI] = cI
                    } else if (!(stampF[cI] == stamp && fVal[cI] == dI
                              && stampG[dI] == stamp && gVal[dI] == cI)) {
                        ok = false
                        break
                    }
                }
            }
            if (!ok) continue@suffix
            answerPos = (Lk + 1) - i0
            break@suffix
        }
    }
    if (answerPos == -1) {
        println("No solution")
        return
    }
    val stamp = curStamp
    val result = CharArray(26)
    val used = BooleanArray(26)
    for (c in 0..25) {
        if (stampF[c] == stamp) {
            val d = fVal[c]
            result[c] = ('A'.code + d).toChar()
            used[d] = true
        }
    }
    var nextFree = 0
    for (c in 0..25) {
        if (stampF[c] != stamp) {
            while (used[nextFree]) nextFree++
            result[c] = ('A'.code + nextFree).toChar()
            used[nextFree] = true
        }
    }
    println(result.concatToString())
    println(answerPos)
}
