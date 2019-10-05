import org.junit.Test;

public class GuiTest {

    @Test
    public void cardTest(){
        Card card = new Card(Card.COLOR.CLUBS, 7);
        new GuiCard(card).getImage();
    }

}
