# Objektorienteeritud Programmeerimise projekt

Antud hoidla sisaldab endas aine OOP raames aine esimest (client_OLD, server) ja teist (client) projekti, mille on
mõlemad valmis teinud Alex Viil ja Janar Aava.

## Nelja dimensiooniline male

Tegu on mitmikmängija käsurea malega, mis töötab tänu Java socketitele. Nimi tuleneb sellest, et mäng ise on
kahedimensooniline, aga kuna on kaks mängijat siis dimensioone on kokku neli.
Saab mängida ühes arvutis, üle LAN-i ja ka üle interneti (kui port **59059** on avatud serveri poolel).

### Graafilise liidesega kliendi jooksutamine

Ava *cmd.exe* ja liigu selle projekti kaustani:
* GUI jooksutamiseks on soovitatav teha järgnev:
    * Liikuda projekti kausta algusesse (kus asub ka fail **build.gradle**).
    * Jooksutada käsk `gradlew jar`.
    * Nüüd peaks projekti kaustas olema juures fail **4DChessClient.jar**.
    * Antud faili jooksutades käivitubki graafilise liidesega klient (töötab *Windows*is, *Linux*is ja ka *MacOS*-is).
        * NB! Antud Java arhiiv sisaldab ka käsurea kliendi ja serveri klasse, neid saab arhiivist kopeerida ja ka
          arhiivist kustutada (näiteks 7-Zip abil).  
  
### Käsurea kliendi / Serveri jooksutamine
  
Kompileerumiseks ja käivitamiseks tuleb veenduda, et .java failid on kõik oma paketi nimega kaustades. ClientCLI.java peab
olema kaustas *client_OLD* ning kõik serveriga seonduv kaustas *server* (nad on üksteisest sõltumatud).  
Edasised käsud tuleb jooksutada *cmd.exe* abil kaustast, kus asuvad eelnevalt nimetatud kaustad kui paketid.  
* Server:
    * kompileerimine `javac -encoding UTF-8 server/Server.java`.
    * jooksutamine `java server/Server`. Ühendusteks kasutab porti **59059**.
* Käsurea klient:
    * kompileerimine `javac -encoding UTF-8 client_OLD/ClientCLI.java`.
    * jooksutamine `java client_OLD/ClientCLI`.

### Salvestamine ja salvestuse laadimine

Programm salvestab automaatselt peale igat käiku ning on võimalik ka käsitsi salvestada kasutades kasutajaliideses vastavat nuppu.
Salvestatud fail on tavaline tekstifail, mille laadimiseks peab serveri käivitamisel käsureal kaasa andma salvestatud faili asukoha.
Näiteks: `java server/Server save.txt`
