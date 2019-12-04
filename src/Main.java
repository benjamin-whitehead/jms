import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JPanel implements ActionListener {
  // Total Size of the Screen
  private ButtonGroup makeSolve = new ButtonGroup();
  private JRadioButton generate = new JRadioButton("Generate New Maze");
  private JTextField xSizeText = new JTextField(10);
  private JTextField ySizeText = new JTextField(10);
  private JRadioButton solve = new JRadioButton("Solve Last Maze");
  private JButton go = new JButton("Go");
  
  public Main() {
    JPanel grid = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    
    makeSolve.add(generate);
    grid.add(generate, gbc);
    
    JLabel xSizeLabel = new JLabel("X Size: ");
    gbc.gridx = 1;
    gbc.gridy = 1;
    grid.add(xSizeLabel, gbc);
    
    gbc.gridx = 2;
    grid.add(xSizeText, gbc);
    
    JLabel ySizeLabel = new JLabel("Y Size: ");
    gbc.gridx = 1;
    gbc.gridy = 2;
    grid.add(ySizeLabel, gbc);
    
    gbc.gridx = 2;
    grid.add(ySizeText, gbc);
    
    makeSolve.add(solve);
    gbc.gridx = 0;
    gbc.gridy = 3;
    grid.add(solve, gbc);
    
    gbc.gridx = 1;
    gbc.gridy = 4;
    grid.add(go, gbc);
    go.setActionCommand("go");
    go.addActionListener(this);
    
    add(grid);
    setVisible(true);
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    System.out.println("action");
    if ("go".equals(e.getActionCommand())) {
      if (solve.isSelected()) {
        new MazeSolver(new ImageProcessor(new File("../res/test.png")));
        System.out.println("hey");
      }
      else if (generate.isSelected() && !xSizeText.getText().equals("") && !ySizeText.getText().equals("")) {
        new MazeGenerator(new ImageProcessor(new File("../res/test.png")), Integer.parseInt(xSizeText.getText()), Integer.parseInt(ySizeText.getText()), "test");
        System.out.println("heyo");
      }
    }
  }
  
  private static void createAndShowGUI() {
    JFrame frame = new JFrame("Maze Solver Launcher");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(200, 400);
    frame.setResizable(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setUndecorated(false);
    
    //Create and set up the content pane.
    JComponent newContentPane = new Main();
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);
    
    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }
  
  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI(); 
      }
    });
  }
}