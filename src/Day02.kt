fun main() {
    fun part1(inputs: List<String>): Int {
        var position = 0
        var depth = 0
        for (input in inputs){
             val items =  input.split(" ")
            when {
                "forward" == items[0] -> {
                    position += items[1].toInt()
                }
                "down" == items[0] -> {
                    depth += items[1].toInt()
                }
                else -> {
                    depth -= items[1].toInt()
                }
            }
        }
        return position * depth
    }

    fun part2(inputs: List<String>): Long {
        var position:Long = 0
        var depth:Long = 0
        var aim:Long = 0

        for (input in inputs){
            val items =  input.split(" ")
            when {
                "forward" == items[0] -> {
                    position += items[1].toLong()
                    depth += aim * items[1].toLong()
                }
                "down" == items[0] -> {
                    aim += items[1].toLong()
                }
                else -> {
                    aim -= items[1].toLong()
                }
            }
        }
        return position * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 900L)
    //check(part2(testInput) == 5)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
