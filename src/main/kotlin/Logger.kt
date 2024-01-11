import node.Address

object Logger {
    val logs: MutableMap<Address, StringBuilder> = mutableMapOf()

    var leastImportance: Mode = Mode.SILENCE_FILL
        set(value) {
            field = value
            println("Log mode changed to $value")
        }

    fun `log`(nodeAddress: Address, message: String, messageImportance: Mode) {
        if (messageImportance.ordinal >= leastImportance.ordinal)
            printOut(nodeAddress, message)
    }

    private fun printOut(nodeAddress: Address, message: String) {
        val printed: String =
            "[LOG] node: " + nodeAddress.toString() + "; at: ${Thread.currentThread().stackTrace[4]}->${Thread.currentThread().stackTrace[3]}; message:\n\t" +
                    message.replace("\n", "\n\t") + "\n"
        print(printed)
        logs.getOrPut(nodeAddress) { StringBuilder() }.append(printed)
    }

    // order sensitive
    enum class Mode {
        SILENCE_FILL, BIG_MESSAGE, RESTRICTED
    }
}