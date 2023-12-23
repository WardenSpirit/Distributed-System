package console

import node.Node

object ShowDetail : ConsoleCommand() {

    const val COMMAND = "d"

    override fun execute(node: Node, commandText: String) {
        println("Printing detail:\n${node.toDetailedString()}")
    }

    override fun isValidInput(commandText: String): Boolean {
        return commandText.trim() == COMMAND
    }
}
