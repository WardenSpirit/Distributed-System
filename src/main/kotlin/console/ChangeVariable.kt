package console

abstract class ChangeVariable: ConsoleCommand() {

    abstract val commandStart: String


    override fun isValidInput(commandText: String): Boolean {

        if (!commandText.trim().startsWith(commandStart)) {
            return false
        }

        val numberPart = getNumberPartFromCommand(commandText)

        return try {
            numberPart.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    protected fun getNumberPartFromCommand(commandText: String): String {
        return commandText.trim().slice(IntRange(commandStart.length, commandText.length - 1)).trim()
    }
}