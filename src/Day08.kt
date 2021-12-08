fun main() {

    val numbers = mapOf(
        "12" to 1,
        "01346" to 2,
        "01236" to 3,
        "1256" to 4,
        "02356" to 5,
        "023456" to 6,
        "012" to 7,
        "0123456" to 8,
        "012356" to 9,
        "012345" to 0
    )

    fun part1(input: List<Pair<List<String>, List<String>>>): Int {
        val unique = setOf(2, 3, 4, 7)
        return input.map { it.second }
            .flatten()
            .count { it.length in unique }
    }

    fun computeHistogram(allNumbers: List<String>): MutableMap<Char, Int> {
        val histogram = mutableMapOf<Char, Int>()
        for (word in allNumbers) {
            for (c in word)
                histogram[c] = histogram.getOrDefault(c, 0) + 1
        }
        return histogram
    }

//     0000
//    5    1
//    5    1
//     6666
//    4    2
//    4    2
//     3333

    fun computeSegmentMap(allNumbers: List<String>): Map<Char, Int> {
        val histogram = computeHistogram(allNumbers)

        val segments = CharArray(7)

        val segment2 = histogram.entries.find { it.value == 9 }?.key
        segments[2] = segment2!!

        val segment5 = histogram.entries.find { it.value == 6 }?.key
        segments[5] = segment5!!

        val segment4 = histogram.entries.find { it.value == 4 }?.key
        segments[4] = segment4!!
        val one = allNumbers.find { it.length == 2 }!!
        val seven = allNumbers.find { it.length == 3 }!!

        for (c in seven) {
            if (c !in one) {
                segments[0] = c
                break
            }
        }

        val four = allNumbers.find { it.length == 4 }!!
        val fourItems = HashSet(four.toMutableList())
        fourItems.removeAll(seven.toMutableList())
        fourItems.remove(segments[5])
        segments[6] = fourItems.first()

        val firstSegment = seven.toMutableList()
        firstSegment.removeAll(listOf(segments[0], segments[2]))
        segments[1] = firstSegment[0]


        val eightComponents = allNumbers.find { it.length == 7 }!!.toMutableList()
        val set = HashSet(eightComponents)
        set.removeAll(
            listOf(
                segments[0],
                segments[1],
                segments[2],
                segments[4],
                segments[5],
                segments[6]
            )
        )
        segments[3] = set.first()


        return mapOf(
            segments[0] to 0,
            segments[1] to 1,
            segments[2] to 2,
            segments[3] to 3,
            segments[4] to 4,
            segments[5] to 5,
            segments[6] to 6
        )
    }

    fun part2(input: List<Pair<List<String>, List<String>>>): Int {

        var sum = 0

        for (line in input) {
            val allNumbers = line.first
            val map = computeSegmentMap(allNumbers)

            val second = line.second

            val s = buildString {
                for (number in second) {
                    val array = IntArray(number.length)
                    for (c in number.indices)
                        array[c] = map[number[c]]!!

                    array.sort()
                    val str = array.joinToString("")
                    val digit = numbers[str]
                    append(digit)
                }
            }

            sum += s.toInt()
        }
        return sum

    }

    fun parseLine(line: String): Pair<List<String>, List<String>> {
        val items = line.split("|")
        return Pair(items[0].split(" ").filter { it.isNotBlank() }, items[1].trim().split(" "))
    }

    val testInput = readInputWithTransform("Day08_test", ::parseLine)
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInputWithTransform("Day08", ::parseLine)
    println(part1(input))
    println(part2(input))
}