package console

import Logger.`                              log`
import node.Node

object ShowDetail : ConsoleCommand() {

    const val COMMAND = "d"

    override fun execute(node: Node, commandText: String) {
        `                              log`(node.address, "printing detail:\n${node.toDetailedString()}", Logger.Mode.RESTRICTED)
    }

    override fun isValidInput(commandText: String): Boolean {
        return commandText.trim() == COMMAND
    }
}
