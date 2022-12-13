package day13

import readInput
import java.util.Stack

typealias PacketPair = Pair<Packet, Packet>

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

    override fun toString(): String {
        return getData().toString()
    }
}

class ListValue : Value {
    private var listValue = mutableListOf<Value>()

    override fun getData(): MutableList<Value> {
        return listValue
    }

    fun add(v: Value) {
        listValue.add(v)
    }

    companion object {
        fun from(value: IntValue): ListValue {
            val l = ListValue()
            l.add(value)
            return l
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("[")
        var prefix = ""
        for (item in listValue) {
            builder.append(prefix)
            builder.append(item.toString())
            prefix = ","
        }
        builder.append("]")
        return builder.toString()
    }
}

data class Packet(val list: ListValue)

class PacketList {
    val packetPairs = mutableListOf<PacketPair>()

    fun process(): Int {
        var sum = 0
        for ((i, pair) in packetPairs.withIndex()) {
            val result = processPair(pair)
            if ((result == null) || (result)) {
                sum+=(i+1)
            }
        }
        return sum
    }

    private fun processPair(pair: PacketPair): Boolean? {
        return compareListValues(pair.first.list, pair.second.list)
    }

    private fun compareListValues(firstList: ListValue, secondList: ListValue): Boolean? {
        var result: Boolean? = null
        for ((i, item) in firstList.getData().withIndex()) {
            result = if (secondList.getData().size > i) {
                compareValues(item, secondList.getData()[i])
            } else {
                false
            }
            if (result != null) {
                break
            }
        }

        if ((result == null) && (firstList.getData().size < secondList.getData().size)) {
            result = true
        }

        return result
    }

    private fun compareValues(first: Value, second: Value): Boolean? {
        var res: Boolean? = null
        var firstValue: Value = first
        var compareLists = true

        if (first is IntValue) {
            if (second is IntValue) {
                if ((first.getData() != second.getData())) {
                    res = (first.getData() < second.getData())
                }
                compareLists = false
            } else {
                firstValue = ListValue.from(first)
            }
        }

        if (compareLists) {
            val secondValue: ListValue = if (second is ListValue) { second } else {
                ListValue.from(second as IntValue)
            }
            res = compareListValues(firstValue as ListValue, secondValue)
        }

        return res
    }
}

fun readSpec(input: List<String>): PacketList {
    val packetList = PacketList()
    var firstPacket: Packet? = null
    for (line in input) {
        if (line.isNotEmpty()) {
            val newPacket = Packet(processList(line))
            firstPacket = if (firstPacket == null) {
                newPacket
            } else {
                packetList.packetPairs.add(firstPacket to newPacket)
                null
            }

        }
    }
    return packetList
}

private fun processList(line: String): ListValue {
    val listStack: Stack<ListValue> = Stack()
    var currentList: ListValue? = null
    val digitBuilder = StringBuilder()
    for (c in line.toCharArray()) {
        if (c == '[') {
            if (currentList != null) {
                listStack.push(currentList)
            }
            currentList = ListValue()
        } else if (c.isDigit()) {
            digitBuilder.append(c)
        } else if ((c == ',') && (digitBuilder.isNotEmpty())) {
            currentList?.add(IntValue.from(digitBuilder.toString()))
            digitBuilder.clear()
        } else if (c == ']') {
            if (digitBuilder.isNotEmpty()) {
                currentList?.add(IntValue.from(digitBuilder.toString()))
                digitBuilder.clear()
            }
            if (listStack.isNotEmpty()) {
                val parentList = listStack.pop()
                if (parentList !== currentList) {
                    parentList.add(currentList!!)
                }
                currentList = parentList
            }
        }
    }
    return currentList!!
}

fun part1(input: List<String>): Int {
    val packetList = readSpec(input)
    return packetList.process()
}

fun part2(input: List<String>): Int {
    return 1
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13/Day13_test")
    println(part1(testInput))
    println(part2(testInput))
}
