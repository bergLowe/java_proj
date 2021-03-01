package life;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

interface Callback {
    void action(int alive, int generation);
}

class MatrixPanel extends JPanel {

    public BufferedImage image;
    public GameOfLifeSetup gol;
    private Callback onUpdate;
    public int generation;
    public Timer timer;

    public MatrixPanel() {
        image = new BufferedImage(300,300,BufferedImage.TYPE_INT_RGB);
        timer = new Timer(0, ae -> createImage(image));
        timer.setDelay(500);
        timer.start();
        this.gol = new GameOfLifeSetup();
        this.generation = 0;
    }

    public void setOnUpdateAction(Callback action) {
        this.onUpdate = action;
    }

    public void createImage(BufferedImage image) {
        Graphics g = image.getGraphics();
        char[][] arr = this.gol.getGrid();
        int r = 0;
        int c = 0;

        onUpdate.action(this.gol.getAlive(), generation);
        this.generation += 1;

        int sqW = 15;
        for (int i = 0; i < 300; i += sqW) {
            if (r == 20) {
                r = 0;
            }
            int sqH = 15;
            for (int j = 0; j < 300; j += sqH) {
                if (c == 20) {
                    c = 0;
                }
                if (arr[r][c] == 'O') {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.GRAY);
                }
                g.fillRect(i, j, sqW, sqH);
                g.setColor(Color.BLACK);
                g.drawRect(i, j, sqW, sqH);
                c += 1;
            }
            r += 1;
        }
        this.gol.generateGenerations(arr);

        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Image observer not required so it can be set to null.
        g.drawImage(image, 0,0, null);
    }
}

public class GameOfLife extends JFrame {
    private final JPanel leftPanel;
    private final JPanel rightPanel;
    private JLabel aliveLabel;
    private JLabel generationLabel;
    private final JButton restartButton;
    private final JToggleButton pauseButton;

    public GameOfLife() {

        // Panels
        leftPanel = new JPanel();
        rightPanel = new JPanel();

        // Buttons
        Icon restartIcon = new ImageIcon("D:\\Documents\\Documents\\Game of Life\\Game of Life\\task\\src\\resources\\icons8-restart-30.png");
        restartButton = new JButton(restartIcon);
        restartButton.setName("ResetButton");

        Icon pauseIcon = new ImageIcon("D:\\Documents\\Documents\\Game of Life\\Game of Life\\task\\src\\resources\\icons8-resume-button-30.png");
        pauseButton = new JToggleButton(pauseIcon);
        pauseButton.setName("PlayToggleButton");

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

        leftPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        leftPanel.add(pauseButton);
        leftPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        leftPanel.add(restartButton);
        
        updateLabel(0);

        MatrixPanel matrixPanel = new MatrixPanel();
        matrixPanel.setPreferredSize(new Dimension(300, 300));

        // Setting up anonymous method using lambda because
        // there is only one method to handle in Interface.
        matrixPanel.setOnUpdateAction((alive, generation) -> {
            generationLabel.setText("Generation #" + generation);
            aliveLabel.setText("Alive: " + alive);
        });

        pauseButton.addActionListener(e -> {
            if (pauseButton.isSelected()) {
                matrixPanel.timer.stop();
            } else {
                matrixPanel.timer.start();
            }
        });

        restartButton.addActionListener(e -> {
            matrixPanel.timer.stop();
            matrixPanel.image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
            matrixPanel.generation = 0;
            matrixPanel.gol = new GameOfLifeSetup();
            matrixPanel.timer.start();
        });

        rightPanel.add(matrixPanel);

        setLayout(new GridLayout(1, 2));

        add(leftPanel, BorderLayout.PAGE_START);
        add(rightPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void updateLabel(int number) {
        /*Label - 1*/
        generationLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        generationLabel.setText("Generation #" + number);
        generationLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        generationLabel.setBounds(10, 5, 300, 20);

        /*Label - 2*/
        aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("Alive: " + number);
        aliveLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        aliveLabel.setBounds(10, 25, 300, 20);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(generationLabel);
        leftPanel.add(aliveLabel);
    }
}