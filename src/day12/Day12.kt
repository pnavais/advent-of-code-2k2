package day12

import readInput

class Id(val x: Int, val y: Int)

class Node(val id: Id, val height: Short, val terminal: Boolean = false) {
    val adjacentNodes = mutableListOf<Node>()
}

fun readNodes(input: List<String>): Node {
    var startingNode: Node? = null
    var currentNode: Node? = null
    val nodes: MutableList<MutableList<Node>> = mutableListOf()

    for ((y, line) in input.withIndex()) {
        val currentRow = mutableListOf<Node>()
        nodes.add(currentRow)
        line.toCharArray().forEachIndexed{ x, c ->
            currentNode = Node(Id(x, y), (c - 'a').toShort(), (c == 'E'))
            currentRow.add(currentNode!!)
            // Add adjacent nodes (up, down, left, right)
            if (y-1>=0) {
                currentNode!!.adjacentNodes.add(nodes[y-1][x])
            }

            // Keep Starting node
            if (c == 'S') {
                startingNode = currentNode
            }
        }
    }

    return startingNode!!
}

fun part1(input: List<String>): Long {
    var startingNode = readNodes(input)
    return 0L
}

fun part2(input: List<String>): Long {
    return 0L
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12/Day12_min")
    println(part1(testInput))
    println(part2(testInput))
}
