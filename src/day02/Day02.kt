package day02

import readInput

fun part1(input: List<String>): Long {
    var totalSum = 0L

    for (moveDesc in input) {
        val points = when (moveDesc) {
            "A X" -> 3 + 1 // Rock vs Rock, Draw, 1 point for Rock
            "A Y" -> 6 + 2 // Rock vs Paper, Win, 2 point for Paper
            "A Z" -> 0 + 3 // Rock vs Scissors , Loss, 3 points for Scissors
            "B X" -> 0 + 1 // Paper vs Rock, Loss, 1 point for Rock
            "B Y" -> 3 + 2 // Paper vs Paper, Draw, 2 points for Paper
            "B Z" -> 6 + 3 // Paper vs Scissors, Win, 3 points for Scissors
            "C X" -> 6 + 1 // Scissors vs Rock, Win, 1 point for Rock
            "C Y" -> 0 + 2 // Scissors vs Paper, Loss, 2 points for Paper
            "C Z" -> 3 + 3 // Scissors vs Scissors, Draw, 3 points for Scissors
            else -> 0
        }
        totalSum += points
    }

    return totalSum
}

fun part2(input: List<String>): Long {
    var totalSum = 0L

    for (moveDesc in input) {
        val points = when (moveDesc) {
            "A X" -> 0 + 3 // Rock vs Scissors, Loss, 3 points for Scissors
            "A Y" -> 3 + 1 // Rock vs Rock, Draw, 1 point for Rock
            "A Z" -> 6 + 2 // Rock vs Paper, Win, 2 points for Paper
            "B X" -> 0 + 1 // Paper vs Rock, Loss, 1 point for Rock
            "B Y" -> 3 + 2 // Paper vs Paper, Draw, 2 points for Paper
            "B Z" -> 6 + 3 // Paper vs Scissors, Win, 3 points for Scissors
            "C X" -> 0 + 2 // Scissors vs Paper, Loss, 2 points for Paper
            "C Y" -> 3 + 3 // Scissors vs Scissors, Draw, 3 points for Scissors
            "C Z" -> 6 + 1 // Scissors vs Rock, Win, 1 point for Rock
            else -> 0
        }
        totalSum += points
    }

    return totalSum
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02/Day02_test")
    println(part1(testInput))
    println(part2(testInput))
}