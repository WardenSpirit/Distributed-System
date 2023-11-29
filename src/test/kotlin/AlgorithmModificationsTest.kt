import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

class AlgorithmModificationsTest {
    val booleans = BooleanSetGenerator(x)

    var step = -1
    val reqs: Array<Array<Boolean>> = arrayOf(
        arrayOf(true, Random.nextBoolean(), Random.nextBoolean()),
        arrayOf(true, Random.nextBoolean(), Random.nextBoolean()),
        arrayOf(true, Random.nextBoolean(), Random.nextBoolean()),
        arrayOf(true, Random.nextBoolean(), Random.nextBoolean()),
    )
    val myRqs: Array<Int> = arrayOf()
    val maxRqs: Array<Int> = arrayOf()
    val grants: Array<Array<Boolean>> = arrayOf()
    val inUses: Array<Boolean> = arrayOf()

    @BeforeEach
    fun setup() {
        // Arrange
        step++
        req = reqs[step]
        myRq = myRqs[step]
        maxRq = maxRqs[step]
        grant = grants[step]
        inUse = inUses[step]
    }

    @Test
    fun testRequestReceipt(step: Int) {
        // Act
        val result: Boolean = didSendResponse()

        // Assert
        assertFalse("Optional failure message", result)
    }

    private fun didSendResponse(): Boolean {
        maxRq = maxOf(maxRq, requesterLastEntry)
        if (requirements[node.id] == true) {
            if (isPrecedence(myLastEntry, requesterLastEntry, node.id, requesterId)) {
                requirements[requesterId] = true
            }
        }
        if ((!inUse && requirements[node.id] != true) ||
            (requirements[node.id] == true && grants[requesterId] != true &&
                    !isPrecedence(myLastEntry, requesterLastEntry, node.id, requesterId))
        ) node.contacts.getCarvalhoRoucairolProxy(requesterId).acceptGrant(node.id)
        if (requirements[node.id] == true) {
            if (grants[requesterId] == true &&
                !isPrecedence(myLastEntry, requesterLastEntry, node.id, requesterId)
            ) {
                grants[requesterId] = false
                node.contacts.getCarvalhoRoucairolProxy(requesterId).acceptGrant(node.id)
                node.contacts.getCarvalhoRoucairolProxy(requesterId).acceptRequest(node.id, myLastEntry)
            }
        }
    }

}