package river;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FarmerGameEngine extends AbstractGameEngine {

    public static final Item BEANS = Item.ITEM_0;
    public static final Item GOOSE = Item.ITEM_1;
    public static final Item WOLF = Item.ITEM_2;
    public static final Item FARMER = Item.ITEM_3;

    public FarmerGameEngine() {

        setGameObjects(new HashMap<Item, GameObject>(){{
            put(WOLF, new GameObject("W", Location.START, Color.CYAN));
            put(GOOSE, new GameObject("G", Location.START, Color.CYAN));
            put(BEANS, new GameObject("B", Location.START, Color.CYAN));
            put(FARMER, new GameObject("F", Location.START, Color.PINK));
        }});
        boatLocation = Location.START;
    }

    @Override
    public void loadBoat(Item item) {
        if (gameObjects.get(item).getItemLocation() == boatLocation) {
            // check if its farmer
            if(gameObjects.get(item).getLabel() == "F"){
                gameObjects.get(item).setLocation(Location.BOAT);
            }
            // check if already someone with farmer
            Boolean someoneAlreadyWithFarmer = false;
            for(Item itm : Item.values()){
                if (!(itm.ordinal() < numberOfItems())) break;
                if(itm != item && itm != FARMER && gameObjects.get(itm).getItemLocation() == Location.BOAT){
                    someoneAlreadyWithFarmer = true;
                }
            }
            if(!someoneAlreadyWithFarmer)
                gameObjects.get(item).setLocation(Location.BOAT);
        }
    }

    @Override
    public void rowBoat() {
        assert (boatLocation != Location.BOAT);
        if (gameObjects.get(FARMER).getItemLocation() == Location.BOAT) {
            boatLocation = boatLocation == Location.START ? Location.FINISH : Location.START;
        }
    }

    public boolean gameIsLost() {
        Location location = gameObjects.get(GOOSE).getItemLocation();
        if (location == Location.BOAT || location == boatLocation
                || location == gameObjects.get(FARMER).getItemLocation()) {
            return false;
        } else if (location == gameObjects.get(WOLF).getItemLocation() ||
                location == gameObjects.get(BEANS).getItemLocation()) {
            return true;
        }
        return false;
    }

    @Override
    public int numberOfItems() {
        return 4;
    }

}