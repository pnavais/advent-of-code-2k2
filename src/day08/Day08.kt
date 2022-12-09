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
    var visible = true
    val height = grid[x][y].height
    var rY = y - 1
    var numTrees = 0L
    while (rY >= 0) {
        numTrees++
        if (grid[x][rY].height >= height) {
            visible = false
            break
        }
        rY--
    }
    val currentScore = grid[x][y].scenicScore
    grid[x][y].scenicScore = if (currentScore <=0 ) { numTrees } else { currentScore * numTrees }
    return visible
}

private fun checkHigherRight(x: Int, y: Int, grid: Grid): Boolean {
    var visible = true
    val height = grid[x][y].height
    var rY = y + 1
    var numTrees = 0L
    while (rY < grid[x].size) {
        numTrees++
        if (grid[x][rY].height >= height) {
            visible = false
            break
        }
        rY++
    }
    val currentScore = grid[x][y].scenicScore
    grid[x][y].scenicScore = if (currentScore <=0 ) { numTrees } else { currentScore * numTrees }
    return visible
}

private fun checkHigherUp(x: Int, y: Int, grid: Grid): Boolean {
    var visible = true
    val height = grid[x][y].height
    var rX = x - 1
    var numTrees = 0L
    while (rX >= 0) {
        numTrees++
        if (grid[rX][y].height >= height) {
            visible = false
            break
        }
        rX--
    }
    val currentScore = grid[x][y].scenicScore
    grid[x][y].scenicScore = if (currentScore <=0 ) { numTrees } else { currentScore * numTrees }
    return visible
}

private fun checkHigherDown(x: Int, y: Int, grid: Grid): Boolean {
    var visible = true
    val height = grid[x][y].height
    var rX = x + 1
    var numTrees = 0L
    while (rX < grid.size) {
        numTrees++
        if (grid[rX][y].height >= height) {
            visible = false
            break
        }
        rX++
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
