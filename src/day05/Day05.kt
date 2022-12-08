package day05

import readInput
import java.util.*
import kotlin.collections.ArrayDeque

val pattern = Regex("^move (\\d+) from (\\d+) to (\\d+)$")

interface CrateMover {
    fun moveCrates(line: String, stacks: MutableList<ArrayDeque<Char>>)
}

private class CrateMover9000 : CrateMover {
    override fun moveCrates(line: String, stacks: MutableList<ArrayDeque<Char>>) {
        val triple = parseMove(line)
        var numMoves = triple.first
        val srcStack = stacks[triple.second!! - 1]
        val targetStack = stacks[triple.third!! - 1]

        while (numMoves!! > 0) {
            val elem = srcStack.removeFirst()
            targetStack.addFirst(elem)
            numMoves--
        }
    }
}

private class CrateMover9001 : CrateMover {
    override fun moveCrates(line: String, stacks: MutableList<ArrayDeque<Char>>) {
        val triple = parseMove(line)
        var numMoves = triple.first
        val srcStack = stacks[triple.second!! - 1]
        val targetStack = stacks[triple.third!! - 1]

        val tempStack = Stack<Char>()
        while (numMoves!! > 0) {
            val elem = srcStack.removeFirst()
            tempStack.push(elem)
            numMoves--
        }
        while (!tempStack.isEmpty()) {
            targetStack.addFirst(tempStack.pop())
        }
    }
}

private fun parseMove(line: String): Triple<Int?, Int?, Int?> {
    val m = pattern.find(line)
    return Triple(m?.groups?.get(1)?.value?.toInt(),
        m?.groups?.get(2)?.value?.toInt(),
        m?.groups?.get(3)?.value?.toInt())
}

fun rearrangeCrates(input: List<String>, crateMover: CrateMover): String {
    val stacks = mutableListOf<ArrayDeque<Char>>()

    for (line in input) {
        if (!line.startsWith("move")) {
            createStack(line, stacks)
        } else {
            crateMover.moveCrates(line, stacks)
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

fun part1(input: List<String>): String {
    return rearrangeCrates(input, CrateMover9000())
}

fun part2(input: List<String>): String {
    return rearrangeCrates(input, CrateMover9001())
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05/Day05_test")
    println(part1(testInput))
    println(part2(testInput))
}
