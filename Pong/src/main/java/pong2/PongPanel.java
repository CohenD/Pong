package pong2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PongPanel extends JPanel {

    private Point ballPoint = new Point(30, 40);
    final int ballDiameter = 20;
    private int dx = 2, dy = 2;
    private int left, right, top, bottom;
    final int padding = 20;
    private Timer ballUpdater;
    private final Dimension paddleDimension = new Dimension(10, 70);
    private final int paddleHorizontalShift = 20;
    private final int paddleX = padding + paddleHorizontalShift;
    private int paddleY = padding;
    private boolean autoMode = false;

    
    private Color ballColor = Color.WHITE;
    private final Color[] colors = new Color[]{
        Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.WHITE,
        Color.DARK_GRAY.darker().darker().darker(), Color.PINK, Color.ORANGE, Color.CYAN, Color.MAGENTA
    };

    private final Random rand = new Random();
    private boolean everyOther = false;
    private int score = 0;
    private boolean hasHitRightWall = false;

    PongPanel() {

        setBackground(Color.WHITE);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setMinimumSize(new Dimension(500, getWidth()));
                left = padding;
                right = PongPanel.this.getWidth() - padding;
                top = padding;
                bottom = getHeight() - padding;
            }
        });

        ballUpdater = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                drawBall();
            }
        });

        ballUpdater.start();

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                if (!autoMode) {
                    paddleY += mwe.getWheelRotation() * 12;
                }
            }
        });
    }

    void reset() {
        ballPoint = new Point(30, 40);
        paddleY = padding;
        ballColor = Color.WHITE;
        everyOther = false;
        score = 0;
        hasHitRightWall = false;
        ballUpdater.start();
        ballUpdater.setDelay(20);
        dx = 1;
        dy = 1;
        setBackground(Color.WHITE);
    }

    public boolean hitVerticalBounds() {
        return ballPoint.x <= left || ballPoint.x + ballDiameter >= right;
    }

    public boolean hitHorizontalBounds() {
        return ballPoint.y <= top || ballPoint.y + ballDiameter >= bottom;
    }

    public boolean hitPaddel() {
        return ballPoint.x <= paddleX && ballPoint.y >= paddleY && ballPoint.y <= paddleY + paddleDimension.height;
    }

    private void gameOver() {
        ballUpdater.stop();
        final ArrayList<String> arrayScores = TopScorers.arrayScores();
        String s = "";
        for (int i = 0; i < arrayScores.size(); i++) {
            s += arrayScores.get(i) + "\n";
        }
        final String ss = s;//for use of anonymous inner class
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(PongPanel.this, "HIGH SCORES\n\n" + ss + "\n\nYour Score was " + score);
                
                String temp = arrayScores.get(arrayScores.size() - 1);
                temp = temp.substring(temp.lastIndexOf(" ") + 1);
                
                if (score > Integer.parseInt(temp) || arrayScores.size() < 10) {
                    String playerName = JOptionPane.showInputDialog(PongPanel.this,
                            "HighScore!\nAdd your name to the list of high scorers",
                            "name here");
                    if (!playerName.equals(null) && !playerName.trim().equals("")) {
                        TopScorers.addNewScore(playerName.trim(), score);
                    }
                }

            }
        });

    }

    public void updateBallPosition() {

        if (hitVerticalBounds()) {
            dx = -dx;

            if (everyOther) {
                ballUpdater.setDelay(ballUpdater.getDelay() / 2 + 1);//speed up only every other
            }
            everyOther = !everyOther;

            if (ballPoint.x <= left) {
                if (hasHitRightWall) {
                    gameOver();
                }
            } else {
                hasHitRightWall = true;
            }

        }

        if (hitHorizontalBounds()) {
            ballColor = colors[rand.nextInt(colors.length)];
            dy = -dy;
            if (autoMode && hasHitRightWall) {
                paddleY = ballPoint.y > 250
                        ? getHeight() - ballPoint.x - padding
                        : ballPoint.x - padding;
            }
            // System.out.println(ballPoint);
        }
        if (hitPaddel()) {
            dx = -dx;

            if (hasHitRightWall) {
                hasHitRightWall = false;
                if (ballColor.equals(Color.YELLOW)) {
                    System.out.println("Golden Ball");
                    score += 9;
                }
                ++score;
            }

            if (score > 10) {
                setBackground(Color.CYAN);
            }
            if (score > 20) {
                setBackground(Color.GREEN);
            }
            if (score > 30) {
                setBackground(Color.PINK);
            }
            if (score > 40) {
                setBackground(Color.ORANGE);
            }
            if (score > 50) {
                setBackground(Color.YELLOW);
            }

            if (getBackground().equals(ballColor) && hasHitRightWall) {
                ballUpdater.setDelay(ballUpdater.getDelay() + 5);
            }
        }
        ballPoint.translate(dx, dy);
    }

    void autoModeToggle() {
        autoMode = !autoMode;
        System.out.println(autoMode);
    }

    public void drawBall() {
        repaint();
        Graphics g = getGraphics();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (paddleY > getHeight() - 90) {
            paddleY = getHeight() - 90;
        } else if (paddleY < 20) {
            paddleY = 20;
        }

        g.setColor(Color.BLACK);
        g.fillRect(left, top, right - left, bottom - top);
        g.drawString("Score: " + score, padding / 2, padding / 2);

        updateBallPosition();
        g.setColor(ballColor);
        g.fillOval(ballPoint.x, ballPoint.y, ballDiameter, ballDiameter);

        g.fillRect(paddleX, paddleY,
                paddleDimension.width, paddleDimension.height);
    }
}
