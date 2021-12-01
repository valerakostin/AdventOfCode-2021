fun main() {
    fun part1(input: List<Int>): Int {
        return input.zipWithNext { a, b -> if (a < b) 1 else 0 }.sum()
    }

    fun part2(input: List<Int>): Int {
       return input.windowed(3)
           .map { it.sum() }
           .zipWithNext { a, b -> if (a < b) 1 else 0 }
           .sum()
        }

        // test if implementation meets criteria from the description, like:
        val testInput = readInts("Day01_test")
        check(part1(testInput) == 7)
        check(part2(testInput) == 5)

        val input = readInts("Day01")
        println(part1(input))
        println(part2(input))
    }
