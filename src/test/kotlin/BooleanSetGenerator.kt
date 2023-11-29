import kotlin.math.pow

class BooleanSetGenerator(val booleans) {
    val BOOLEANS = 5
    var invocation: Int = 0
    private val booleanSequence: Iterator<Boolean> = sequence {
        while (invocation != 2.toDouble().pow(BOOLEANS.toDouble()).toInt()) {
            val set: String = invocation.toString(2).padStart(BOOLEANS, '0')
            for (char in set) {
                yield(char == '1')
            }
            invocation++
        }
    }.iterator()

    private fun pseudoBoolean(): Boolean {
        return booleanSequence.next()
    }

    private fun getNthCipher(number: Int, n: Int): Boolean {
        return number.toString(2)[n] == '1'
    }

    fun main() {
        repeat(10) {
            repeat(BOOLEANS) {
                print(if (pseudoBoolean()) 1 else 0)
            }
            println()
        }
    }
}