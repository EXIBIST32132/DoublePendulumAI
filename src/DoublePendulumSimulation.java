import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class DoublePendulumSimulation extends JPanel {
    private double pendulumAngle = Math.PI / 4; // Initial angle of the pendulum
    private double angularVelocity = 0;        // Angular velocity of the pendulum
    private final double gravity = 9.8;        // Acceleration due to gravity
    private final double rodLength = 150;      // Length of the pendulum rod
    private double pivotX = 400;               // Horizontal position of the main body
    private final double pivotY = 300;         // Fixed Y position of the rod
    private double horizontalSpeed = 0;        // Speed of the pivot body

    public DoublePendulumSimulation() {
        // Timer for updating simulation
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updatePhysics();
                repaint();
            }
        }, 0, 16); // 60 FPS

        // Key listener for moving the pivot
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    horizontalSpeed = -5;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    horizontalSpeed = 5;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                horizontalSpeed = 0;
            }
        });
        setFocusable(true);
    }

    private void updatePhysics() {
        // Update the pivot's horizontal position
        pivotX += horizontalSpeed;

        // Simulate pendulum motion
        double angularAcceleration = -(gravity / rodLength) * Math.sin(pendulumAngle);
        angularVelocity += angularAcceleration * 0.016; // Assuming time step of ~16ms
        pendulumAngle += angularVelocity * 0.016;

        // Damping to prevent infinite oscillation
        angularVelocity *= 0.99;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the horizontal rod
        g2.setStroke(new BasicStroke(5));
        g2.drawLine(100, (int) pivotY, 700, (int) pivotY);

        // Draw the main body
        g2.setColor(Color.RED);
        g2.fillRect((int) pivotX - 10, (int) pivotY - 10, 20, 20);

        // Draw the pendulum rod
        double pendulumX = pivotX + rodLength * Math.sin(pendulumAngle);
        double pendulumY = pivotY + rodLength * Math.cos(pendulumAngle);
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine((int) pivotX, (int) pivotY, (int) pendulumX, (int) pendulumY);

        // Draw the weight block
        g2.setColor(Color.GREEN);
        g2.fillRect((int) pendulumX - 10, (int) pendulumY - 10, 20, 20);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Double Pendulum Simulation");
        DoublePendulumSimulation panel = new DoublePendulumSimulation();
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
