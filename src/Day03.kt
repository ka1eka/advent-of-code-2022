fun main() {
    fun part1(input: List<String>): Int = input
        .asSequence()
        .map {
            it.toList()
        }.map {
            it.chunked(it.size / 2)
        }.map {
            it[0].intersect(it[1].toSet())
        }.onEach {
            check(it.size == 1)
        }.map {
            it.first()
        }.sumOf {
            when (it) {
                in 'a'..'z' -> (it - 'a' + 1)
                in 'A'..'Z' -> (it - 'A' + 27)
                else -> 0
            }
        }

    fun part2(input: List<String>): Int = input
        .asSequence()
        .map {
            it.toList()
        }.chunked(3)
        .map {
            it[0].intersect(it[1].intersect(it[2].toSet()))
        }.onEach {
            check(it.size == 1)
        }.map {
            it.first()
        }.sumOf {
            when (it) {
                in 'a'..'z' -> (it - 'a' + 1)
                in 'A'..'Z' -> (it - 'A' + 27)
                else -> 0
            }
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
