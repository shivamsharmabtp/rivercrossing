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


    private final Rectangle leftBoatRect = new Rectangle(140, 275, 110, 50);
    private final Rectangle rightBoatRect = new Rectangle(550, 275, 110, 50);
    private final Rectangle restartButtonRect = new Rectangle(350, 120, 100, 30);

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
            dx = {60, 0, 0, 60},
            dy = {60, 60, 0, 0},
            boatDx = {60, 60, 60, 0};

    private Map<Item, Rectangle> leftRectMap;
    private Map<Item, Rectangle> rightRectMap;
    private Map<Item, Rectangle> leftBoatRectMap;
    private Map<Item, Rectangle> rightBoatRectMap;

    private void initializeConfig(){
        leftRectMap = new HashMap<>();
        rightRectMap = new HashMap<>();
        leftBoatRectMap = new HashMap<>();
        rightBoatRectMap = new HashMap<>();

        for (Item item : Item.values()) {
            leftRectMap.put(item, new Rectangle(leftBaseX + dx[item.ordinal()], leftBaseY + dy[item.ordinal()], 50, 50));
            rightRectMap.put(item, new Rectangle(rightBaseX + dx[item.ordinal()], rightBaseY + dy[item.ordinal()], 50, 50));
            leftBoatRectMap.put(item, new Rectangle(boatLeftBase + boatDx[item.ordinal()], 215, 50, 50));
            rightBoatRectMap.put(item, new Rectangle(boatRightBase + boatDx[item.ordinal()], 215, 50, 50));
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
            if (engine.getItemLocation(item) == Location.START) {
                paintRectangle(engine.getItemColor(item), engine.getItemLabel(item), leftRectMap.get(item));
            }
            else if (engine.getItemLocation(item) == Location.FINISH) {
                paintRectangle(engine.getItemColor(item), engine.getItemLabel(item), rightRectMap.get(item));
            }
            if (engine.getBoatLocation() == Location.START) {
                paintRectangle(Color.ORANGE, "", new Rectangle(boatLeftBase, 275, 110, 50));
                if (engine.getItemLocation(item) == Location.BOAT) {
                    paintRectangle(engine.getItemColor(item), engine.getItemLabel(item), leftBoatRectMap.get(item));
                }
            }
            else if (engine.getBoatLocation() == Location.FINISH) {
                paintRectangle(Color.ORANGE, "", new Rectangle(boatRightBase, 275, 110, 50));
                if (engine.getItemLocation(item) == Location.BOAT) {
                    paintRectangle(engine.getItemColor(item), engine.getItemLabel(item), rightBoatRectMap.get(item));
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
            paintRestartButton(g);
        }
    }



    public void paintRectangle(Color color, String str, Rectangle rect) {
        g.setColor(color);
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

    public void paintRestartButton(Graphics g) {
        g.setColor(Color.BLACK);
        paintBorder(restartButtonRect, 3, g);
        g.setColor(Color.PINK);
        paintRectangle(Color.PINK, "Restart",restartButtonRect);
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

        if (restart) {
            if (this.restartButtonRect.contains(e.getPoint())) {
                engine.resetGame();
                restart = false;
                repaint();
            }
            return;
        }

        for (Item item : Item.values()) {
            if(leftRectMap.get(item).contains(e.getPoint()) && engine.getItemLocation(item) == Location.START){
                engine.loadBoat(item);
            }
            else if(rightRectMap.get(item).contains(e.getPoint()) && engine.getItemLocation(item) == Location.FINISH){
                engine.loadBoat(item);
            }

            if(leftBoatRectMap.get(item).contains(e.getPoint()) && engine.getItemLocation(item) == Location.BOAT){
                engine.unloadBoat(item);
            }
            if(rightBoatRectMap.get(item).contains(e.getPoint()) && engine.getItemLocation(item) == Location.BOAT){
                engine.unloadBoat(item);
            }
        }

        if (leftBoatRect.contains(e.getPoint()) && engine.getBoatLocation() == Location.START) {
            engine.rowBoat();
        } else if (rightBoatRect.contains(e.getPoint()) && engine.getBoatLocation() == Location.FINISH ) {
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