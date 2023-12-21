package node.algorithm

import node.Address
import java.rmi.Remote
import java.rmi.RemoteException

interface RemoteCarvalhoRoucairol : Remote {
    @Throws(RemoteException::class)
    fun acceptRequest(requester: Address, requesterLastEntry: Int)
    @Throws(RemoteException::class)
    fun acceptGrant(granter: Address, granterSharedVariable: Int, granterLastEntry: Int)
}
