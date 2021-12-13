enum class FoldDirection {
    UP, LEFT
}

fun main() {

    data class FoldCommand(val step: Int, val direction: FoldDirection) {

        fun isUpDirection() = direction == FoldDirection.UP

        fun isLeftDirection() = direction == FoldDirection.LEFT

        fun middle() = step - step % 2
    }

    fun parseInput(input: List<String>): Pair<Collection<Point>, List<FoldCommand>> {

        val index = input.indexOf("")
        if (index != -1) {

            val points = input.take(index).map {
                val (x, y) = it.split(",")
                Point(x.toInt(), y.toInt())
            }.toSet()

            val commands = input.takeLast(input.size - index - 1).map {
                val idx = it.indexOf("=") + 1
                if (it.startsWith("fold along y="))
                    FoldCommand(it.substring(idx).toInt(), FoldDirection.UP)
                else
                    FoldCommand(it.substring(idx).toInt(), FoldDirection.LEFT)
            }.toList()

            return Pair(points, commands)
        }
        return Pair(listOf(), listOf())
    }

    fun foldPaper(input: Pair<Collection<Point>, List<FoldCommand>>): Collection<Point> {
        var points = input.first
        val commands = input.second

        var maxX = points.maxOf { it.x }
        var maxY = points.maxOf { it.y }

        for (command in commands) {
            val workSet = mutableSetOf<Point>()
            for (point in points) {
                if (command.isUpDirection() && point.y > command.step)
                    workSet.add(point.copy(y = maxY - point.y))
                else if (command.isLeftDirection() && point.x > command.step)
                    workSet.add(point.copy(x = maxX - point.x))
                else
                    workSet.add(point)
            }
            points = workSet

            if (command.isUpDirection())
                maxY = command.middle()
            else
                maxX = command.middle()
        }
        return points
    }

    fun printMessage(points: Collection<Point>) {
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }

        for (column in 0..maxY) {
            for (row in 0..maxX) {
                if (points.contains(Point(row, column)))
                    print("#")
                else
                    print(" ")
            }
            println()
        }
    }

    fun part1(input: Pair<Collection<Point>, List<FoldCommand>>): Int {
        val points = foldPaper(Pair(input.first, listOf(input.second.first())))
        return points.size
    }

    fun part2(input: Pair<Collection<Point>, List<FoldCommand>>) {
        val points = foldPaper(input)
        printMessage(points)
    }

    val testInput = parseInput(readInput("Day13_test"))
    check(part1(testInput) == 17)
    val input = parseInput(readInput("Day13"))
    println(part1(input))

    part2(input)
}
