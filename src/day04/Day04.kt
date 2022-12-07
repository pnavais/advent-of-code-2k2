package day04

import readInput


private fun overlapsTotally(
    firstRangeDef: LongRange,
    secondStart: Long,
    secondEnd: Long,
    secondRangeDef: LongRange,
    firstStart: Long,
    firstEnd: Long
): Boolean {
    return (firstRangeDef.contains(secondStart) && firstRangeDef.contains(secondEnd)) ||
                (secondRangeDef.contains(firstStart) && secondRangeDef.contains(firstEnd))
}

private fun overlapsPartially(
    firstRangeDef: LongRange,
    secondStart: Long,
    secondEnd: Long,
    secondRangeDef: LongRange,
    firstStart: Long,
    firstEnd: Long
): Boolean {
    return (firstRangeDef.contains(secondStart) || firstRangeDef.contains(secondEnd)) ||
            (secondRangeDef.contains(firstStart) || secondRangeDef.contains(firstEnd))
}

fun checkOverlaps(input: List<String>, comparator: (
    firstRangeDef: LongRange,
    secondStart: Long,
    secondEnd: Long,
    secondRangeDef: LongRange,
    firstStart: Long,
    firstEnd: Long
) -> Boolean): Long {
    var totalOverlaps = 0L

    for (rangePair in input) {
        val (firstRange, secondRange) = rangePair.split(",")
        val (firstStart, firstEnd) = firstRange.split("-").map { s -> s.toLong() }
        val (secondStart, secondEnd) = secondRange.split("-").map { s -> s.toLong() }

        val firstRangeDef = firstStart..firstEnd
        val secondRangeDef = secondStart..secondEnd

        val overlaps = comparator(firstRangeDef, secondStart, secondEnd, secondRangeDef, firstStart, firstEnd)

        if (overlaps) {
            totalOverlaps++
        }
    }

    return totalOverlaps
}

fun part1(input: List<String>): Long {
    return checkOverlaps(input, ::overlapsTotally)
}

fun part2(input: List<String>): Long {
    return checkOverlaps(input, ::overlapsPartially)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04/Day04_test")
    println(part1(testInput))
    println(part2(testInput))
}
