import java.util.PriorityQueue
import kotlin.math.sqrt

fun main() {
    val startSymbol = 'S'
    val endSymbol = 'E'

    fun Pair<Int, Int>.distance(other: Pair<Int, Int>) =
        sqrt(0.0 + (this.first - other.first) * (this.first - other.first) + (this.second - other.second) * (this.second - other.second))

    fun List<String>.height(position: Pair<Int, Int>): Char =
        when (val current = this[position.second][position.first]) {
            startSymbol -> 'a'
            endSymbol -> 'z'
            else -> current
        }

    class Node(val position: Pair<Int, Int>, val target: Pair<Int, Int>) {
        val distance = position.distance(target)
        private var _cost: Int = 1
        private var _score: Double = distance + _cost

        var parent: Node? = null
            set(value) {
                field = value
                _cost = 1 + (value?.cost ?: 0)
                _score = distance + _cost
            }

        val cost: Int
            get() = _cost

        val score: Double
            get() = _score

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Node

            if (position != other.position) return false

            return true
        }

        override fun hashCode(): Int = position.hashCode()
    }

    fun getNeighbors(current: Node, input: List<String>): List<Node> = listOf(
        Pair(current.position.first, current.position.second - 1),
        Pair(current.position.first, current.position.second + 1),
        Pair(current.position.first - 1, current.position.second),
        Pair(current.position.first + 1, current.position.second)
    ).filter {
        it.second >= 0 && it.second < input.size
                && it.first >= 0 && it.first < input[it.second].length
                && input.height(it) - input.height(current.position) <= 1
    }.map { Node(it, current.target) }

    fun findShortestPath(startPosition: Pair<Int, Int>, endPosition: Pair<Int, Int>, input: List<String>): Int? {
        val path = ArrayDeque<Pair<Int, Int>>()
        val openList = PriorityQueue<Node>(compareBy { it.score })
        val start = Node(startPosition, endPosition)
        openList.add(start)
        val closedList = mutableSetOf<Node>()
        var current: Node = start
        while (openList.isNotEmpty() && current.position != endPosition) {
            current = openList.poll()!!
            closedList.add(current)
            val neighbors = getNeighbors(current, input)
            for (n in neighbors) {
                if (!closedList.contains(n)) {
                    if (!openList.contains(n)) {
                        n.parent = current
                        openList.add(n)
                    }
                }
            }
        }

        var step: Node? = closedList.find { it.position == endPosition } ?: return null
        while (step != null && step.position != startPosition) {
            path.addFirst(step.position)
            step = step.parent
        }

        return path.size
    }

    fun part1(input: List<String>): Int {
        val start = input
            .withIndex()
            .find { it.value.startsWith(startSymbol) }
            .let { Pair(0, it!!.index) }
        val end = input
            .withIndex()
            .find { it.value.contains(endSymbol) }
            .let { Pair(it!!.value.indexOf(endSymbol), it.index) }
        return findShortestPath(start, end, input)!!
    }

    fun part2(input: List<String>): Int {
        val end = input
            .withIndex()
            .find { it.value.contains(endSymbol) }
            .let { Pair(it!!.value.indexOf(endSymbol), it.index) }
        return input
            .withIndex()
            .filter {
                it.value.contains(startSymbol) || it.value.contains('a')
            }
            .flatMap { line ->
                line.value
                    .toCharArray()
                    .withIndex()
                    .filter { ch -> ch.value == startSymbol || ch.value == 'a' }
                    .map { ch -> ch.index }
                    .map { x -> Pair(x, line.index) }
            }.mapNotNull { findShortestPath(it, end, input) }
            .min()
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
