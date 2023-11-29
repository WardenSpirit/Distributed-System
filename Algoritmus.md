# Carvalho-Roucairol
- modifikace Ricart-Agrawaly
    - když chceme vstoupit do kritické sekce, čekáme na odpovědi typu "za mě můžeš" od ostatních
    - umožňuje uzlu vstoupit do kritické sekce opakovaně, pokud mezitím nedostal požadavek od jiného
    - druhy zpráv: žádost a odpověď
- proměnné:
    - myRq: int – kdy jsme byli naposledy obesílali ostatní s žádostmi o vstup do kritické sekce
    - maxRq: int – maximální hodnota žádostí, které k nám přišly
    - inUse: boolean – zda se nacházíme v kritické sekci
    - req: boolean\[\] – kterým uzlům má poslat odpověď po výstupu z kritické sekce
    - grant: kdo nám dal svolení vstoupit do kritické sekce
- algoritmus:
    - vznik žádosti:
        1. nastavíme true na svém indexu v poli req
        2. nastavíme myRq na maxRq + 1
        3. všem uzlům, které nám nedali svolení, pošleme žádost – s myRq a indexem
        4. čekáme, než ode všech dostaneme svolení (vše v poli grant je true)
        5. nastavíme false na svém indexu v poli req // a co když v tuto chvíli dostaneme žádost?
        6. nastavíme inUse na true
        7. vstoupíme do kritické sekce
        8. nastavíme inUse na false
        9. u každého uzlu, který má v req true, si nastavíme svolení v poli grant na false a req na false a pošleme mu odpověď
    - přijetí žádosti:
        1. inicializujeme maxRq na maximum z dosavadního maxRq a hodnoty uvedené v žádosti
        2. pokud:
           - jsme v kritické sekci,
           - nebo čekáme na svolení ke vstupu do kritické sekce (req\[i\]) a zároveň máme přednost (tzn. myRq je menší než hodnota v žádosti (nebo se rovná, ale máme lepší id)):
               - nastavíme req\[j\] na true
        3. pokud:
           - nejsme v kritické sekci (inUse) ani nečekáme na svolení (req\[i\]),
           - nebo (čekáme, nemáme svolení od odesílatele žádosti a nemáme přednost):
             - pošleme mu odpověď
        4. pokud:
           - čekáme na svolení (req\[i\]),
           - máme-li od odesílatele svolení
           - a nemáme-li před ním přednost:
             - nastavíme jeho svolení na false a pošleme mu odpověď a pak i žádost (s myRq a indexem)
    - přijetí odpovědi:
        - nastaví si, že od odesílatele máme svolení (true v poli grant)

## Co když tehdy dostaneme žádost?
Náš stav:
- <code>req[localId]</code> je <code>false</code>
- nepodstatné: <code>myRq</code> je <code>maxRq + 1</code>
- ode všech uzlů jsme dostali svolení
  - v poli <code>grant</code> jsou jen <code>true</code>
- <code>inUse</code> je <code>false</code>

Následně po přijetí žádosti:
- nepodstatné: maxRq nastaveno na max(maxRq, hodnota v žádosti)
- <code>req[localId]</code> je <code>false</code> a <code>inUse</code> je <code>false</code> takže posíláme odpověď (svolení)

### Navrhovaná oprava
- když se rozhodneme (prakticky alespoň těsně předtím), že máme všechny granty, už nesmíme přijímat žádosti
- všechny granty si zamkneme proti odebrání (nastavení na false) předtím, co zjišťujeme, zda je máme, ale proti přidání (nastavení na true) je necháme odemčené.
  - granty odemkneme poté, co opustíme kritickou sekci (už před inUse?)
  - zamkneme to tak, že si kód vzniku žádosti rozdělíme na části, při jejichž vykonávání se kód žádosti usměrní do příslušné větve
    - pro to je třeba zavést novou proměnnou namísto req[localId] a inUse (např. enumeraci)

## Kontrola, zda větve přijetí žádosti zachycují všechny možné stavy
- definující proměnné jsou: inUse, req[localId], přednost a grant[senderId] (0000 až 1111)
- pokrytí větví:
  1. 1xxx, x11x (projde už jen 011x)
  2. 00xx, x100 (projde už jen 0100)
  3. x101 (projde už jen 0101)
- ano, skutečně jsou pokryty všechny stavy

# Zadání
ME - Ca-Ro - Kotlin RMI - shared variable