package console

import node.Node
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

// copied from the example on Moodle (ConsoleHandler) and modified
class ConsoleControls(private val localNode: Node) : Runnable {
    private val reader: BufferedReader = BufferedReader(InputStreamReader(System.`in`))

    override fun run() {
        var commandline: String
        var reading = true
        do {
            print("\nComManD>")
            try {
                commandline = reader.readLine()
                parseCommandLine(commandline)
            } catch (e: IOException) {
                System.err.println("ConsoleHandler: error in reading console input.")
                e.printStackTrace()
                reading = false
            }
        } while (reading)
        println("Closing ConsoleHandler.")
    }

    private fun parseCommandLine(commandText: String) {
        if (!Help.executeIfValid(localNode, commandText) &&
            !ShowStatus.executeIfValid(localNode, commandText) &&
            !ShowDetail.executeIfValid(localNode, commandText) &&
            !ShowSharedVariable.executeIfValid(localNode, commandText) &&
            !ChangeVariableImmediately.executeIfValid(localNode, commandText) &&
            !ChangeVariableSteply.executeIfValid(localNode, commandText)
        ) {
            Unknown.execute(localNode, commandText)
        }
    }
}