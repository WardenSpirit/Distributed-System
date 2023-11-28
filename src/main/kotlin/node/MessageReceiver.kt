package node

import java.rmi.RemoteException

// class named after MessageReceiver from the example project on Moodle.
class MessageReceiver(node: Node) : RegisteringRemote(node), CarvalhoRoucairol {

    override val interfaceName: String
        get() = Webweaver.INTERFACE_NAME

    private var myLastEntry: Int = 0                //myRq, kdy jsme do kritické sekce naposledy vstoupili
    private var maxRq: Int = 0                      // maximální hodnota žádostí, které k nám přišly
    private var inUse: Boolean = false              // zda se nacházíme v kritické sekci
    private val requirements: MutableMap<Long, Boolean> =
        mutableMapOf()      // kterým uzlům má poslat odpověď po výstupu z kritické sekce
    private val grants: MutableMap<Long, Boolean> =
        mutableMapOf()      // kdo nám dal svolení vstoupit do kritické sekce


    @Throws(RemoteException::class)
    override fun requireEntry(operation: () -> Unit) {
        requirements[node.id] = true
        myLastEntry++
        grants.filterValues { !it }.keys.forEach {
            node.contacts.getCarvalhoRoucairolProxy(it).acceptRequest(node.id, myLastEntry)
        }
        while (grants.values.contains(false)) Thread.sleep(50)
        requirements[node.id] = false
        //co když zde dostaneme žádost?
        inUse = true
        operation()
        inUse = false
        requirements.filterValues { it }.keys.forEach {
            grants[it] = false
            requirements[it] = false
            node.contacts.getCarvalhoRoucairolProxy(it).acceptGrant(node.id)
        }
    }

    @Throws(RemoteException::class)
    override fun acceptRequest(requesterId: Long, requesterLastEntry: Int) {
        maxRq = maxOf(maxRq, requesterLastEntry)
        if (requirements[node.id] == true) {
            if (isPrecedence(myLastEntry, requesterLastEntry, node.id, requesterId)) {
                requirements[requesterId] = true
            }
        }
        if ((!inUse && requirements[node.id] != true) ||
            (requirements[node.id] == true && grants[requesterId] != true &&
                    !isPrecedence(myLastEntry, requesterLastEntry, node.id, requesterId))
        ) node.contacts.getCarvalhoRoucairolProxy(requesterId).acceptGrant(node.id)
        if (requirements[node.id] == true) {
            if (grants[requesterId] == true &&
                !isPrecedence(myLastEntry, requesterLastEntry, node.id, requesterId)
            ) {
                grants[requesterId] = false
                node.contacts.getCarvalhoRoucairolProxy(requesterId).acceptGrant(node.id)
                node.contacts.getCarvalhoRoucairolProxy(requesterId).acceptRequest(node.id, myLastEntry)
            }
        }
    }

    private fun isPrecedence(myLastEntry: Int, otherLastEntry: Int, myId: Long, otherId: Long): Boolean =
        myLastEntry < otherLastEntry || myLastEntry == otherLastEntry && myId > otherId

    @Throws(RemoteException::class)
    override fun acceptGrant(granterId: Long) {
        grants[granterId] = true
    }


    companion object {
        const val INTERFACE_NAME = "Carvalho-Roucairol"
    }
}