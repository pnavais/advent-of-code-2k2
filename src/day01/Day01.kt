fun main() {

    fun part1(input: List<String>): Pair<Long, Long> {

        var maxElf = 1L
        var currentElf = 1L
        var currentSum = 0L
        var maxSum = -1L

        for (value in input) {
            if (value.isNotEmpty()) {
                currentSum += value.toLong()
            } else {
                if (currentSum > maxSum) {
                    maxSum = currentSum
                    maxElf = currentElf
                }
                currentSum = 0
                currentElf++
            }
        }

        return maxElf to maxSum
    }

    fun part2(input: List<String>): Long {

        var currentElf = 1L
        var currentSum = 0L

        val totalList = mutableListOf<Long>();

        for (value in input) {
            if (value.isNotEmpty()) {
                currentSum += value.toLong()
            } else {
                totalList.add(currentSum)
                currentSum = 0
                currentElf++
            }
        }

        return totalList.sortedDescending().toMutableList().subList(0,3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01/Day01_test")
    println(part1(testInput))
    println(part2(testInput))
}
