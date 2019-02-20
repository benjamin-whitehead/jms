// File: Node.java
// Author: Benjamin Whitehead
// Documentor: Benjamin Whitehead
// Date: 11 May 2017

import java.awt.Point;

public class Node {
 
  // Current Position of Node
  private Point position;
  // NodeID, current Node Number
  private static int nodeID;
  
  public Node(int x, int y) {
    this.position = new Point(x, y);
    nodeID++;
  }
  
  public Point getPosition() {
    return this.position;
  }
  
  public int getNodeID() {
    return nodeID;
  }
  
  public String toString() {
    return ("Node: " + nodeID + " XPOS: [" + position.getX() + "] YPOS: [" + position.getY() + "]");
  }
  
    
    
}