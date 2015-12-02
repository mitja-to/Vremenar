# Vremenar

Example of a job interview task for the position Junior Android Developer.
The given instructions in Slovenian:

Naloga 1 (Android)

V tej nalogi boš ustvaril aplikacijo, ki prikazuje podatke o vremenu iz OpenWeatherMap API

(http://openweathermap.org/api). Aplikacija omogoča dodajanje večjega števila mest in pregled informacij o vremenu.
Kjer se le da uporabi privzete Android komponente (če še nisi, si posodobi support library na verzijo 22.2.1).

1. Ustvari projekt v Android Studio (target sdk 22, min sdk 15)
2. Ustvari 3 Activityje:

  a. Prvi Activity vsebuje Recycler View s seznamom mest in gumb (FAB) za
  dodajanje mesta. Ob prvem zagonu aplikacije je seznam prazen (naj bo jasno
  razvidno, da je na ekranu seznam, ki je trenutno prazen).

  b. Drugi Activity je namenjen dodajanju mesta in vsebuje edit text (floating label) za
  vnos imena mesta in gumb za dokončanje. V toolbaru naj bo možnost izhoda iz
  activityja brez vnosa mesta.
  
  c. Tretji Activity predstavlja informacije o vremenu: izbrano mesto, trenutna
  temperatura (temp), vlažnost (humidity) in opis (description). Prikaže se ob
  pritisku na posamezen vnos na prvem Activityu.

3. Implementiraj prvi Activity in prikaz seznama mest.
4. Implementiraj drugi Activity in ga poveži s prvim, tako da bo mogoče dodajanje mesta na seznam.
5. Implementiraj tretji activity in prikaz informacij o vremenu. Informacije o vremenu naloži iz REST APIja­ 
   Endpoint najdeš v dokumentaciji OpenWeatherMap. Activity poveži s prvim Activityjem, tako da bo mogoč ogled informacij o različnih mestih.
6. Implementiraj lokalno shranjevanje podatkov, tako da seznam mest ostane v aplikaciji tudi, če se ta zapre.
7. Seznamu mest dodaj še prikaz temperature na prvem screenu, ki se obnovi iz REST API ob potegu tabele navzdol (pull to refresh).
8. Implementiraj brisanje mest iz seznama na prvem Activityu. Če uporabnik horizontalno povleče mesto v levo ali desno smer, se mesto odstrani iz 
seznama in hkrati se prikaže snack bar, ki uporabniku sporoči, da je bilo mesto odstranjeno.

Povezave in pomoč:

­ http://openweathermap.org/api
­ https://developer.android.com/intl/zh­CN/reference/android/support/design/widget/FloatingActionButton.html
­ https://developer.android.com/intl/zh­CN/reference/android/support/design/widget/TextInputLayout.html
­ https://developer.android.com/intl/zh­CN/reference/android/support/v4/widget/SwipeRefreshLayout.html
