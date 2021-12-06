fun main() {

    fun solve(days: Int, initialData: List<Int>): Long {
        var data = mutableMapOf<Int, Long>()
        for (day in initialData) {
            data[day] = data.getOrDefault(day, 0L) + 1
        }

        repeat(days) {
            val tmp = mutableMapOf<Int, Long>()

            for ((key, value) in data.entries) {
                if (key == 0) {
                    tmp[6] = tmp.getOrDefault(6, 0) + value
                    tmp[8] = tmp.getOrDefault(8, 0) + value

                } else {
                    val nKey = key - 1
                    tmp[nKey] = tmp.getOrDefault(nKey, 0) + value
                }
            }
            data = tmp
        }
        return data.values.sum()

    }

    fun part1(input: List<Int>): Long {
       return  solve( 80, input)
    }

    fun part2(input: List<Int>): Long {
        return  solve( 256, input)
    }

    val testInput = readIntLine("Day06_test")

    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readIntLine("Day06")
    println(part1(input))
    println(part2(input))
}
