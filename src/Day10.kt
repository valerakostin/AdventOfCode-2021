import java.util.*

fun main() {

    fun getMissing(stack: Stack<Char>): String {
        return buildString {
            while (stack.isNotEmpty()) {
                when (stack.pop()) {
                    '[' -> append(']')
                    '<' -> append('>')
                    '{' -> append('}')
                    '(' -> append(')')
                }
            }
        }
    }

    fun checkChunk(str: String): Pair<Char?, String?> {
        val stack = Stack<Char>()

        for (c in str) {
            if (c == '(' || c == '{' || c == '<' || c == '[') {
                stack.push(c)
            } else if (!stack.isEmpty()) {
                if (stack.peek() == '(' && c == ')')
                    stack.pop()
                else if (stack.peek() == '<' && c == '>')
                    stack.pop()
                else if (stack.peek() == '[' && c == ']')
                    stack.pop()
                else if (stack.peek() == '{' && c == '}')
                    stack.pop()
                else
                    return Pair(c, null)
            }
        }
        return Pair(null, getMissing(stack))
    }


    fun part1(input: List<Char>): Int {

        val scores = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )
        return input.sumOf { scores.getOrDefault(it, 0) }
    }

    fun computeScore(str: String, scoreMap: Map<Char, Int>): Long {
        var localSum = scoreMap.getOrDefault(str[0], 0).toLong()
        for (i in 1 until str.length) {
            val add = scoreMap.getOrDefault(str[i], 0)
            val n = localSum * 5

            localSum = n + add
        }
        return localSum
    }

    fun part2(input: List<String>): Long {

        val scores = mapOf(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4
        )

        val list = input.map { computeScore(it, scores) }.sorted()
        return list[list.size / 2]

    }

    val testInput = readInputWithTransform("Day10_test", ::checkChunk)
    val testPart1 = testInput.mapNotNull { it.first }
    val testPart2 = testInput.mapNotNull { it.second }

    check(part1(testPart1) == 26397)
    check(part2(testPart2) == 288957L)


    val input = readInputWithTransform("Day10", ::checkChunk)
    val part1 = input.mapNotNull { it.first }
    val part2 = input.mapNotNull { it.second }
    println(part1(part1))
    println(part2(part2))
}
