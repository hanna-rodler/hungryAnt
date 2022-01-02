import kwm.Drawing;
import kwm.ant.AntMatrix;

/*-----------------------------------------------------------------------------
 * AntMatrixTest.java
 * Author: Hanna Rodler
 * Created: 06.04.2021
 * Last Changed: 14.04.2021
 * Task: OOP Aufgabe 4
 *
 * This class tests AntMatrix.java.
 * --------------------------------------------------------------------------*/
public class AntMatrixTest{
  public static void main(String[] args){
    Drawing.begin("HungryAnt", 600, 500);
    AntMatrix hungryAnt=new AntMatrix(12, 10);
    hungryAnt.startAnimation();
    
    Drawing.end();
  }
}
