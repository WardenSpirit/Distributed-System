package console

import node.Node

object Help : ConsoleCommand() {

    const val COMMAND = "?"

    override fun execute(node: Node, commandText: String) {
        println(
            "Commands for console\n" +
                    COMMAND + " = this help\n" +
                    ShowStatus.COMMAND + " = print the node's brief status\n" +
                    ShowDetail.COMMAND + " = print the node's detailed status\n" +
                    ShowSharedVariable.COMMAND + " = print the node's shared variable\n" +
                    ChangeVariableImmediately.commandStart + "<number> = change the value of the shared variable (add the number to it)\n" +
                    ChangeVariableSteply.commandStart + "<number> = change the value of the shared variable (add the number to it) and stop the operation step by step"
        )
    }

    override fun isValidInput(commandText: String): Boolean {
        return commandText.trim() == COMMAND
    }
}
