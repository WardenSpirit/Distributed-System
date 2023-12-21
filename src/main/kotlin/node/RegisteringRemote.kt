package node

import Logger
import Logger.`                              log`
import node.algorithm.RemoteCarvalhoRoucairol
import java.rmi.Remote
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject


abstract class RegisteringRemote(val node: Node, val delayedSending: Boolean) : Remote {

    private companion object {
        var localRegistry: Registry? = null
        var systemHostnameSet: Boolean = false
    }

    abstract val interfaceName: String

    // copied from the example project on Moodle and modified
    fun register() {
        if (!systemHostnameSet) {
            System.setProperty("java.rmi.server.hostname", node.address.hostname)       // optimizable: must be done only once
        }

        try {
            val skeletalStub = UnicastRemoteObject.exportObject(this, node.address.port)
            localRegistry = localRegistry ?: LocateRegistry.createRegistry(node.address.port)
            localRegistry?.rebind(interfaceName, skeletalStub) ?: throw RegistryCouldNotBeBoundException("The local registry was null!")
`                              log`(
                node.address,
                "$interfaceName successfully bound by registry at ${node.address.hostname}:${node.address.port}.",
                Logger.Mode.SILENCE_FILL
            )
        } catch (e: Exception) {
            `                              log`(
                node.address,
                "Something is wrong with Java RMI – $interfaceName:\n\t" + e.message,
                Logger.Mode.BIG_MESSAGE
            )
        }
    }

    // method copied from the example on Moodle
    protected fun getProxy(proxiedNode: Address, localInterface: Joinable): Joinable? {
        if (delayedSending) Thread.sleep(3000)      // cleanable: move this feature to another class

        if (proxiedNode == node.address) return localInterface

        return try {
            val remoteRegistry = LocateRegistry.getRegistry(proxiedNode.hostname, proxiedNode.port)
            val proxyToReturn = remoteRegistry.lookup(interfaceName) as Joinable
            `                              log`(
                node.address,
                "proxyToReturn is initialized: $proxyToReturn",
                Logger.Mode.BIG_MESSAGE
            )
            return proxyToReturn
        } catch (e: Exception) {
            // transitive RM exception
            `                              log`(
                node.address,
                "connecting finished with exception: ${e.message}",
                Logger.Mode.BIG_MESSAGE
            )
            node.webweaver.disconnectFromMissing(proxiedNode)
            null
        }
    }


    protected fun getProxy(proxiedNode: Address, localInterface: RemoteCarvalhoRoucairol): RemoteCarvalhoRoucairol? {
        if (delayedSending) Thread.sleep(3000)      // cleanable: move this feature to another class

        if (proxiedNode == node.address) return localInterface

        return try {
            val remoteRegistry = LocateRegistry.getRegistry(proxiedNode.hostname, proxiedNode.port)
            val proxyToReturn = remoteRegistry.lookup(interfaceName) as RemoteCarvalhoRoucairol
            `                              log`(
                node.address,
                "proxyToReturn is initialized: $proxyToReturn",
                Logger.Mode.BIG_MESSAGE
            )
            return proxyToReturn
        } catch (e: Exception) {
            // transitive RM exception
            `                              log`(
                node.address,
                "connecting finished with exception: ${e.message}",
                Logger.Mode.BIG_MESSAGE
            )
            node.webweaver.disconnectFromMissing(proxiedNode)
            null
        }
    }
}

class RegistryCouldNotBeBoundException(message: String) : Exception(message)
