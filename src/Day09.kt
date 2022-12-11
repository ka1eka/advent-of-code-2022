import kotlin.math.abs

fun main() {
    fun Pair<Int, Int>.isTouching(other: Pair<Int, Int>): Boolean =
        this == other || abs(first - other.first) <= 1 && abs(second - other.second) <= 1

    fun Pair<Int, Int>.moveUp(): Pair<Int, Int> = Pair(first, second - 1)

    fun Pair<Int, Int>.moveDown(): Pair<Int, Int> = Pair(first, second + 1)

    fun Pair<Int, Int>.moveLeft(): Pair<Int, Int> = Pair(first - 1, second)

    fun Pair<Int, Int>.moveRight(): Pair<Int, Int> = Pair(first + 1, second)

    fun Pair<Pair<Int, Int>, Pair<Int, Int>>.move(block: () -> Pair<Int, Int>): Pair<Pair<Int, Int>, Pair<Int, Int>> =
        with(block()) {
            Pair(
                this,
                if (this.isTouching(this@move.second)) this@move.second else this@move.first
            )
        }

    fun move(target: Pair<Int, Int>, start: Pair<Int, Int>): Pair<Int, Int> {
        return listOf(
            start.moveUp(),
            start.moveDown(),
            start.moveLeft(),
            start.moveRight(),
            start.moveUp().moveLeft(),
            start.moveUp().moveRight(),
            start.moveDown().moveLeft(),
            start.moveDown().moveRight()
        ).minBy {
            abs(target.first - it.first) + abs(target.second - it.second)
        }
    }

    fun MutableList<Pair<Int, Int>>.move(block: () -> Pair<Int, Int>) = with(block()) {
        this@move[0] = this
        for (i in 1 until this@move.size) {
            if (this@move[i].isTouching(this@move[i - 1])) {
                break
            }
            this@move[i] = move(this@move[i - 1], this@move[i])
        }
    }

    fun part1(input: List<String>): Int = with(ArrayDeque(listOf(Pair(Pair(0, 0), Pair(0, 0))))) {
        input
            .map { it.split(' ') }
            .map { Pair(it[0], it[1].toInt()) }
            .forEach {
                var start = this.last()
                when (it.first) {
                    "U" -> repeat(it.second) {
                        start = start.move { start.first.moveUp() }
                        this.addLast(start)
                    }

                    "D" -> repeat(it.second) {
                        start = start.move { start.first.moveDown() }
                        this.addLast(start)
                    }

                    "L" -> repeat(it.second) {
                        start = start.move { start.first.moveLeft() }
                        this.addLast(start)
                    }

                    "R" -> repeat(it.second) {
                        start = start.move { start.first.moveRight() }
                        this.addLast(start)
                    }
                }
            }
        this.map { it.second }
            .toSet()
            .size
    }


    fun part2(input: List<String>): Int = with(mutableSetOf<Pair<Int, Int>>()) {
        val rope = MutableList(10) { Pair(0, 0) }
        input
            .map { it.split(' ') }
            .map { Pair(it[0], it[1].toInt()) }
            .forEach {
                when (it.first) {
                    "U" -> repeat(it.second) {
                        rope.move { rope.first().moveUp() }
                        this.add(rope.last())
                    }

                    "D" -> repeat(it.second) {
                        rope.move { rope.first().moveDown() }
                        this.add(rope.last())
                    }

                    "L" -> repeat(it.second) {
                        rope.move { rope.first().moveLeft() }
                        this.add(rope.last())
                    }

                    "R" -> repeat(it.second) {
                        rope.move { rope.first().moveRight() }
                        this.add(rope.last())
                    }
                }
            }
        this.size
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
