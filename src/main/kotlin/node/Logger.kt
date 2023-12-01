package node

object Logger {
    val logs: MutableMap<Address, StringBuilder> = mutableMapOf()

    var leastImportance: Mode = Mode.BIG_MESSAGE
        set(value) {
            field = value
            println("Log mode changed to $value")
        }

    fun log(nodeAddress: Address, message: String, messageImportance: Mode) {
        if (messageImportance == Mode.BIG_MESSAGE || leastImportance == Mode.SILENCE_FILL) {
            printOut(nodeAddress, "\n" + message + "\n")
        } else {
            printOut(nodeAddress, ".")
        }
    }

    private fun printOut(nodeAddress: Address, message: String) {
        val printed: String =
            Thread.currentThread().stackTrace[1].toString() + "\t" + String.format("% 20d", nodeAddress) + message + "\n"
        print(printed)
        logs.getOrPut(nodeAddress) { StringBuilder() }.append(printed)
    }

    enum class Mode {
        SILENCE_FILL, BIG_MESSAGE
    }
}