package console

import node.Node

object ShowSharedVariable : ConsoleCommand() {

    const val COMMAND = "v"

    override fun execute(node: Node, commandText: String) {
        println("The value of the shared variable: ${node.messageReceiver.requireEntry { sharedVariable -> sharedVariable }}")
    }

    override fun isValidInput(commandText: String): Boolean {
        return commandText.trim() == COMMAND
    }
}
