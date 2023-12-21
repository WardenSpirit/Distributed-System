import java.rmi.Remote
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject


val address = Address("127.0.0.1", 800)
val interfaceName = "funny"
var localRegistry: Registry? = null

fun main(args: Array<String>) {
        val tested = Registeringie()
        tested.register()
        println("I have registered...")
        val tested2 = Registeringie()
        println("Now I will try to get my own proxy...")
        println("The secret message is:\n\t${tested2.getProxy().sayHello()}")
        println("the end")
}

interface MyRemote : Remote {
    @Throws(RemoteException::class)
    fun sayHello(): String
}

class Registeringie : MyRemote {
    fun register() {
        System.setProperty("java.rmi.server.hostname", address.hostname)

        val skeletonStub = UnicastRemoteObject.exportObject(this, 0) as MyRemote
        localRegistry = localRegistry ?: LocateRegistry.createRegistry(address.port)
        localRegistry?.rebind(interfaceName, skeletonStub) ?: run { throw RegistryCouldNotBeBoundException("The local registry was null!") }
    }

    fun getProxy(): MyRemote {
        val remoteRegistry: Registry = LocateRegistry.getRegistry(address.hostname, address.port)
        println("remoteRegistry: $remoteRegistry")
        return remoteRegistry.lookup(interfaceName) as MyRemote
    }

    override fun sayHello(): String {
        return "Hello, world!"
    }
}

class RegistryCouldNotBeBoundException(message: String) : Exception(message)

data class Address(val hostname: String, val port: Int) : Comparable<Address> {
    override fun compareTo(other: Address): Int {
        if (hostname > other.hostname) {
            return 1
        }
        if (hostname < other.hostname) {
            return -1
        }
        return port.compareTo(other.port)
    }

    override fun toString(): String {
        return "$hostname:$port"
    }
}