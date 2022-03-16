package river;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RiverGUI extends JPanel implements MouseListener {

    // ==========================================================
    // Fields (hotspots)
    // ==========================================================


    private final Rectangle leftBoatRect = new Rectangle(140, 275, 120, 50);
    private final Rectangle rightBoatRect = new Rectangle(550, 275, 120, 50);
    private final Rectangle farmerRestartRect = new Rectangle(300, 120, 100, 30);
    private final Rectangle monsterRestartRect = new Rectangle(420, 120, 100, 30);

    // ==========================================================
    // Private Fields
    // ==========================================================

    private GameEngine engine; // Model
    private boolean restart = false;

    // ==========================================================
    // Constructor
    // ==========================================================
    private int
            leftBaseX = 20,
            leftBaseY = 215,
            rightBaseX = 670,
            rightBaseY = 215,
            boatLeftBase = 140,
            boatRightBase = 550;
    private int[]
            dx = {0, 0, 0, 60, 60, 60},
            dy = {60, 0, -60, 60, 0, -60};

    private Map<Item, Rectangle> leftRectMap;
    private Map<Item, Rectangle> rightRectMap;

    private Item[] objectsOnBoat;

    public void loadObjectsOnBoat(Item item) {
        engine.loadBoat(item);
        if (objectsOnBoat[0] == null) objectsOnBoat[0] = item;
        else if (objectsOnBoat[1] == null) objectsOnBoat[1] = item;
    }

    public void unloadObjectsOnBoat(Item item) {
        engine.unloadBoat(item);
        if (objectsOnBoat[0] == item) objectsOnBoat[0] = null;
        else if (objectsOnBoat[1] == item) objectsOnBoat[1] = null;
    }

    public Rectangle getItemOnBoatRect(Item item, Location location) {
        int dX = 0;
        if (objectsOnBoat[1] == item) {
            dX = 60;
        }
        if (location == Location.START)
            return new Rectangle(boatLeftBase + dX, 215, 50, 50);
        return new Rectangle(boatRightBase + dX, 215, 50, 50);
    }

    private void initializeConfig() {
        leftRectMap = new HashMap<>();
        rightRectMap = new HashMap<>();
        objectsOnBoat = new Item[2];

        for (Item item : Item.values()) {
            if (item.ordinal() >= engine.numberOfItems()) break;
            leftRectMap.put(item, new Rectangle(leftBaseX + dx[item.ordinal()], leftBaseY + dy[item.ordinal()], 50, 50));
            rightRectMap.put(item, new Rectangle(rightBaseX + dx[item.ordinal()], rightBaseY + dy[item.ordinal()], 50, 50));
        }
    }

    public RiverGUI() {
        engine = new FarmerGameEngine();
        addMouseListener(this);
        initializeConfig();
    }

    // ==========================================================
    // Paint Methods (View)
    // ==========================================================

    private Graphics g;

    @Override
    public void paintComponent(Graphics g_args) {
        this.g = g_args;
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (Item item : Item.values()) {
            if (item.ordinal() >= engine.numberOfItems()) break;
            if (engine.getItemLocation(item) == Location.START) {
                paintRectangle(null, engine.getItemLabel(item), leftRectMap.get(item));
            } else if (engine.getItemLocation(item) == Location.FINISH) {
                paintRectangle(null, engine.getItemLabel(item), rightRectMap.get(item));
            }
            if (engine.getBoatLocation() == Location.START) {
                paintRectangle(Color.ORANGE, "", leftBoatRect);
                if (engine.getItemLocation(item) == Location.BOAT) {
                    paintRectangle(null, engine.getItemLabel(item), getItemOnBoatRect(item, Location.START));
                }
            } else if (engine.getBoatLocation() == Location.FINISH) {
                paintRectangle(Color.ORANGE, "", rightBoatRect);
                if (engine.getItemLocation(item) == Location.BOAT) {
                    paintRectangle(null, engine.getItemLabel(item), getItemOnBoatRect(item, Location.FINISH));
                }
            }
        }

        String message = "";
        if (engine.gameIsLost()) {
            message = "You Lost!";
            restart = true;
        }
        if (engine.gameIsWon()) {
            message = "You Won!";
            restart = true;
        }
        paintMessage(message, g);
        if (restart) {
            paintRestartButton(g, "Farmer", farmerRestartRect);
            paintRestartButton(g, "Monster", monsterRestartRect);
        }
    }

    public void paintRectangle(Color color, String str, Rectangle rect) {
        if (color != null)
            g.setColor(color);
        else g.setColor(Color.GRAY);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(Color.BLACK);
        int fontSize = (rect.height >= 40) ? 36 : 18;
        g.setFont(new Font("Verdana", Font.BOLD, fontSize));
        FontMetrics fm = g.getFontMetrics();
        int strXCord = rect.x + rect.width / 2 - fm.stringWidth(str) / 2;
        int strYCord = rect.y + rect.height / 2 + fontSize / 2 - 4;
        g.drawString(str, strXCord, strYCord);
    }

    public void paintMessage(String message, Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana", Font.BOLD, 36));
        FontMetrics fm = g.getFontMetrics();
        int strXCoord = 400 - fm.stringWidth(message) / 2;
        int strYCoord = 100;
        g.drawString(message, strXCoord, strYCoord);
    }

    public void paintRestartButton(Graphics g, String buttonText, Rectangle restartButtonRect) {
        g.setColor(Color.BLACK);
        paintBorder(restartButtonRect, 3, g);
        g.setColor(Color.PINK);
        paintRectangle(Color.PINK, buttonText, restartButtonRect);
    }

    public void paintBorder(Rectangle r, int thickness, Graphics g) {
        g.fillRect(r.x - thickness, r.y - thickness, r.width + (2 * thickness), r.height + (2 * thickness));
    }

    // ==========================================================
    // Startup Methods
    // ==========================================================

    /**
     * Create the GUI and show it. For thread safety, this method should be invoked
     * from the event-dispatching thread.
     */
    private static void createAndShowGUI() {

        // Create and set up the window
        JFrame frame = new JFrame("RiverCrossing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane
        RiverGUI newContentPane = new RiverGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        // Display the window
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(RiverGUI::createAndShowGUI);
    }

    // ==========================================================
    // MouseListener Methods (Controller)
    // ==========================================================

    @Override
    public void mouseClicked(MouseEvent e) {
        Boolean eventNotHandled = true;
        if (eventNotHandled && restart) {
            if (this.farmerRestartRect.contains(e.getPoint())) {
                engine = new FarmerGameEngine();
                engine.resetGame();
                initializeConfig();
                restart = false;
                repaint();
            }else if (this.monsterRestartRect.contains(e.getPoint())) {
                engine = new MonsterGameEngine();
                engine.resetGame();
                initializeConfig();
                restart = false;
                repaint();
            }
            eventNotHandled = false;
        }

        for (Item item : Item.values()) {
            if (item.ordinal() >= engine.numberOfItems()) break;
            if (eventNotHandled && leftRectMap.get(item).contains(e.getPoint()) && engine.getItemLocation(item) == Location.START) {
                loadObjectsOnBoat(item);
                eventNotHandled = false;
            } else if (eventNotHandled && rightRectMap.get(item).contains(e.getPoint()) && engine.getItemLocation(item) == Location.FINISH) {
                loadObjectsOnBoat(item);
                eventNotHandled = false;
            }
        }

        if (eventNotHandled) {
            for (Item item : objectsOnBoat) {
                if (getItemOnBoatRect(item, Location.START).contains(e.getPoint()) || getItemOnBoatRect(item, Location.FINISH).contains(e.getPoint())) {
                    unloadObjectsOnBoat(item);
                    eventNotHandled = false;
                }
            }
        }

        if (eventNotHandled && leftBoatRect.contains(e.getPoint()) && engine.getBoatLocation() == Location.START) {
            engine.rowBoat();
        } else if (eventNotHandled && rightBoatRect.contains(e.getPoint()) && engine.getBoatLocation() == Location.FINISH) {
            engine.rowBoat();
        }

        repaint();
    }

    // ----------------------------------------------------------
    // None of these methods will be used
    // ----------------------------------------------------------

    @Override
    public void mousePressed(MouseEvent e) {
        //
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }
}