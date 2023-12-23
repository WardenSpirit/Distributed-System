package node.algorithm

import Logger.`                              log`
import Logger.Mode.BIG_MESSAGE
import Logger.Mode.SILENCE_FILL
import node.Address
import node.Node
import node.RegisteringRemote

// class named after MessageReceiver from the example project on Moodle.
class MessageReceiver(node: Node, delayedSending: Boolean) : RegisteringRemote(node, delayedSending), RemoteCarvalhoRoucairol {

    companion object {
        private const val INTERFACE_NAME = "Carvalho-Roucairol"
    }

    override val interfaceName: String
        get() = INTERFACE_NAME

    var myLastEntry: Int = 0                // myRq, tj. kdy jsme do kritické sekce naposledy vstoupili
        private set(value) {
            `                              log`(node.address, "changing myLastEntry to $value", SILENCE_FILL)
            field = value
        }
    var maxVersion: Int = 0                      // maximální hodnota žádostí, které k nám přišly
        private set(value) {
            `                              log`(node.address, "changing maxVersion to $value", SILENCE_FILL)
            field = value
        }
    private var sharedVariable: Int = 0

    val relations: Relations = Relations(this)
    private var requestStrategy: RequestReactionStrategy = Granting()

    fun requireEntry(criticalOperation: (Int) -> Int): Int {
        requestStrategy = Requesting()

        synchronized(relations) {       // po kontrole, že grant mám, mi ho žádost nesmí sebrat
            relations.requestMissingGrantsRepeatedly(node.address, myLastEntry)
            requestStrategy = GrantsLocked()
        }

        sharedVariable = criticalOperation(sharedVariable)
        myLastEntry = myLastEntry.coerceAtLeast(maxVersion + 1)
        requestStrategy = Granting()

        synchronized(relations) {       // po kontrole, že se o grant nehlásil, se mi o něj žádost nesmí přihlásit
            relations.sendOutRequiredGrants(node.address, myLastEntry, sharedVariable)
        }
        return sharedVariable
    }

    fun requireEntrySteply(criticalOperation: (Int) -> Int): Int {
        requestStrategy = Requesting()
        waitForUserInput("Now I have stopped after changing the state to Requesting (but not requesting yet). Let me continue with a line terminator.")

        synchronized(relations) {       // po kontrole, že grant mám, mi ho žádost nesmí sebrat
            relations.requestMissingGrantsRepeatedly(node.address, myLastEntry)
            waitForUserInput("Now I have stopped having all grants available. Let me lock grants and continue with a line terminator.")
            requestStrategy = GrantsLocked()
        }

        sharedVariable = criticalOperation(sharedVariable)
        myLastEntry = myLastEntry.coerceAtLeast(maxVersion + 1)
        requestStrategy = Granting()
        waitForUserInput("Now I have stopped after the critical section. Let me send grants to others who require them with a line terminator.")

        synchronized(relations) {       // po kontrole, že se o grant nehlásil, se mi o něj žádost nesmí přihlásit, jinak ho nedostane
            relations.sendOutRequiredGrants(node.address, myLastEntry, sharedVariable)
        }
        return sharedVariable
    }

    private fun waitForUserInput(message: String) {
        println(message)
        readLine()
    }


    override fun acceptRequest(requester: Address, requesterLastEntry: Int) {
        `                              log`(node.address, "accepting request from $requester", SILENCE_FILL)
        maxVersion = maxOf(maxVersion, requesterLastEntry)
        synchronized(relations) {
            requestStrategy.reactOnRequest(requester, requesterLastEntry)
        }
    }

    override fun acceptGrant(granter: Address, granterSharedVariable: Int, granterLastEntry: Int) {
        `                              log`(
            node.address,
            "accepting grant: from $granter, its variable: $granterSharedVariable, its lastEntry - my maxVersion: $granterLastEntry-$maxVersion",
            SILENCE_FILL
        )
        synchronized(this) {
            if (maxVersion <= granterLastEntry) {
                maxVersion = granterLastEntry
                sharedVariable = granterSharedVariable
            }
        }
        relations.addGrant(granter)
    }

    fun getProxy(proxiedNode: Address): RemoteCarvalhoRoucairol? {
        return super.getProxy(proxiedNode, node.messageReceiver)
    }


    abstract inner class RequestReactionStrategy {
        abstract fun reactOnRequest(requester: Address, requesterLastEntry: Int)

        fun precedesTo(otherLastEntry: Int, otherId: Address): Boolean =
            myLastEntry < otherLastEntry || (myLastEntry == otherLastEntry && node.address > otherId)
    }

    inner class Granting : RequestReactionStrategy() {
        override fun reactOnRequest(requester: Address, requesterLastEntry: Int) {
            relations.giveGrant(node.address, requester, myLastEntry, sharedVariable)
        }
    }

    inner class Requesting : RequestReactionStrategy() {
        override fun reactOnRequest(requester: Address, requesterLastEntry: Int) {
            if (precedesTo(requesterLastEntry, requester)) {
                relations.markAsWaiting(requester)
            } else {
                relations.lendGrant(node.address, myLastEntry, requester, sharedVariable)
            }
        }
    }

    inner class GrantsLocked : RequestReactionStrategy() {
        override fun reactOnRequest(requester: Address, requesterLastEntry: Int) {
            relations.letAwaitGrant(requester)
        }
    }
}