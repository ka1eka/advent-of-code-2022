fun main() {
    fun Sequence<String>.chunkedOnNull(): Sequence<List<String>> = sequence {
        val buffer = mutableListOf<String>()
        for (element in this@chunkedOnNull) {
            if (element.isEmpty()) {
                yield(buffer.toList())
                buffer.clear()
            } else {
                buffer += element
            }
        }
        if (buffer.isNotEmpty()) yield(buffer)
    }

    fun initStacks(startingConfig: List<String>): List<ArrayDeque<Char>> {
        val longestLine = startingConfig.maxBy { it.length }
        val totalNumber = (longestLine.length + 1) / 4
        val stacks = List(totalNumber) {
            ArrayDeque<Char>()
        }
        startingConfig
            .asSequence()
            .take(startingConfig.size - 1)
            .map {
                it.chunked(4) { crate -> crate[1] }
            }
            .forEach {
                it.forEachIndexed { crateIndex, crate ->
                    if (crate.isLetter()) {
                        stacks[crateIndex].addLast(crate)
                    }
                }
            }

        return stacks
    }

    val moveRegex = "move\\s|\\sfrom\\s|\\sto\\s".toRegex()
    fun parseMove(move: String): List<Int> = move
        .splitToSequence(moveRegex)
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .toList()

    fun moveCrates9000(stacks: List<ArrayDeque<Char>>, moves: List<String>) {
        moves
            .map { parseMove(it) }
            .forEach {
                val count = it[0]
                val from = it[1] - 1
                val to = it[2] - 1
                repeat(count) {
                    stacks[to].addFirst(stacks[from].removeFirst())
                }
            }
    }

    fun moveCrates9001(stacks: List<ArrayDeque<Char>>, moves: List<String>) {
        moves
            .map { parseMove(it) }
            .forEach {
                val count = it[0]
                val from = it[1] - 1
                val to = it[2] - 1
                val current = ArrayDeque<Char>()
                repeat(count) {
                    current.addLast(stacks[from].removeFirst())
                }
                while (current.isNotEmpty()) {
                    stacks[to].addFirst(current.removeLast())
                }
            }
    }

    fun part1(input: List<String>): String = input
        .asSequence()
        .chunkedOnNull()
        .toList()
        .let {
            val crates = initStacks(it[0])
            moveCrates9000(crates, it[1])
            crates
        }.mapNotNull {
            it.firstOrNull()
        }
        .toCharArray()
        .concatToString()

    fun part2(input: List<String>): String = input
        .asSequence()
        .chunkedOnNull()
        .toList()
        .let {
            val crates = initStacks(it[0])
            moveCrates9001(crates, it[1])
            crates
        }.mapNotNull {
            it.firstOrNull()
        }
        .toCharArray()
        .concatToString()

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
