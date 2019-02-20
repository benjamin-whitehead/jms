import java.io.File;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.Dimension;

public class MazeRunner extends JFrame {
  private final Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private final int ScreenHeight = (int) ScreenSize.getHeight() - 70;
  private final int ScreenWidth = (int) ScreenSize.getWidth();
  public int[][] adjacencyMatrix;
  private ArrayList<Point> criticalPoints;
  private ArrayList<Point> path = new ArrayList<Point>();
  private ArrayList<Point> whiteOut = new ArrayList<Point>();
  private int pathInt = 0;
  private boolean pathEnabled = true, firstTime = true;
  private int xSize, ySize, yAdjust, xAdjust;
  
  //Find Maze Path Yourself
  public MazeRunner(int[][] adjacencyMatrix){
    final int[][] maze = adjacencyMatrix;
    this.adjacencyMatrix = adjacencyMatrix;
    setSize(ScreenWidth, ScreenHeight);
    setResizable(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setUndecorated(false);
    setVisible(true);
    
    criticalPoints = MazeSolver.findCriticalPoints(adjacencyMatrix);
    path.add(criticalPoints.get(0));
    
    addKeyListener(new KeyAdapter(){
      public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT && !gameFinish()){
          int x = (int) path.get(pathInt).getX(), y = (int) path.get(pathInt).getY();
          if (x > 0 && maze[x - 1][y] == 1){
            if (pathInt > 0 && path.get(pathInt - 1).equals(new Point(x - 1, y))){
              whiteOut.add(path.get(pathInt));
              path.remove(pathInt);
              pathInt--;
            }
            else{
              path.add(new Point(x - 1, y));
              pathInt++;
            }          
          }
          repaint();
        }
        else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT && !gameFinish()){
          int x = (int) path.get(pathInt).getX(), y = (int) path.get(pathInt).getY();
          if (x < maze.length - 1 && maze[x + 1][y] == 1){
            if (pathInt > 0 && path.get(pathInt - 1).equals(new Point(x + 1, y))){
              whiteOut.add(path.get(pathInt));
              path.remove(pathInt);
              pathInt--;
            }
            else{
              path.add(new Point(x + 1, y));
              pathInt++;
            }          
          }
          repaint();
        }
        else if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP && !gameFinish()){
          int x = (int) path.get(pathInt).getX(), y = (int) path.get(pathInt).getY();
          if (y > 0 && maze[x][y - 1] == 1){
            if (pathInt > 0 && path.get(pathInt - 1).equals(new Point(x, y - 1))){
              whiteOut.add(path.get(pathInt));
              path.remove(pathInt);
              pathInt--;
            }
            else{
              path.add(new Point(x, y - 1));
              pathInt++;
            }          
          }
          repaint();
        }
        else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN && !gameFinish()){
          int x = (int) path.get(pathInt).getX(), y = (int) path.get(pathInt).getY();
          if (y < maze[0].length - 1 && maze[x][y + 1] == 1){
            if (pathInt > 0 && path.get(pathInt - 1).equals(new Point(x, y + 1))){
              whiteOut.add(path.get(pathInt));
              path.remove(pathInt);
              pathInt--;
            }
            else{
              path.add(new Point(x, y + 1));
              pathInt++;
            }          
          }
          repaint();
        }
      }
    });
    
    //Eventual Mouse Follow
    /*
    addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        int mouseX = (int) mouse.getX();
        int mouseY = (int) mouse.getY() - 28;
      }
    });
    */
  }
  
  public boolean gameFinish(){
    return criticalPoints.get(1).equals(path.get(pathInt));
  }
  
  //Versus AI mode
  public MazeRunner(int[][] adjacencyMatrix, ArrayList<Point> path){
  }
  
  public void paint(Graphics g){
    if (firstTime){
      //Blank out screen
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, ScreenWidth, ScreenHeight);
      
      //Main grid
      int SquareSize = ScreenHeight;
      if (ScreenHeight > ScreenWidth)
        SquareSize = ScreenWidth;
      
      xSize = (SquareSize / adjacencyMatrix.length);
      ySize = (SquareSize / adjacencyMatrix[0].length);
      yAdjust = 31;
      xAdjust = 9;
      
      for (int x = 0; x < adjacencyMatrix.length; x++)
        for (int y = 0; y < adjacencyMatrix[0].length; y++){
        if (adjacencyMatrix[x][y] == 1){
          g.setColor(Color.WHITE);
          g.fillRect(xAdjust + x * xSize, yAdjust + y * ySize, xSize, ySize);
          g.setColor(Color.BLACK);
          g.drawRect(xAdjust + x * xSize, yAdjust + y * ySize, xSize, ySize);
        }
        else if (adjacencyMatrix[x][y] == 2){
          g.setColor(Color.BLUE);
          g.fillRect(xAdjust + x * xSize, yAdjust + y * ySize, xSize, ySize);
          g.setColor(Color.BLACK);
          g.drawRect(xAdjust + x * xSize, yAdjust + y * ySize, xSize, ySize);
        }
        else{
          g.setColor(Color.BLACK);
          g.fillRect(xAdjust + x * xSize, yAdjust + y * ySize, xSize, ySize);
          //g.setColor(Color.WHITE);
          g.drawRect(xAdjust + x * xSize, yAdjust + y * ySize, xSize, ySize);
        }
      }
      firstTime = false;
    }
    
    if (pathEnabled){
      Point currentEndPoint = path.get(pathInt);
      int x3 = (int) currentEndPoint.getX();
      int y3 = (int) currentEndPoint.getY();
      
      g.setColor(Color.DARK_GRAY);
      g.fillRect(xAdjust + x3 * xSize, yAdjust + y3 * ySize, xSize, ySize);
      g.setColor(Color.BLACK);
      g.drawRect(xAdjust + x3 * xSize, yAdjust + y3 * ySize, xSize, ySize);
      
      if (pathInt > 0){
        Point currentPoint = path.get(pathInt - 1);
        int x2 = (int) currentPoint.getX();
        int y2 = (int) currentPoint.getY();
      
        g.setColor(Color.GRAY);
        g.fillRect(xAdjust + x2 * xSize, yAdjust + y2 * ySize, xSize, ySize);
        g.setColor(Color.BLACK);
        g.drawRect(xAdjust + x2 * xSize, yAdjust + y2 * ySize, xSize, ySize);
      }
      
      if (whiteOut.size() > 0){
        Point whitePoint = whiteOut.get(0);
        whiteOut.remove(0);
        int x4 = (int) whitePoint.getX();
        int y4 = (int) whitePoint.getY();
        
        g.setColor(Color.WHITE);
        g.fillRect(xAdjust + x4 * xSize, yAdjust + y4 * ySize, xSize, ySize);
        g.setColor(Color.BLACK);
        g.drawRect(xAdjust + x4 * xSize, yAdjust + y4 * ySize, xSize, ySize);
      }
    }
    //Move in path
    if (pathInt < path.size() - 1)
      pathInt++;
  }
  
  public static void main(String[] args){
    new MazeRunner(new ImageProcessor(new File("../res/test.png")).createAdjacencyMatrix());
  }
}