package day14

import readInput
import kotlin.math.max
import kotlin.math.min

enum class Type(val c: Char) {
    AIR('.'), ROCK('#'), SAND('+'), SAND_STALLED('o');

    override fun toString(): String {
        return c.toString()
    }
}

enum class Direction {
    DOWN, DOWN_LEFT, DOWN_RIGHT
}

open class Element(val type: Type, open var x: Int, open var y: Int) {
    override fun toString(): String {
        return "${type},[$x,$y]"
    }
}

data class SandElement(override var x: Int = 500, override var y: Int = 0) : Element(Type.SAND, x, y)
data class RockElement(override var x: Int, override var y: Int) : Element(Type.ROCK, x, y)
data class GridDef(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int)
typealias ElementGrid = MutableList<MutableList<Element>>

class RockPath(val rocks: List<RockElement>)
class RockPaths(val paths: List<RockPath>)

class RockGrid(private val gridDef: GridDef) {

    private val grid: ElementGrid = mutableListOf()

    init {
        val width = gridDef.maxX - gridDef.minX + 1
        for (y in 0 .. gridDef.maxY) {
            val row = mutableListOf<Element>()
            for (x in 0 until width) {
                row.add(Element(Type.AIR, x, y))
            }
            grid.add(row)
        }
    }

    fun dropSand(): Int {
        var outOfBounds = false
        var counter = 0
        while (!outOfBounds) {
            var keepFalling = true
            val currentSand = SandElement()
            addElement(currentSand)
            while (keepFalling && !outOfBounds) {
                // Check next drop position from down, down-left, down-right
                with(tryMove(currentSand, Direction.DOWN)) {
                    keepFalling = first
                    outOfBounds = second
                }
                if (!keepFalling && !outOfBounds) {
                    with(tryMove(currentSand, Direction.DOWN_LEFT)) {
                        keepFalling = first
                        outOfBounds = second
                    }
                }
                if (!keepFalling && !outOfBounds) {
                    with(tryMove(currentSand, Direction.DOWN_RIGHT)) {
                        keepFalling = first
                        outOfBounds = second
                    }
                }
            }
            val elementType = if (!outOfBounds) { Type.SAND_STALLED } else { Type.AIR }
            if (elementType == Type.SAND_STALLED) {
                counter++
            }
            grid[currentSand.y][currentSand.x] = Element(elementType, currentSand.x, currentSand.y)
        }
        return counter
    }

    private fun tryMove(sand: SandElement, direction: Direction) : Pair<Boolean, Boolean> {
        var canMove = true
        var outOfBounds = false
        var y = 0
        var x = sand.x

        if (sand.y + 1 < grid.size) {
            y = sand.y + 1
            if ((direction == Direction.DOWN_LEFT) && (x - 1 >= 0)) {
                x -= 1
            } else if ((direction == Direction.DOWN_RIGHT) && (x + 1 < grid[sand.y].size)) {
                x += 1
            } else if (direction != Direction.DOWN) {
                canMove  = false
                outOfBounds = true
            }
        } else {
            canMove = false
            //outOfBounds = (x - 1 < 0) || (x + 1 > grid[sand.y].size)
            outOfBounds = true
        }

        if (canMove && (grid[y][x].type == Type.AIR)) {
            canMove = true
            swapElements(sand, grid[y][x])
        } else {
            canMove = false
        }

        return canMove to outOfBounds
    }

    private fun swapElements(firstElement: Element, secondElement: Element) {
        val origX = firstElement.x
        val origY = firstElement.y

        grid[origY][origX] = secondElement
        grid[secondElement.y][secondElement.x] = firstElement
        firstElement.x = secondElement.x
        firstElement.y = secondElement.y

        secondElement.x = origX
        secondElement.y = origY
    }

    fun addElement(element: Element) {
        val normalizedX = element.x - gridDef.minX
        element.x = if (normalizedX >= 0) { normalizedX } else { element.x }
        grid[element.y][element.x] = element
    }

    fun debug() {
        val maxLength = (grid.size-1).toString().length
        for (y in 0 until grid.size) {
            print("${y.toString().padStart(maxLength)} ")
            for (x in 0 until grid[y].size) {
                print(grid[y][x].type)
            }
            println()
        }
    }

    fun countElements(type: Type): Int {
        var sum = 0
        for (y in 0 until grid.size) {
            sum+= grid[y].count { it.type == type }
        }
        return sum
    }
}

fun readScan(input: List<String>): RockGrid {
    val scanData = readRockPaths(input)
    val grid = RockGrid(scanData.second)

    for (rockPath in scanData.first.paths) {
        for (i in 0 until rockPath.rocks.size - 1) {
            val start = rockPath.rocks[i]
            val end = rockPath.rocks[i+1]
            grid.addElement(start)
            grid.addElement(end)
            addRockPath(start, end, grid, (start.x == end.x))
        }
    }

    return grid
}

private fun addRockPath(start: RockElement, end: RockElement, grid: RockGrid, isHorizontal: Boolean) {
    val startPos = if (isHorizontal) { start.y } else { start.x }
    val endPos = if (isHorizontal) { end.y } else { end.x }
    for (childPos in min(startPos, endPos) until max(startPos, endPos)) {
        val x = if (isHorizontal) { start.x } else { childPos }
        val y = if (isHorizontal) { childPos } else { start.y }
        grid.addElement(RockElement(x, y))
    }
}

private fun readRockPaths(input: List<String>): Pair<RockPaths, GridDef> {
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
            minX = min(minX, x)
            maxX = max(maxX, x)
            minY = min(minY, y)
            maxY = max(maxY, y)
        }
        rockPaths.add(RockPath(rockList))
    }
    return RockPaths(rockPaths) to GridDef(minX, minY, maxX, maxY)
}

fun part1(input: List<String>): Int {
    val grid = readScan(input)
    val counter = grid.dropSand()
    grid.debug()
    return grid.countElements(Type.SAND_STALLED)
}

fun part2(input: List<String>): Int {
    return 0
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14/Day14_test")
    println(part1(testInput))
    println(part2(testInput))
}
