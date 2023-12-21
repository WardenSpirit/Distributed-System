package console

import node.Node

object Unknown : ConsoleCommand() {

    public override fun execute(node: Node, commandText: String) {
        Logger.`                              log`(node.address, "\"$commandText\" is an UNKNOWN COMMAND", Logger.Mode.BIG_MESSAGE)
        Help.executeIfValid(node, Help.COMMAND)
    }

    override fun isValidInput(commandText: String): Boolean {
        return true
    }
}
