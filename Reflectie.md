# V1

## 🔗 Pre coaching

### Geschatte Progress (in procent): ...%

### Status
*Waar sta je globaal? Wat loopt goed en minder goed? Hoe verloopt de samenwerking? Wie heeft globaal welke delen van de applicatie uitgewerkt? (details progress kunnen we bekijken op het issue board)*

BV.  
De start ging wat moeizaam, maar nu we de basis onder de knie hebben merken we dat we sneller zaken kunnen toevoegen. We hebben wel een paar keer een tijd vastgezeten (vb. error bij de connectie naar de databank...). De simulator is volledig afgewerkt op dit moment.

### Stories
*(enkel voor stories die speciale aandacht vergen)*

BV. 
- [16 Payment API implementeren]: we hebben niet correct werkend gekregen met XML.

### Quality
*Acties (refactorings,...) die nog gepland staan om de kwaliteit van je project te verhogen (maak hiervoor issues aan!): [issue nummer]: toelichting [issue nummer]: toelichting*

BV. 
- [12 Code open voor toekomstige andere prijsberekeningsmethoden]: Functionaliteit van de prijsberekening is geïmplementeerd maar we moeten er nog voor zorgen dat er via Spring snel naar andere prijsstrategieën gewisseld kan worden?

### Vragen
*Eventuele vragen voor je coach*

BV.
- Moeten we verplicht gebruik maken van de schedulingfunctionaliteit van Spring?
- Mogen we ons voor de unit testen beperken tot de zaken die gevraagd worden in de opgave?


## Post coaching

### Feedback
*(in te vullen na gesprek)*

BV.  
We zonden op verschillende plaatsen tegen het single responsibility principe en moeten het project overlopen om dit op orde te krijgen. Er zit businesslogica in de Controller die daar niet thuis hoort vb. prijscalculatie. We moeten erover waken dat we op schema blijven want we lopen een beetje achter.
