# Carvalho-Roucairol
- modifikace Ricart-Agrawaly
    - když chceme vstoupit do kritické sekce, čekáme na odpovědi typu "za mě můžeš" od ostatních
    - umožňuje uzlu vstoupit do kritické sekce opakovaně, pokud mezitím nedostal požadavek od jiného
    - druhy zpráv: žádost a odpověď
- proměnné:
    - myRq: int – kdy jsme byli naposledy v kritické sekci
    - maxRq: int – maximální hodnota žádostí, které k nám přišly
    - inUse: boolean – zda se nacházíme v kritické sekci
    - req: boolean\[\] – kterým uzlům má poslat odpověď po výstupu z kritické sekce
    - grants: kdo nám dal svolení vstoupit do kritické sekce
- algoritmus:
    - žádost:
        1. nastavíme true na svém indexu v poli req
        2. nastavíme myRq na maxRq + 1
        3. všem uzlům, které nám nedali svolení, pošleme žádost s myRq (a indexem)
        4. čekáme, než ode všech dostaneme svolení (vše v poli grant je true)
        5. nastavíme false na svém indexu v poli req
        6. nastavíme inUse na true
        7. vstoupíme do kritické sekce
        8. nastavíme inUse na false
        9. u každého uzlu, který má v req true, si nastavíme svolení v poli grant na false a req na false a pošleme mu odpověď
    - přijetí žádosti:
        1. inicializujeme maxRq na maximum z dosavadního maxRq a hodnotu uvedenou v žádosti
        2. pokud čekáme na svolení ke vstupu do kritické sekce (req\[i\]):
        - máme-li přednost, tedy myRq je menší než hodnota v žádosti (nebo se rovná, ale máme lepší id), nastavíme req\[j\] na true
        3. pokud nejsme v kritické sekci (inUse) ani nečekáme na svolení (req\[i\]), nebo (čekáme, nemáme svolení od odesílatele žádosti a nemáme přednost):
        - pošleme mu odpověď
        4. pokud čekáme na svolení ke vstupu do kritické sekce (req\[i\]):
        - máme-li od odesílatele svolení a nemáme-li před ním přednost, nastavíme jeho svolení na false a pošleme mu odpověď a pak i žádost
    - přijetí odpovědi:
        - nastaví si, že od odesílatele máme svolení (true v poli grant)

# Zadání
ME - Ca-Ro - Kotlin RMI - shared variable