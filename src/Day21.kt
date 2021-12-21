fun main() {
    fun part1(input: Pair<Int, Int>): Long {
        var score1 = 0L
        var score2 = 0L

        var p1 = input.first
        var p2 = input.second

        for (i in generateSequence(0) { it + 1 }) {
            val roll = (i * 3) * 3 + 6
            val pos = if (i % 2 == 0) p1 else p2

            val cp = (roll + pos) % 10
            val newPlace = if (cp == 0) 10 else cp

            if (i % 2 == 0) {
                p1 = newPlace
                score1 += p1
            } else {
                p2 = newPlace
                score2 += p2
            }

            if (score1 >= 1000 || score2 >= 1000) {
                val score = if (score1 > score2) score2 else score1
                return score * ((i * 3) + 3)
            }
        }
        return 0
    }

    //  fun part2(input: Pair<Int, Int>): Int {
    //      return 0
    //   }

    fun parseInput(input: List<String>): Pair<Int, Int> {
        val index = input[0].indexOfLast { it == ' ' }
        val first = input[0].substring(index).trim().toInt()

        val index2 = input[1].indexOfLast { it == ' ' }
        val second = input[1].substring(index2).trim().toInt()

        return Pair(first, second)
    }

    val testInput = parseInput(readInput("Day21_test"))
    check(part1(testInput) == 739785L)


    val input = parseInput(readInput("Day21"))
    println(part1(input))

    // check(part2(testInput) == 0)
    // println(part2(input))
}
