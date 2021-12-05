import kotlin.math.max
import kotlin.math.min

fun main() {
    val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()

    data class Point(val x: Int, val y: Int)
    data class Interval(val start: Point, val end: Point) {

        fun isHorizontal() = start.y == end.y

        fun isVertical() = start.x == end.x

        fun isInXInterval(x: Int): Boolean {
            return if (start.x <= end.x)
                start.x <= x && end.x >= x
            else
                end.x <= x && start.x >= x
        }

        fun gerVerticalRange(): IntRange {
            return if (start.y <= end.y)
                start.y..end.y
            else end.y..start.y
        }
    }

    fun parseInterval(rawString: String): Interval {
        val (_, x1, y1, x2, y2) = regex.find(rawString)!!.groupValues
        return Interval(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt()))
    }

    fun filterHandVIntervals(interval: Interval) =
        interval.start.x == interval.end.x || interval.start.y == interval.end.y

    fun segmentSegmentIntersection(interval: Interval, sweepYMin: Point, sweepYMax: Point): Point {
        val a = interval.start
        val b = interval.end

        val a1 = (b.y - a.y).toDouble()
        val b1 = (a.x - b.x).toDouble()
        val c1 = a1 * (a.x) + b1 * (a.y)

        val a2 = (sweepYMax.y - sweepYMin.y).toDouble()
        val b2 = (sweepYMin.x - sweepYMax.x).toDouble()
        val c2 = a2 * (sweepYMin.x) + b2 * (sweepYMin.y)

        val determinant = a1 * b2 - a2 * b1

        val x = (b2 * c1 - b1 * c2) / determinant
        val y = (a1 * c2 - a2 * c1) / determinant
        return Point(x.toInt(), y.toInt())
    }

    fun computeOverlapping(intervals: List<Interval>): Int {
        val min = intervals.minOf { min(it.start.x, it.end.x) }
        val max = intervals.maxOf { max(it.start.x, it.end.x) }

        val minY = intervals.minOf { min(it.start.y, it.end.y) }
        val maxY = intervals.maxOf { max(it.start.y, it.end.y) }

        val cache = mutableMapOf<Point, Int>()

        for (i in min..max) {
            for (interval in intervals) {
                if (interval.isInXInterval(i)) {
                    when {
                        interval.isHorizontal() -> {
                            val point = Point(i, interval.start.y)
                            cache[point] = cache.getOrDefault(point, 0) + 1
                        }
                        interval.isVertical() -> {
                            val range = interval.gerVerticalRange()
                            for (h in range) {
                                val point = Point(i, h)
                                cache[point] = cache.getOrDefault(point, 0) + 1
                            }
                        }
                        else -> {
                            val p = segmentSegmentIntersection(interval, Point(i, minY), Point(i, maxY))
                            cache[p] = cache.getOrDefault(p, 0) + 1
                        }
                    }
                }
            }
        }
        return cache.values.filter { it > 1 }.count()
    }

    fun part1(intervals: List<Interval>): Int {
        return computeOverlapping(intervals)
    }

    fun part2(intervals: List<Interval>): Int {
        return computeOverlapping(intervals)
    }

    val testInput1 = readInputWithTransform("Day05_test", ::parseInterval, ::filterHandVIntervals)
    val input1 = readInputWithTransform("Day05", ::parseInterval, ::filterHandVIntervals)
    check(part1(testInput1) == 5)
    println(part1(input1))

    val testInput2 = readInputWithTransform("Day05_test", ::parseInterval)
    val input2 = readInputWithTransform("Day05", ::parseInterval)
    check(part2(testInput2) == 12)
    println(part2(input2))
}
