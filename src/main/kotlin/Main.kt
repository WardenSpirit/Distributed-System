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
    if (args.size !in 2..3) throw IllegalArgumentException()
    try {
        val localAddress = addresses[Integer.parseInt(args[0])]
        val joinedAddress = addresses[Integer.parseInt(args[1])]
        val delay: Boolean = args.size == 3 && (args[2] == "-s" || args[2] == "slow")
        Node(localAddress, joinedAddress, delay).run()
    } catch (e: NumberFormatException) {
        println("These program arguments don't work!")
    }
}