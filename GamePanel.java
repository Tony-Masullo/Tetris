import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.Color;

/**
 * Write a description of class GamePanel here.
 *
 * @author Greg Johnson, University of Connecticut
 * @version 0.3
 */
public class GamePanel extends JPanel implements ActionListener
{
    
    // instance variables - replace the example below with your own
    private PieceProxy _piece;
    private Timer _timer;
    private Random _generator;
    private Boolean _gameOver;
    private SmartRectangle[][] _board;
    private KeyUpListener _upKey;
    private KeyDownListener _downKey;
    private KeyLeftListener _leftKey;
    private KeyRightListener _rightKey;
    private KeyPListener _pauseKey;
    private KeySpaceListener _spaceKey;
    private JLabel _label;
    
    /**
     * Constructor for objects of class GamePanel
     */
    public GamePanel()
    {
        // initialise instance variables
        this.setBackground(Color.BLACK);
        this.setSize(new Dimension(TetrisConstants.BLOCK_SIZE*(TetrisConstants.BOARD_WIDTH), TetrisConstants.BLOCK_SIZE*(TetrisConstants.BOARD_HEIGHT)+15));
        this.setPreferredSize(new Dimension(TetrisConstants.BLOCK_SIZE*(TetrisConstants.BOARD_WIDTH), TetrisConstants.BLOCK_SIZE*(TetrisConstants.BOARD_HEIGHT)+15));
        _gameOver = false;
        _board = new SmartRectangle[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH];
        _upKey = new KeyUpListener(this);
        _downKey = new KeyDownListener(this);
        _leftKey = new KeyLeftListener(this);
        _rightKey = new KeyRightListener(this);
        _pauseKey = new KeyPListener(this);
        _spaceKey = new KeySpaceListener(this);
        _generator = new Random();
        
        _piece = new PieceProxy();
        _piece.setPiece(tetriminoFactory());
        
        _timer = new Timer(500, this);
        _timer.start();

    }
    
    public Tetrimino tetriminoFactory()
    /** 
     * This method implements the factory method design pattern to build new tetriminos during Tetris game play.
     */
    {
        Tetrimino newPiece;
        int randomNumber;
        
        int x = (TetrisConstants.BOARD_WIDTH/2) * TetrisConstants.BLOCK_SIZE;
        int y = 0;
        randomNumber = (int) (Math.floor(Math.random()*7)+1);
        switch(randomNumber) {
            case 1: newPiece = new Z(x,y,this);     break;
            case 2: newPiece = new S(x,y,this);     break;
            case 3: newPiece = new L(x,y,this);     break;
            case 4: newPiece = new J(x,y,this);     break;
            case 5: newPiece = new O(x,y,this);     break;
            case 6: newPiece = new I(x,y,this);     break;
            default: newPiece = new T(x,y,this);     break;
        }
        _piece.setPiece(newPiece);
        return newPiece;
    }
    
    public void paintComponent (java.awt.Graphics aBrush) 
    {
        super.paintComponent(aBrush);
        java.awt.Graphics2D betterBrush = (java.awt.Graphics2D)aBrush;
        
        _piece.fill(betterBrush);
        _piece.draw(betterBrush);
        
        SmartRectangle gamepiece;
        
        for (int i = 0; i<_board.length; i++){
                for (int j = 0; j<_board[i].length; j++){
                    gamepiece = _board[i][j];
                    if (gamepiece != null){
                        gamepiece.fill(betterBrush);
                        gamepiece.draw(betterBrush);
                    }
                } 
            }
       }
    /**
     * This method takes two integers representing the column and row of a cell on the game board a component rectangle into which a
     * tetrimino wishes to move. This can be prevented by either the cell being off of the game board (not a valid cell) or by the
     * cell being occupied by another SmartRectangle.
     * 
     * @param c The column of the cell in question on the game board.
     * @param r The row of the cell in question on the game board.
     * @return boolean This function returns whether the component rectangle can be moved into this cell.
     */
    public boolean canMove(int c, int r)
    {
      return isFree(c,r) && isValid(c,r);
    }
    
