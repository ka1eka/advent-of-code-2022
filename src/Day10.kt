fun main() {
    fun part1(input: List<String>): Int = sequence {
        val breakPoints = setOf(20, 60, 100, 140, 180, 220)
        var x = 1
        var cycle = 0
        input.forEach { command ->
            val previousCycle = cycle
            when (command) {
                "noop" -> cycle++
                else -> cycle += 2
            }
            breakPoints.find { it in (previousCycle + 1)..cycle }
                ?.run {
                    yield(this * x)
                }

            if (command != "noop") {
                x += command.substring(5).toInt()
            }
        }
    }.sum()


    fun part2(input: List<String>) = with(MutableList(40 * 6) { '.' }) {
        var position = 1
        var cycle = 0
        fun render() {
            val col = (cycle % (40 * 6)) % 40
            val row = (cycle % (40 * 6)) / 40
            if (col in position - 1..position + 1) {
                this[row * 40 + col] = '#'
            } else {
                this[row * 40 + col] = '.'
            }
            cycle++
        }
        input.forEach { command ->
            when (command) {
                "noop" -> render()
                else -> {
                    repeat(2) { render() }
                    position += command.substring(5).toInt()
                }
            }
        }
        this.chunked(40)
            .joinToString("\n") {
                it.joinToString("")
            }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(part2(testInput) == buildString {
        append("##..##..##..##..##..##..##..##..##..##..\n")
        append("###...###...###...###...###...###...###.\n")
        append("####....####....####....####....####....\n")
        append("#####.....#####.....#####.....#####.....\n")
        append("######......######......######......####\n")
        append("#######.......#######.......#######.....")
    })

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
