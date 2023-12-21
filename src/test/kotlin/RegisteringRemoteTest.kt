import java.rmi.Remote
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject


fun main(args: Array<String>) {
    TestMessageReceiver().register()
    TestWebweaver().register()
}

abstract class RegisteringRemoteTest {

}

var registry: Registry? = null

class TestMessageReceiver : Remote {

    val interfaceName: String
        get() = INTERFACE_NAME
    private val nodeAddressHostname: String = "10.0.0.72"
    private val nodeAddressPort: Int = 800

    fun register() {
        System.setProperty("java.rmi.server.hostname", nodeAddressHostname)

        try {
            val stub = UnicastRemoteObject.exportObject(this, PORT_ADDEND + nodeAddressPort)
            registry = LocateRegistry.createRegistry(PORT_ADDEND + nodeAddressPort)
            registry!!.rebind(interfaceName, stub)

        } catch (e: Exception) {
            println("Chyba: " + e.message)
        }
        println("Úspěch 1!")
    }

    companion object {
        const val INTERFACE_NAME = "MessageReceiver"
        const val PORT_ADDEND: Int = 4000
    }
}

class TestWebweaver : Remote {

    val interfaceName: String
        get() = INTERFACE_NAME
    private val nodeAddressHostname: String = "127.0.0.72"
    private val nodeAddressPort: Int = 800

    fun register() {
        System.setProperty("java.rmi.server.hostname", nodeAddressHostname)

        try {
            val stub = UnicastRemoteObject.exportObject(this, PORT_ADDEND + nodeAddressPort)
            //val registry = LocateRegistry.createRegistry(PORT_ADDEND + nodeAddressPort)
            registry!!.rebind(interfaceName, stub)

        } catch (e: Exception) {
            println("Chyba: " + e.message)
        }
        println("Úspěch 2!")
    }

    companion object {
        const val INTERFACE_NAME = "Webweaver"
        const val PORT_ADDEND: Int = 4000
    }
}