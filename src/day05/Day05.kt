package day05

import readInput
import kotlin.collections.ArrayDeque

val pattern = Regex("^move (\\d+) from (\\d+) to (\\d+)$")

fun part1(input: List<String>): String {
    val stacks = mutableListOf<ArrayDeque<Char>>()

    for (line in input) {
        if (!line.startsWith("move")) {
            createStack(line, stacks)
        } else {
            makeMove(line, stacks)
        }
    }

    printStacks(stacks)
    val builder = StringBuilder()
    for (stack in stacks) {
        builder.append(stack.first())
    }
    return builder.toString()
}

private fun printStacks(stacks: MutableList<ArrayDeque<Char>>) {
    println("---")
    println("Stacks ")
    for ((index, stack) in stacks.withIndex()) {
        print("Stack #${index + 1} (size: ${stack.size}):")
        for (item in stack.indices.reversed()) {
            print(" ${stack[item]}")
        }
        println()
    }
}

private fun createStack(line: String, stacks: MutableList<ArrayDeque<Char>>) {
    var index: Int = line.indexOf("[")
    while (index >= 0) {
        while (stacks.size < ((index / 4) + 1)) {
            stacks.add(ArrayDeque())

        }
        stacks[index / 4].add(line[index + 1])
        index = line.indexOf("[", index + 1)
    }
}

private fun makeMove(line: String, stacks: MutableList<ArrayDeque<Char>>) {
    val m = pattern.find(line)
    var numMoves = m?.groups?.get(1)?.value?.toInt()
    val srcStackIdx = m?.groups?.get(2)?.value?.toInt()
    val targetStackIdx = m?.groups?.get(3)?.value?.toInt()

    while (numMoves!! > 0) {
        val srcStack = stacks[srcStackIdx!! - 1]
        val targetStack = stacks[targetStackIdx!! - 1]
        val elem = srcStack.removeFirst()
        targetStack.addFirst(elem)
        numMoves--
    }

}

fun part2(input: List<String>): String {
    return ""
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05/Day05_test")
    println(part1(testInput))
    println(part2(testInput))
}
