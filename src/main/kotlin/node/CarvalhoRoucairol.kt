package node

import java.rmi.Remote
import java.rmi.RemoteException

interface CarvalhoRoucairol : Remote {
    @Throws(RemoteException::class)
    fun requireEntry(operation: () -> Unit)
    @Throws(RemoteException::class)
    fun acceptRequest(requesterId: Long, requesterLastEntry: Int)
    @Throws(RemoteException::class)
    fun acceptGrant(granterId: Long)
}