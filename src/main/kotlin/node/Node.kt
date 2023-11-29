package node

import cz.ctu.fee.dsv.semework.ConsoleControls
import node.Logger.log
import java.rmi.RemoteException
import kotlin.system.exitProcess

class Node(val address: Address, private val toJoin: Address) : Runnable {
    val id: Long = generateId(address.hostname, address.port)

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
            val newContacts: Map<Long, Address> = contacts.getWebweaverProxy(toJoin).join(id, address)
            contacts.nodesInWeb.clear()
            contacts.nodesInWeb.putAll(newContacts)
        } catch (e: RemoteException) {
            e.printStackTrace()
            exitProcess(1)
        }

        log(id, "Contacts after joining: $contacts", Logger.Mode.SILENCE_FILL)
        Thread(ConsoleControls(this)).start()
    }
}

//copied from the example on Moodle and modified
private fun generateId(myIP: String, port: Int): Long {
    // generates ((((<port> * 256) + <IPv4_dec1> * 256) + <IPv4_dec2> * 256) + <IPv4_dec3> * 256) + <IPv4_dec4>
    val array = myIP.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    var id: Long = 0
    var temp: Long
    for (s in array) {
        try {
            temp = s.toLong()
            id = id * 2 + temp
        } catch (e: NumberFormatException) {
            // Handle the case where parsing fails (invalid IPv4 address)
            id = 666000666000L
            break
        }
    }
    id += port * 1000000000000L
    return id
}