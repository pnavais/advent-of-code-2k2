package day10

import readInput
import kotlin.text.StringBuilder

class CycleRegister(private val frequency: Int = 20, private val step: Int = 40) {

    private var registerX: Int = 1
    private val signalStrengths = mutableListOf<Int>()
    private var cycleNum = 0
    private val rows = mutableListOf<String>()
    private val currentRow = StringBuilder()

    fun registerCycle(numCycles: Int, value: Int) {
        for (i in 1 .. numCycles) {
            cycleNum++
            processSignal()
        }
        registerX+=value
    }

    fun getTotal(): Int {
        return signalStrengths.sum()
    }

    fun draw(): String {
        val builder = StringBuilder()
        for (row in rows) {
            builder.append(row)
            builder.append("\n")
        }
        return builder.toString()
    }

    private fun processSignal() {
        drawPixel()
        if ((cycleNum == frequency) || (cycleNum % step == frequency)) {
            signalStrengths.add(cycleNum * registerX)
        }
        if (currentRow.length % step == 0) {
            rows.add(currentRow.toString())
            currentRow.clear()
        }
    }

    private fun drawPixel() {
        // Check if sprite overlaps current pos (i.e. currentRow length)
        var c = "."
        if ((registerX-1..registerX+1).contains(currentRow.length)) {
            c = "#"
        }
        currentRow.append(c)
    }

}

private fun processInput(input: List<String>): CycleRegister {
    val cycleRegister = CycleRegister()

    for (inst in input) {
        if (inst == "noop") {
            cycleRegister.registerCycle(1, 0)
        } else {
            val (_, data) = inst.split(" ")
            cycleRegister.registerCycle(2, data.toInt())
        }
    }

    return cycleRegister
}

fun part1(input: List<String>): Int {
    return processInput(input).getTotal()
}

fun part2(input: List<String>): String {
    val cycleRegister = processInput(input)
    return cycleRegister.draw()
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10/Day10_test")
    println(part1(testInput))
    println(part2(testInput))
}
