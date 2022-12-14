package day14

import readInput

enum class Type {
    AIR, ROCK, SAND
}

open class Element(val type: Type, var x: Int, var y: Int)
class RockElement(x: Int, y: Int) : Element(Type.ROCK, x, y)

class RockPath(val rocks: List<RockElement>)
class RockPaths(val minX: Int, val minY: Int, val paths: List<RockPath>)

typealias ElementGrid = MutableList<MutableList<Element>>

class RockGrid(val width: Int, val height: Int) {
    var refX: Int = 0
    val refY: Int = 0

    private val grid: ElementGrid = mutableListOf()

    init {
        for (y in 1..height) {
            for (x in 1..height)
        }

    }

    fun addElement(element: Element) {

    }
}

fun readScan(input: List<String>): RockGrid {
    val rockPaths = readRockPaths(input)
    val grid = RockGrid()

    for (rockPath in rockPaths.paths) {
        for (rock in rockPath.rocks) {
            grid.addElement(rock)
        }
    }

    return grid
}

private fun readRockPaths(input: List<String>): RockPaths {
    val rockPaths = mutableListOf<RockPath>()
    var minX = Integer.MAX_VALUE
    var maxX = 0
    var minY = Integer.MAX_VALUE
    var maxY = 0

    for (line in input) {
        val rockList = mutableListOf<RockElement>()
        line.split(" -> ").forEach { coord ->
            val (x, y) = coord.split(",").map { it.toInt() }
            rockList.add(RockElement(x, y))
            minX = minOf(minX, x)
            maxX = maxOf(maxX, x)
            minY = maxOf(minY, y)
            maxY = maxOf(maxY, y)
        }
        rockPaths.add(RockPath(rockList))
    }
    return RockPaths(minX, minY, rockPaths)
}

fun part1(input: List<String>): Int {
    val grid = readScan(input)
    return 0
}

fun part2(input: List<String>): Int {
    return 0
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14/Day14_min")
    println(part1(testInput))
    println(part2(testInput))
}
