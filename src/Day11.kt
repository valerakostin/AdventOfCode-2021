fun main() {
    data class Loc(val x: Int, val y: Int) {
        fun neighbours(): List<Loc> {
            val neighbours = mutableListOf<Loc>()

            val rows = listOf(-1, 0, 1)
            val columns = listOf(-1, 0, 1)

            for (xOffset in rows) {
                for (yOffset in columns) {
                    if (xOffset == 0 && yOffset == 0)
                        continue
                    val nx = x + xOffset
                    val ny = y + yOffset

                    if (nx in 0..9 && ny in 0..9)
                        neighbours.add(Loc(nx, ny))
                }
            }
            return neighbours
        }
    }

    fun parseInput(input: List<String>): Map<Loc, Int> {
        val octopusLoc = mutableMapOf<Loc, Int>()

        for (row in input.indices) {
            val line = input[row]
            for (column in line.indices) {
                val cellValue = line[column].digitToInt()
                val loc = Loc(row, column)
                octopusLoc[loc] = cellValue
            }
        }
        return octopusLoc
    }


    fun processStep(input: Map<Loc, Int>): Map<Loc, Int> {
        val set = mutableSetOf<Loc>()

        val workSet = mutableMapOf<Loc, Int>()
        workSet.putAll(input)
        for ((k, v) in workSet) {
            workSet[k] = v + 1
        }

        var octopus = workSet.entries.find { it.value >= 10 }?.key

        while (octopus != null) {
            set.add(octopus)
            workSet[octopus] = 0
            val neighbours = octopus.neighbours()

            for (n in neighbours) {
                if (n !in set) {
                    val value = workSet[n]
                    if (value != null)
                        workSet[n] = value + 1
                }
            }
            octopus = workSet.entries.find { it.value >= 10 }?.key
        }
        return workSet
    }

    fun part1(input: Map<Loc, Int>): Int {
        var sum = 0
        var state = input
        repeat(100) {
            state = processStep(state)
            sum += state.values.count { it == 0 }
        }
        return sum
    }


    fun part2(input: Map<Loc, Int>): Int {
        var state = input
        var step = 0

        while (state.values.count { it == 0 } != input.size) {
            state = processStep(state)
            step++
        }
        return step
    }

    val testInput = parseInput(readInput("Day11_test"))

    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = parseInput(readInput("Day11"))
    println(part1(input))
    println(part2(input))
}
