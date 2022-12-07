package Day03

import readInput

fun main() {

    fun computePriority(item : Char) : Short {
        var itemId = item.code.toShort()
        itemId = if (itemId >= 'a'.code.toShort() && itemId <= 'z'.code.toShort()) {
            itemId.minus('a'.code.toShort()-1).toShort()
        } else if (itemId >= 'A'.code.toShort() && itemId <= 'Z'.code.toShort()) {
            itemId.minus('A'.code.toShort()-27).toShort()
        } else {
            0
        }

        return itemId
    }

    fun part1(input: List<String>) : Long {
        var totalPriorities = 0L

        for (ruckSackSpec in input) {
            val firstRuckSack = ruckSackSpec.substring(0, ruckSackSpec.length/2)
            val secondRuckSack = ruckSackSpec.substring(ruckSackSpec.length/2)
            var commonItem = Character.MIN_VALUE
            for (item in secondRuckSack.toCharArray()) {
                if (firstRuckSack.indexOf(item) != -1) {
                    commonItem = item
                    break
                }
            }
            val priority = computePriority(commonItem)
            totalPriorities+=priority
        }

        return totalPriorities
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03/Day03_test")
    println(part1(testInput))
    println(part2(testInput))
}
