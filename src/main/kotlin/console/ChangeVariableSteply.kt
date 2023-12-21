package console

import Logger.`                              log`
import node.Node

object ChangeVariableSteply : ChangeVariable() {

    override val commandStart = "cs "

    override fun execute(node: Node, commandText: String) {
        val addend = getNumberPartFromCommand(commandText).toInt()
        node.messageReceiver.requireEntrySteply { sharedVariable ->
            `                              log`(node.address, "Now I have stopped inside the critical section after loading sharedVariable, before changing it. Shut me down or let me do my job by a line terminator.", Logger.Mode.BIG_MESSAGE)
            readLine()
            sharedVariable + addend
        }
    }
}