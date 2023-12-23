package console

import node.Node

object ShowStatus : ConsoleCommand() {

    const val COMMAND: String = "s"

    override fun execute(node: Node, commandText: String) {
        println("Printing status: $node")
    }

    override fun isValidInput(commandText: String): Boolean {
        return commandText.trim() == COMMAND
    }
}