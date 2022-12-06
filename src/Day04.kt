fun main() {
    fun expand(assigment: String): Set<Int> = assigment
        .split('-')
        .map { it.toInt() }
        .let { Pair(it[0], it[1]) }
        .let { (it.first..it.second).toSet() }

    fun part1(input: List<String>): Int = input
        .map { it.split(',') }
        .map {
            Pair(
                expand(it[0]),
                expand(it[1])
            )
        }.count {
            it.first.containsAll(it.second) || it.second.containsAll(it.first)
        }


    fun part2(input: List<String>): Int = input
        .map { it.split(',') }
        .map {
            expand(it[0]) intersect expand(it[1])
        }.count {
            it.isNotEmpty()
        }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
