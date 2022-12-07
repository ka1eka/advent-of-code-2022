fun main() {
    abstract class Entry(val name: String, val children: MutableList<Entry>? = null) {
        abstract val size: Long
        override fun toString(): String = name
    }

    class Directory(name: String) : Entry(name, mutableListOf()) {
        override val size: Long
            get() = children!!.sumOf { it.size }
    }

    class File(name: String, override val size: Long) : Entry(name)

    fun cd(pwd: Directory, target: String): Directory {
        return pwd.children!!.find { it.name == target } as Directory
    }

    fun parseOutput(root: Directory, dirs: MutableList<Directory>, input: List<String>) {
        val path = ArrayDeque<Directory>()
        var listing = false
        for (line in input) {
            if (listing) {
                if (line.startsWith("\$")) {
                    listing = false
                } else {
                    if (line.startsWith("dir ")) {
                        val dirName = line.substring(4)
                        if (path.first().children?.find { it.name == dirName } == null) {
                            val dir = Directory(dirName)
                            path.first().children!!.add(dir)
                            dirs.add(dir)
                        }
                    } else {
                        line.split(' ')
                            .run {
                                val fileName = this[1]
                                if (path.first().children?.find { it.name == fileName } == null) {
                                    val file = File(fileName, this[0].toLong())
                                    path.first().children!!.add(file)
                                }
                            }
                    }
                }
            }
            if (!listing) {
                if (line == "\$ cd /") {
                    path.clear()
                    path.addFirst(root)
                } else if (line == "\$ cd ..") {
                    path.removeFirst()
                } else if (line.startsWith("\$ cd ")) {
                    val pwd = cd(path.first(), line.substring(5))
                    path.addFirst(pwd)
                }else if (line == "\$ ls") {
                    listing = true
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        val root = Directory("/")
        val dirs: MutableList<Directory> = mutableListOf()
        parseOutput(root, dirs, input)

        return dirs
            .asSequence()
            .filter { it.size <= 100000 }
            .sumOf { it.size }
    }

    fun part2(input: List<String>): Long {
        val root = Directory("/")
        val dirs: MutableList<Directory> = mutableListOf()
        parseOutput(root, dirs, input)
        val remainingSpace = 70000000 - root.size
        val requiredSpace = 30000000 - remainingSpace


        return dirs
            .asSequence()
            .filter { it.size >= requiredSpace }
            .minOf { it.size }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437L)
    check(part2(testInput) == 24933642L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
