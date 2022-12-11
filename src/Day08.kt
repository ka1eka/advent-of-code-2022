fun main() {
    class Calculator1(private val input: List<String>) {
        private val rows = input.size
        private val cols = input[0].length
        private val lefts: MutableList<Char?> = MutableList(rows) { null }
        private val rights: MutableList<Char?> = MutableList(rows) { null }
        private val tops: MutableList<Char?> = MutableList(cols) { null }
        private val bottoms: MutableList<Char?> = MutableList(cols) { null }
        private val visibility: List<MutableList<Boolean>> = List(rows) {
            MutableList(cols) { false }
        }

        fun toVisibleSequence(): Sequence<Int> = sequence {
            for (y in 0 until rows) {
                for (x in 0 until cols) {
                    if(isVisible(lefts, tops, x, y)) {
                        yield(1)
                    }
                    if(isVisible(rights, bottoms, cols - x - 1, rows - y - 1)) {
                        yield(1)
                    }
                }
            }
        }

        private fun isVisible(vLine: MutableList<Char?>, hLine: MutableList<Char?>, x: Int, y: Int): Boolean {
            val current = input[y][x]
            var visible = false
            if (isVisible(vLine, current, y)) {
                visible = true
            }
            if (isVisible(hLine, current, x)) {
                visible = true
            }
            if (!visibility[y][x] && visible) {
                visibility[y][x] = true

                return true
            }

            return false
        }

        private fun isVisible(line: MutableList<Char?>, current: Char, index: Int): Boolean {
            if (line[index] == null || current > line[index]!!) {
                line[index] = current

                return true
            }

            return false
        }
    }

    class Calculator2(private val input: List<String>) {
        private val rows = input.size
        private val cols = input[0].length
        private val lefts: List<MutableList<Int?>> = List(rows) { MutableList(cols) { null } }
        private val rights: List<MutableList<Int?>> = List(rows) { MutableList(cols) { null } }
        private val tops: List<MutableList<Int?>> = List(rows) { MutableList(cols) { null } }
        private val bottoms: List<MutableList<Int?>> = List(rows) { MutableList(cols) { null } }

        fun toScenicScoreSequence(): Sequence<Int> = sequence {
            for (y in 0 until rows) {
                for (x in 0 until cols) {
                    countVisibleTrees(lefts, tops, x, y, -1, -1)
                    countVisibleTrees(rights, bottoms, cols - x - 1, rows - y - 1, 1, 1)
                    if (lefts[y][x] != null && rights[y][x] != null && tops[y][x] != null && bottoms[y][x] != null) {
                        yield(lefts[y][x]!! * rights[y][x]!! * tops[y][x]!! * bottoms[y][x]!!)
                    }
                }
            }
        }

        private fun countVisibleTrees(
            vLine: List<MutableList<Int?>>,
            hLine: List<MutableList<Int?>>,
            x: Int,
            y: Int,
            dx: Int,
            dy: Int
        ) {
            val current = input[y][x]
            vLine[y][x] = countVisibleTrees(vLine, current, x + dx, y, dx, 0)
            hLine[y][x] = countVisibleTrees(hLine, current, x, y + dy, 0, dy)
        }

        private fun countVisibleTrees(
            line: List<MutableList<Int?>>,
            target: Char,
            x: Int,
            y: Int,
            dx: Int,
            dy: Int
        ): Int {
            val current = input.getOrNull(y)?.getOrNull(x) ?: return 0
            if (current >= target) {
                return 1
            }

            val currentCount = line[y][x]!!
            if (currentCount == 0) {
                return 1
            }
            return currentCount + countVisibleTrees(line, target, x + dx * currentCount, y + dy * currentCount, dx, dy)
        }
    }

    fun part1(input: List<String>): Int = with(Calculator1(input)) { this.toVisibleSequence().sum() }

    fun part2(input: List<String>): Int = with(Calculator2(input)) { this.toScenicScoreSequence().max() }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
