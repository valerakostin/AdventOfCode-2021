data class Game(val numbers: List<Int>, val cards: List<Card>) {
    fun playGame(): Int {
        for (number in numbers) {
            for (card in cards) {
                card.update(number)
                if (card.isReady())
                    return number * card.getSum()
            }
        }
        return -1
    }

    fun playLoseGame(): Int {
        val ready = HashSet<Int>()

        for (number in numbers) {
            for (cardIndex in cards.indices) {
                val card = cards[cardIndex]
                card.update(number)
                if (card.isReady() && cardIndex !in ready) {
                    ready.add(cardIndex)

                    if (ready.size == cards.size)
                        return card.getSum() * number
                }
            }
        }
        return -1
    }
}

class Card(private val fields: MutableMap<Int, Pair<Int, Int>>) {

    private val rows = IntArray(5) { 0 }
    private val columns = IntArray(5) { 0 }
    private var isReady: Boolean = false


    fun update(number: Int) {

        val pair = fields.remove(number)
        if (pair != null) {
            rows[pair.first]++
            columns[pair.second]++
            if (!isReady && (rows[pair.first] == 5 || columns[pair.second] == 5))
                isReady = true
        }
    }

    fun isReady() = isReady

    fun getSum() = fields.keys.sum()
}


fun main() {

    fun parseInput(name: String): Game {
        val input = readInput(name)
        val numbers = input[0].split(",").map { it.toInt() }.toList()

        val cards = ArrayList<Card>()
        var row = 0
        var map = HashMap<Int, Pair<Int, Int>>()

        for (i in 2 until input.size) {
            val s = input[i]
            if (s.isNotBlank()) {
                val rowNumber = s.split(" ").filter { it.isNotBlank() }
                    .map { it.trim().toInt() }
                    .toList()
                for (column in rowNumber.indices) {
                    val number = rowNumber[column]
                    val position = Pair(row, column)
                    map[number] = position
                }
                row++
            } else {
                val card = Card(map)
                cards.add(card)
                row = 0
                map = HashMap()
            }
        }
        // last card
        val card = Card(map)
        cards.add(card)
        return Game(numbers, cards)
    }

    fun part1(game: Game): Int {
        return game.playGame()
    }


    fun part2(game: Game): Int {
        return game.playLoseGame()
    }

    val testGame = parseInput("Day04_test")
    check(part1(testGame) == 4512)

    val realGame = parseInput("Day04")
    println(part1(realGame))

    check(part2(parseInput("Day04_test")) == 1924)
    println(part2(parseInput("Day04")))
}
