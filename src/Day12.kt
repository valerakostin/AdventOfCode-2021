fun main() {
    class Graph {
        val edges = mutableMapOf<String, MutableList<String>>()

        fun addEdge(start: String, end: String) {
            val adjEdges = edges.computeIfAbsent(start) { mutableListOf() }
            adjEdges.add(end)
            edges[start] = adjEdges
        }

        fun neighbours(vertex: String): List<String> {
            return edges.getOrDefault(vertex, listOf())
        }

        private fun allPaths(
            from: String,
            to: String,
            visited: MutableMap<String, Int>,
            path: MutableList<String>,
            paths: MutableSet<String>,
            checkFunction: (String, Map<String, Int>) -> Boolean
        ) {
            if (from == to) {
                val p = path.joinToString(separator = "-")
                //     println(p)
                paths.add(p)
                return
            }

            visited[from] = visited.getOrDefault(from, 0) + 1

            val neighbours = neighbours(from)
            for (neighbour in neighbours) {
                if (checkFunction(neighbour, visited)) {
                    path.add(neighbour)
                    allPaths(neighbour, to, visited, path, paths, checkFunction)
                    path.removeAt(path.size - 1)
                }
            }

            val v = visited.getOrDefault(from, 0) - 1
            if (v > 0)
                visited[from] = v
            else
                visited.remove(from)

        }

        fun findAllDistinctPaths(
            start: String,
            end: String,
            checkFunction: (String, Map<String, Int>) -> Boolean
        ): Set<String> {
            val paths = HashSet<String>()
            val path = ArrayList<String>()
            path.add(start)

            allPaths(start, end, HashMap(), path, paths, checkFunction)
            return paths
        }
    }

    fun parseGraph(input: List<String>): Graph {
        val graph = Graph()
        for (line in input) {
            val (start, end) = line.split("-")
            graph.addEdge(start, end)
            graph.addEdge(end, start)
        }
        return graph
    }

    fun part1(graph: Graph): Int {
        val checkFunction: (String, Map<String, Int>) -> Boolean =
            { vertex, visited -> vertex.all { it.isUpperCase() } || vertex !in visited }
        return graph.findAllDistinctPaths("start", "end", checkFunction).size
    }

    fun allowUpperCaseAndTwiceLowerCase(candidate: String, map: Map<String, Int>): Boolean {

        if (candidate.all { it.isUpperCase() })
            return true

        if (candidate == "start")
            return false

        val value = map[candidate] ?: return true

        if (value >= 2)
            return false

        else if (value == 1 && map.entries.filter { !it.key.all { c -> c.isUpperCase() } }.count { it.value >= 2 } == 0)
            return true
        return false
    }

    fun part2(graph: Graph): Int {
        return graph.findAllDistinctPaths("start", "end", ::allowUpperCaseAndTwiceLowerCase).size
    }

    val testInput1 = parseGraph(readInput("Day12_test1"))
    check(part1(testInput1) == 10)
    val testInput2 = parseGraph(readInput("Day12_test2"))
    check(part1(testInput2) == 19)
    val testInput3 = parseGraph(readInput("Day12_test3"))
    check(part1(testInput3) == 226)


    val input = parseGraph(readInput("Day12"))
    println(part1(input))


    check(part2(testInput1) == 36)
    check(part2(testInput2) == 103)
    check(part2(testInput3) == 3509)

    println(part2(input))
}
