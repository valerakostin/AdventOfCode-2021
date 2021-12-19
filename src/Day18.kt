import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

fun main() {

    val numberPattern = """\d\d+""".toRegex()
    val pair = """\[\d+,\d+]""".toRegex()

    fun getRightNumber(str: String, start: Int): IntRange? {
        var s = -1
        var e = -1
        for (i in start until str.length) {
            if (str[i].isDigit()) {
                s = i
                e = i
                break
            }
        }
        if (s != -1) {
            for (i in s + 1 until str.length) {
                if (!str[i].isDigit())
                    break
                else
                    e = i
            }
        }
        if (s != -1 && e != -1)
            return s..e
        return null
    }

    fun getLeftNumber(str: String, start: Int): IntRange? {
        var s = -1
        var e = -1

        for (i in start - 1 downTo 0) {
            if (str[i].isDigit()) {
                s = i
                e = i
                break
            }
        }
        if (s != -1) {
            for (i in s - 1 downTo 0) {
                if (!str[i].isDigit())
                    break
                else
                    e = i
            }
        }

        if (s != -1 && e != -1) {
            return if (e > s) s..e else e..s
        }
        return null
    }

    fun searchForNumber(str: String): IntRange? {
        val found = numberPattern.find(str)
        if (found != null) {
            return found.range
        }
        return null
    }

    fun hasDepth(str: String, depth: Int = 4): IntRange? {
        val stack = Stack<Int>()
        for (i in str.indices) {
            if (str[i] == '[') {
                stack.push(i)
            } else if (str[i] == ']') {
                if (stack.size > depth)
                    return stack.pop() + 1 until i
                stack.pop()
            }
        }
        return null
    }

    fun explode(str: String, range: IntRange): String {
        var newString = str
        val items = str.subSequence(range).split(",")
        val pLeft = items[0].toLong()
        val pRight = items[1].toLong()

        // right
        val right = getRightNumber(str, range.last + 1)

        if (right != null) {
            val number = str.substring(right)
            val sRight = number.toInt() + pRight

            newString = str.substring(0 until right.first) +
                    sRight.toString() + str.substring(right.last + 1)
        }
        // middle
        val s1 = newString.substring(0, range.first - 1)
        val s2 = newString.substring(range.last + 2)
        newString = s1 + '0' + s2

        val left = getLeftNumber(s1, s1.length - 1)
        if (left != null) {
            val number = newString.substring(left)
            val sLeft = number.toInt() + pLeft
            val l1 = newString.substring(0 until left.first)
            val l2 = newString.substring(left.last + 1)
            newString = l1 + sLeft.toString() + l2
        }
        return newString
    }

    fun split(str: String, range: IntRange): String {
        val substring = str.substring(range)
        val n = substring.toInt()

        val left = n / 2
        val right = (n.toDouble() / 2.0).roundToInt()
        val replacement = "[$left,$right]"

        val s1 = str.substring(0 until range.first)
        val s2 = str.substring(range.last + substring.length - 1)

        return s1 + replacement + s2
    }

    fun reduce(str: String): String {
        var newString = str

        while (true) {
            val range = hasDepth(newString)
            if (range != null) {
                newString = explode(newString, range)
                continue
            }
            val numberRange = searchForNumber(newString)
            if (numberRange != null) {
                newString = split(newString, numberRange)
                continue

            } else
                break
        }

        return newString
    }

    fun concat(left: String, right: String) = "[${left},${right}]"

    fun solve(input: List<String>): String {
        val reducesInput = input.map { (it) }
        return reducesInput.reduce { acc, str -> reduce(concat(acc, str)) }
    }

    fun computeMagnitude(reduced: String): Int {
        var str = reduced
        var found = pair.find(str)
        while (found != null) {
            val value = found.value
            val strRaw = value.removeSurrounding("[", "]")
            val items = strRaw.split(",")
            val first = items[0].toInt() * 3
            val second = items[1].toInt() * 2
            val sum = first + second
            val start = str.substring(0, found.range.first)
            val end = str.substring(found.range.last + 1)

            str = start + sum + end
            found = pair.find(str)
        }
        return str.toInt()
    }


    fun part1(input: List<String>): Int {
        val reduced = solve(input)
        return computeMagnitude(reduced)
    }

    fun part2(input: List<String>): Int {

        var max = Int.MIN_VALUE
        for (x in input) {
            for (y in input) {
                if (x != y) {
                    val v1 = computeMagnitude(reduce(concat(x, y)))
                    val v2 = computeMagnitude(reduce(concat(y, x)))
                    max = max(max, max(v1, v2))
                }
            }
        }
        return max
    }

    check(computeMagnitude("[9,1]") == 29)
    check(computeMagnitude("[[9,1],[1,9]]") == 129)
    check(
        solve(
            listOf(
                "[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]",
                "[2,9]"
            )
        ) == "[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]"
    )

    check(reduce("[[[[[9,8],1],2],3],4]") == "[[[[0,9],2],3],4]")
    check(reduce("[7,[6,[5,[4,[3,2]]]]]") == "[7,[6,[5,[7,0]]]]")
    check(reduce("[[6,[5,[4,[3,2]]]],1]") == "[[6,[5,[7,0]]],3]")
    check(reduce("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]") == "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
    check(reduce("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]") == "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
    check(reduce("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]") == "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")

    check(solve(listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]")) == "[[[[1,1],[2,2]],[3,3]],[4,4]]")
    check(solve(listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]")) == "[[[[3,0],[5,3]],[4,4]],[5,5]]")
    check(solve(listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]")) == "[[[[5,0],[7,4]],[5,5]],[6,6]]")

    check(
        solve(
            listOf(
                "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
                "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
                "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
                "[7,[5,[[3,8],[1,4]]]]",
                "[[2,[2,2]],[8,[8,1]]]",
                "[2,9]",
                "[1,[[[9,3],9],[[9,0],[0,7]]]]",
                "[[[5,[7,4]],7],1]",
                "[[[[4,2],2],6],[8,7]]"
            )
        ) == "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
    )

    check(
        solve(
            listOf(
                "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
                "[[[5,[2,8]],4],[5,[[9,9],0]]]",
                "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
                "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
                "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
                "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
                "[[[[5,4],[7,7]],8],[[8,3],8]]",
                "[[9,3],[[9,9],[6,[4,9]]]]",
                "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
                "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"
            )
        ) == "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]"
    )

    val testInput = readInput("Day18_test")
    check(part1(testInput)==4140)
    check(part2(testInput)==3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
