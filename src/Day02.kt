fun main() {
    val rules1 = mapOf(
        Pair('A', 'A') to 3,
        Pair('A', 'B') to 6,
        Pair('A', 'C') to 0,
        Pair('B', 'A') to 0,
        Pair('B', 'B') to 3,
        Pair('B', 'C') to 6,
        Pair('C', 'A') to 6,
        Pair('C', 'B') to 0,
        Pair('C', 'C') to 3,
    )

    val rules2 = rules1
        .entries
        .associate {
            Pair(it.key.first, it.value) to it.key
        }

    fun calcChoicePoints(me: Char): Int = me - 'A' + 1

    fun calcPart1Score(round: Pair<Char, Char>): Int = rules1[round]!! + calcChoicePoints(round.second)

    fun calcPart2Score(round: Pair<Char, Char>): Int = calcPart1Score(
        rules2[Pair(
            round.first,
            when (round.second) {
                'Y' -> 3
                'Z' -> 6
                else -> 0
            }
        )]!!
    )

    fun part1(input: List<String>): Int = input
        .map {
            it.split(' ')
        }.map {
            Pair(it[0][0], it[1][0] - ('X' - 'A'))
        }.sumOf {
            calcPart1Score(it)
        }

    fun part2(input: List<String>): Int = input
        .map {
            it.split(' ')
        }.map {
            Pair(it[0][0], it[1][0])
        }.sumOf {
            calcPart2Score(it)
        }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
