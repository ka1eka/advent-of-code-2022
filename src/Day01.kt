fun main() {
    fun part1(input: List<String>): Int {
        var max = 0
        var current = 0
        var lineIndex = 0
        while (lineIndex <= input.size) {
            val currentLine = if (lineIndex == input.size) "" else input[lineIndex]
            if (currentLine.isEmpty()) {
                if (current > max) {
                    max = current
                }
                current = 0
            } else {
                current += currentLine.toInt()
            }
            lineIndex++
        }
        return max
    }

    fun part2(input: List<String>): Int {
        var top1 = 0
        var top2 = 0
        var top3 = 0
        var current = 0
        var lineIndex = 0
        while (lineIndex <= input.size) {
            val currentLine = if (lineIndex == input.size) "" else input[lineIndex]
            if (currentLine.isEmpty()) {
                if (current > top3) {
                    top3 = current
                    if (current > top2) {
                        top3 = top2
                        top2 = current
                        if (current > top1) {
                            top2 = top1
                            top1 = current
                        }
                    }
                }
                current = 0
            } else {
                current += currentLine.toInt()
            }
            lineIndex++
        }
        return top1 + top2 + top3
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
