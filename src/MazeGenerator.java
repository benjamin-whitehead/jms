import java.util.ArrayList;
import java.awt.Point;
import java.io.File;

public class MazeGenerator { 
  private int[][] maze;
  private Point critOne, critTwo;
  private MazeGenLauncher launcher;
  
  public MazeGenerator(ImageProcessor processor, int xDimension, int yDimension, String name) {
    maze = new int[xDimension][yDimension];
    PaintMaze();
    CritPoints();
    try{
      Thread.sleep((int) Math.pow(xDimension, 1.5));
    }
    catch(InterruptedException ex){
      System.out.println(ex);
    }
    System.out.println("Done pausing.");
    
    HoodMaze();
    processor.createImage(processor.mazeImage(maze), name);
  }
  
  public void ChangeCoords(Point changed){
    int x = (int) changed.getX();
    int y = (int) changed.getY();
    maze[x][y] = 1;
    launcher.addPoint(changed);
    
    //Pause
    try{
      Thread.sleep(1);
    }
    catch(InterruptedException ex){
      System.out.println(ex);
    }
  }
  
  public void PaintMaze(){
    //Checkerboard the maze
    for(int x = 0; x < maze.length; x++)
      for(int y = 0; y < maze[0].length; y++)
        //Odd squares are white
        if (x % 2 == 1 && y % 2 == 1)
          maze[x][y] = 1;
        //Rest is black
        else
          maze[x][y] = 0;
  }
  
  public void CritPoints(){
    //Pick Start and End sides of maze
    int rand = (int) (Math.random() * 4) + 1, rand2 = (int) (Math.random() * 4) + 1;
    //System.out.println("Rand1: " + rand + " Rand2: " + rand2);
    
    //Choose X and Y coordinates for the "Start"
    switch (rand){
      case 1: critOne = new Point(RandCord(true), 0); break;
      case 2: critOne = new Point(RandCord(true), maze[0].length - 1); break;
      case 3: critOne = new Point(0, RandCord(true)); break;
      case 4: critOne = new Point(maze.length - 1, RandCord(true)); break;
    }
    
    //Choose X and Y coordinates for the "End"
    switch (rand2){
      case 1: critTwo = new Point(RandCord(true), 0); break;
      case 2: critTwo = new Point(RandCord(true), maze[0].length - 1); break;
      case 3: critTwo = new Point(0, RandCord(true)); break;
      case 4: critTwo = new Point(maze.length - 1, RandCord(true)); break;
    }

    //Change Start and End to be Blank Cells
    maze[(int) critOne.getX()][(int) critOne.getY()] = 1;
    System.out.println("Crit One: (" + (int) critOne.getX() + ", " + (int) critOne.getY() + ")");
    maze[(int) critTwo.getX()][(int) critTwo.getY()] = 1;
    System.out.println("Crit Two : (" + (int) critTwo.getX() + ", " + (int) critTwo.getY() + ")");
    
    //Draw Maze
    launcher = new MazeGenLauncher(maze);
  }
  
  public int RandCord(boolean odder){
    int randCord = (int) (Math.random() * (maze.length - 2)) + 1;
    if (odder && randCord % 2 == 0){
      if (randCord > 1)
        randCord--;
      else
        randCord++;
    }
    return randCord;
  }
  
  public int checkUsage(Point check){
    int usage = 0;
    int x = (int) check.getX();
    int y = (int) check.getY();
    //System.out.println("CU Point: (" + x + ", " + y + ")");
    if (x < maze.length - 2 && maze[x + 1][y] == 1)
      usage++;
    if (x > 2 && maze[x - 1][y] == 1)
      usage++;
    if (y < maze[0].length - 2 && maze[x][y + 1] == 1)
      usage++;
    if (y > 2 && maze[x][y - 1] == 1)
      usage++;
    //System.out.println("Usage: " + usage + " Success");
    return usage;
  }

