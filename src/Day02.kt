fun main() {
    fun part1(input: List<String>): Int {
        var score = 0
        var lineIndex = 0
        while (lineIndex < input.size) {
            val currentLine = input[lineIndex]
            val round = currentLine.split(' ')
            val op = round[0][0]
            val me = round[1][0] - ('X' - 'A')
            score += me - 'A' + 1
            if (op == me) {
                score += 3
            } else if (op == 'A') {
                if (me == 'B') {
                    score += 6
                }
            } else if (op == 'B') {
                if (me == 'C') {
                    score += 6
                }
            } else if (op == 'C') {
                if (me == 'A') {
                    score += 6
                }
            }
            lineIndex++
        }
        return score
    }

    fun part2(input: List<String>): Int {
        var score = 0
        var lineIndex = 0
        while (lineIndex < input.size) {
            val currentLine = input[lineIndex]
            val round = currentLine.split(' ')
            val op = round[0][0]
            val goal = round[1][0]
            score += when (goal) {
                'Y' -> 3
                'Z' -> 6
                else -> 0
            }
            if (goal == 'Y') {
                score += op - 'A' + 1
            } else if (goal == 'X') {
                when (op) {
                    'A' -> {
                        score += 3
                    }
                    'B' -> {
                        score += 1
                    }
                    'C' -> {
                        score += 2
                    }
                }
            } else if (goal == 'Z') {
                when (op) {
                    'A' -> {
                        score += 2
                    }
                    'B' -> {
                        score += 3
                    }
                    'C' -> {
                        score += 1
                    }
                }
            }
            lineIndex++
        }
        return score
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
