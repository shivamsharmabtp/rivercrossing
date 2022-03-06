package river;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MonsterGameEngine extends AbstractGameEngine {

    public static final Item MUNCHKIN_1 = Item.ITEM_0;
    public static final Item MUNCHKIN_2 = Item.ITEM_1;
    public static final Item MUNCHKIN_3 = Item.ITEM_2;
    public static final Item MONSTER_1 = Item.ITEM_3;
    public static final Item MONSTER_2 = Item.ITEM_4;
    public static final Item MONSTER_3 = Item.ITEM_5;


    public MonsterGameEngine() {
        setGameObjects(new HashMap<Item, GameObject>(){{
            put(MUNCHKIN_1, new GameObject("MU1", Location.START, Color.CYAN));
            put(MUNCHKIN_2, new GameObject("MU2", Location.START, Color.CYAN));
            put(MUNCHKIN_3, new GameObject("MU3", Location.START, Color.CYAN));
            put(MONSTER_1, new GameObject("MO1", Location.START, Color.PINK));
            put(MONSTER_2, new GameObject("MO2", Location.START, Color.PINK));
            put(MONSTER_3, new GameObject("MO3", Location.START, Color.PINK));
        }});
        boatLocation = Location.START;
    }

    public boolean gameIsLost() {

        return false;
    }

    @Override
    public int numberOfItems() {
        return 6;
    }

}