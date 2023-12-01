package node

data class Address(val hostname: String, val port: Int) : Comparable<Address> {
    constructor(copied: Address) : this(copied.hostname, copied.port)

    override fun compareTo(other: Address): Int {
        if (hostname > other.hostname) {
            return 1
        }
        if (hostname < other.hostname) {
            return -1
        }
        return port.compareTo(other.port)
    }
}