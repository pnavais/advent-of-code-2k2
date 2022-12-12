package day12

import readInput
import java.util.*

typealias NodeGrid = MutableList<MutableList<Node>>

data class Id(val x: Int, val y: Int)

data class Path(val cost: Int = 1, val targetNode: Node)

class Node(private val id: Id, private val c: Char) {
    var pathValue: Int = -1
    var visited: Boolean = false
    var sourceNode: Node? = null
    var height: Short = 0
    val adjacentNodes = mutableListOf<Path>()

    init {
        height = when (c) {
            'S' -> 0
            'E' -> ('z' - 'a').toShort()
            else -> (c - 'a').toShort()
        }
    }

    companion object {
        fun from(x: Int, y: Int, c: Char): Node {
            return Node(Id(x, y), c)
        }
    }

    fun isReachable(node: Node): Boolean {
        return (height >= node.height) || ((height+1).toShort() == node.height)
    }

    override fun toString(): String {
        return "$id, Height: $height, Path: $pathValue, Val: $c, Visited: $visited"
    }
}

class Graph(private var nodes: NodeGrid) {
    private var startNode: Node = nodes[0][0]
    private var endNode: Node = nodes[0][0]

    constructor(startNode: Node, endNode: Node, grid: NodeGrid) : this(grid){
        this.startNode = startNode
        this.endNode = endNode
    }

    // Dijkstra's algorithm
    fun findShortestPath(startNode: Node = this.startNode, endNode: Node = this.endNode): List<Node> {
        val shortestPath = mutableListOf<Node>()
        // Step 1: Assign infinity to all vertex except starting node
        for (y in 1 until nodes.size) {
            for (x in 1 until nodes[y].size) {
                nodes[y][x].pathValue = -1
                nodes[y][x].visited = false
            }
        }
        startNode.pathValue = 0

        // Step 2: Go to each adjacent vertex and update its path length
        val compareByMinPath: Comparator<Node> = compareBy { it.pathValue }
        val nodeQueue: PriorityQueue<Node> = PriorityQueue(compareByMinPath)
        val nodesAdded = mutableSetOf<Node>()
        nodeQueue.add(startNode)

        while (nodeQueue.isNotEmpty()) {
            val currentNode = nodeQueue.poll()
            currentNode.visited = true
            for (sibling in currentNode.adjacentNodes) {
                val targetNode = sibling.targetNode
                if (!targetNode.visited) {
                    // Only update length is lesser than current
                    if ((targetNode.pathValue == -1) || (targetNode.pathValue > (currentNode.pathValue + sibling.cost))) {
                        targetNode.pathValue = currentNode.pathValue + sibling.cost
                        targetNode.sourceNode = currentNode
                    }
                    // Add to queue
                    if (!nodesAdded.contains(targetNode)) {
                        nodeQueue.add(targetNode)
                        nodesAdded.add(targetNode)
                    }
                }
            }
        }

        // Step 3: Backtrack from end node to get the list of source nodes to start
        var pathNode: Node? = endNode
        while ((pathNode != null) && (pathNode != startNode)) {
            shortestPath.add(pathNode)
            pathNode = pathNode.sourceNode
        }

        return shortestPath
    }
}

fun readNodes(input: List<String>): Graph {
    var startNode: Node? = null
    var endNode: Node? = null
    var currentNode: Node? = null
    val nodes: NodeGrid = mutableListOf()

    for ((y, line) in input.withIndex()) {
        val currentRow = mutableListOf<Node>()
        nodes.add(currentRow)
        line.toCharArray().forEachIndexed{ x, c ->
            currentNode = Node.from(x, y, c)
            currentRow.add(currentNode!!)
            // Add adjacent look-ahead nodes (up, left)
            if (y-1>=0) {
                if (currentNode!!.isReachable(nodes[y-1][x])) {
                    currentNode!!.adjacentNodes.add(Path(1, nodes[y-1][x])) // Add upper node
                }
                if (nodes[y-1][x].isReachable(currentNode!!)) {
                    nodes[y-1][x].adjacentNodes.add(Path(1, currentNode!!)) // Set current node as adjacent in upper node
                }
            }
            if (x-1>=0) {
                if (currentNode!!.isReachable(nodes[y][x-1])) {
                    currentNode!!.adjacentNodes.add(Path(1, nodes[y][x-1])) // Add left node
                }
                if (nodes[y][x-1].isReachable(currentNode!!)) {
                    nodes[y][x-1].adjacentNodes.add(Path(1, currentNode!!)) // Set current node as adjacent in left node
                }
            }

            // Keep Starting & end nodes
            if (c == 'S') {
                startNode = currentNode
            } else if (c == 'E') {
                endNode = currentNode
            }
        }
    }

    return Graph(startNode!!, endNode!!, nodes)
}

fun part1(input: List<String>): Int {
    val nodeGrid = readNodes(input)
    return nodeGrid.findShortestPath().size
}

fun part2(input: List<String>): Int {
    return 0
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12/Day12_test")
    println(part1(testInput))
    println(part2(testInput))
}
