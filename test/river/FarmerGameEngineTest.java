package river;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class FarmerGameEngineTest {

    private FarmerGameEngine engine;
    String W = "\uD83D\uDC3A",
            G = "\uD83D\uDC10",
            B = "\uD83C\uDF3F",
            F = "\uD83E\uDDD1\u200D";

    @Before
    public void setUp() throws Exception {
        engine = new FarmerGameEngine();
    }

    @Test
    public void testObjectCallThroughs() {

        Assert.assertEquals(F, engine.getItemLabel(Item.ITEM_3));
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_3));
        Assert.assertEquals(Color.PINK, engine.getItemColor(Item.ITEM_3));

        Assert.assertEquals(W, engine.getItemLabel(Item.ITEM_2));
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_2));
        Assert.assertEquals(Color.CYAN, engine.getItemColor(Item.ITEM_2));

        Assert.assertEquals(G, engine.getItemLabel(Item.ITEM_1));
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_1));
        Assert.assertEquals(Color.CYAN, engine.getItemColor(Item.ITEM_1));

        Assert.assertEquals(B, engine.getItemLabel(Item.ITEM_0));
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_0));
        Assert.assertEquals(Color.CYAN, engine.getItemColor(Item.ITEM_1));
    }

    @Test
    public void testMidTransport() {
        engine.loadBoat(Item.ITEM_3);
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_1));
        engine.loadBoat(Item.ITEM_1);
        Assert.assertEquals(Location.BOAT, engine.getItemLocation(Item.ITEM_1));
        engine.rowBoat();
        engine.unloadBoat(Item.ITEM_1);
        Assert.assertEquals(Location.FINISH, engine.getItemLocation(Item.ITEM_1));
    }

    @Test
    public void testWinningGame() {
        // transport the goose
        engine.loadBoat(Item.ITEM_1);
        engine.loadBoat(Item.ITEM_3);
        engine.rowBoat();
        engine.unloadBoat(Item.ITEM_1);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // go back alone
        engine.rowBoat();
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        transport(Item.ITEM_2);
        transport(Item.ITEM_1);
        transport(Item.ITEM_0);
        engine.rowBoat();
        engine.loadBoat(Item.ITEM_1);
        engine.rowBoat();
        engine.unloadBoat(Item.ITEM_1);
        engine.unloadBoat(Item.ITEM_3);
        Assert.assertEquals(true, engine.gameIsWon());
    }

    @Test
    public void testLosingGame() {
        engine.loadBoat(Item.ITEM_3);
        // transport the goose
        transport(Item.ITEM_1);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        engine.rowBoat();
        transport(Item.ITEM_2);
        engine.rowBoat();

        Assert.assertTrue(engine.gameIsLost());

    }

    @Test
    public void testError() {
        // transport the goose
        engine.loadBoat(Item.ITEM_3);

        transport(Item.ITEM_1);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // save the state
        Location topLoc = engine.getItemLocation(Item.ITEM_2);
        Location midLoc = engine.getItemLocation(Item.ITEM_1);
        Location bottomLoc = engine.getItemLocation(Item.ITEM_0);
        Location playerLoc = engine.getItemLocation(Item.ITEM_3);

        // This action should do nothing since the wolf is not on the same shore as the
        // boat
        engine.loadBoat(Item.ITEM_2);

        // check that the state has not changed
        Assert.assertEquals(topLoc, engine.getItemLocation(Item.ITEM_2));
        Assert.assertEquals(midLoc, engine.getItemLocation(Item.ITEM_1));
        Assert.assertEquals(bottomLoc, engine.getItemLocation(Item.ITEM_0));
        Assert.assertEquals(playerLoc, engine.getItemLocation(Item.ITEM_3));
    }

    private void transport(Item item) {
        engine.loadBoat(item);
        engine.rowBoat();
        engine.unloadBoat(item);
    }

    @Test
    public void getNumberOfItems(){
        Assert.assertEquals(4,engine.numberOfItems());
    }

    @Test
    public void getItemColor() {
        Assert.assertEquals(Color.PINK, engine.getItemColor(Item.ITEM_3));
        Assert.assertEquals(Color.CYAN, engine.getItemColor(Item.ITEM_2));
        Assert.assertEquals(Color.CYAN, engine.getItemColor(Item.ITEM_1));
    }

    @Test
    public void getItemLabel() {
        Assert.assertEquals(F, engine.getItemLabel(Item.ITEM_3));
        Assert.assertEquals(W, engine.getItemLabel(Item.ITEM_2));
        Assert.assertEquals(G, engine.getItemLabel(Item.ITEM_1));
        Assert.assertEquals(B, engine.getItemLabel(Item.ITEM_0));

    }

    @Test
    public void getItemLocation() {
        engine.loadBoat(Item.ITEM_3);
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_1));
        engine.loadBoat(Item.ITEM_1);
        Assert.assertEquals(Location.BOAT, engine.getItemLocation(Item.ITEM_1));

    }

    @Test
    public void setItemLocation() {
        engine.loadBoat(Item.ITEM_3);
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_1));
        engine.setItemLocation(Item.ITEM_3,Location.FINISH);
        Assert.assertEquals(Location.FINISH, engine.getItemLocation(Item.ITEM_3));
    }

    @Test
    public void reset(){
        engine.gameObjects.forEach((item, value) -> {
            value.setLocation(Location.FINISH);
        });
        engine.resetGame();
        engine.gameObjects.forEach((item, value) -> {
            Assert.assertEquals(Location.START, engine.getItemLocation(item));
        });
    }

    @Test
    public void rowBoat(){
        engine.setItemLocation(Item.ITEM_3,Location.START);
        engine.setItemLocation(Item.ITEM_1,Location.BOAT);
        engine.setItemLocation(Item.ITEM_2,Location.BOAT);
        engine.rowBoat();
        Assert.assertEquals(Location.START, engine.getBoatLocation());

        engine.setItemLocation(Item.ITEM_3,Location.BOAT);
        engine.setItemLocation(Item.ITEM_1,Location.BOAT);
        engine.rowBoat();
        Assert.assertEquals(Location.FINISH, engine.getBoatLocation());
    }

    @Test
    public void loadBoat(){
        engine.loadBoat(Item.ITEM_3);
        engine.loadBoat(Item.ITEM_1);
        engine.loadBoat(Item.ITEM_2);
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_2));
        Assert.assertEquals(Location.BOAT, engine.getItemLocation(Item.ITEM_3));
        Assert.assertEquals(Location.BOAT, engine.getItemLocation(Item.ITEM_1));

    }
}