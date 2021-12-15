import java.util.*

fun main() {
    data class Cell(val x: Int, val y: Int, val distance: Int = Int.MAX_VALUE) : Comparable<Cell> {

        override fun compareTo(other: Cell): Int {
            return when {
                distance < other.distance -> -1
                distance > other.distance -> 1
                else -> 0
            }
        }

        fun neighbours(): List<Cell> {
            return buildList {
                if (x - 1 >= 0)
                    add(Cell(x - 1, y))
                add(Cell(x + 1, y))
                if (y - 1 >= 0)
                    add(Cell(x, y - 1))
                add(Cell(x, y + 1))
            }
        }
    }

    fun parseGrid(input: List<String>): Array<IntArray> {
        val w = input[0].length
        val h = input.size

        val array = Array(h) { IntArray(w) { 0 } }
        for (i in input.indices) {
            val line = input[i]
            array[i] = IntArray(line.length) { line[it].digitToInt() }
        }
        return array
    }

    fun shortestPath(grid: Array<IntArray>): Int {
        val row = grid[0].size
        val col = grid.size
        val distance = Array(row) { IntArray(col) { Int.MAX_VALUE } }

        distance[0][0] = grid[0][0]

        val queue = PriorityQueue<Cell>(row * col)


        queue.add(Cell(0, 0, distance[0][0]))

        while (!queue.isEmpty()) {
            val cell = queue.poll()
            val neighbours = cell.neighbours()

            for (neighbour in neighbours) {
                if (neighbour.x < row && neighbour.y < col) {
                    val v = distance[cell.x][cell.y] +
                            grid[neighbour.x][neighbour.y]
                    if (distance[neighbour.x][neighbour.y] > v) {


                        if (distance[neighbour.x][neighbour.y] != Int.MAX_VALUE) {
                            val adj = Cell(
                                neighbour.x, neighbour.y,
                                distance[neighbour.x][neighbour.y]
                            )
                            queue.remove(adj)
                        }

                        distance[neighbour.x][neighbour.y] = distance[cell.x][cell.y] +
                                grid[neighbour.x][neighbour.y]
                        queue.add(
                            Cell(
                                neighbour.x, neighbour.y,
                                distance[neighbour.x][neighbour.y]
                            )
                        )
                    }
                }
            }
        }
        return distance[row - 1][col - 1] - distance[0][0]
    }

    fun extendInput(grid: Array<IntArray>): Array<IntArray> {
        val w = grid[0].size * 5
        val h = grid.size * 5

        val newArray = Array(h) { IntArray(w) { 0 } }

        for (i in grid.indices) {
            val line = newArray[i]
            val shortPart = grid[i]
            val portionSize = shortPart.size
            for (j in line.indices) {
                val v = j / portionSize
                val index = j - v * portionSize
                var value = shortPart[index] + v
                if (value > 9)
                    value -= 9
                line[j] = value
            }
        }

        for (i in grid.size until h) {
            val line = newArray[i]


            val portionSize = grid.size
            val v = i / portionSize
            val index = i - v * portionSize

            val data = newArray[index]

            for (idx in data.indices) {
                var value = data[idx] + v
                if (value > 9)
                    value -= 9
                line[idx] = value
            }
        }

        return newArray
    }

    fun part1(grid: Array<IntArray>): Int {
        return shortestPath(grid)
    }

    fun part2(grid: Array<IntArray>): Int {
        return shortestPath(grid)
    }

    val testInput = parseGrid(readInput("Day15_test"))
    check(part1(testInput) == 40)

    val input = parseGrid(readInput("Day15"))
    println(part1(input))

    check(part2(extendInput(testInput)) == 315)
    println(part2(extendInput(input)))
}