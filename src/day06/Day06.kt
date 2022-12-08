package day06

import readInput

private fun readPackets(input: List<String>, maxLength: Int): List<Pair<String, Long>> {
    return input.map { s ->
        var idx = 0L
        val charSet = HashSet<Char>()
        var packetMarker = mutableListOf<Char>()
        for (c in s.toCharArray()) {
            idx++
            if (charSet.contains(c)) {
                packetMarker = packetMarker.subList(packetMarker.indexOf(c)+1, packetMarker.size)
                charSet.clear()
                packetMarker.forEach(charSet::add)
            }
            packetMarker.add(c)
            charSet.add(c)
            if (packetMarker.size == maxLength) {
                break
            }
        }
        s to idx
    }.toList()
}

fun part1(input: List<String>): List<Pair<String, Long>> {
    return readPackets(input, 4)
}

fun part2(input: List<String>): List<Pair<String, Long>>  {
    return readPackets(input, 14)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06/Day06_test")
    println(part1(testInput))
    println(part2(testInput))
}
