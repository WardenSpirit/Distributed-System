package node

import cz.ctu.fee.dsv.semework.ConsoleControls
import node.Logger.log
import java.rmi.RemoteException
import kotlin.system.exitProcess

class Node(val address: Address, private val toJoin: Address) : Runnable {
    val messageReceiver: MessageReceiver = MessageReceiver(this)
    val webweaver: Webweaver = Webweaver(this)
    val contacts: Contacts = Contacts(this)

    init {
        messageReceiver.register()
        webweaver.register()
    }

    // the structure of this method is copied from the example on Moodle
    override fun run() {
        try {
            val newContacts: MutableList<Address> = contacts.getWebweaverProxy(toJoin).join(address)
            contacts.nodesInWeb.clear()
            contacts.nodesInWeb.addAll(newContacts)
        } catch (e: RemoteException) {
            e.printStackTrace()
            exitProcess(1)
        }

        log(address, "Contacts after joining: $contacts", Logger.Mode.SILENCE_FILL)
        Thread(ConsoleControls(this)).start()
    }
}