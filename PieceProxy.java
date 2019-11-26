
/**
 * Write a description of class TetriminoProxy here.
 *
 * @author Greg Johnson, University of Connecticut
 * @version 0.3
 */
public class PieceProxy implements Animatable
{
    // instance variables - replace the example below with your own
    private Animatable _p;     

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public SmartRectangle[] getBlocks(){
        Tetrimino p = (Tetrimino) _p;
        return new SmartRectangle[]{p._block1,p._block2,p._block3,p._block4};
    }
    public void setPiece(Animatable m)
    {
        _p = m;
    }
    public void fill (java.awt.Graphics2D aBrush){
    _p.fill(aBrush);
    }
    public void draw (java.awt.Graphics2D aBrush) {
    _p.draw(aBrush);
    }
    public boolean moveUp()
    {
        return _p.moveUp();
    }
    public boolean moveDown()
    {
        return _p.moveDown();
    }    
    public boolean moveLeft()
    {
        return _p.moveLeft();
    }
    public boolean moveRight()
    {
        return _p.moveRight();
    }
    public boolean turnLeft()
    {
        return _p.turnLeft();
    }
    public boolean turnRight()
    {
        return _p.turnRight();
    }
   
}
