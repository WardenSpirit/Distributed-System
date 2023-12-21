package node

import java.rmi.Remote
import java.rmi.RemoteException

interface Joinable : Remote {
    @Throws(RemoteException::class)
    fun interchangeContacts(joiningAddress: Address, joiningContacts: HashSet<Address>): HashSet<Address>
}