fun main() {
    fun Sequence<Int?>.chunkedOnNull(): Sequence<List<Int>> = sequence {
        val buffer = mutableListOf<Int>()
        for (element in this@chunkedOnNull) {
            if (element == null) {
                yield(buffer)
                buffer.clear()
            } else {
                buffer += element
            }
        }
        if (buffer.isNotEmpty()) yield(buffer)
    }

    fun part1(input: List<String>): Int = input
        .asSequence()
        .map { it.toIntOrNull() }
        .chunkedOnNull()
        .map { it.sum() }
        .max()

    fun part2(input: List<String>): Int = input
        .asSequence()
        .map { it.toIntOrNull() }
        .chunkedOnNull()
        .map { it.sum() }
        .sortedDescending()
        .take(3)
        .sum()

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
