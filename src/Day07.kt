import kotlin.math.abs

fun main() {

    fun part1(input: List<Int>): Int {
        return input.minOf { position -> input.sumOf { abs(it - position) } }
    }

    fun part2(input: List<Int>): Int {
        val max = input.maxOf { it }
        return (0..max).minOf { position ->
            input.sumOf {
                val sum = abs(it - position)
                (sum * (sum + 1)) / 2
            }
        }
    }

    val testInput = readIntLine("Day07_test")

    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readIntLine("Day07")
    println(part1(input))
    println(part2(input))
}
