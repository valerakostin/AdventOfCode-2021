import kotlin.math.pow

fun main() {
    fun part1(inputs: List<String>): Int {
        val w = inputs[0].length
        var power = 0
        for (i in 0 until w) {
            var ones = 0
            for (s in inputs) {
                if (s[i] == '1')
                    ones++
            }
            if (ones > inputs.size / 2)
                power += 2.0.pow(w - i - 1).toInt()
        }

        val maxValue = (2.toDouble().pow(w)).toInt() - 1
        val gamma = maxValue - power

        return power * gamma
    }


    fun part2(inputs: List<String>): Int {

        fun reduceData(workSet: List<String>, position: Int, condition: (Int, Int) -> Boolean): Int {
            while (workSet.size > 1 && position < workSet[0].length) {
                val result = workSet.groupBy { it[position] }
                val zeros = result.getOrDefault('0', listOf())
                val ones = result.getOrDefault('1', listOf())

                val data = if (condition(zeros.size, ones.size))
                    ones
                else
                    zeros
                return reduceData(data, position + 1, condition)
            }

            return workSet[0].toInt(2)
        }

        val oxygenGenerator = reduceData(inputs, 0) { zeros, ones -> zeros <= ones }
        val scrubber = reduceData(inputs, 0) { zeros, ones -> zeros > ones }

        return oxygenGenerator * scrubber
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
