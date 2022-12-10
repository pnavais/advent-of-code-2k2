package day09

import readInput
import kotlin.math.abs

enum class Direction {
    LEFT, UP, RIGHT, DOWN, NONE;

    companion object {
        fun from(c: String): Direction {
            return when (c) {
                "L" -> LEFT
                "U" -> UP
                "R" -> RIGHT
                "D" -> DOWN
                else -> NONE
            }
        }
    }
}

interface Visitor<L> {
    fun visit(x: Long, y: Long)
    fun getData(): L
}

interface VisitorFactory<L> {
    fun createVisitor(): Visitor<L>
}

val defaultVisitorFactory = object : VisitorFactory<Long> {
    override fun createVisitor(): Visitor<Long> {
        return UniqueVisitor()
    }
}

class UniqueVisitor : Visitor<Long> {
    private var data: Long = 0L
    private var visitedPos: MutableMap<Pair<Long, Long>, Long> = mutableMapOf()

    override fun visit(x: Long, y: Long) {
        val key = x to y
        if (!visitedPos.containsKey(key)) {
            data++
            visitedPos[key] = 1
        } else {
            val prev = visitedPos[key]
            visitedPos[key] = prev!! + 1
        }
    }

    override fun getData(): Long {
        return data
    }
}

class Node(var x: Long = 0, var y: Long = 0, val parent: Node? = null) {
    constructor(parent: Node) : this(0, 0, parent)
}

class Rope<L>(private val head: Node = Node(), private var size: Long, private val visitorFactory: VisitorFactory<L>) {

    private var tails = mutableListOf<Node>()
    private var visitors = mutableListOf<Visitor<L>>()

    constructor(visitor: VisitorFactory<L>) : this(Node(), 1, visitor)
    constructor(size: Long, visitor: VisitorFactory<L>) : this(Node(), size, visitor)

    init {
        size = if (size < 0) { 0 } else { size }
        tails.add(Node(head))
        visitors.add(visitorFactory.createVisitor())
        var previous: Node = tails[0]
        (2..size).forEach { _ ->
            previous = Node(previous)
            tails.add(previous)
            visitors.add(visitorFactory.createVisitor())
        }
    }

    fun move(dir: Direction, numMoves: Long) {
        for (move in 1..numMoves) {
            moveHead(dir)
            val tailsIterator = tails.iterator()
            val visitorsIterator = visitors.iterator()
            while (tailsIterator.hasNext() && visitorsIterator.hasNext()) {
                moveTail(tailsIterator.next(), visitorsIterator.next())
            }
        }
    }

    fun getLastData(): L? {
        return visitors.last().getData()
    }

    private fun moveHead(dir: Direction) {
        when (dir) {
            Direction.LEFT -> head.x -= 1
            Direction.UP -> head.y += 1
            Direction.RIGHT -> head.x += 1
            Direction.DOWN -> head.y -= 1
            Direction.NONE -> Unit
        }
    }

    private fun moveTail(tail: Node, visitor: Visitor<L>) {
        // Check distance from head and move accordingly :
        // - If adjacent, do nothing
        // - If head 2 steps up, down, left or right, move to reduce distance to 1 step :
        //      * If same row, either move left or right depending on your current x (x > head_x ? move left : move right)
        //      * If same column, either move up or down depending on your current y (y > head_y ? move down : move up)
        //      * If not same row, nor column, move left/right and up/down to remain at distance
        if (!isAdjacent(tail)) {
            tail.x = updatePos(tail.parent!!.x, tail.x, tail.parent.y, tail.y)
            tail.y = updatePos(tail.parent.y, tail.y, tail.parent.x, tail.x)
        }
        visitor.visit(tail.x, tail.y)
    }

    private fun isAdjacent(tail: Node): Boolean {
         return ((abs(tail.parent!!.x - tail.x) <= 1) && (abs(tail.parent.y - tail.y) <= 1))
    }

    private fun updatePos(headPos: Long, tailPos: Long, headTargetPos: Long, tailTargetPos: Long): Long {
        var r = tailPos
        if (headPos > tailPos) {
            r = if (headTargetPos == tailTargetPos) { headPos -1 } else { tailPos +  1}
        } else if (headPos != tailPos) {
            r = if (headTargetPos == tailTargetPos) { headPos + 1 } else { tailPos - 1 }
        }
        return r
    }
}

fun moveRope(input: List<String>, size: Long = 1): Long {
    val rope = Rope(size, defaultVisitorFactory)
    for (move in input) {
        val (d, n) = move.split(" ")
        rope.move(Direction.from(d), n.toLong())
    }
    return rope.getLastData()?:0L
}

fun part1(input: List<String>): Long {
    return moveRope(input)
}

fun part2(input: List<String>): Long {
    return moveRope(input, 9)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09/Day09_test")
    println(part1(testInput))
    println(part2(testInput))
}
