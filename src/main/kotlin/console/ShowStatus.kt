package console

import Logger.`                              log`
import node.Node

object ShowStatus : ConsoleCommand() {

    const val COMMAND: String = "s"

    override fun execute(node: Node, commandText: String) {
        `                              log`(node.address, "printing status: $node", Logger.Mode.RESTRICTED)
    }

    override fun isValidInput(commandText: String): Boolean {
        return commandText.trim() == COMMAND
    }
}