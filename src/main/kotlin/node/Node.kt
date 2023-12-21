package node

import Logger.`                              log`
import console.ConsoleControls
import node.algorithm.MessageReceiver

class Node(val address: Address, private val toJoin: Address, delayedSending: Boolean = false) : Runnable {

    val messageReceiver: MessageReceiver = MessageReceiver(this, delayedSending)
    val webweaver: Webweaver = Webweaver(this, delayedSending)

    init {
        messageReceiver.register()
        webweaver.register()
        `                              log`(address, "node created", Logger.Mode.SILENCE_FILL)
    }

    // the structure of this method is copied from the example on Moodle
    override fun run() {
        if (address != toJoin) {
            webweaver.joinWeb(toJoin)
        } else {
            `                              log`(address, "I feel alone, the address to join is my own.", Logger.Mode.SILENCE_FILL)
        }
        Thread(ConsoleControls(this)).start()
    }

    override fun toString(): String {
        return "node{address=$address; contacts=${messageReceiver.relations.mappings.keys.size}}"
    }

    fun toDetailedString(): String {
        val contacts = StringBuilder()
        contacts.append("\t")
        messageReceiver.relations.mappings.keys.forEach { contacts.append(it).append(",\n\t") }
        val algorithmData =
            "\tmyLastEntry=" + messageReceiver.myLastEntry + ";\n" +
                    "\tmaxVersion=" + messageReceiver.maxVersion + ";\n" +
                    "\trelations=" + messageReceiver.relations

                    return "node{\n" +
                            "\taddress=$address;\n" +
                            "\tcontacts=${messageReceiver.relations.mappings.keys.size}:\n" +
                            "\t${contacts};\n" +
                            "$algorithmData}"
    }
}