import node.Address
import node.Node

val first = Address("127.0.0.71", 800)
val second = Address("127.0.0.72", 800)
val third = Address("127.0.0.73", 800)
val fourth = Address("127.0.0.74", 800)
val fifth = Address("127.0.0.75", 800)

fun main(args: Array<String>) {
    if (args.size !in 4..5) throw IllegalArgumentException()

    val localAddress = Address(args[0], Integer.parseInt(args[1]))
    val joinedAddress = Address(args[2], Integer.parseInt(args[3]))
    val delay: Boolean = args.size == 5 && (args[4] == "-s" || args[4] == "slow")

    Node(localAddress, joinedAddress, delay).run()
}