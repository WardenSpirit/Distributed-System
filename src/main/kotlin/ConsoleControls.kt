package cz.ctu.fee.dsv.semework

import node.Node
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

// copied from the example on Moodle (ConsoleHandler) and modified
class ConsoleControls(private val myNode: Node) : Runnable {
    private val reader: BufferedReader = BufferedReader(InputStreamReader(System.`in`))

    override fun run() {
        var commandline: String
        var reading = true
        do {
            print("\ncmd > ")
            try {
                commandline = reader.readLine()
                parseCommandLine(commandline)
            } catch (e: IOException) {
                System.err.println("ConsoleHandler – error in reading console input.")
                e.printStackTrace()
                reading = false
            }
        } while (reading)
        System.out.println("Closing ConsoleHandler.")
    }

    private fun parseCommandLine(commandline: String) {
        when (commandline) {
            "h" -> myNode.sendHelloToNext()
            "s" -> myNode.printStatus()
            "?" -> {
                println("? – this help")
                println("h – send Hello message to the follower (next)")
                println("s – print node status")
            }
            else ->                 // do nothing
                println("Unrecognized command.")
        }
    }
}