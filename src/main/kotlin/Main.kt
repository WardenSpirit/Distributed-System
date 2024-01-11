import node.Address
import node.Node

val addresses = arrayOf(
    Address("127.0.0.70", 1070),
    Address("127.0.0.71", 1071),
    Address("127.0.0.72", 1072),
    Address("127.0.0.73", 1073),
    Address("127.0.0.74", 1074),
    Address("127.0.0.75", 1075)
)

fun main(args: Array<String>) {
    val localAndJoinedAddress: Array<Address> = getAddressesFromArgs(args)
    val delay: Boolean?
    delay = args[args.size - 1].contains("s")
    Node(localAndJoinedAddress[0], localAndJoinedAddress[1], delay).run()
}

/**
 * Returns an array of 2 addresses. Behaviour depends on whether the first arg contains dot (.) character.
 */
fun getAddressesFromArgs(args: Array<String>): Array<Address> {
    lateinit var localAddress: Address
    lateinit var joinedAddress: Address
    if (args[0].contains(".")) {
        try {
            localAddress = Address(args[0], Integer.parseInt(args[1]))
        } catch (e: NumberFormatException) {
            println("The second program argument doesn't work!")
        }
        try {
            joinedAddress = Address(args[2], Integer.parseInt(args[3]))
        } catch (e: NumberFormatException) {
            println("The fourth program argument doesn't work!")
        }
    } else {
        try {
            localAddress = addresses[Integer.parseInt(args[0])]
        } catch (e: NumberFormatException) {
            println("The first program argument doesn't work!")
        }
        try {
            joinedAddress = addresses[Integer.parseInt(args[1])]
        } catch (e: NumberFormatException) {
            println("The second program argument doesn't work!")
        }
    }
    return arrayOf(localAddress, joinedAddress)
}