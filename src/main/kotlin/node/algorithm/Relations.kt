package node.algorithm

import Logger.`                              log`
import node.Address

class Relations(private val messageReceiver: MessageReceiver) {
    val mappings: MutableMap<Address, Relation> = mutableMapOf()

    fun requestMissingGrantsRepeatedly(myAddress: Address, myLastEntry: Int) {
        while (requestMissingGrants(
                myAddress,
                myLastEntry
            ) > 0
        ) {       // optimizable: glutting the web with requests to every node we don't have grant from, set should decrease on size
            `                              log`(messageReceiver.node.address, "I am missing some grants.", Logger.Mode.SILENCE_FILL)
            Thread.sleep(250)
        }
        `                              log`(messageReceiver.node.address, "I have grants from everyone.", Logger.Mode.SILENCE_FILL)
    }

    fun requestMissingGrants(myAddress: Address, myLastEntry: Int): Int {
        // optimizable: Když dostaneme žádost od uzlu s předností a pak zjistíme, že od něj nemáme grant předtím, než dostaneme odpověď s grantem, pošleme mu zde žádost zbytečně ještě jednou. (Grant mít true/false/pending)*/
        var missing = 0
        mappings.filterValues { !it.providingGrant }.keys.forEach {
            messageReceiver.getProxy(it)?.acceptRequest(myAddress, myLastEntry)
            missing++
        }
        return missing
    }

    fun sendOutRequiredGrants(myAddress: Address, myLastEntry: Int, mySharedVariable: Int) {
        mappings.filterValues { it.awaitingRequirement }.keys.forEach { address ->
            giveGrant(address, myAddress, myLastEntry, mySharedVariable)
        }
    }

    fun giveGrant(myAddress: Address, recipientAddress: Address, myLastEntry: Int, mySharedVariable: Int) {
        mappings[recipientAddress]?.let {
            messageReceiver.getProxy(recipientAddress)?.acceptGrant(myAddress, mySharedVariable, myLastEntry)
            it.providingGrant = false
            it.awaitingRequirement = false
        }
    }

    fun addIncomingMapping(added: Address) {
        mappings[added] = Relation(awaitingRequirement = false, providingGrant = true)
    }

    fun addPresentMapping(added: Address) {
        mappings[added] = Relation(awaitingRequirement = false, providingGrant = false)
    }

    fun eraseKey(missing: Address) {
        mappings.remove(missing)
    }

    fun addGrant(granter: Address) {
        mappings[granter]?.providingGrant = true
    }

    fun markAsWaiting(requester: Address) {
        mappings[requester]?.awaitingRequirement = true
    }

    fun lendGrant(myAddress: Address, myLastEntry: Int, requester: Address, mySharedVariable: Int) {
        mappings[requester]?.let {
            if (it.providingGrant) {
                giveGrant(
                    myAddress,
                    requester,
                    myLastEntry,
                    mySharedVariable
                )        // optimizable: if not requested while iterating the requester's grant, requesting twice
                messageReceiver.getProxy(requester)?.acceptRequest(myAddress, myLastEntry)
            } else {
                giveGrant(myAddress, requester, myLastEntry, mySharedVariable)
            }
        }
    }

    fun letAwaitGrant(requester: Address) {
        mappings[requester]?.awaitingRequirement = true
    }


    override fun toString(): String {
        val text = StringBuilder().append(mappings.size).append("{\n")
        mappings.forEach { (address, relation) ->
            text.append("\n\t").append(address).append(" -> ")
                .append("grant=").append(relation.providingGrant).append(", ")
                .append("awaitingRequirement=").append(relation.awaitingRequirement)
        }
        text.append("}")
        return text.toString()
    }
}
