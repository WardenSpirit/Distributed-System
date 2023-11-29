import node.Address
import node.Node

fun main(args: Array<String>) {

    Node(Address(args[0], Integer.parseInt(args[1])), Address(args[2], Integer.parseInt(args[3]))).run()
}