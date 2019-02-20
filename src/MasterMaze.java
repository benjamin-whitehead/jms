import java.io.File;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.MouseInfo;

public class MasterMaze extends JFrame {
  private final int screenSize = 790;
  public int[][] adjacencyMatrix;
  private ArrayList<Point> criticalPoints;
  private ArrayList<Point> path = new ArrayList<Point>();
  //private ImageProcessor processor;
  private int pathInt = 0;
  private boolean finished = false, pathEnabled = true;
  
  //Find Maze Path Yourself
  public MasterMaze(int[][] adjacencyMatrix, ImageProcessor processor){
    final int[][] maze = adjacencyMatrix;
    this.adjacencyMatrix = adjacencyMatrix;
    //this.processor = processor;
    setSize(1440,797);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setUndecorated(true);
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
    
    //Eventual Mouse Follow and necessities
    addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        int mouseX = (int) mouse.getX();
        int mouseY = (int) mouse.getY() - 28;
        
        //1255, 5, 180, 140
        if (mouseX >= 1255 && mouseX <= 1395 && mouseY >= 5 && mouseY <= 145){
          MazeSolve();
        }
      }
    });
    
    repaint();
    finished = gameFinish();
    while(!finished){
      try{
        Thread.sleep(1000);
      }
      catch(InterruptedException ex){
        System.out.println(ex);
      }
      finished = gameFinish();
    }
    
    repaint();
  }
  
  public void MazeSolve(){
    //ArrayList<Point> path = new MazeSolvercopy2(processor).shortest;
  }
  
  public boolean gameFinish(){
    return criticalPoints.get(1).equals(path.get(pathInt));
  }
  
  public void paint(Graphics g){
    //Blank out screen
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, 1440, 797);
    
    //Main grid
    int xSize = (screenSize / adjacencyMatrix.length);
    int ySize = (screenSize / adjacencyMatrix[0].length);
    for (int x = 0; x < adjacencyMatrix.length; x++)
      for (int y = 0; y < adjacencyMatrix[0].length; y++){
      if (adjacencyMatrix[x][y] == 1){
        g.setColor(Color.WHITE);
        g.fillRect(5 + x * xSize, 5 + y * ySize, xSize, ySize);
        g.setColor(Color.BLACK);
        g.drawRect(5 + x * xSize, 5 + y * ySize, xSize, ySize);
      }
      else if (adjacencyMatrix[x][y] == 2){
        g.setColor(Color.BLUE);
        g.fillRect(5 + x * xSize, 5 + y * ySize, xSize, ySize);
        g.setColor(Color.BLACK);
        g.drawRect(5 + x * xSize, 5 + y * ySize, xSize, ySize);
      }
      else{
        g.setColor(Color.BLACK);
        g.fillRect(5 + x * xSize, 5 + y * ySize, xSize, ySize);
        //g.setColor(Color.WHITE);
        g.drawRect(5 + x * xSize, 5 + y * ySize, xSize, ySize);
      }
    }
    
    if (pathEnabled){
      for (int g2 = 0; g2 < pathInt; g2++){
        Point currentPoint = path.get(g2);
        int x2 = (int) currentPoint.getX();
        int y2 = (int) currentPoint.getY();
        
        g.setColor(Color.GRAY);
        g.fillRect(5 + x2 * xSize, 5 + y2 * ySize, xSize, ySize);
        g.setColor(Color.BLACK);
        g.drawRect(5 + x2 * xSize, 5 + y2 * ySize, xSize, ySize);
      }
      
      Point currentPoint = path.get(pathInt);
      int x2 = (int) currentPoint.getX();
      int y2 = (int) currentPoint.getY();
      
      g.setColor(Color.DARK_GRAY);
      g.fillRect(5 + x2 * xSize, 5 + y2 * ySize, xSize, ySize);
      g.setColor(Color.BLACK);
      g.drawRect(5 + x2 * xSize, 5 + y2 * ySize, xSize, ySize);
      
      //Move in path
      if (pathInt < path.size() - 1)
        pathInt++;
      else
        finished = true;
    }
    
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(1255, 5, 180, 140);
    g.setColor(Color.BLACK);
    g.drawRect(1255, 5, 180, 140);
  }
  
  public static void main(String[] args){
    ImageProcessor processor = new ImageProcessor(new File("../res/test.png"));
    new MasterMaze(processor.createAdjacencyMatrix(), processor);
  }
}