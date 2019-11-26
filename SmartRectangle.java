/**
 * Chapter 9: SmartRectangle.java
 * Adds capabilities to the Java2D.Double rectangle.
 * Same as the class defined in Chapter 7.
 */
public class SmartRectangle extends java.awt.geom.Rectangle2D.Double {
    private java.awt.Color _borderColor, _fillColor;
    private int _rotation;
    private final int STROKE_WIDTH = 2;

    public SmartRectangle(java.awt.Color aColor){
	_borderColor = aColor;
	_fillColor = aColor; // solid color to start
	_rotation = 0;
    }
 
   // methods not provided by Java
    public void setBorderColor (java.awt.Color aColor) {
	_borderColor = aColor;
    }
    public void setFillColor (java.awt.Color aColor) {
	_fillColor = aColor;
    }
    public void setColor (java.awt.Color aColor) {
	_borderColor = aColor;
	_fillColor = aColor;
    }
    public void setRotation (int aRotation) {
	_rotation = aRotation;
    }

    // more readable versions of methods provided by Java
    public void setLocation (double x, double y) {
	this.setFrame (x, y, this.getWidth(), this.getHeight());
    }
    public void setSize (int aWidth, int aHeight) {
	this.setFrame(this.getX(), this.getY(), aWidth, aHeight);
    }

    // not provided by Java
    public void fill (java.awt.Graphics2D aBrush){
	java.awt.Color oldColor = aBrush.getColor();
	aBrush.setColor(_fillColor);
	aBrush.fill(this);
	aBrush.setColor(oldColor);
    }
    public void draw (java.awt.Graphics2D aBrush) {
	java.awt.Color oldColor = aBrush.getColor();
	aBrush.setColor(_borderColor);
	java.awt.Stroke oldStroke = aBrush.getStroke();
	aBrush.setStroke(new java.awt.BasicStroke(STROKE_WIDTH));
	aBrush.draw(this);
	aBrush.setStroke(oldStroke);
	aBrush.setColor(oldColor);
    }
}
