package day03

import readInput

fun computePriority(item: Char): Short {
    var itemId = item.code.toShort()
    itemId = if (itemId >= 'a'.code.toShort() && itemId <= 'z'.code.toShort()) {
        itemId.minus('a'.code.toShort() - 1).toShort()
    } else if (itemId >= 'A'.code.toShort() && itemId <= 'Z'.code.toShort()) {
        itemId.minus('A'.code.toShort() - 27).toShort()
    } else {
        0
    }

    return itemId
}

fun part1(input: List<String>): Long {
    var totalPriorities = 0L

    for (ruckSackSpec in input) {
        val firstRuckSack = ruckSackSpec.substring(0, ruckSackSpec.length / 2)
        val secondRuckSack = ruckSackSpec.substring(ruckSackSpec.length / 2)
        var commonItem = Character.MIN_VALUE
        for (item in secondRuckSack.toCharArray()) {
            if (firstRuckSack.indexOf(item) != -1) {
                commonItem = item
                break
            }
        }
        totalPriorities += computePriority(commonItem)
    }

    return totalPriorities
}

fun part2(input: List<String>): Long {
    var totalPriorities = 0L

    for (i in input.indices step 3) {
        var commonItem = Character.MIN_VALUE
        val firstRuckSack = input[i]
        val secondRuckSack = input[i + 1]
        val thirdRuckSack = input[i + 2]
        for (item in firstRuckSack.toCharArray()) {
            if ((secondRuckSack.indexOf(item) != -1) && (thirdRuckSack.indexOf(item) != -1)) {
                commonItem = item
                break
            }
        }
        totalPriorities += computePriority(commonItem)
    }

    return totalPriorities
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    println(part1(testInput))
    println(part2(testInput))
}
