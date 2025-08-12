import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Collections
import java.util.TreeMap
import java.util.SortedSet
import java.util.TreeSet
import kotlin.math.max
import kotlin.math.min

data class Rect(val id: Int, val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    fun area(): Long {
        return (x2 - x1 + 1L) * (y2 - y1 + 1L)
    }
    fun intersect(other: Rect): Rect? {
        val nx1 = max(this.x1, other.x1)
        val ny1 = max(this.y1, other.y1)
        val nx2 = min(this.x2, other.x2)
        val ny2 = min(this.y2, other.y2)
        return if (nx1 <= nx2 && ny1 <= ny2) {
            Rect(-1, nx1, ny1, nx2, ny2)
        } else {
            null
        }
    }
}

data class Event(val x: Int, val y1: Int, val y2: Int, val type: Int) : Comparable<Event> {
    override fun compareTo(other: Event): Int {
        val xComp = x.compareTo(other.x)
        if (xComp != 0) return xComp
        return type.compareTo(other.type)
    }
}

private class SegmentTreeNode {
    var count: Int = 0
    var coveredLength: Long = 0L
    var left: SegmentTreeNode? = null
    var right: SegmentTreeNode? = null
}

private class SegmentTree(private val yCoordinates: List<Int>) {
    private val root = SegmentTreeNode()
    private val yCoordMap: Map<Int, Int> = yCoordinates.withIndex().associate { it.value to it.index }
    private val numY = yCoordinates.size
    init {
        if (numY >= 2) {
            build(root, 0, numY - 2)
        }
    }
    private fun build(node: SegmentTreeNode, l: Int, r: Int) {
        if (l == r) return
        val mid = l + (r - l) / 2
        node.left = SegmentTreeNode()
        node.right = SegmentTreeNode()
        build(node.left!!, l, mid)
        build(node.right!!, mid + 1, r)
    }
    fun getTotalCoveredLength(): Long {
        return root.coveredLength
    }
    fun update(y1: Int, y2: Int, delta: Int) {
        val idx1 = yCoordMap[y1] ?: return
        val idx2 = yCoordMap[y2] ?: return
        if (idx1 < idx2 && numY >= 2) {
            updateRange(root, 0, numY - 2, idx1, idx2 - 1, delta)
        }
    }
    private fun updateRange(node: SegmentTreeNode, nodeL: Int, nodeR: Int, targetL: Int, targetR: Int, delta: Int) {
        if (nodeL > targetR || nodeR < targetL) return
        if (targetL <= nodeL && nodeR <= targetR) {
            node.count += delta
        } else {
            if (node.left != null && node.right != null) {
                val mid = nodeL + (nodeR - nodeL) / 2
                updateRange(node.left!!, nodeL, mid, targetL, targetR, delta)
                updateRange(node.right!!, mid + 1, nodeR, targetL, targetR, delta)
            }
        }
        if (node.count > 0) {
            node.coveredLength = (yCoordinates[nodeR + 1] - yCoordinates[nodeL]).toLong()
        } else {
            if (nodeL == nodeR) {
                node.coveredLength = 0L
            } else {
                node.coveredLength = (node.left?.coveredLength ?: 0L) + (node.right?.coveredLength ?: 0L)
            }
        }
    }
}

private fun calculateUnionArea(rectangles: List<Rect>): Long {
    if (rectangles.isEmpty()) return 0L
    val events = mutableListOf<Event>()
    val yCoordsSet: SortedSet<Int> = TreeSet()
    for (rect in rectangles) {
        events.add(Event(rect.x1, rect.y1, rect.y2 + 1, 1))
        events.add(Event(rect.x2 + 1, rect.y1, rect.y2 + 1, -1))
        yCoordsSet.add(rect.y1)
        yCoordsSet.add(rect.y2 + 1)
    }
    if (events.isEmpty()) return 0L
    Collections.sort(events)
    val yCoordsList = yCoordsSet.toList()
    if (yCoordsList.size < 2) return 0L
    val segmentTree = SegmentTree(yCoordsList)
    var totalArea = 0L
    var lastX = events[0].x
    for (i in events.indices) {
        val event = events[i]
        val currentX = event.x
        val width = currentX - lastX
        if (width > 0) {
            val coveredYLength = segmentTree.getTotalCoveredLength()
            totalArea += coveredYLength * width
        }
        segmentTree.update(event.y1, event.y2, event.type)
        lastX = currentX
    }
    return totalArea
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val rectangles = mutableListOf<Rect>()
    for (i in 0 until n) {
        val parts = reader.readLine().split(" ").map { it.toInt() }
        rectangles.add(Rect(i, parts[0], parts[1], parts[2], parts[3]))
    }
    var visibleCount = 0
    val processedRectangles = mutableListOf<Rect>()
    for (i in n - 1 downTo 0) {
        val currentRect = rectangles[i]
        val currentArea = currentRect.area()
        val clippedRects = processedRectangles.mapNotNull { it.intersect(currentRect) }
        val coveredArea = calculateUnionArea(clippedRects)
        if (coveredArea < currentArea) {
            visibleCount++
        }
        processedRectangles.add(currentRect)
    }
    writer.write(visibleCount.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
