package day11

import readInput
import java.util.*
import kotlin.math.floor

enum class Operator {
    SUM, MULTIPLY, SUBTRACT, DIVIDE, NONE;

    companion object {
        fun from(c: Char): Operator {
            return when (c) {
                '+' -> SUM
                '*' -> MULTIPLY
                '-' -> SUBTRACT
                '/' -> DIVIDE
                else -> NONE
            }
        }
    }
}

class Factor(val isOld: Boolean = false, val value: Long = 0L) {
    companion object {
        fun from(s: String): Factor {
            return if (s == "old") { Factor(true, 0L) } else {
                Factor(false, s.toLong())
            }
        }
    }
}

class Operation(private val first: Factor, private val operator: Operator, private val second: Factor) {
    fun run(old: Long): Long {
        val firstValue = if (first.isOld) { old } else { first.value }
        val secondValue = if (second.isOld) { old } else { second.value }
        return when (operator) {
            Operator.SUM -> firstValue + secondValue
            Operator.MULTIPLY -> firstValue * secondValue
            Operator.SUBTRACT -> firstValue - secondValue
            Operator.DIVIDE -> firstValue / secondValue
            Operator.NONE -> firstValue
        }
    }
}

class Monkey(val id: Int,
             var items: Queue<Long> = LinkedList(),
             var operation: Operation? = null,
             var tester: (new: Long) -> Int = { _ -> 0 },
             var reducer: (new: Long) -> Long = { n -> n }) {
    var visitor: ItemVisitor = MonkeyItemCounter()
}

interface ItemVisitor {
    fun visitItem(item: Long)
    fun getData(): Long
}

class MonkeyItemCounter : ItemVisitor {
    private var counter: Long = 0L
    override fun visitItem(item: Long) {
        counter++
    }

    override fun getData(): Long {
        return counter
    }
}

class MonkeyGroup {
    private val monkeyList: MutableList<Monkey> = mutableListOf()
    private var lcm: Long = 0L

    fun parseSpec(input: List<String>) {
        var monkey: Monkey? = null
        val dividers = mutableListOf<Long>()
        var divider = 0L
        var monkeyTargetId = 0
        for (line in input) {
            if (line.startsWith("Monkey")) {
                val id = Regex("^Monkey (\\d+):$").find(line)?.groupValues?.get(1)?.toInt()
                monkey = Monkey(id!!)
            } else if ("^\\s*Starting items:".toRegex().containsMatchIn(line)) {
                line.split(": ")[1].split(", ").map { i -> i.toLong() }.forEach { i -> monkey?.items?.add(i) }
            } else if ("^\\s*Operation:".toRegex().containsMatchIn(line)) {
                val (first, op, second) = line.split(" = ")[1].split(" ")
                monkey?.operation = Operation(Factor.from(first), Operator.from(op.first()), Factor.from(second))
            } else if ("^\\s*Test: divisible by".toRegex().containsMatchIn(line)) {
                divider = Regex("divisible by (\\d+)").find(line)?.groupValues?.get(1)?.toLong()!!
                dividers.add(divider)
            } else if ("^\\s*If (true|false):".toRegex().containsMatchIn(line)) {
                monkeyTargetId = parseTester(line, monkeyTargetId, monkey, divider)
            }
        }

        lcm = computeLcm(dividers)
    }

    fun run(numRounds: Long, debug: Boolean = false, useReducer: Boolean = false, worryFunction: (n: Long) -> Long = { n -> n }) {
        for (i in 1..numRounds) {
            for (monkey in monkeyList) {
                while (monkey.items.isNotEmpty()) {
                    monkey.visitor.visitItem(i)
                    val original = monkey.items.poll()
                    var newItem = monkey.operation?.run(original)?.let { worryFunction(it) }
                    val targetId = newItem?.let { monkey.tester(it) }
                    newItem = if (useReducer) { newItem?.let { monkey.reducer(it) } } else { newItem }
                    monkeyList[targetId!!].items.add(newItem)
                }
            }
            if (debug) {
                printStatus(i)
            }
        }
        //if (debug) {
            printVisitorStats()
        //}
    }

    fun computeMonkeyBusiness(): Long {
        return monkeyList.map { monkey -> monkey.visitor.getData() }.sortedDescending().take(2).reduce { x, y -> x * y }
    }

    private fun parseTester(line: String, monkeyTargetId: Int, monkey: Monkey?, divider: Long): Int {
        val monkeyId = Regex("throw to monkey (\\d+)").find(line)?.groupValues?.get(1)?.toInt()!!
        if (line.contains("If false")) {
            monkey?.tester = { n -> if (n % divider == 0L) { monkeyTargetId} else { monkeyId } }
            monkey?.reducer = { n -> n % lcm }
            monkeyList.add(monkey!!)
        }
        return monkeyId
    }

    private fun computeLcm(nums: List<Long>): Long {
        val lcmMap = mutableMapOf<Long, Int>()
        var found = false
        var mcm = -1L
        var counter = 1L
        while (!found) {
            for (i in nums) {
                val m = i * counter
                lcmMap.merge(m, 1) { o, _ -> o + 1 }
                if (lcmMap[m] == nums.size) {
                    mcm = m
                    found = true
                }
            }
            counter++
        }
        return mcm
    }

    private fun printStatus(roundNum: Long) {
        println("After round $roundNum, the monkeys are holding items with these worry levels:")
        for (monkey in monkeyList) {
            print("Monkey ${monkey.id}: ")
            var prefix = ""
            for (item in monkey.items) {
                print("${prefix}${item}")
                prefix = ", "
            }
            println()
        }
        println()
    }

    private fun printVisitorStats() {
        for (monkey in monkeyList) {
            println("Monkey ${monkey.id} inspected items ${monkey.visitor.getData()} times.")
        }
    }
}

fun part1(input: List<String>): Long {
    val monkeyGroup = MonkeyGroup()
    monkeyGroup.parseSpec(input)
    monkeyGroup.run(20, worryFunction =  { n -> n.toFloat().div(3.0f).let { floor(it).toLong() } })
    return monkeyGroup.computeMonkeyBusiness()
}

fun part2(input: List<String>): Long {
    val monkeyGroup = MonkeyGroup()
    monkeyGroup.parseSpec(input)
    monkeyGroup.run(10000, debug = false, useReducer = true)
    return monkeyGroup.computeMonkeyBusiness()
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11/Day11_test")
    println(part1(testInput))
    println(part2(testInput))
}
