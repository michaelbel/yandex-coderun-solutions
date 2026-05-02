fun solve(n: Int, a: IntArray, m: Int, b: IntArray): Int {
    var onesA = 0
    var zerosA = 0
    for (v in a) {
        if (v == 1) onesA++ else zerosA++
    }
    var onesB = 0
    var zerosB = 0
    for (v in b) {
        if (v == 1) onesB++ else zerosB++
    }

    val afterA = IntArray(zerosA + 1)
    afterA[0] = onesA
    var seenOnes = 0
    var z = 0
    for (v in a) {
        if (v == 1) {
            seenOnes++
        } else {
            z++
            afterA[z] = onesA - seenOnes
        }
    }

    val afterB = IntArray(zerosB + 1)
    afterB[0] = onesB
    seenOnes = 0
    z = 0
    for (v in b) {
        if (v == 1) {
            seenOnes++
        } else {
            z++
            afterB[z] = onesB - seenOnes
        }
    }

    val limit = if (zerosA < zerosB) zerosA else zerosB
    var ans = 0
    var k = 0
    while (k <= limit) {
        val minOnes = if (afterA[k] < afterB[k]) afterA[k] else afterB[k]
        val cur = k + minOnes
        if (cur > ans) ans = cur
        k++
    }
    return ans
}
