package river;

import java.awt.*;
import java.util.Map;

public abstract class AbstractGameEngine implements GameEngine{

    protected Map<Item, GameObject> gameObjects;
    protected Location boatLocation;
    protected void setGameObjects(Map<Item, GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public abstract int numberOfItems();

    @Override
    public Color getItemColor(Item item) {
        return gameObjects.get(item).getColor();
    }

    @Override
    public Location getItemLocation(Item item) {
        return gameObjects.get(item).getItemLocation();
    }

    @Override
    public void setItemLocation(Item item, Location location) {
        gameObjects.get(item).setLocation(location);
    }

    public Location getBoatLocation() {
        return boatLocation;
    }

    @Override
    public String getItemLabel(Item item) {
        return gameObjects.get(item).getLabel();
    }

    @Override
    public void loadBoat(Item item){
        if (gameObjects.get(item).getItemLocation() == boatLocation) {
            // check if someone already on Boat
            int passengersAboard = 0;
            for(Item itm : Item.values()){
                if(itm != item && gameObjects.get(itm).getItemLocation() == Location.BOAT){
                    passengersAboard++;
                }
            }
            if(passengersAboard < 2)
                gameObjects.get(item).setLocation(Location.BOAT);
        }
    }

    @Override
    public void unloadBoat(Item item) {
        if (gameObjects.get(item).getItemLocation() == Location.BOAT) {
            gameObjects.get(item).setLocation(boatLocation);
        }
    }

    public void rowBoat() {
        Boolean anyoneOnBoat = false;
        for(Item item : Item.values()){
            if(gameObjects.get(item).getItemLocation() == Location.BOAT){
                anyoneOnBoat = true;
                break;
            }
        }
        if(anyoneOnBoat)
            boatLocation = boatLocation == Location.START ? Location.FINISH : Location.START;
    }

    @Override
    public boolean gameIsWon() {
        return gameObjects.values().stream().allMatch(x -> x.getItemLocation() == Location.FINISH);
    }

    @Override
    public abstract boolean gameIsLost();

    @Override
    public void resetGame() {
        gameObjects.forEach((item, value) -> {
            value.setLocation(Location.START);
        });
        boatLocation = Location.START;
    }
}