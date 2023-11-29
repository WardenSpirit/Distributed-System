import kotlin.math.pow
import kotlin.system.exitProcess

class BooleanSetGenerator(val booleans: Int) {
    private var invocation: Int = 0

    private val booleanSequence: Iterator<Boolean> = sequence {
        while (invocation != 2.toDouble().pow(booleans.toDouble()).toInt()) {
            val set: String = invocation.toString(2).padStart(booleans, '0')
            for (char in set) {
                yield(char == '1')
            }
            invocation++
        }
        println("Byl přesáhnut počet stavů.")
        exitProcess(1)
    }.iterator()

    fun pseudoBoolean(): Boolean {
        return booleanSequence.next()
    }
}