package node

import java.rmi.Remote
import java.rmi.RemoteException

interface CarvalhoRoucairol : Remote {
    fun requireEntry(criticalOperation: () -> Unit)
    @Throws(RemoteException::class)
    fun acceptRequest(requester: Address, requesterLastEntry: Int)
    @Throws(RemoteException::class)
    fun acceptGrant(granter: Address)
}