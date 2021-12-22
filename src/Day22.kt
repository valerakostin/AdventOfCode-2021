fun main() {
    data class Point3D(val x: Int, val y: Int, val z: Int)

    data class Cube(val mode: String, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
        fun isInRange(range: IntRange): Boolean {
            return xRange.first in range &&
                    xRange.last in range &&
                    yRange.first in range &&
                    yRange.last in range &&
                    zRange.first in range &&
                    zRange.last in range
        }

        fun toPoints(): Collection<Point3D> {
            val points = mutableSetOf<Point3D>()
            for (x in xRange) {
                for (y in yRange) {
                    for (z in zRange) {
                        val p = Point3D(x, y, z)
                        points.add(p)
                    }
                }
            }
            return points
        }
    }

    fun part1(cubes: List<Cube>): Int {
        val points = mutableSetOf<Point3D>()

        for (cube in cubes) {
            if (cube.isInRange(-50..50)) {
                println("Process $cube")
                val toPoints = cube.toPoints()
                if (cube.mode == "on")
                    points.addAll(toPoints)
                else
                    points.removeAll(toPoints)
            }
        }
        return points.size
    }


    fun parseInput(input: List<String>): List<Cube> {
        val regEx = """(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""".toRegex()

        val cubes = mutableListOf<Cube>()

        for (line in input) {
            val matchResult = regEx.find(line)
            val (mode, xMin, xMax, yMin, yMax, zMin, zMax) = matchResult!!.destructured

            val xr = xMin.toInt()..xMax.toInt()
            val yr = yMin.toInt()..yMax.toInt()
            val zr = zMin.toInt()..zMax.toInt()


            cubes.add(Cube(mode, xr, yr, zr))
        }
        return cubes
    }

    val testInput = readInput("Day22_test")
    check(part1(parseInput(testInput)) == 39)

    val testInput2 = readInput("Day22_test2")
    check(part1(parseInput(testInput2)) == 590784)
    //check(part2(parseInput(testInput)) == 0L)

    val input = readInput("Day22")
    println(part1(parseInput(input)))
}
