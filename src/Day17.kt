import kotlin.math.max

fun main() {
    data class Trench(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int) {

        fun isIn(point: Point): Boolean {
            return point.x in xMin..xMax && point.y in yMin..yMax
        }

        fun istOut(point: Point): Boolean {
            return point.x > xMax || point.y < yMin
        }
    }

    fun parseInput(text: String): Trench {
        val pattern = """target area: x=(-?\d+)..(-?\d+), y=(-?\d+)..(-?\d+)""".toRegex()
        val match = pattern.find(text)
        val (xMinS, xMaxS, yMinS, yMaxS) = match!!.destructured
        return Trench(xMinS.toInt(), xMaxS.toInt(), yMinS.toInt(), yMaxS.toInt())
    }

    fun checkVelocity(x: Int, y: Int, trench: Trench): List<Point> {

        var currentLocation = Point(0, 0)
        var velX = x
        var velY = y

        val points = mutableListOf<Point>()

        while (!trench.istOut(currentLocation)) {
            currentLocation = Point(currentLocation.x + velX, currentLocation.y + velY)

            if (trench.istOut(currentLocation)) {
                points.clear()
                return listOf()
            } else if (trench.isIn(currentLocation)) {
                points.add(currentLocation)
                return points
            }
            points.add(currentLocation)
            if (velX > 0)
                velX--
            velY--
        }

        return listOf()
    }

    fun part1(trench: Trench): Int {
        // use binary search or more efficient solution
        var max = Integer.MIN_VALUE
        for (x in 1..trench.xMax) {
            for (y in trench.yMin..100) {
                val p = checkVelocity(x, y, trench)
                if (p.isNotEmpty())
                    max = max(max, p.maxOf { it.y })
            }
        }
        return max
    }

    fun part2(trench: Trench): Int {
        var counter = 0
        // use binary search or more efficient solution
        for (x in 0..trench.xMax) {
            for (y in trench.yMin..100) {
                val p = checkVelocity(x, y, trench)
                if (p.isNotEmpty())
                    counter++
            }
        }
        return counter
    }

    val testInput = readObject("Day17_test",::parseInput)
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readObject("Day17",::parseInput)
    println(part1(input))
    println(part2(input))
}
