fun main() {
    data class Location(val row: Int, val column: Int) {

        fun neighbours(): List<Location> {
            val neighbours = mutableListOf<Location>()
            if (row - 1 >= 0)
                neighbours.add(Location(row - 1, column))
            if (column - 1 >= 0)
                neighbours.add(Location(row, column - 1))

            neighbours.add(Location(row + 1, column))
            neighbours.add(Location(row, column + 1))

            return neighbours
        }
    }

    fun computeLowerPoints(input: Map<Location, Int>): List<Location> {
        val lowerPoints = mutableListOf<Location>()

        for ((key, value) in input) {
            val neighbours = key.neighbours()
            if (neighbours.map { input.getOrDefault(it, Integer.MAX_VALUE) }.all { value < it })
                lowerPoints.add(key)
        }
        return lowerPoints
    }

    fun part1(input: Map<Location, Int>): Int {
        return computeLowerPoints(input).sumOf { input[it]!! + 1 }
    }

    fun computeBasin(key: Location, input: Map<Location, Int>, cache: MutableSet<Location>) {
        val value = input[key]
        val newPoints = mutableListOf<Location>()
        if (value != null) {
           val neighbours =  key.neighbours()
            for ( neighbour in neighbours){
                val nValue = input[neighbour]
                if (nValue != null && nValue != 9 && nValue > value && neighbour !in cache) {
                    newPoints.add(neighbour)
                }
            }
            cache.addAll(newPoints)
            for (p in newPoints)
                computeBasin(p, input, cache)
        }
    }


    fun part2(input: Map<Location, Int>): Int {
        val basinsPoints = mutableMapOf<Location, Set<Location>>()

        val lowerPoints = computeLowerPoints(input)

        for (loc in lowerPoints) {
            val cache = mutableSetOf<Location>()
            cache.add(loc)
            computeBasin(loc, input, cache)
            basinsPoints[loc] = cache
        }
        return basinsPoints.toList().sortedBy { (_, v) -> v.size }
            .reversed()
            .take(3)
            .map { it.second.size }
            .reduce { prod, element -> prod * element }
    }

    fun getLocationMap(input: List<String>): Map<Location, Int> {
        val map = mutableMapOf<Location, Int>()

        for (row in input.indices) {
            val s = input[row]

            for (column in s.indices) {
                val v = s[column].digitToInt()
                map[Location(row, column)] = v
            }
        }
        return map
    }

    val testInput = getLocationMap(readInput("Day09_test"))
    check(part1(testInput) == 15)
    val input = getLocationMap(readInput("Day09"))
    println(part1(input))

    check(part2(testInput) == 1134)
    println(part2(input))
}
