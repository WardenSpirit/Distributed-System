package node

import node.Logger.log
import java.rmi.Remote
import java.rmi.registry.LocateRegistry
import java.rmi.server.UnicastRemoteObject

abstract class RegisteringRemote(val node: Node) : Remote {

    abstract val interfaceName: String

    /**
     * Create the skeleton and registry bind it with the specified interface name using the new registry.
     * Before this method is called, the node's id and address must be fully (i.e. deeply) initialized.
     */
    // this block is copied from the example project on Moodle and modified.
    fun register() {
        System.setProperty("java.rmi.server.hostname", node.address.hostname)

        try {
            val skeleton = UnicastRemoteObject.exportObject(this, PORT_ADDEND + node.address.port)
            val registry =
                LocateRegistry.createRegistry(node.address.port)        // Create registry and (re)register object name and skeleton in it
            registry.rebind(interfaceName, skeleton)
        } catch (e: Exception) {
            log(node.address, "$interfaceName – something is wrong: " + e.message, Logger.Mode.BIG_MESSAGE)
        }

        log(node.address, "$interfaceName is started", Logger.Mode.SILENCE_FILL)
    }

    companion object {
        const val PORT_ADDEND: Int = 40000
    }
}