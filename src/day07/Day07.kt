package day07

import readInput

private const val TOTAL_DISK_SIZE: Long = 70000000
private const val REQUIRED_UNUSED_SPACE: Long = 30000000
private const val MAX_SIZE_LIMIT: Int = 100000

class File(val name: String, val size: Long) {
}

class Directory(val name: String, val parent: Directory?) {
    val subDirectories = mutableMapOf<String, Directory>()
    val files = mutableListOf<File>()

    fun computeSize(): Long {
        var size = files.sumOf { f -> f.size }
        for (dir in subDirectories.values) {
            size += dir.computeSize()
        }
        return size
    }
    fun getAllDirs(): List<Directory> {
        val dirList = mutableListOf<Directory>()
        dirList.add(this)
        for (d in subDirectories.values) {
            dirList.addAll(d.getAllDirs())
        }
        return dirList
    }
}

class DirTree {
    var root: Directory = Directory("/", null)
    var currentDir: Directory = root

    fun getAllDirs(): List<Directory> {
        return root.getAllDirs()
    }
}

fun buildTree(input: List<String>): DirTree {
    val dirTree = DirTree()
    for (line in input) {
        if (line.startsWith("$ cd ")) {
            // Prepare directory
            val dir = line.removePrefix("$ cd ")
            if (dir == "/") {
                dirTree.currentDir = dirTree.root
            } else if (dir == "..") {
                dirTree.currentDir = dirTree.currentDir.parent!!
            } else {
                dirTree.currentDir = dirTree.currentDir.subDirectories[dir]!!
            }
        } else if (!line.startsWith("$")) {
            // Read files and dirs
            if (line.startsWith("dir ")) {
                val dir = line.removePrefix("dir ")
                dirTree.currentDir.subDirectories[dir] = Directory(dir, dirTree.currentDir)
            } else {
                val (size, name) = line.split(" ")
                dirTree.currentDir.files.add(File(name, size.toLong()))
            }
        }
    }
    dirTree.currentDir = dirTree.root
    return dirTree
}

fun part1(input: List<String>): Long  {
    val dirTree = buildTree(input)
    val totalSize = dirTree.getAllDirs().filter { directory ->
        directory.computeSize() <= MAX_SIZE_LIMIT
    }.sumOf { d -> d.computeSize() }

    return totalSize
}

fun part2(input: List<String>): Long  {
    val dirTree = buildTree(input)
    val neededSpace = REQUIRED_UNUSED_SPACE - (TOTAL_DISK_SIZE - dirTree.root.computeSize())
    return dirTree.getAllDirs().map { d -> d.computeSize() }.filter { s ->
        s >= neededSpace
    }.minOf { it }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07/Day07_test")
    println(part1(testInput))
    println(part2(testInput))
}
