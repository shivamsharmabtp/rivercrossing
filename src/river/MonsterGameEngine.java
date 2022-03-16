package river;

import java.awt.*;
import java.util.HashMap;

public class MonsterGameEngine extends AbstractGameEngine {

    public static final Item MONSTER_1 = Item.ITEM_0;
    public static final Item MUNCHKIN_1 = Item.ITEM_1;
    public static final Item MONSTER_2 = Item.ITEM_2;
    public static final Item MUNCHKIN_2 = Item.ITEM_3;
    public static final Item MONSTER_3 = Item.ITEM_4;
    public static final Item MUNCHKIN_3 = Item.ITEM_5;


    public MonsterGameEngine() {
        String Monster = "\uD83D\uDC76";
        String Munchkin = "\uD83D\uDC7F";
        setGameObjects(new HashMap<Item, GameObject>(){{
            put(MUNCHKIN_1, new GameObject(Monster, Location.START, Color.CYAN));
            put(MUNCHKIN_2, new GameObject(Monster, Location.START, Color.CYAN));
            put(MUNCHKIN_3, new GameObject(Monster, Location.START, Color.CYAN));
            put(MONSTER_1, new GameObject(Munchkin, Location.START, Color.PINK));
            put(MONSTER_2, new GameObject(Munchkin, Location.START, Color.PINK));
            put(MONSTER_3, new GameObject(Munchkin, Location.START, Color.PINK));
        }});
        boatLocation = Location.START;
    }

    public boolean gameIsLost() {
        int leftMonsters = 0,
                rightMonsters = 0,
                leftMunchkins = 0,
                rightMunchkins = 0;

        for(Item item : Item.values()){
            if (!(item.ordinal() < numberOfItems())) break;
            if(item.ordinal()%2 == 0){
                if(gameObjects.get(item).getLocation() == Location.START) leftMonsters++;
                else if(gameObjects.get(item).getLocation() == Location.FINISH) rightMonsters++;
                else if(gameObjects.get(item).getLocation() == Location.BOAT){
                    if(getBoatLocation() == Location.START) leftMonsters++;
                    else rightMonsters++;
                }
            }else{
                if(gameObjects.get(item).getLocation() == Location.START) leftMunchkins++;
                else if(gameObjects.get(item).getLocation() == Location.FINISH) rightMunchkins++;
                else if(gameObjects.get(item).getLocation() == Location.BOAT) {
                    if (getBoatLocation() == Location.START) leftMunchkins++;
                    else rightMunchkins++;
                }
            }
        }
        if((leftMonsters > leftMunchkins && leftMunchkins > 0) || (rightMonsters > rightMunchkins && rightMunchkins > 0)){
            return true;
        }
        return false;
    }

    @Override
    public int numberOfItems() {
        return 6;
    }

}