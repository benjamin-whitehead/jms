import java.io.File;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;

public class MazeGenLauncher extends JFrame {
  // Total Size of the Screen
  private final Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private final int ScreenHeight = (int) ScreenSize.getHeight() - 70;
  private final int ScreenWidth = (int) ScreenSize.getWidth();
  // AdjacencyMatrix from MazeSolver
  private int[][] adjacencyMatrix;
  //White out stopping variable
  private boolean first = true;
  //Graphics variables
  private int xSize = 0, ySize = 0, xAdjust = 9, yAdjust = 31;
  private Point future = new Point(0, 0);
  
//Toggleable
  //Number The Boxes
  private final boolean boxNumbers = false;
  
  // Constructor that takes in AdjacencyMatrix and a solved path, sets up the GUI
  public MazeGenLauncher(int[][] adjacencyMatrix){
    this.adjacencyMatrix = adjacencyMatrix;
    
    //Screen commands
    setSize(ScreenWidth,ScreenHeight);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setResizable(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setUndecorated(false);
    setVisible(true);
  }
  
  // Graphics Handler Code
  public void paint(Graphics g){
    if (first){
      //Blank out rectangle on screen
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, ScreenWidth, ScreenHeight); 
      
      //Main sqaure grid
      int SquareSize = ScreenHeight;
      if (ScreenHeight > ScreenWidth)
        SquareSize = ScreenWidth;
      
      xSize = (SquareSize / adjacencyMatrix.length);
      ySize = (SquareSize / adjacencyMatrix[0].length);
      
      g.setColor(Color.BLACK);
      g.fillRect(xAdjust, yAdjust, xSize * adjacencyMatrix[0].length, ySize * adjacencyMatrix.length);
      
      boolean draw = true;
      
      for (int x = 0; x < adjacencyMatrix.length; x++)
        for (int y = 0; y < adjacencyMatrix[0].length; y++){
        draw = true;
        switch (adjacencyMatrix[x][y]){
          case 0: g.setColor(Color.BLACK); draw = false; break;
          case 1: g.setColor(Color.WHITE); break;
          case 2: g.setColor(Color.LIGHT_GRAY); break;
          case 3: g.setColor(Color.GREEN); break;
          case 4: g.setColor(Color.YELLOW); break;
          case 5: g.setColor(Color.ORANGE); break;
          case 6: g.setColor(Color.RED); break;
        }
        if (draw)
          g.fillRect(xAdjust + x * xSize + 1, yAdjust + y * ySize + 1, xSize - 1, ySize - 1);
        
        if (adjacencyMatrix[x][y] != 0)
          g.setColor(Color.BLACK);
        else
          g.setColor(Color.WHITE);
        
        if (boxNumbers){
          g.drawString(y + "", xAdjust + x * xSize, yAdjust + (y + 1) * ySize);
          g.drawString(x + "", xAdjust + x * xSize, yAdjust + (int) ySize / 2 + y * ySize);
        }
      }
      first = false;
    }
    
    if (!future.equals(new Point(0, 0))){
      int x2 = (int) future.getX();
      int y2 = (int) future.getY();
      //System.out.println("Draw Point: (" + x2 + ", " + y2 + ")");
        
      g.setColor(Color.WHITE);
      g.fillRect(xAdjust + x2 * xSize + 1, yAdjust + y2 * ySize + 1, xSize - 1, ySize - 1);
      
      future = new Point(0, 0);
    }
  }
  
  public void addPoint(Point future){
    this.future = future;
    repaint();
  }
  
  public static void main(String[] args){
    new MazeGenLauncher(new ImageProcessor(new File("../res/test.png")).createAdjacencyMatrix());
  }
}