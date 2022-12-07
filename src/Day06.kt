fun main() {
    fun detectMarker(input: List<String>, size: Int) = input
        .first()
        .toCharArray()
        .asSequence()
        .windowed(size)
        .indexOfFirst {
            it.distinct().size == size
        } + size

    fun part1(input: List<String>): Int = detectMarker(input, 4)

    fun part2(input: List<String>): Int = detectMarker(input, 14)

    val testInput = readInput("Day06_test").map { listOf(it) }
    check(part1(testInput[0]) == 7)
    check(part1(testInput[1]) == 5)
    check(part1(testInput[2]) == 6)
    check(part1(testInput[3]) == 10)
    check(part1(testInput[4]) == 11)
    check(part2(testInput[0]) == 19)
    check(part2(testInput[1]) == 23)
    check(part2(testInput[2]) == 23)
    check(part2(testInput[3]) == 29)
    check(part2(testInput[4]) == 26)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
