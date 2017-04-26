package pong2;

import java.awt.BorderLayout;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

class PongPanel extends JPanel {

    private Point ballPoint = new Point(30, 40);
    final int ballDiameter = 20;
    int dx = 1, dy = 1;
    int left, right, top, bottom;
    final int padding = 20;
private Timer ballUpdater;
private Dimension paddleDimension = new Dimension(10,50);
private final int paddleHorizontalShift = 20;
private final int paddleX = padding + paddleHorizontalShift;
private int paddleY = padding;
    PongPanel() {

        System.out.println(left + "," + right + "," + top + "," + bottom);
        setBackground(Color.BLACK);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                left = padding;
                right = PongPanel.this.getWidth() - padding;
                top = padding;
                bottom = getHeight() - padding;
            }
        });

        ballUpdater = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                drawBall();

            }
        });
        ballUpdater.start();
        
        addMouseWheelListener( new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                paddleY += mwe.getWheelRotation();
                repaint();
             }
        });
    }

    public boolean hitVerticalBounds() {
        return ballPoint.x <= left || ballPoint.x + ballDiameter >= right;
    }

    public boolean hitHorizontalBounds() {
        return ballPoint.y <= top || ballPoint.y + ballDiameter >= bottom;
    }

    public void updateBallPosition() {        
        if (hitVerticalBounds()) {
            dx = -dx;
            //System.out.println(ballPoint);
            ballUpdater.setDelay(ballUpdater.getDelay() / 2);
        }
        if (hitHorizontalBounds()) {
            dy = -dy;
           // System.out.println(ballPoint);
        }
        ballPoint.translate(dx, dy);

    }
private boolean firstCall;
    public void drawBall()
    {
        repaint();
        Graphics g = getGraphics();
        
//        if (!firstCall){
//        g.setColor(Color.BLACK);
//        g.fillOval(ballPoint.x, ballPoint.y, ballDiameter, ballDiameter);
//        }

    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);                updateBallPosition();
 g.setColor(Color.WHITE);
        g.fillOval(ballPoint.x, ballPoint.y, ballDiameter, ballDiameter);
       
         g.fillRect(paddleX, paddleY,
                 paddleDimension.width, paddleDimension.height);
       
    }
}

class PongApp extends JFrame {

    PongApp() {
        add(new PongPanel());
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

public class Main {

    public static void main(String[] args) {
        new PongApp();
    }

}
