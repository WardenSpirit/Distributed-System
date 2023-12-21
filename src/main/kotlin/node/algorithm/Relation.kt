package node.algorithm

data class Relation(
    var awaitingRequirement: Boolean,      // zda má poslat odpověď po výstupu z kritické sekce
    var providingGrant: Boolean             // zda nám dal svolení vstoupit do kritické sekce
)