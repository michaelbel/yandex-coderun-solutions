import java.io.BufferedInputStream

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
        var c = readByte()
        while (c <= 32) c = readByte()
        var res = 0
        while (c > 32) {
            res = res * 10 + (c - 48)
            c = readByte()
        }
        return res
    }
}

private class Trie(capacity: Int) {
    private val left = IntArray(capacity)
    private val right = IntArray(capacity)
    private val maxVal = IntArray(capacity)
    private val lazy = IntArray(capacity)
    private var count = 1
    private val maxBits = 19
    var root = newNode()

    private fun newNode(): Int {
        return count++
    }

    private fun pull(u: Int) {
        val l = left[u]
        val r = right[u]
        var m = 0
        if (l != 0 && maxVal[l] > m) m = maxVal[l]
        if (r != 0 && maxVal[r] > m) m = maxVal[r]
        maxVal[u] = m
    }

    private fun pushLazy(u: Int, bit: Int) {
        val mask = lazy[u]
        if (mask == 0) return
        lazy[u] = 0

        if (((mask shr bit) and 1) == 1) {
            val tmp = left[u]
            left[u] = right[u]
            right[u] = tmp
        }

        val l = left[u]
        val r = right[u]
        if (l != 0) lazy[l] = lazy[l] xor mask
        if (r != 0) lazy[r] = lazy[r] xor mask
    }

    fun insert(index: Int, value: Int) {
        root = insertRec(root, index, value, 0)
    }

    private fun insertRec(u: Int, key: Int, value: Int, bit: Int): Int {
        var node = u
        if (node == 0) node = newNode()

        if (bit == maxBits) {
            maxVal[node] = value
            return node
        }

        pushLazy(node, bit)

        if (((key shr bit) and 1) == 0) {
            left[node] = insertRec(left[node], key, value, bit + 1)
        } else {
            right[node] = insertRec(right[node], key, value, bit + 1)
        }

        pull(node)
        return node
    }

    fun delete(index: Int): Int {
        deletedValue = 0
        root = deleteRec(root, index, 0)
        return deletedValue
    }

    private var deletedValue = 0

    private fun deleteRec(u: Int, key: Int, bit: Int): Int {
        if (u == 0) return 0

        if (bit == maxBits) {
            deletedValue = maxVal[u]
            return 0
        }

        pushLazy(u, bit)

        if (((key shr bit) and 1) == 0) {
            left[u] = deleteRec(left[u], key, bit + 1)
        } else {
            right[u] = deleteRec(right[u], key, bit + 1)
        }

        pull(u)
        if (left[u] == 0 && right[u] == 0) return 0
        return u
    }

    fun shiftPlusOne() {
        if (root != 0) addOneRec(root, 0)
    }

    private fun addOneRec(u: Int, bit: Int) {
        if (bit >= maxBits) return
        pushLazy(u, bit)

        val tmp = left[u]
        left[u] = right[u]
        right[u] = tmp

        val l = left[u]
        if (l != 0) addOneRec(l, bit + 1)

        pull(u)
    }

    fun shiftMinusOne() {
        if (root != 0) subOneRec(root, 0)
    }

    private fun subOneRec(u: Int, bit: Int) {
        if (bit >= maxBits) return
        pushLazy(u, bit)

        val l = left[u]
        if (l != 0) subOneRec(l, bit + 1)

        val tmp = left[u]
        left[u] = right[u]
        right[u] = tmp

        pull(u)
    }

    fun applyXor(x: Int) {
        if (root != 0) lazy[root] = lazy[root] xor x
    }

    fun getMax(): Int {
        return if (root == 0) 0 else maxVal[root]
    }
}

fun main() {
    val fs = FastScanner()
    val n = fs.nextInt()
    val trie = Trie(10_000_000)
    var size = 0
    val out = StringBuilder()

    repeat(n) {
        when (fs.nextInt()) {
            1 -> {
                val x = fs.nextInt()
                trie.insert(size, x)
                size++
            }
            2 -> {
                val x = fs.nextInt()
                trie.shiftPlusOne()
                trie.insert(0, x)
                size++
            }
            3 -> {
                val v = trie.delete(size - 1)
                size--
                out.append(v).append('\n')
            }
            4 -> {
                val v = trie.delete(0)
                trie.shiftMinusOne()
                size--
                out.append(v).append('\n')
            }
            5 -> {
                val x = fs.nextInt()
                trie.applyXor(x)
            }
            6 -> {
                out.append(trie.getMax()).append('\n')
            }
        }
    }

    print(out.toString())
}
