package console

import node.Node

object ChangeVariableImmediately : ChangeVariable() {

    override val commandStart = "c "

    override fun execute(node: Node, commandText: String) {
        val addend = getNumberPartFromCommand(commandText).toInt()
        node.messageReceiver.requireEntry { sharedVariable -> sharedVariable + addend }
    }
}