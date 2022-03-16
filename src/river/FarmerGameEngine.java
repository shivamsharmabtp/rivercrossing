package river;

import java.awt.*;
import java.util.HashMap;

public class FarmerGameEngine extends AbstractGameEngine {

    public static final Item BEANS = Item.ITEM_0;
    public static final Item GOOSE = Item.ITEM_1;
    public static final Item WOLF = Item.ITEM_2;
    public static final Item FARMER = Item.ITEM_3;

    public FarmerGameEngine() {
        String W = "\uD83D\uDC3A",
                G = "\uD83D\uDC10",
                B = "\uD83C\uDF3F",
                F = "\uD83E\uDDD1\u200D";

        setGameObjects(new HashMap<Item, GameObject>(){{
            put(WOLF, new GameObject(W, Location.START, Color.CYAN));
            put(GOOSE, new GameObject(G, Location.START, Color.CYAN));
            put(BEANS, new GameObject(B, Location.START, Color.CYAN));
            put(FARMER, new GameObject(F, Location.START, Color.PINK));
        }});
        boatLocation = Location.START;
    }

    @Override
    public void rowBoat() {
        if (gameObjects.get(FARMER).getLocation() == Location.BOAT) {
            boatLocation = boatLocation == Location.START ? Location.FINISH : Location.START;
        }
    }

    public boolean gameIsLost() {
        Location location = gameObjects.get(GOOSE).getLocation();
        if (location == Location.BOAT || location == boatLocation
                || location == gameObjects.get(FARMER).getLocation()) {
            return false;
        } else if (location == gameObjects.get(WOLF).getLocation() ||
                location == gameObjects.get(BEANS).getLocation()) {
            return true;
        }
        return false;
    }

    @Override
    public int numberOfItems() {
        return 4;
    }

}