package console

import Logger.`                              log`
import node.Node

object ShowSharedVariable : ConsoleCommand() {

    const val COMMAND = "v"

    override fun execute(node: Node, commandText: String) {
        `                              log`(node.address,
            "the value of the shared variable: ${node.messageReceiver.requireEntry { sharedVariable -> sharedVariable }}", Logger.Mode.RESTRICTED)
    }

    override fun isValidInput(commandText: String): Boolean {
        return commandText.trim() == COMMAND
    }
}
