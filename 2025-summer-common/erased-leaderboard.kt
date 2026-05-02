fun solution(n: Int, m: Int, p: IntArray): IntArray {
    val mL = m.toLong()
    val a = LongArray(n) { mL }
    var prevIdx = -1
    var prevSum = 0L
    for (i in 0 until n) {
        val pi = p[i].toLong()
        if (pi != -1L) {
            if (pi < mL * (i + 1L)) return intArrayOf(-1)
            if (prevIdx != -1) {
                if (pi - prevSum < mL * (i - prevIdx).toLong()) return intArrayOf(-1)
            }
            prevIdx = i
            prevSum = pi
        }
    }
    prevIdx = -1
    prevSum = 0L
    for (i in 0 until n) {
        val pi = p[i].toLong()
        if (pi != -1L) {
            val len = i - prevIdx
            val minInc = mL * len.toLong()
            val extra = pi - (prevSum + minInc)
            a[i] += extra
            prevIdx = i
            prevSum = pi
        }
    }
    val res = IntArray(n)
    for (i in 0 until n) {
        res[i] = a[i].toInt()
    }
    return res
}
