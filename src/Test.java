import java.io.File;

public class Test {
  public static void main(String[] args) {
    new MazeGenerator(new ImageProcessor(new File("../res/test.png")), 251, 251, "test");
    
    new MazeSolver(new ImageProcessor(new File("../res/test.png")));
  }
}