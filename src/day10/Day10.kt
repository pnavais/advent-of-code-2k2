package day10

import readInput

class CycleRegister(private val frequency: Long = 20, private val step: Long = 40) {

    private var registerX: Long = 1
    private val signalStrengths = mutableListOf<Long>()
    private var cycleNum = 0L

    fun registerCycle(numCycles: Long, value: Long) {
        for (i in 1 .. numCycles) {
            cycleNum++
            keepSignal()
        }
        registerX+=value
    }

    private fun keepSignal() {
        if ((cycleNum == frequency) || (cycleNum % step == frequency)) {
            signalStrengths.add(cycleNum * registerX)
        }
    }

    fun getTotal(): Long {
        return signalStrengths.sum()
    }

}

fun part1(input: List<String>): Long {
    val cycleRegister = CycleRegister()

    for (inst in input) {
        if (inst == "noop") {
            cycleRegister.registerCycle(1, 0)
        } else {
            val (_, data) = inst.split(" ")
            cycleRegister.registerCycle(2, data.toLong())
        }
    }

    return cycleRegister.getTotal()
}

fun part2(input: List<String>): Long {
    return 0L
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10/Day10_test")
    println(part1(testInput))
    println(part2(testInput))
}
