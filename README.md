# Objektorienteeritud Programmeerimise projekt

Antud hoidla sisaldab endas aine OOP raames aine esimest (client_OLD, server) ja teist (client) projekti, mille on
mõlemad valmis teinud Alex Viil ja Janar
Aava.

## Nelja dimensiooniline male

Tegu on mitmikmängija käsurea malega, mis töötab tänu Java socketitele. Nimi tuleneb sellest, et mäng ise on
kahedimensooniline, aga kuna on kaks mängijat, siis dimensioone on kokku neli.
Saab mängida ühes arvutis, üle LAN-i ja  ka üle interneti (kui port **59059** on avatud serveri poolel).

## Jooksutamine

Ava cmd.exe ja liigu selle projekti kaustani (ning sealt edasi iga klassi paketi kaustani kui vaja):
* Server on tavaline java klass, piisab kompileerimisest `javac -encoding UTF-8 Server.java` ja
  jooksutamisest `java Server`. Ühendusteks kasutab porti **59059**.
  
* ClientCLI (käsurea klient) on tavaline iseseisev java klass, piisab kompileerimisest `javac -encoding UTF-8 ClientCLI.java` ja 
  jooksutamisest `java ClientCLI`.

* GUI jooksutamiseks on soovitatav teha järgnev:
    * Liikuda projekti kausta algusesse (kus asub ka fail **build.gradle**).
    * Jooksutada käsk `gradlew jar`.
    * Nüüd peaks projekti kaustas olema juures fail **4DChessClient.jar**.
    * Antud faili jooksutades käivitubki graafilise liidesega klient (töötab *Windows*is, *Linux*is ja ka *MacOS*-is).
        * NB! Antud Java arhiiv sisaldab ka käsurea kliendi ja serveri klasse, neid saab arhiivist kopeerida ja ka
          arhiivist kustutada (näiteks 7-zip abil).
