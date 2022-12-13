package day13

import readInput
import java.util.Stack

interface Value {
    fun getData(): Any
}

class IntValue(private val data: Int) : Value {
    override fun getData(): Int {
        return this.data
    }
    companion object {
        fun from(s: String): IntValue {
            return IntValue(s.toInt())
        }
    }
}

class ListValue : Value {
    private var listValue = mutableListOf<Value>()

    override fun getData(): Any {
        return listValue
    }

    fun add(v: Value) {
        listValue.add(v)
    }
}

fun readSpec(input: List<String>): List<ListValue> {

    val listsValue = mutableListOf<ListValue>()

    for (line in input) {
        if (line.isNotEmpty()) {
            listsValue.add(processList(line))
        }
    }

    return listsValue
}

private fun processList(line: String): ListValue {
    val listStack: Stack<ListValue> = Stack()
    var currentList: ListValue? = null
    val digitBuilder = StringBuilder()
    for (c in line.toCharArray()) {
        if (c == '[') {
            currentList = if (currentList == null) ListValue()
            listStack.push(currentList)
        } else if (c.isDigit()) {
            digitBuilder.append(c)
        } else if (c == ',') {
            currentList.add(IntValue.from(digitBuilder.toString()))
            digitBuilder.clear()
        } else if (c == ']') {
            if (digitBuilder.isNotEmpty()) {
                currentList.add(IntValue.from(digitBuilder.toString()))
            }
            val parentList = listStack.pop()
            if (parentList !== currentList) {
                parentList.add(currentList)
            }
            currentList = parentList

        }
    }
    return currentList
}

fun part1(input: List<String>): Int {
    val listsValue = readSpec(input)
    return 1
}

fun part2(input: List<String>): Int {
    return 1
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13/Day13_min")
    println(part1(testInput))
    println(part2(testInput))
}
