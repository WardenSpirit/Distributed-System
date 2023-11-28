package node

import java.rmi.NotBoundException
import java.rmi.Remote
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry

class Contacts(private val node: Node) {
    val nodesInWeb: MutableMap<Long, Address> = mutableMapOf(Pair(node.id, node.address))

    // method copied from the example on Moodle
    @Throws(RemoteException::class)
    fun getCarvalhoRoucairolProxy(proxiedNode: Address): CarvalhoRoucairol {
        return getProxy(proxiedNode, node.messageReceiver, MessageReceiver.INTERFACE_NAME) as CarvalhoRoucairol
    }

    @Throws(RemoteException::class)
    fun getWebweaverProxy(proxiedNode: Address): Webweaver {
        return getProxy(proxiedNode, node.webweaver, Webweaver.INTERFACE_NAME) as Webweaver
    }

    private fun getProxy(proxiedNode: Address, localInterface: Remote, interfaceName: String) : Remote {
        if (proxiedNode == node.address) return localInterface

        try {
            val registry = LocateRegistry.getRegistry(proxiedNode.hostname, proxiedNode.port)
            return registry.lookup(interfaceName)
        } catch (nbe: NotBoundException) {
            // transitive RM exception
            throw RemoteException(nbe.message)
        }
    }

    @Throws(RemoteException::class)
    fun getCarvalhoRoucairolProxy(proxiedNode: Long): CarvalhoRoucairol {
        return getCarvalhoRoucairolProxy(nodesInWeb.getValue(proxiedNode))
    }

    @Throws(RemoteException::class)
    fun getWebweaverProxy(proxiedNode: Long): Webweaver {
        return getWebweaverProxy(nodesInWeb.getValue(proxiedNode))
    }
}