    /**
     * This method takes two integers representing the column and row of a cell on the game board a component rectangle into which a
     * tetrimino wishes to move. This method returns a boolean indicating whether the cell on the game board is empty.
     * 
     * @param c The column of the cell in question on the game board.
     * @param r The row of the cell in question on the game board.
     * @return boolean This function returns whether the cell on the game board is free.
     */    
    private boolean isFree(int c, int r)
    {
        Boolean free;
        if (_board[r][c] != null){
            free = false;
        }
        else {free = true;
        }
        return free;
    }
    /**
     * This method takes two integers representing the column and row of a cell on the game board a component rectangle into which a
     * tetrimino wishes to move. This function checks to see if the cell at (c, r) is a valid location on the game board.
     * 
     * @param c The column of the cell in question on the game board.
     * @param r The row of the cell in question on the game board.
     * @return boolean This function returns whether the location (c, r) is within the bounds of the game board.
     */
    private boolean isValid(int c, int r)
    {
        return (r >=0 
               && r<= (TetrisConstants.BOARD_HEIGHT - 1) 
               && c >=0 
               && c<= (TetrisConstants.BOARD_WIDTH - 1));
    }
     /**
     * 
     * @param r The SmartRectangle to add to the game board.
     * @return Nothing
     */   
    public void addToBoard(SmartRectangle r)
    {
        int col = (int)r.getX()/TetrisConstants.BLOCK_SIZE;
        int row = (int)r.getY()/TetrisConstants.BLOCK_SIZE;
        _board[row][col] = r;
        
    }
    /**
     * This method takes one integer representing the row of cells on the game board to move down on the screen after a full 
     * row of squares has been removed.
     * 
     * @param row The row in question on the game board.
     * @return Nothing
     */
    private void moveBlocksDown(int row)
    {   
        for (int i=0; i<TetrisConstants.BOARD_WIDTH; i++) {
            _board[row + 1][i] = _board[row][i];
            if(_board[row+1][i]!= null)
                _board[row+1][i].y += TetrisConstants.BLOCK_SIZE;
        }
    }
    /**
     * This method checks each row of the game board to see if it is full of rectangles and should be removed. It calls
     * moveBlocksDown to adjust the game board after the removal of a row.
     * 
     * @return Nothing
     */
    private void checkRows(){
        for(int i=0; i<TetrisConstants.BOARD_HEIGHT; i++) {
            int blocksInRow = 0;
            for(int j=0; j<TetrisConstants.BOARD_WIDTH; j++) {
                if(_board[i][j] != null) blocksInRow++;
            }
            if(blocksInRow == TetrisConstants.BOARD_WIDTH){
                
                for(int j=0; j<TetrisConstants.BOARD_WIDTH; j++) {
                    _board[i][j] = null;
                }
                for(int k=i-1; k>=0; k--) {
                    moveBlocksDown(k);
                }
            }
        }
    }
    /**
     * This method checks to see if the game has ended.
     * 
     * @return boolean This function returns whether the game is over or not.
     */
    private boolean checkEndGame()
    {
        for(int i = 0; i < TetrisConstants.BOARD_WIDTH; i++){
            if(_board[0][i] != null) {
                System.out.println("End!");
                return true;
            }
        }
        return false;
    }
    private void newBlock() {
        _piece = new PieceProxy();
        _piece.setPiece(tetriminoFactory());
    }
    public void actionPerformed(ActionEvent e)
    {
        if(!checkEndGame()) {
            SmartRectangle[] blocks = _piece.getBlocks();
            boolean canMove = true;
            try {
                
                for (SmartRectangle rect : blocks) {
                    if (!canMove((int) rect.x / TetrisConstants.BLOCK_SIZE, (int) (rect.y / TetrisConstants.BLOCK_SIZE) + 1)) {
                        canMove = false;
                    }
                }
                if (canMove) {
                    _piece.moveDown();
                } 
                else {
                    throw new RuntimeException("Pieces cannot move");
                }
            } 
            catch (RuntimeException re) {
                
                for (SmartRectangle block : blocks) {
                    addToBoard(block);
                }

                checkRows();
                newBlock();
            }
        } 
        else {       
            _timer.stop();
            _label.setVisible(true);
        }
        repaint();
    }
    private class KeyUpListener extends KeyInteractor 
    {
        public KeyUpListener(JPanel p)
        {
            super(p,KeyEvent.VK_UP);
        }
        