  public Point nearestCell(Point crit){
    Point start = crit;
    if (crit.getX() == 0)
      start = new Point((int) crit.getX() + 1, (int) crit.getY());
    else if (crit.getX() == maze[0].length - 1)
      start = new Point((int) crit.getX() - 1, (int) crit.getY());
    else if (crit.getY() == 0)
      start = new Point((int) crit.getX(), (int) crit.getY() + 1);
    else if (crit.getY() == maze.length - 1)
      start = new Point((int) crit.getX(), (int) crit.getY() - 1);
    return start;
  }
  
  public void HoodMaze(){
    ArrayList<Point> mainPath = new ArrayList<Point>();
    ArrayList<Point> alongPath = new ArrayList<Point>();
    ArrayList<Point> unusedPoints = new ArrayList<Point>();
    
    Point startOne = nearestCell(critOne);
    unusedPoints.remove(startOne);
    Point startTwo = nearestCell(critTwo);
    unusedPoints.remove(startTwo);
    Point current = new Point((int) startOne.getX(), (int) startOne.getY());
    
    for (int x = 1; x < maze.length - 1; x += 2)
      for (int y = 1; y < maze[0].length - 1; y += 2)
        unusedPoints.add(new Point(x, y));
    
    while (unusedPoints.size() > 1){
      //System.out.println("Unused Points: " + unusedPoints.size());
      Point future = randomDirection(current);
      if (!future.equals(new Point(0, 0))){
        current = future;
        unusedPoints.remove(current);
        mainPath.add(current);
        alongPath.add(current);
      }
      else if (mainPath.size() > 0){
        current = mainPath.remove((int) (Math.random() * mainPath.size()));
      }
      //System.out.println("Current: (" + ((int) current.getX()) + ", " + ((int) current.getY()) + ")");
    }
  }
  
  public Point randomDirection(Point start){
    int x = (int) start.getX(), y = (int) start.getY();
    //System.out.println("Rando: (" + x + ", " + y + ")");
    boolean changed = false;
    Point next = new Point(0, 0);
    
    //ArrayList of 1 to 4 that determines order
    ArrayList<Integer> order = new ArrayList<Integer>();
    order.add(1); order.add(2); order.add(3); order.add(4);
    
    //Take random of 1 to 4 to try in order
    for (int count = 0; count < 4 && !changed; count++){
      switch (order.remove((int) (Math.random() * order.size()))){
        case 1: 
          if (x < maze.length - 2 && checkUsage(new Point(x + 2, y)) == 0){
            next = new Point(x + 2, y);
            ChangeCoords(new Point(x + 1, y));
            changed = true;
          }
          break;
        case 2: 
          if (x > 2 && checkUsage(new Point(x - 2, y)) == 0){
            next = new Point(x - 2, y);
            ChangeCoords(new Point (x - 1, y));
            changed = true;
          }
          break;
        case 3: 
          if (y < maze[0].length - 2 && checkUsage(new Point(x, y + 2)) == 0){
            next = new Point(x, y + 2);
            ChangeCoords(new Point(x, y + 1));
            changed = true;
          }
          break;
        case 4: 
          if (y > 2 && checkUsage(new Point(x, y - 2)) == 0){
            next = new Point(x, y - 2);
            ChangeCoords(new Point(x, y - 1));
            changed = true;
          }
          break;
      }
    }
    //System.out.println("Next: " + next);
    return next;
  }

  public static void main(String[] args){
    //Grab test.png, make X, by Y, save as "test"
    //new MazeGenerator(new ImageProcessor(new File("../res/test.png")), 21, 21, "test");
    //new MazeGenerator(new ImageProcessor(new File("../res/test.png")), 51, 51, "test");
    new MazeGenerator(new ImageProcessor(new File("../res/test.png")), 131, 131, "test");
    //new MazeGenerator(new ImageProcessor(new File("../res/test.png")), 501, 501, "test");
  }
}