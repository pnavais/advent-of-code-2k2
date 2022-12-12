package day12

import readInput

class Id(val x: Int, val y: Int)

class Node(private val id: Id, private val height: Short) {
    companion object {
        fun from(x: Int, y: Int, c: Char): Node {
            val height: Short = when (c) {
                'S' -> -1
                'E' -> (('z' - 'a')+1).toShort()
                else -> (c - 'a').toShort()
            }
            return Node(Id(x, y), height)
        }
    }
    fun isReachable(node: Node): Boolean {
        return node.height in height-1..height+1
    }

    val adjacentNodes = mutableListOf<Node>()
}

fun readNodes(input: List<String>): Pair<Node, Node> {
    var startNode: Node? = null
    var endNode: Node? = null
    var currentNode: Node? = null
    val nodes: MutableList<MutableList<Node>> = mutableListOf()

    for ((y, line) in input.withIndex()) {
        val currentRow = mutableListOf<Node>()
        nodes.add(currentRow)
        line.toCharArray().forEachIndexed{ x, c ->
            currentNode = Node.from(x, y, c)
            currentRow.add(currentNode!!)
            // Add adjacent look-ahead nodes (up, left)
            if ((y-1>=0) && currentNode!!.isReachable(nodes[y-1][x])) {
                currentNode!!.adjacentNodes.add(nodes[y-1][x]) // Add upper node
                nodes[y-1][x].adjacentNodes.add(currentNode!!) // Set current node as adjacent in upper node (down)
            }
            if ((x-1>=0) && currentNode!!.isReachable(nodes[y][x-1])) {
                currentNode!!.adjacentNodes.add(nodes[y][x-1]) // Add left node
                nodes[y][x-1].adjacentNodes.add(currentNode!!) // Set current node as adjacent in left node (right)
            }

            // Keep Starting node
            if (c == 'S') {
                startNode = currentNode
            } else if (c == 'E') {
                endNode = currentNode
            }
        }
    }

    return startNode!! to endNode!!
}

fun part1(input: List<String>): Long {
    var (startingNode, endNode) = readNodes(input)
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