        public  void actionPerformed (ActionEvent e) {
            try {
                _piece.turnRight();
                SmartRectangle[] blocks = _piece.getBlocks();
                for (SmartRectangle r : blocks) {
                    if (!canMove((int) r.x / TetrisConstants.BLOCK_SIZE, (int) r.y / TetrisConstants.BLOCK_SIZE))
                        _piece.turnLeft();
                }
            } catch (ArrayIndexOutOfBoundsException Exception) {
                _piece.turnLeft();
            }
            repaint();
        }
    }
    private class KeyDownListener extends KeyInteractor 
    {
        public KeyDownListener(JPanel p)
        {
            super(p,KeyEvent.VK_DOWN);
        }
        
        public  void actionPerformed (ActionEvent e) {
             _piece.moveDown();
            try {
                SmartRectangle[] blocks = _piece.getBlocks();
                for (SmartRectangle r : blocks) {
                    if(!canMove((int) r.x/TetrisConstants.BLOCK_SIZE, (int) r.y/TetrisConstants.BLOCK_SIZE))
                        _piece.moveUp();
                }
            } 
            catch (ArrayIndexOutOfBoundsException Exception) {
                _piece.moveUp();
            }
            repaint();
        }
    } 
    private class KeyLeftListener extends KeyInteractor 
    {
        public KeyLeftListener(JPanel p)
        {
            super(p,KeyEvent.VK_LEFT);
        }
        
        public  void actionPerformed (ActionEvent e) {
            _piece.moveLeft();
            try {
                SmartRectangle[] blocks = _piece.getBlocks();
                for (SmartRectangle r : blocks) {
                    if(!canMove((int) r.x/TetrisConstants.BLOCK_SIZE, (int) r.y/TetrisConstants.BLOCK_SIZE))
                        _piece.moveRight();
                }
            } catch (ArrayIndexOutOfBoundsException Exception) {
                _piece.moveRight();
            }
            repaint();
        }
    } 
    private class KeyRightListener extends KeyInteractor 
    {
        public KeyRightListener(JPanel p)
        {
            super(p,KeyEvent.VK_RIGHT);
        }
        
        public  void actionPerformed (ActionEvent e) {
            _piece.moveRight();
            try {
                SmartRectangle[] blocks = _piece.getBlocks();
                for (SmartRectangle r : blocks) {
                    if(!canMove((int) r.x/TetrisConstants.BLOCK_SIZE, (int) r.y/TetrisConstants.BLOCK_SIZE))
                        _piece.moveLeft();
                }
            } catch (ArrayIndexOutOfBoundsException Exception) {
                _piece.moveLeft();
            }
            repaint();
        }
    }
    private class KeyPListener extends KeyInteractor 
    {
        public KeyPListener(JPanel p)
        {
            super(p,KeyEvent.VK_P);
        }
        
        public  void actionPerformed (ActionEvent e) {
            if(_timer.isRunning()){
                _timer.stop();
            }
            else
                _timer.start();
        }
    }
    
    private class KeySpaceListener extends KeyInteractor
    {
        public KeySpaceListener(JPanel p)
        {
            super(p,KeyEvent.VK_SPACE);
        }

        public  void actionPerformed (ActionEvent e) {
            SmartRectangle[] blocks = {};
            while(true) {
                boolean end = false;
                _piece.moveDown();
                try {
                    blocks = _piece.getBlocks();
                    for (SmartRectangle r : blocks) {
                        if(!canMove((int) r.x/TetrisConstants.BLOCK_SIZE, (int) r.y/TetrisConstants.BLOCK_SIZE)) {
                            end = true;
                            _piece.moveUp();

                            break;
                        }
                    }
                    if (end == true) break;
                } 
                catch (ArrayIndexOutOfBoundsException Exception) {

                    _piece.moveUp();
                    break;
                }
            }
            for(SmartRectangle r : blocks) {
                addToBoard(r);
            }
            checkRows();
            newBlock();
            repaint();
        }
    }
}
