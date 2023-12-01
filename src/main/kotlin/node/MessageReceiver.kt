package node

import java.rmi.RemoteException

// class named after MessageReceiver from the example project on Moodle.
class MessageReceiver(node: Node) : RegisteringRemote(node), CarvalhoRoucairol {

    override val interfaceName: String
        get() = INTERFACE_NAME

    private var myLastEntry: Int = 0                //myRq, kdy jsme do kritické sekce naposledy vstoupili
    private var maxRq: Int = 0                      // maximální hodnota žádostí, které k nám přišly
    private val requirements: MutableMap<Address, Boolean> =
        mutableMapOf()      // kterým uzlům má poslat odpověď po výstupu z kritické sekce
    private val grants: MutableMap<Address, Boolean> =
        mutableMapOf()      // kdo nám dal svolení vstoupit do kritické sekce
    private var entryPhase: EntryPhase = EntryPhase.GRANTING

    override fun requireEntry(criticalOperation: () -> Unit) {
        entryPhase = EntryPhase.AWAITING_GRANTS
        sendRequestsToRest()
        while (grants.values.contains(false)) Thread.sleep(50)      // optimizable
        entryPhase = EntryPhase.CRITICAL_SECTION
        criticalOperation()
        entryPhase = EntryPhase.GRANTING
        sendOutRequiredGrants()
    }

    private fun sendRequestsToRest() {
        myLastEntry++
        grants.filterValues { !it }.keys.forEach {
            node.contacts.getCarvalhoRoucairolProxy(it).acceptRequest(node.address, myLastEntry)
        }
    }

    private fun sendOutRequiredGrants() {
        requirements.filterValues { it }.keys.forEach {
            grants[it] = false
            requirements[it] = false
            sendGrant(it)
        }
    }

    @Throws(RemoteException::class)
    override fun acceptRequest(requester: Address, requesterLastEntry: Int) {
        maxRq = maxOf(maxRq, requesterLastEntry)

        when (entryPhase) {
            EntryPhase.AWAITING_GRANTS -> {
                if (precedes(requesterLastEntry, requester)) {
                    requirements[requester] = true
                } else if (grants[requester] == false) {
                    sendGrant(requester)
                } else {
                    respect(requester)
                }
            }
            EntryPhase.CRITICAL_SECTION -> {
                requirements[requester] = true
            }
            EntryPhase.GRANTING -> {
                sendGrant(requester)
            }
        }
    }

    private fun sendGrant(recipientAddress: Address) {
        node.contacts.getCarvalhoRoucairolProxy(recipientAddress).acceptGrant(node.address)
    }

    private fun precedes(otherLastEntry: Int, otherId: Address): Boolean =
        myLastEntry < otherLastEntry || myLastEntry == otherLastEntry && node.address > otherId


    private fun respect(respectedAddress: Address) {
        grants[respectedAddress] = false
        sendGrant(respectedAddress)
        node.contacts.getCarvalhoRoucairolProxy(respectedAddress).acceptRequest(node.address, myLastEntry)
    }

    @Throws(RemoteException::class)
    override fun acceptGrant(granter: Address) {
        grants[granter] = true
    }

    companion object {
        const val INTERFACE_NAME = "Carvalho-Roucairol"
    }

    enum class EntryPhase {
        AWAITING_GRANTS,
        CRITICAL_SECTION,
        GRANTING,
    }
}