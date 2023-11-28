package node

val logs: MutableMap<Long, StringBuilder> = mutableMapOf()

var leastImportance: Mode = Mode.BIG_MESSAGE
    set(value) {
        field = value
        println("Log mode changed to " + value)
    }

fun log(nodeId: Long, message: String, messageImportance: Mode) {
    if (messageImportance == Mode.BIG_MESSAGE || leastImportance == Mode.SILENCE_FILL) {
        printOut(nodeId, "\n" + message + "\n")
    } else {
        printOut(nodeId, ".")
    }
}

fun printOut(nodeId: Long, message: String) {
    val printed: String = Thread.currentThread().stackTrace[1].toString() + "\t" + String.format("%09d", nodeId) + message + "\n"
    print(printed)
    logs.getOrPut(nodeId) { StringBuilder() }.append(printed)
}

enum class Mode {
    SILENCE_FILL, BIG_MESSAGE
}
