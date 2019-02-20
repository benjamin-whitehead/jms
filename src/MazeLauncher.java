// File: MazeLauncher.java
// Author: Jason Domogala
// Documentor: Benjamin Whitehead
// Date: 11 May 2018

import java.io.File;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;

import java.awt.Toolkit;
import java.awt.Dimension;

public class MazeLauncher extends JFrame {
  
  // Total Size of the Screen
  private final Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private final int ScreenHeight = (int) ScreenSize.getHeight() - 70;
  private final int ScreenWidth = (int) ScreenSize.getWidth();
  // AdjacencyMatrix from MazeSolver
  private int[][] adjacencyMatrix;
  // Path found using MazeSolver
  private ArrayList<Point> path;
  private boolean pathEnabled;
  
  //Number The Boxes
  private final boolean boxNumbers = false;
  
  // Constructor that takes in the adjacencyMatrix and sets up the GUI
  public MazeLauncher(int[][] adjacencyMatrix){
    this.adjacencyMatrix = adjacencyMatrix;
    setSize(ScreenWidth, ScreenHeight);
    //System.out.println(ScreenWidth);
    //System.out.println(ScreenHeight);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setResizable(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setUndecorated(false);
    setVisible(true);
  }
  
  // Constructor that takes in AdjacencyMatrix and a solved path, sets up the GUI
  public MazeLauncher(int[][] adjacencyMatrix, ArrayList<Point> path){
    pathEnabled = true;
    this.adjacencyMatrix = adjacencyMatrix;
    this.path = path;
    
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
    //Blank out rectangle on screen
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, ScreenWidth, ScreenHeight); 
    
    //Main sqaure grid
    int SquareSize = ScreenHeight;
    if (ScreenHeight > ScreenWidth)
      SquareSize = ScreenWidth;
    
    int xSize = (SquareSize / adjacencyMatrix.length);
    int ySize = (SquareSize / adjacencyMatrix[0].length);
    int yAdjust = 31;
    int xAdjust = 9;
    
    for (int x = 0; x < adjacencyMatrix.length; x++)
      for (int y = 0; y < adjacencyMatrix[0].length; y++){
      switch (adjacencyMatrix[x][y]){
        case 0: g.setColor(Color.BLACK); break;
        case 1: g.setColor(Color.WHITE); break;
        case 2: g.setColor(Color.WHITE); break;
        case 3: g.setColor(Color.GREEN); break;
        case 4: g.setColor(Color.YELLOW); break;
        case 5: g.setColor(Color.ORANGE); break;
        case 6: g.setColor(Color.RED); break;
      }
      g.fillRect(xAdjust + x * xSize, yAdjust + y * ySize, xSize, ySize);
      g.setColor(Color.BLACK);
      g.drawRect(xAdjust + x * xSize, yAdjust + y * ySize, xSize, ySize);
      
      if (adjacencyMatrix[x][y] != 0)
        g.setColor(Color.BLACK);
      else
        g.setColor(Color.WHITE);
      
      if (boxNumbers){
        g.drawString(y + "", xAdjust + x * xSize, yAdjust + (y + 1) * ySize);
        g.drawString(x + "", xAdjust + x * xSize, yAdjust + (int) ySize / 2 + y * ySize);
      }
    }
    
    if (pathEnabled){
      for (int g2 = 0; g2 < path.size() - 1; g2++){
        Point currentPoint = path.get(g2);
        int x2 = (int) currentPoint.getX();
        int y2 = (int) currentPoint.getY();
        
        g.setColor(Color.GRAY);
        g.fillRect(xAdjust + x2 * xSize, yAdjust + y2 * ySize, xSize, ySize);
        g.setColor(Color.BLACK);
        g.drawRect(xAdjust + x2 * xSize, yAdjust + y2 * ySize, xSize, ySize);
        
        Point currentEndPoint = path.get(g2 + 1);
        int x3 = (int) currentEndPoint.getX();
        int y3 = (int) currentEndPoint.getY();
      
        g.setColor(Color.DARK_GRAY);
        g.fillRect(xAdjust + x3 * xSize, yAdjust + y3 * ySize, xSize, ySize);
        g.setColor(Color.BLACK);
        g.drawRect(xAdjust + x3 * xSize, yAdjust + y3 * ySize, xSize, ySize);
        
        try{
          Thread.sleep(2);
        }
        catch(InterruptedException ex){
          System.out.println(ex);
        }
      }
    }
  }
  
  public static void main(String[] args){
    ImageProcessor processor = new ImageProcessor(new File("../res/test.png"));
    new MazeLauncher(processor.createAdjacencyMatrix());
  }
}