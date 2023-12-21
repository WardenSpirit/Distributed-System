package console

import node.Node

abstract class ConsoleCommand {

    /**
     * @return true if the command was executed, false otherwise
     */
    fun executeIfValid(node: Node, commandText: String): Boolean {
        if (isValidInput(commandText)) {
            execute(node, commandText)
            return true
        }
        return false
    }

    protected abstract fun execute(node: Node, commandText: String)
    protected abstract fun isValidInput(commandText: String): Boolean
}