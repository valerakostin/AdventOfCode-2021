fun main() {
    fun parseInput(lines: List<String>): Pair<String, Map<String, Char>> {

        val mapping = mutableMapOf<String, Char>()
        for (i in 2 until lines.size) {
            val (left, right) = lines[i].split(" -> ")
            mapping[left] = right[0]
        }
        return Pair<String, Map<String, Char>>(lines[0], mapping)
    }

    fun computeDiff(word: String, cache: Map<String, Char>, repeat: Int): Long {
        val letters = mutableMapOf<Char, Long>()
        for (letter in word) {
            letters[letter] = letters.getOrDefault(letter, 0) + 1
        }
        val comps = word.windowed(2)

        var workSet = mutableMapOf<String, Long>()
        for (comp in comps)
            workSet[comp] = workSet.getOrDefault(comp, 0) + 1

        repeat(repeat) {
            val copy = mutableMapOf<String, Long>()
            for ((k, v) in workSet.entries) {
                val insertion = cache[k]
                if (insertion != null) {

                    val current = letters.getOrDefault(insertion, 0)
                    letters[insertion] = current + v


                    val left = k[0].toString() + insertion
                    val right = insertion.toString() + k[1]

                    copy[left] = copy.getOrDefault(left, 0) + v
                    copy[right] = copy.getOrDefault(right, 0) + v
                }
            }
            workSet = copy
        }

        val min = letters.minOf { it.value }
        val max = letters.maxOf { it.value }
        return max - min
    }

    fun part1(word: String, cache: Map<String, Char>): Long {
        return computeDiff(word, cache, 10)
    }

    fun part2(word: String, cache: Map<String, Char>): Long {
        return computeDiff(word, cache, 40)
    }

    val testInput = parseInput(readInput("Day14_test"))
    check(part1(testInput.first, testInput.second) == 1588L)

    val input = parseInput(readInput("Day14"))
    println(part1(input.first, input.second))

    check(part2(testInput.first, testInput.second) == 2188189693529L)
    println(part2(input.first, input.second))
}
