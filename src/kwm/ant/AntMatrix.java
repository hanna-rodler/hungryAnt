package kwm.ant;

import kwm.Drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*-----------------------------------------------------------------------------
 * AntMatrix.java
 * Author: Hanna Rodler
 * Created: 06.04.2021
 * Last Changed: 14.04.2021
 * Task: OOP Aufgabe 4
 *
 * This class implements Langton's ant. It is a two-dimensional universal
 *    Turing machine with simple rules.
 * This application consist of a two-dimensional matrix of type boolean.
 * True denotes a gray square, false denotes a white square.
 * The ant moves according to these rules:
 * # At a white square, turn 90° clockwise and change color to gray.
 * # At a gray square, turn 90° counter-clockwise and change the color to
 *    white.
 * Then it moves forward one unit into the direction it looks in and exerts
 * these rules again. If the ant crawls out of the matrix, it crawls back in
 * on the other side.
 *
 * AntMatrix.java contains the following methods:
 * - public void init(): [Initialize matrix[][] with false.]
 * - public void placeAnt(): [Randomly place ant in matrix.]
 * - public void setDirection(): [Sets direction that ant looks in.]
 * - public void nextSecond(): [Moves ant into direction it looks, changes
 *    color of field (white<-->gray) if ant was on it, changes direction that
 *    ant looks in.]
 * - public void turnAnt(): [Turns ant 90° to left if field is gray(true)
 *    or right if field is white(false).]
 * - turnRight(): [Turns ant 90° to the right.]
 * - turnLeft(): [Turns ant 90° to the left.]
 * - changeColor(): [If ant was on field changes color from gray to white or
 *    from white to gray.
 * - moveAnt(): [Moves ant into direction it looks. If it crawls out of the
 *    matrix, lets it crawl in on opposite side.]
 * - draw(): [Draws the matrix and ant.]
 * - antIsHere(int i, int j): [Checks if ant is at matrix[i][j]. Returns true
 *    if there, false if not there.
 * - drawSquare(int i, int j): [Draws square of matrix.]
 * - drawAnt(int i, int j): [Draws ant.]
 * - startAnimation(): [Starts animation.]
 * - actionPerformed(ActionEvent e): [Calls nextSecond() and draw().]
 * --------------------------------------------------------------------------*/
public class AntMatrix implements ActionListener{
  public boolean[][] matrix; // false = white, true = grey
  public Timer timer;
  public static int delay=1000; //1sec
  public int width;
  public int height;
  public int antI;
  public int antJ;
  
  // [direction that ant looks in:
  // 'l' = left, 'r' = right, 'u' = up, 'd' = down.]
  public char direction;
  
  // Constants:
  public final int OFFSET_X=50;
  public final int OFFSET_Y=50;
  public final int SIZE=40;
  public final int CENTER_ANT_X=20;
  public final int CENTER_ANT_Y=20;
  
  /**
   * [This constructor initializes the matrix and draws it the first time.]
   *
   * @param width  [width of matrix]
   * @param height [height of matrix]
   */
  public AntMatrix(int width, int height){
    this.width=width;
    this.height=height;
    this.matrix=new boolean[width][height];
    
    this.init();
    this.draw();
  }
  
  /**
   * [Initializes the matrix with false.
   * Calls placeAnt() and setDirection().]
   */
  public void init(){
    for(int i=0; i<this.width; i++){
      for(int j=0; j<this.height; j++){
        this.matrix[i][j]=false;
      }
    }
    placeAnt();
    setDirection();
  }
  
  /**
   * [Places ant in a random field in the matrix.
   * | Called by init().]
   */
  public void placeAnt(){
    this.antI=(int) (Math.random()*(matrix.length)-1);
    this.antJ=(int) (Math.random()*(matrix[antI].length-1));
  }
  
  /**
   * [Randomly sets direction that ant is looking in.
   * 'r' --> Ant looks right
   * 'l' --> Ant looks left
   * 'u' --> Ant looks up
   * 'd' --> Ant looks down.
   * | called by init().]
   */
  public void setDirection(){
    int direct=(int) (Math.random()*4);
    switch (direct) {
      case 0:
        this.direction='r'; //right
        break;
      case 1:
        this.direction='l'; // left
        break;
      case 2:
        this.direction='u'; //up
        break;
      case 3:
        this.direction='d'; //down
        break;
      default:
        break;
    }
  }
  
  /**
   * [ Calculates from the current moving direction of the ant which field
   * it is going to crawl to next.
   * If the field is white(false) it changes to color to gray(true) and turns
   * 90° to the right.
   * If the field is gray(true) it changes the color to white(false) and
   * turns ant 90° to the left.
   * It also calculates the new crawling direction.
   * | Called by actionPerformed(ActionEvent e)
   * | calls turnAnt(), changeColor() and moveAnt().]
   */
  public void nextSecond(){
    turnAnt();
    changeColor();
    moveAnt();
  }
  
  /**
   * [Turns ant 90° to left if field is gray(true)
   * or right if field is white(false).
   * | called by nextSecond().]
   */
  public void turnAnt(){
    if(matrix[antI][antJ]) // [if gray (true) turn left]
      turnLeft();
    else if(!matrix[antI][antJ]) // [if white (false) turn right]
      turnRight();
  }
  
  /**
   * [Turns Ant 90° to the right.
   * | called by turnAnt().]
   */
  public void turnRight(){
    switch (this.direction) {
      case 'r':
        direction='d';
        break;
      case 'd':
        direction='l';
        break;
      case 'l':
        direction='u';
        break;
      case 'u':
        direction='r';
        break;
      default:
        break;
    }
  }
  
  /**
   * [Turns Ant 90° to the left.
   * | Called by turnAnt()]
   */
  public void turnLeft(){
    if(direction == 'r') direction='u';
    else if(direction == 'u') direction='l';
    else if(direction == 'l') direction='d';
    else if(direction == 'd') direction='r';
  }
  
  /**
   * [If field that ant was on was gray(true), turns field white (false)
   * if field was white(false), turns field gray(true).
   * | Called by nextSecond().]
   */
  public void changeColor(){
    if(matrix[antI][antJ]) this.matrix[antI][antJ]=false;
    else if(!matrix[antI][antJ]) this.matrix[antI][antJ]=true;
  }
  
  /**
   * [Moves ant into the direction it looks. If ant would crawl "out" of
   * matrix, let it crawl in on the opposite side.
   * | Called by nextSecond().]
   */
  public void moveAnt(){
    switch (this.direction) {
      case 'r':
        // look right, move right
        antI++;
        if(antI == width) antI=0;
        break;
      case 'l':
        //look left, move left
        antI--;
        if(antI == -1) antI=width-1;
        break;
      case 'u':
        //look up, move up
        antJ--;
        if(antJ == -1) antJ=height-1;
        break;
      case 'd':
        // look down, move down
        antJ++;
        if(antJ == height) antJ=0;
        break;
      default:
        break;
    }
  }
  
  /**
   * [Draws the matrix by drawing squares with a dark border and cells in
   * white or gray color.
   * | Called by constructor or actionPerformed(ActionEvent e)
   * | calls drawSquare(int i, int j), antIsHere(int i, int j) and drawAnt
   * (int i, int j).]
   */
  public void draw(){
    for(int i=0; i<this.width; i++){
      for(int j=0; j<this.height; j++){
        drawSquare(i, j);
        if(antIsHere(i, j)) drawAnt(i, j);
      }
    }
    Drawing.paint();
  }
  
  /**
   * [Checks if ant is in the place matrix[i][j].]
   *
   * @param i [current row i]
   * @param j [current col j]
   * @return [true if ant is in matrix[i][j], false if ant is not in
   * matrix[i][j].]
   */
  public boolean antIsHere(int i, int j){
    if(i == antI && j == antJ) return true;
    else return false;
  }
  
  /**
   * [Draws squares for every values of matrix[][].
   * If matrix[i][j] is false, draw white. If true, draw gray.
   * Draws a black border for each square.]
   *
   * @param i [current i in matrix]
   * @param j [current j in matrix]
   */
  public void drawSquare(int i, int j){
    int x=OFFSET_X+i*SIZE;
    int y=OFFSET_Y+j*SIZE;
    Drawing.graphics.setColor(this.matrix[i][j] ? Color.GRAY : Color.WHITE);
    Drawing.graphics.fillRect(x, y, SIZE, SIZE);
    Drawing.graphics.setColor(Color.BLACK);
    Drawing.graphics.drawRect(x, y, SIZE, SIZE);
  }
  
  /**
   * [Draws the aunt with the head in the direction it is going in next.]
   *
   * @param i [current i in matrix]
   * @param j [current j in matrix]
   */
  public void drawAnt(int i, int j){
    int x=OFFSET_X+i*SIZE+CENTER_ANT_X;
    int y=OFFSET_Y+j*SIZE+CENTER_ANT_Y;
    
    if(direction == 'r'){ // [Ant looks up]
      Drawing.graphics.setColor(Color.RED);
      Drawing.graphics
        .fillPolygon(new int[]{x-10, x+15, x-10}, new int[]{y-10, y, y+10}, 3);
    }else if(direction == 'l'){ // [Ant looks left]
      Drawing.graphics.setColor(Color.RED);
      Drawing.graphics
        .fillPolygon(new int[]{x+10, x-15, x+10}, new int[]{y+10, y, y-10}, 3);
    }else if(direction == 'u'){ // [Ant looks up]
      Drawing.graphics.setColor(Color.RED);
      Drawing.graphics
        .fillPolygon(new int[]{x-10, x, x+10}, new int[]{y+10, y-10, y+10}, 3);
    }else if(direction == 'd'){ // [Ant looks down]
      Drawing.graphics.setColor(Color.RED);
      Drawing.graphics
        .fillPolygon(new int[]{x+10, x, x-10}, new int[]{y-10, y+10, y-10}, 3);
    }
    
  }
  
  /**
   * [Starts animation of AntMatrix.java.]
   */
  public void startAnimation(){
    this.timer=new Timer(AntMatrix.delay, this);
    this.timer.start();
  }
  
  /**
   * [If last action is performed, execute nextSecond() and draw().]
   *
   * @param e [event]
   */
  public void actionPerformed(ActionEvent e){
    this.nextSecond();
    this.draw();
  }
}