// File: MazeSolver.java
// Authors: Jason Domogala, Benjamin Whitehead
// Date: 11 May 2018

import java.util.ArrayList;
import java.awt.Point;
import java.io.File;

public class MazeSolver {

  // Maze, which is the raw RGB values of the image, adjacencyMatrix of the graph which is declared later.
  public int maze[][], adjacencyMatrix[][];
  //Jframe
  public MazeLauncher mazeLaunch;
  
  public ArrayList<Point> shortest = new ArrayList<Point>();
  
  // Constructor that takes in an ImageProcessor to do all the converting to and from temRGB -> Image
  // Converts the maze back into a PNG at the end.
  public MazeSolver(ImageProcessor processor){
    this.maze = processor.getImageMap();
    adjacencyMatrix = processor.createAdjacencyMatrix();
    //mazeLaunch = new MazeLauncher(adjacencyMatrix);
    adjacencyMatrix = deadEnder(adjacencyMatrix);
    //mazeLaunch = new MazeLauncher(adjacencyMatrix);
    adjacencyMatrix = looper(adjacencyMatrix);
    
    //Get All Paths
    ArrayList<ArrayList<Point>> solvedPaths = solveAlgorithm(adjacencyMatrix);
    System.out.println("Number of Paths: " + solvedPaths.size());
    int sizer = solvedPaths.size();
    
    //Get Shortest Path
    for (int x = 0; x < sizer; x++){
      if (solvedPaths.get(x).size() < shortest.size() || shortest.size() == 0)
        shortest = solvedPaths.get(x);
      mazeLaunch = new MazeLauncher(adjacencyMatrix, solvedPaths.get(x));
      processor.createImage(maze, "output");
    }
    System.out.println("Shortest Path: " + shortest.size());
      
    //processor.createImage(maze, "output");
  }
  
  //The best maze solver the world hasn't seen
  private ArrayList<ArrayList<Point>> solveAlgorithm(int[][] maze) {
    //All the splits and possiblities
    ArrayList<ArrayList<Point>> allPaths = new ArrayList<ArrayList<Point>>();
    ArrayList<ArrayList<Point>> solvedPaths = new ArrayList<ArrayList<Point>>();
    
    //Current path being looked at
    ArrayList<Point> currentPath = new ArrayList<Point>();
    
    //Start and end point
    ArrayList<Point> criticalPoints = findCriticalPoints(maze);
    ArrayList<Point> startList = new ArrayList<Point>();
    
    if (criticalPoints.size() == 2){
      startList.add(criticalPoints.get(0));
      currentPath.add(criticalPoints.get(0));
      allPaths.add(currentPath);
    }
    
    boolean end = false;
    
    while(allPaths.size() > 0 && !end){
      currentPath = allPaths.get(0);
      //System.out.println("Current Path: " + currentPath);
      //System.out.println("Current Path End: " + currentPath.get(currentPath.size() - 1));
      allPaths.remove(0);
      if (currentPath.get(currentPath.size() - 1).equals(criticalPoints.get(1))){
        //System.out.println("Current Path End: " + currentPath.get(currentPath.size() - 1) + " Path End: " + criticalPoints.get(1) + " Equals? " + currentPath.get(currentPath.size() - 1).equals(criticalPoints.get(1)));
        solvedPaths.add(0, currentPath);
        //end = true;
        //MazeLauncher(adjacencyMatrix, currentPath);
      }
      
      else{
        //System.out.println("Current Path End: " + currentPath.get(currentPath.size() - 1) + " Path End: " + criticalPoints.get(1) + " Equals? " + currentPath.get(currentPath.size() - 1).equals(criticalPoints.get(1)));
        ArrayList<Point> possibilities = checkAround(currentPath, maze);
        //System.out.println("Possibilities: " + possibilities);
        ArrayList<Point> temp = new ArrayList<Point>(currentPath);
        int sizer = possibilities.size();
        for (int i = 0; i < sizer; i++){
          temp.add(possibilities.get(0));
          //System.out.println("Temp: " + temp);
          allPaths.add(0, temp);
          possibilities.remove(0);
          temp = new ArrayList<Point>(currentPath);
        }
      }
    }

    return solvedPaths;
  }
  
  private ArrayList<Point> checkAround(ArrayList<Point> path, int[][] maze){
    ArrayList<Point> possibilities = new ArrayList<Point>();
    Point last = path.get(path.size() - 1);
    int x = (int) last.getX(), y = (int) last.getY();
    //System.out.println("Point last: (" + x + ", " + y + ")");
    if(y > 0 && !path.contains(new Point(x, y - 1)) && maze[x][y - 1] == 1){
      //System.out.println("(" + x + ", " + (y - 1) + ") " + maze[x][y - 1]);
      possibilities.add(new Point(x, y - 1));
    }
    if(y < maze[0].length - 1 && !path.contains(new Point(x, y + 1)) && maze[x][y + 1] == 1){
      //System.out.println("(" + x + ", " + (y + 1) + ") " + maze[x][y + 1]);
      possibilities.add(new Point(x, y + 1));
    }
    if(x < maze.length - 1 && !path.contains(new Point(x + 1, y)) && maze[x + 1][y] == 1){
      //System.out.println("(" + (x + 1) + ", " + y + ") " + maze[x + 1][y]);
      possibilities.add(new Point(x + 1, y));
    }
    if(x > 0 && !path.contains(new Point(x - 1, y)) && maze[x - 1][y] == 1){
      //System.out.println("(" + (x - 1) + ", " + y + ") " + maze[x - 1][y]);
      possibilities.add(new Point(x - 1, y));
    }
    //System.out.println("Possibilities: " + possibilities);
    return possibilities;
  }
  
