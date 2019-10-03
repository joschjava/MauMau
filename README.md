# Bugs
## 1
Exception in thread "JavaFX Application Thread" java.lang.RuntimeException: Card Card(color=HERZ, value=8) can't be layed on Card(color=HERZ, value=11)
	at Game.requestPutCardOnStapel(Game.java:176)
	at Player.playCard(Player.java:49)
	at Player.playCardId(Player.java:37)
	at Controller.submitAction(Controller.java:129)
	at Controller.lambda$initialize$0(Controller.java:50)
	at javafx.base/com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:86)
