// File: ImageProcessor.java
// Author: Benjamin Whitehead
// Documentor: Benjamin Whitehead
// Date: 14 May 2018

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

public class ImageProcessor {
  // Constant values for both BLACK and WHITE RGB
  //private final int BLACK_RGB = -1, WHITE_RGB = 16777216;
  // Maze, which is the raw RGB values of the image, adjacencyMatrix of the graph which is declared later.
  private int[][] adjacencyMatrix;
  // HashMap representation of the maze, is used to convert to adjacencyMatrix later on
  private static HashMap<Integer, ArrayList<Node>> mazeGraph;
  // Variable to represent the height of the image and the width of the image, as well as the actual imageMap 
  public static int imageHeight, imageWidth, imageMap[][];
  // Actual input image
  private BufferedImage image;
  
  // Constructor that takes in the path to the image
  public ImageProcessor(File imagePath) {
    try {
      System.out.println("Loading file: " + imagePath.toString());
      this.image = ImageIO.read(imagePath);
      ImageProcessor.imageHeight = image.getHeight();
      ImageProcessor.imageWidth = image.getWidth();
      ImageProcessor.imageMap = new int[imageWidth][imageHeight];
      System.out.println("Width: [" + imageWidth + "] Height: [" + imageHeight + "]");
      imageMap = getImageMap();
      mazeGraph = populateGraph(imageMap);
      adjacencyMatrix = createAdjacencyMatrix();
    }
    catch(Exception ex) {
      ex.printStackTrace();
      System.exit(-1);
    }
  }
  
  // Returns the imageMap 2D Array
  public int[][] getImageMap() {
    for(int row = 0; row < imageMap.length; row++) {
      for(int col = 0; col < imageMap[row].length; col++) {
        imageMap[row][col] = image.getRGB(row, col);
      }
    }
    return ImageProcessor.imageMap;
  }
  
  // Method that takes in a 2D RGB Image Array and saves it to a png image 
  public void createImage(int[][] image, String name) {
    try {
      BufferedImage sampleImage = new BufferedImage(image.length, image[0].length, BufferedImage.TYPE_INT_RGB);
      for(int row = 0; row < image.length; row++) {
        for(int col = 0; col < image[row].length; col++) {
          sampleImage.setRGB(row, col, image[row][col]);
        }
      } 
      File output = new File("../res/" + name + ".png");
      ImageIO.write(sampleImage, "png", output);  
      System.out.println("Saving image to: " + output.toString());
    }
    catch(Exception ex) {
      ex.printStackTrace();
      System.exit(-1);
    }
  }
  
  // Method that takes in a 2D RGB Image Array and saves it to a png image 
  public void createImage(int[][] image, ArrayList<Point> path, String name) {
    int red = Color.RED.getRGB();
    try {
      BufferedImage sampleImage = new BufferedImage(image.length, image[0].length, BufferedImage.TYPE_INT_RGB);
      for(int row = 0; row < image.length; row++) {
        for(int col = 0; col < image[row].length; col++) {
          sampleImage.setRGB(row, col, image[row][col]);
          if (path.contains(new Point(row, col)))
            sampleImage.setRGB(row, col, red);
        }
      } 
      File output = new File("../res/" + name + ".png");
      ImageIO.write(sampleImage, "png", output);  
      System.out.println("Saving image to: " + output.toString());
    }
    catch(Exception ex) {
      ex.printStackTrace();
      System.exit(-1);
    }
  }
  
  // Method that creates and stores all the nodes for the maze
  public HashMap<Integer, ArrayList<Node>> populateGraph(int[][] maze) {
    // Creates a temporary HashMap that is returned at the end of the method.
    HashMap<Integer, ArrayList<Node>> temp = new HashMap<Integer, ArrayList<Node>>();
    for(int row = 0; row < maze.length; row++) {
      // Creates a new ArrayList to store all of the nodes in the row of the maze
      ArrayList<Node> current = new ArrayList<Node>();
      for(int col = 0; col < maze[0].length; col++) {
        // If the maze has a raw pixel data of -1 (black) create a node there and add it to the ArrayList
        if(maze[row][col] == -1) {
          Node currentNode = new Node(row, col);
          current.add(currentNode);
        }
      }
      // Add the ArrayList to the HashMap
      temp.put(row, current);
    }
    // Return the HashMap
    return temp;
  }
  
  // Method that creates an AdjacencyMatrix, takes in a graph, as well as the height and width of the maze
  public int[][] createAdjacencyMatrix() {
    int[][] temp = new int[imageHeight][imageWidth];
    System.out.println("Creating Adjacency Matrix: Width [" + imageWidth + "] Height [" + imageHeight + "]");
    // Loop through the HashMap
    for(int x = 0; x < mazeGraph.size(); x++) {
      // Retrieve the ArrayList from the HashMap
      ArrayList<Node> current = mazeGraph.get(x);
      for(Node y : current)
        // At the node X and Y, place a 1 on the adjacency matrix
        temp[(int) y.getPosition().getX()][(int) y.getPosition().getY()] = 1;
    }
    // Return the adjacencyMatrix
    return temp;  
  }
  
  public int[][] mazeImage(int[][] maze){
    int[][] RGBmaze = new int[maze.length][maze[0].length];
    for (int x = 0; x < maze.length; x++)
      for (int y = 0; y < maze[0].length; y++){
        if (maze[x][y] == 1)
          RGBmaze[x][y] = -1;
        else
          RGBmaze[x][y] = 16777216;
      }
    return RGBmaze;
  }
  
  public int getImageHeight() {
    return imageHeight;
  }
  
  public int getImageWidth() {
    return imageWidth;
  }
  
  public int[][] getAdjacency(){
    return adjacencyMatrix;
  }
}