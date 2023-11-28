package node

import java.rmi.RemoteException

class Webweaver(node: Node) : RegisteringRemote(node) {

    override val interfaceName: String
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
    fun join(joiningId: Long, joiningAddress: Address): MutableMap<Long, Address> {

        if (joiningId == node.id) {
            log(node.id, "I am the first and only node in this web", Mode.BIG_MESSAGE)
            return node.contacts.nodesInWeb
        }

        log(node.id, "Someone is joining ...", Mode.BIG_MESSAGE)
        node.contacts.nodesInWeb[joiningId] = joiningAddress
        return node.contacts.nodesInWeb
    }


    companion object {
        const val INTERFACE_NAME = "Webweaver"
    }
}