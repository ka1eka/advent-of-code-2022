import javax.naming.OperationNotSupportedException

data class MonkeyDefinition(
    val items: List<Int>,
    val operation: Char,
    val operationArg: Int?,
    val divider: Int,
    val trueTarget: Int,
    val falseTarget: Int
)

interface Monkeys<T> {
    fun catch(target: Int, item: T)
}

interface SeriousMonkeys<T> : Monkeys<T> {
    val dividers: Set<Int>
}

interface Monkey<T> {
    var inspectionsCount: Int
    fun simulateTurn()
    fun catch(item: T)
}

class NormalMonkey(private val definition: MonkeyDefinition, private val monkeys: Monkeys<Long>) : Monkey<Long> {
    private val items: ArrayDeque<Long> = ArrayDeque(definition.items.map { it.toLong() })
    override var inspectionsCount: Int = 0

    override fun simulateTurn() {
        while (items.isNotEmpty()) {
            val initial = items.removeFirst()
            var inspected = when (definition.operation) {
                '*' -> when (definition.operationArg) {
                    null -> initial * initial

                    else -> {
                        initial * definition.operationArg
                    }
                }

                '+' -> {
                    initial + definition.operationArg!!
                }

                else -> throw OperationNotSupportedException()
            }
            inspected /= 3
            when (inspected % definition.divider) {
                0L -> monkeys.catch(definition.trueTarget, inspected)
                else -> monkeys.catch(definition.falseTarget, inspected)
            }
            inspectionsCount++
        }
    }

    override fun catch(item: Long) = items.addLast(item)
}

class DivisibleLevel(initial: Int, private val divider: Int) {
    private var reminder = initial % divider
    fun pow2() {
        reminder = reminder * reminder % divider
    }

    fun multiply(arg: Int) {
        reminder = reminder * arg % divider
    }

    fun plus(arg: Int) {
        reminder = (reminder + arg) % divider
    }

    fun isDivisible(): Boolean = reminder == 0
}

class SeriousWorryLevel(initial: Int, dividers: Set<Int>) {
    private val levels = dividers.associateWith { DivisibleLevel(initial, it) }
    fun pow2() = levels.values.forEach(DivisibleLevel::pow2)

    fun multiply(arg: Int) = levels.values.forEach { it.multiply(arg) }

    fun plus(arg: Int) = levels.values.forEach { it.plus(arg) }

    fun isDivisibleBy(divider: Int) = levels[divider]!!.isDivisible()
}

class SeriousMonkey(
    private val definition: MonkeyDefinition,
    private val monkeys: SeriousMonkeys<SeriousWorryLevel>
) : Monkey<SeriousWorryLevel> {
    private val items = ArrayDeque(definition.items.map { SeriousWorryLevel(it, monkeys.dividers) })

    override var inspectionsCount: Int = 0

    override fun simulateTurn() {
        while (items.isNotEmpty()) {
            val item = items.removeFirst()
            when (definition.operation) {
                '*' -> when (definition.operationArg) {
                    null -> item.pow2()
                    else -> item.multiply(definition.operationArg)
                }

                '+' -> item.plus(definition.operationArg!!)
            }
            val target = if (item.isDivisibleBy(definition.divider)) {
                definition.trueTarget
            } else {
                definition.falseTarget
            }
            monkeys.catch(target, item)
            inspectionsCount++
        }
    }

    override fun catch(item: SeriousWorryLevel) = items.addLast(item)
}

fun main() {
    fun parseMonkeyDefinitions(input: List<String>) = input.chunked(7) {
        val items = it[1].substring(18).split(", ").map(String::toInt)
        val operation = it[2][23]
        val operationArg = it[2].substring(25).toIntOrNull()
        val test = it[3].substring(21).toInt()
        val trueTarget = it[4].substring(29).toInt()
        val falseTarget = it[5].substring(30).toInt()
        MonkeyDefinition(
            items,
            operation,
            operationArg,
            test,
            trueTarget,
            falseTarget
        )
    }

    class MonkeyBusiness<T>(input: List<String>, block: (MonkeyDefinition, SeriousMonkeys<T>) -> Monkey<T>) :
        SeriousMonkeys<T> {
        private val monkeyDefinitions = parseMonkeyDefinitions(input)
        override val dividers: Set<Int> = monkeyDefinitions.map { it.divider }.toSet()
        private val monkeys: List<Monkey<T>> = monkeyDefinitions
            .map { block(it, this) }

        fun simulateRound() = monkeys.forEach(Monkey<T>::simulateTurn)

        fun toInspectionsSequence(): Sequence<Int> = monkeys
            .asSequence()
            .map { it.inspectionsCount }

        override fun catch(target: Int, item: T) {
            monkeys[target].catch(item)
        }
    }

    fun part1(input: List<String>): Long = with(MonkeyBusiness(input, ::NormalMonkey)) {
        repeat(20) {
            this.simulateRound()
        }
        this.toInspectionsSequence()
            .sortedDescending()
            .take(2)
            .fold(1L) { acc, current -> acc * current }
    }

    fun part2(input: List<String>): Long = with(MonkeyBusiness(input, ::SeriousMonkey)) {
        repeat(10000) {
            this.simulateRound()
        }
        this.toInspectionsSequence()
            .sortedDescending()
            .take(2)
            .fold(1L) { acc, current -> acc * current }
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
