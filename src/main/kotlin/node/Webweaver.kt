package node

import node.Logger.log
import java.rmi.RemoteException

class Webweaver(node: Node) : RegisteringRemote(node) {

    override val interfaceName
        get() = INTERFACE_NAME

    /**
     * Repair variables if you find out that another node is not responding.
     */
    @Throws(RemoteException::class)
    fun repairMissing(missing: Address) {
        TODO("not implemented yet")
    }

    /**
     * Initialize the contacts in both invoking this and argument nodes, i.e. join the web this is part of.
     */
    @Throws(RemoteException::class)
    fun join(joiningAddress: Address): MutableList<Address> {

        if (joiningAddress == node.address) {
            log(node.address, "I am the first and only node in this web", Logger.Mode.BIG_MESSAGE)

        } else {
            log(node.address, "Someone is joining ...", Logger.Mode.BIG_MESSAGE)

            node.contacts.nodesInWeb.add(joiningAddress)
        }
        return node.contacts.nodesInWeb
    }


    companion object {
        const val INTERFACE_NAME = "Webweaver"
    }
}