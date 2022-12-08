package day08

import readInput

typealias Grid = MutableList<List<TreeInfo>>

class TreeInfo(val height: Short, var visible: Boolean)

fun buildGrid(input: List<String>): Grid {
    val grid = mutableListOf<List<TreeInfo>>()
    for (line in input) {
        grid.add(line.toCharArray().map { c ->
            val height = Character.getNumericValue(c).toShort()
            val visible = true
            TreeInfo(height, visible)
        }.toList())
    }

    return grid
}

fun computeVisibility(grid: Grid): Long {
    var innerVisible = 0L
    for (x in 1 until grid.size - 1) {
        for (y in 1 until grid[x].size - 1) {
            val visible = checkHigherLeft(x, y, grid)
                    || checkHigherRight(x, y, grid)
                    || checkHigherUp(x, y, grid)
                    || checkHigherDown(x, y, grid)

            grid[x][y].visible = visible

            if (visible) {
                innerVisible++
            }
        }
    }
    return innerVisible
}

private fun checkHigherLeft(x: Int, y: Int, grid: Grid): Boolean {
    var visible = true
    val height = grid[x][y].height
    var rY = y - 1
    while (rY >= 0) {
        if (grid[x][rY].height >= height) {
            visible = false
            break
        }
        rY--
    }
    return visible
}

private fun checkHigherRight(x: Int, y: Int, grid: Grid): Boolean {
    var visible = true
    val height = grid[x][y].height
    var rY = y + 1
    while (rY < grid[x].size) {
        if (grid[x][rY].height >= height) {
            visible = false
            break
        }
        rY++
    }
    return visible
}

private fun checkHigherUp(x: Int, y: Int, grid: Grid): Boolean {
    var visible = true
    val height = grid[x][y].height
    var rX = x - 1
    while (rX >= 0) {
        if (grid[rX][y].height >= height) {
            visible = false
            break
        }
        rX--
    }
    return visible
}

private fun checkHigherDown(x: Int, y: Int, grid: Grid): Boolean {
    var visible = true
    val height = grid[x][y].height
    var rX = x + 1
    while (rX < grid.size) {
        if (grid[rX][y].height >= height) {
            visible = false
            break
        }
        rX++
    }
    return visible
}

fun part1(input: List<String>): Long {
    val grid = buildGrid(input)
    var totalVisible = ((grid.size - 2) * 2).toLong() + (grid[0].size * 2).toLong()
    totalVisible += computeVisibility(grid)
    return totalVisible
}

fun part2(input: List<String>): Long {
    return 0L
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08/Day08_test")
    println(part1(testInput))
    println(part2(testInput))
}