  //Start and end point Finder
  public static ArrayList<Point> findCriticalPoints(int[][] maze) {
    ArrayList<Point> criticalPoints = new ArrayList<Point>();
    int pointsFound = 0;
    //System.out.println((maze.length - 1) + " " + (maze[0].length - 1));
    for(int x = 0; x < maze.length; x++) {
      if(maze[x][0] == 1){
        //System.out.println("(" + x + ", " + 0 + ") " + maze[x][0]);
        criticalPoints.add(new Point(x, 0)); pointsFound++;
      }
    }
    for(int y = 0; pointsFound < 2 && y < maze[0].length; y++) {
      if(maze[0][y] == 1){
        //System.out.println("(" + 0 + ", " + y + ") " + maze[0][y]);
        criticalPoints.add(new Point(0, y)); pointsFound++;
      }
    }
    for(int x = 0; pointsFound < 2 && x < maze.length; x++) {
      if(maze[x][maze[0].length - 1] == 1){
        //System.out.println("(" + x + ", " + (maze.length - 1) + ") " + maze[x][maze[0].length - 1]);
        criticalPoints.add(new Point(x, maze[0].length - 1)); pointsFound++;
      }
    }
    for(int y = 0; pointsFound < 2 && y < maze[0].length; y++) {
      if(maze[maze.length - 1][y] == 1){
        //System.out.println("(" + (maze.length - 1) + ", " + y + ") " + maze[maze.length - 1][y]);
        criticalPoints.add(new Point(maze.length - 1, y)); pointsFound++;
      }
    }
    System.out.println(criticalPoints);
    return criticalPoints;
  }
  
  private int[][] deadEnder(int[][] adjacencyMatrix){
    int[][] adjacency = arrayCopier(adjacencyMatrix);
    for(int x = 1; x < adjacencyMatrix.length - 1; x++) {  
      for(int y = 1; y < adjacencyMatrix[0].length - 1; y++) {
        adjacency = arrayCopier(adjacencyMatrix);
        if (adjacency[x][y] == 1){
          ArrayList<Point> deadList = new ArrayList<Point>();
          ArrayList<Point> killed = new ArrayList<Point>();
          deadList.add(new Point(x, y));
          int x2 = x, y2 = y;
          adjacency[x2][y2] = 2;
          //System.out.println("Dead Exp: (" + x2 + ", " + y2 + ")");
          deadList = checkAround(deadList, adjacency);
          //if (deadList.size() == 1)
            //adjacencyMatrix[x][y] = 2;
          boolean changed = false;
          /*
          if (deadList.size() > 0){
            Point capOff = deadList.get(0);
            adjacency[x2][y2] = 2;
            killed.add(capOff);
            //System.out.println("End Points: " + deadList + " Capped Off: (" + x2 + ", " + y2 + ")");
            x2 = (int) capOff.getX();
            y2 = (int) capOff.getY();
            deadList = checkAround(deadList, adjacency);
          }
          */
          
          //Keep on cutting off path if only one option
          while (deadList.size() == 1){
            changed = true;
            Point capOff = deadList.get(0);
            adjacency[x2][y2] = 2;
            //System.out.println("Dead String: (" + x2 + ", " + y2 + ")");
            killed.add(capOff);
            x2 = (int) capOff.getX();
            y2 = (int) capOff.getY();
            deadList = checkAround(deadList, adjacency);
          }
          
          //Checks for Dots
          if (deadList.size() == 0){
            changed = true;
            //System.out.println("Dead Dot: (" + x2 + ", " + y2 + ")");
            adjacency[x2][y2] = 2;
          }
          
          //Change adjacencyMatrix
          if (changed){
            adjacencyMatrix = arrayCopier(adjacency);
          }
        }
      }
    }
    return adjacencyMatrix;
  }
  
  private int[][] looper(int[][] adjacencyMatrix){
    int[][] adjacency = arrayCopier(adjacencyMatrix);
    for(int x = 1; x < adjacency.length - 1; x++) {  
      for(int y = 1; y < adjacency[0].length - 1; y++) {
        adjacency[x][y] = 3;
        Point current = new Point(x, y);
        ArrayList<Point> loop = new ArrayList<Point>();
        loop.add(current);
        ArrayList<Point> deadList = checkAround(loop, adjacency);
        
        //while (deadList.size() > 0){
        // 
        //}
      }
    }
    return adjacencyMatrix;
  }
  
  //Deep Array Copier
  private int[][] arrayCopier(int[][] copied){
    int[][] copy = new int[copied.length][copied[0].length];
    for (int x = 0; x < copied.length; x++)
      for (int y = 0; y < copied[0].length; y++)
        copy[x][y] = copied[x][y];
    return copy;
  }
  
  public void pointPrinter(String Message, Point printed){
    System.out.println(Message + "(" + ((int) printed.getX()) + ", " + ((int) printed.getY()) + ")");
  }
  
  public static void main(String[] args) {
    MazeSolver mazeSolve = new MazeSolver(new ImageProcessor(new File("../res/test.png")));
    MazeLauncher mazeLaunch = new MazeLauncher(mazeSolve.adjacencyMatrix, mazeSolve.shortest);
  }
  
  public int[][] getAdjacencyMatrix(){
    return adjacencyMatrix;
  }
}