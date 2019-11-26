//source code for KeyInteractor:

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A simplified class for keyboard interaction. Subclasses override
 * the  actionPerformed method to do something useful. While         
the javax.swing.JComponent passed in can technically be
 * anything, it should generally be the main panel in a GUI.
 * The call to super() in the subclass will need parameters--the component 
 * and the keyCode at least.
 *
 * @author John Goodwin (simplified rdm)
 */


public abstract class KeyInteractor extends AbstractAction {

    public static final String COMMAND_PREFIX = "KeyInteractor";

  /**
     * Creates a key interactor for the specified component.
     *
     * @param comp the component that will receive the key input
     * @param keyCode the (int) key code that the interactor will respond to
     * @param mod the (int) modifier of that key code. 
     *  see constants in KeyEvent for codes
     */
   public KeyInteractor (JComponent comp, int keyCode, int mod) { 
	super();
	// a name that identifies this action
	String actionID = COMMAND_PREFIX + keyCode + "." + mod;

	//map the keystroke to the actionID in the InputMap
	InputMap imap = comp.getInputMap();
	imap.put(KeyStroke.getKeyStroke(keyCode,mod), actionID);

	//map the action key to this
	ActionMap amap = comp.getActionMap();
	amap.put(actionID, this);
    }

    public KeyInteractor (JComponent comp, int keyCode) { 
	this(comp,keyCode,0);  // default--no CTRL-SHIFT-etc modifier
    }

    /**
     *
     * The method that gets called when the key is pressed.
     *
     * @param e the ActionEvent associated with the current call
     */
    public abstract void actionPerformed (ActionEvent e);

}
