package node

import Logger.`log`

class Webweaver(node: Node, delayedSending: Boolean) : RegisteringRemote(node, delayedSending), Joinable {

    private companion object {
        private const val INTERFACE_NAME = "Webweaver"
    }

    override val interfaceName
        get() = INTERFACE_NAME

    fun joinWeb(toJoin: Address) {
        disconnectFromAllMissing()
        getProxy(toJoin, this)?.let { proxyToJoin ->
            val receivedContacts: HashSet<Address> = proxyToJoin.interchangeContacts(node.address, node.messageReceiver.relations.mappings.keys.toHashSet())
            val contactsNewToMe = receivedContacts.plus(toJoin).minus(node.address).subtract(node.messageReceiver.relations.mappings.keys).toSet()
            spreadMyContactsToAll(contactsNewToMe)
            contactsNewToMe.forEach { node.messageReceiver.relations.addPresentMapping(it) }
            `log`(node.address, "Attempt to add $toJoin to contacts was successful.", Logger.Mode.SILENCE_FILL)
        }
    }

    /**
     * Adds joiningContacts in this (remote) node and returns this node's contacts for the node with joiningAddress to add them too.
     * @return all the nodes this node knew about right before adding the joining node.
     */
    override fun interchangeContacts(joiningAddress: Address, joiningContacts: HashSet<Address>): HashSet<Address> {
        `log`(node.address, "$joiningAddress joining me (${node.address})", Logger.Mode.BIG_MESSAGE)
        disconnectFromAllMissing()
        val formerOthersInWeb: HashSet<Address> = node.messageReceiver.relations.mappings.keys.toHashSet()
        node.messageReceiver.relations.addIncomingMapping(joiningAddress)
        joiningContacts.forEach { node.messageReceiver.relations.addIncomingMapping(it) }
        return formerOthersInWeb
    }

    private fun disconnectFromAllMissing() {
        node.messageReceiver.relations.mappings.keys.forEach {
            getProxy(it, this)
        }
    }

    private fun spreadMyContactsToAll(recipients: Set<Address>) {
        recipients.forEach {
            getProxy(it, this)?.interchangeContacts(node.address, node.messageReceiver.relations.mappings.keys.toHashSet())
        }
    }

    fun disconnectFromMissing(missing: Address) {
        `log`(
            node.address,
            "Message could not be sent to $missing. Disconnecting.",
            Logger.Mode.BIG_MESSAGE
        )
        node.messageReceiver.relations.eraseKey(missing)
    }
}