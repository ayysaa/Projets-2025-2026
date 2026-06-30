JC = javac
JRE = java
JCFLAGS = -cp src -implicit:none

all: src/main/Display.class

src/game/Marqueur.class: src/game/Marqueur.java
	$(JC) $(JCFLAGS) src/game/Marqueur.java

src/display/NiceButton.class: src/display/NiceButton.java
	$(JC) $(JCFLAGS) src/display/NiceButton.java

src/game/Case.class: src/game/Case.java src/game/Marqueur.class
	$(JC) $(JCFLAGS) src/game/Case.java

src/game/Demineur.class: src/game/Demineur.java src/game/Case.class src/game/Marqueur.class
	$(JC) $(JCFLAGS) src/game/Demineur.java

src/game/SaveManager.class: src/game/SaveManager.java src/game/Demineur.class
	$(JC) $(JCFLAGS) src/game/SaveManager.java

src/controler/CaseClickListener.class: src/controler/CaseClickListener.java src/game/Demineur.class
	$(JC) $(JCFLAGS) src/controler/CaseClickListener.java

src/display/MainMenu.class: src/display/MainMenu.java src/display/NiceButton.class src/game/SaveManager.class
	$(JC) $(JCFLAGS) src/display/MainMenu.java

src/display/ConfigScreen.class: src/display/ConfigScreen.java src/display/NiceButton.class
	$(JC) $(JCFLAGS) src/display/ConfigScreen.java

src/display/EndScreen.class: src/display/EndScreen.java src/display/NiceButton.class
	$(JC) $(JCFLAGS) src/display/EndScreen.java

src/display/GameScreen.class: src/display/GameScreen.java src/display/NiceButton.class src/game/Demineur.class src/controler/CaseClickListener.class
	$(JC) $(JCFLAGS) src/display/GameScreen.java

src/main/Display.class: src/main/Display.java \
                        src/display/MainMenu.class \
                        src/display/ConfigScreen.class \
                        src/display/GameScreen.class \
                        src/display/EndScreen.class \
                        src/game/Demineur.class
	$(JC) $(JCFLAGS) src/main/Display.java

run: all
	$(JRE) -cp src main.Display

clean:
	rm -f src/controler/*.class src/display/*.class src/game/*.class src/main/*.class

.PHONY: all run clean