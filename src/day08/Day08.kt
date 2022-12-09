package day08

import readInput
import kotlin.math.max

typealias Grid = MutableList<List<TreeInfo>>

class TreeInfo(val height: Short, var visible: Boolean, var scenicScore: Long)

fun buildGrid(input: List<String>): Grid {
    val grid = mutableListOf<List<TreeInfo>>()
    for (line in input) {
        grid.add(line.toCharArray().map { c ->
            val height = Character.getNumericValue(c).toShort()
            val visible = true
            TreeInfo(height, visible, -1L)
        }.toList())
    }

    return grid
}

fun computeVisibility(grid: Grid): Pair<Long, Long> {
    var innerVisible = 0L
    var maxScenicScore = -1L
    for (x in 1 until grid.size - 1) {
        for (y in 1 until grid[x].size - 1) {
            val visibleLeft = checkHigherLeft(x, y, grid)
            val visibleRight = checkHigherRight(x, y, grid)
            val visibleUp = checkHigherUp(x, y, grid)
            val visibleDown = checkHigherDown(x, y, grid)

            grid[x][y].visible = visibleLeft || visibleUp || visibleDown || visibleRight

            if (grid[x][y].visible) {
                innerVisible++
            }
            maxScenicScore = max(maxScenicScore, grid[x][y].scenicScore)
        }
    }
    return innerVisible to maxScenicScore
}

private fun checkHigherLeft(x: Int, y: Int, grid: Grid): Boolean {
    return checkHigherMove(x, y, grid, { _, b -> 0 to b - 1 }, { _, rY, _ -> rY >= 0 }, {a, _, _, rB, g, h  -> g[a][rB].height >= h}, { _, rY -> 0 to rY - 1 })
}

private fun checkHigherRight(x: Int, y: Int, grid: Grid): Boolean {
    return checkHigherMove(x, y, grid, { _, b -> 0 to b + 1 }, { _, rY, g -> rY < g[x].size }, {a, _, _, rB, g, h  -> g[a][rB].height >= h}, { _, rY -> 0 to rY + 1 })
}

private fun checkHigherUp(x: Int, y: Int, grid: Grid): Boolean {
    return checkHigherMove(x, y, grid, { a, _ -> a - 1 to 0 }, { rX, _, _ -> rX >= 0 }, { _, b, rA, _, g, h  -> g[rA][b].height >= h }, { rX, _ -> rX - 1 to 0 })
}

private fun checkHigherDown(x: Int, y: Int, grid: Grid): Boolean {
    return checkHigherMove(x, y, grid, { a, _ -> a + 1 to 0 }, { rX, _, g -> rX < g.size }, { _, b, rA, _, g, h  -> g[rA][b].height >= h }, { rX, _ -> rX + 1 to 0})
}

private fun checkHigherMove(x: Int, y: Int, grid: Grid,
                            initializer: (x: Int, y: Int) -> Pair<Int, Int>,
                            checker: (rX: Int, rY: Int, grid: Grid) -> Boolean,
                            visibleChecker: (x: Int, y: Int, rX: Int, rY: Int, grid: Grid, height: Short) -> Boolean,
                            incrementer: (rX: Int, rY: Int) -> Pair<Int, Int>): Boolean {
    var visible = true
    val height = grid[x][y].height
    var (rX, rY) = initializer(x, y)
    var numTrees = 0L
    while (checker(rX, rY, grid)) {
        numTrees++
        if (visibleChecker(x, y, rX, rY, grid, height)) {
            visible = false
            break
        }
        incrementer(rX, rY).let {
            rX = it.first
            rY = it.second
        }
    }
    val currentScore = grid[x][y].scenicScore
    grid[x][y].scenicScore = if (currentScore <=0 ) { numTrees } else { currentScore * numTrees }
    return visible
}

fun part1(input: List<String>): Long {
    val grid = buildGrid(input)
    var totalVisible = ((grid.size - 2) * 2).toLong() + (grid[0].size * 2).toLong()
    totalVisible += computeVisibility(grid).first
    return totalVisible
}

fun part2(input: List<String>): Long {
    val grid = buildGrid(input)
    return computeVisibility(grid).second
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08/Day08_test")
    println(part1(testInput))
    println(part2(testInput))
}
