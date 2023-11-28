package node

data class Address(val hostname: String, val port: Int) {
    constructor(copied: Address) : this(copied.hostname, copied.port)
}