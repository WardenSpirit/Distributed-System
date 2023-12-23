package console

import node.Node

object Unknown : ConsoleCommand() {

    public override fun execute(node: Node, commandText: String) {
        println("\"$commandText\" is an UNKNOWN COMMAND")
        Help.executeIfValid(node, Help.COMMAND)
    }

    override fun isValidInput(commandText: String): Boolean {
        return true
    }
}
