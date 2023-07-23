# TO-DO list

`[next item]`: RGL-18 

* move ability code to its basic effects (probably requires RGL-16 and RGL-17)
* RGL-16 checks if an ability could be applied should return some results, not the simple boolean  
* RGL-17 ability should return some results of its application 
    (this should help with the correct sequence of the events during the ability cast)
* add ability criteria for the caster (related to RGL-10)
* create the system of commands
* **RGL-9** try to do typed characters
* **RGL-7** Abstract out the console output (View)
* **RGL-8** create document with overall description and entities (Ability-related)

----
# In Progress

* **RGL-15** add description and/or help to the abilities, at least in javadoc

----
# Implemented 

* RGL-13 added "move back", "move forward" ability effects.
* RGL-14 removed nullable party characters
* RGL-12 implemented friend recognition system (DungeonCharacter 'team' property / helpful methods in Skirmish)
* RGL-11 Remove dead characters from the battlefield
* RGL-10 Created criteria classes for the main target of the ability
* RGL-9 Parameterized ability with the type of its basic effect
* RGL-8 create ability system which works
* RGL-7 added basic colors to the console output
* RGL-6 documented the requirements for the console command system
* RGL-5 Created a game loop for skirmish, take input from the player, validate input and perform abilities, complete skirmish event loop
* RGL-4 create party, adjust abilities
* RGL-3 Created some monsters
* RGL-2 Created **Experience**, **CharacterClass** and some implementations, **DungeonCharacter**,
    **Ability** and some implementations.
* RGL-1 Create Kotlin project.
