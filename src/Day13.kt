data class ListOrInt(val list: List<ListOrInt>? = null, val int: Int? = null) {
    fun wrap(): ListOrInt = when (int) {
        null -> this
        else -> ListOrInt(listOf(this))
    }
}

fun main() {
    fun isCorrectOrder(left: ListOrInt, right: ListOrInt?): Boolean? {
        when {
            right == null -> return false
            left.int != null && right.int != null -> return when {
                left.int < right.int -> true
                left.int == right.int -> null
                else -> false
            }

            left.list != null && right.list != null -> return when {
                left.list.isEmpty() && right.list.isNotEmpty() -> true
                else -> left
                    .list
                    .asSequence()
                    .mapIndexed { index, it ->
                        isCorrectOrder(it, right.list.getOrNull(index))
                    }.firstNotNullOfOrNull { it } ?: (if (left.list.size == right.list.size) null else true)
            }

            else -> return isCorrectOrder(left.wrap(), right.wrap())
        }
    }

    fun isCorrectOrder(left: List<ListOrInt>, right: List<ListOrInt>): Boolean =
        left
            .asSequence()
            .mapIndexed { index, it ->
                isCorrectOrder(it, right.getOrNull(index))
            }.firstNotNullOfOrNull { it } ?: true

    fun compare(left: List<ListOrInt>, right: List<ListOrInt>): Int =
        when (isCorrectOrder(left, right)) {
            true -> -1
            false -> 1
        }

    fun parseMessage(line: String): List<ListOrInt> {
        if (line.isBlank()) {
            return listOf()
        }

        val path = ArrayDeque<MutableList<ListOrInt>>()
        path.add(mutableListOf())
        var index = 1
        var accumulator = ""
        fun accumulate() {
            if (accumulator.isNotBlank()) {
                path.last().add(ListOrInt(int = accumulator.toInt()))
                accumulator = ""
            }
        }
        while (index < line.length - 1) {
            when (val current = line[index]) {
                '[' -> path.addLast(mutableListOf())
                ']' -> {
                    accumulate()
                    val list = path.removeLast()
                    path.last().add(ListOrInt(list))
                }

                ',' -> accumulate()
                else -> accumulator += current
            }
            index++
        }
        accumulate()

        return path.first()
    }

    fun part1(input: List<String>): Int = input
        .asSequence()
        .map { parseMessage(it) }
        .chunked(3)
        .withIndex()
        .filter {
            isCorrectOrder(it.value[0], it.value[1])
        }
        .sumOf { it.index + 1 }

    val marker1 = listOf(ListOrInt(listOf(ListOrInt(int = 2))))
    val marker2 = listOf(ListOrInt(listOf(ListOrInt(int = 6))))
    fun part2(input: List<String>): Int = (
            input
                .asSequence()
                .filter { it.isNotBlank() }
                .map { parseMessage(it) } + sequenceOf(marker1, marker2)
            )
        .sortedWith(::compare)
        .withIndex()
        .filter { it.value == marker1 || it.value == marker2 }
        .fold(1) { acc, it ->
            acc * (it.index + 1)
        }


    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    val part2 = part2(testInput)
    check(part2 == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
