package ij.gui;
import ij.IJ;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/** A modal dialog box with a one line message and
	"Yes", "No" and "Cancel" buttons. */
public class SelectKeyChannelDialog extends Dialog implements ActionListener, KeyListener, ItemListener {
    private Choice channelChoices;
    private Choice methodChoices;

    private Button yesB, noB, cancelB;
    private boolean cancelPressed, yesPressed;
	private boolean firstPaint = true;
	private int keyChannel=1;
	private String regDeconMethod;
	private JSpinner modeFractionSpinnerA, modeFractionSpinnerB;
	public JSpinner getSubtractionFractionSpinnerA() {
		return modeFractionSpinnerA;
	}

	public JSpinner getSubtractionFractionSpinnerB() {
		return modeFractionSpinnerB;
	}

	private JSpinner iterationSpinner;
	private Panel optPanel;
	private int iterations;
	private Choice matrixPriming;
	private String matPrimMethod;
	private Double subFractA;
	private Double subFractB;
	private Checkbox depthSeek;
	private boolean autodepth;
	private Choice abRelOriChoices;
	private int abRelOriValue;
	
	public double getSubFractA() {
//		return subFractA;
		return 1;
	}

	public void setSubFractA(double subFract) {
		this.subFractA = subFract;
	}
	
	public double getSubFractB() {
//		return subFractB;
		return 1;
	}

	public void setSubFractB(double subFract) {
		this.subFractB = subFract;
	}



	public String getMatPrimMethod() {
		return matPrimMethod;
	}

	public void setMatPrimMethod(String matPrimMethod) {
		this.matPrimMethod = matPrimMethod;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public JSpinner getIterationSpinner() {
		return iterationSpinner;
	}

	public void setIterationSpinner(JSpinner iterationSpinner) {
		this.iterationSpinner = iterationSpinner;
	}

	public String getRegDeconMethod() {
		return regDeconMethod;
	}

	public SelectKeyChannelDialog(Frame parent, String title, String msg) {
		super(parent, title, true);
		setLayout(new BorderLayout());
		Panel panel = new Panel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		MultiLineLabel message = new MultiLineLabel(msg);
		message.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel.add(message);
		channelChoices = new Choice();
		channelChoices.add("Key registration on Channel 1");
		channelChoices.add("Key registration on Channel 2");
		abRelOriChoices = new Choice();
		abRelOriChoices.add("Input Volumes Rel. Orientation = -1");
		abRelOriChoices.add("Input Volumes Rel. Orientation = 0");
		abRelOriChoices.add("Input Volumes Rel. Orientation = +1");
		methodChoices = new Choice();
		methodChoices.add("MinGuo GPU method");
		methodChoices.add("mipav CPU method");
		matrixPriming = new Choice();
		matrixPriming.add("Prime registration with previous matrix");
		matrixPriming.add("Fresh registration for every volume");
		
		panel.add(channelChoices);
		panel.add(abRelOriChoices);
		panel.add(methodChoices);
		panel.add(matrixPriming);
		add("North", panel);
		
		panel = new Panel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 8));
		if (IJ.isMacintosh() && msg.startsWith("Save")) {
			yesB = new Button("  Save  ");
			noB = new Button("Don't Save");
			cancelB = new Button("  Cancel  ");
		} else {
			yesB = new Button("  Yes  ");
			noB = new Button("  No  ");
			cancelB = new Button(" Cancel ");
		}
		channelChoices.addItemListener(this);
		abRelOriChoices.addItemListener(this);
		methodChoices.addItemListener(this);
		matrixPriming.addItemListener(this);
		yesB.addActionListener(this);
		noB.addActionListener(this);
		cancelB.addActionListener(this);
		yesB.addKeyListener(this);
		noB.addKeyListener(this);
		cancelB.addKeyListener(this);
		if (IJ.isMacintosh()) {
			panel.add(yesB);
			panel.add(noB);
			panel.add(cancelB);
			setResizable(false);
		} else {
			panel.add(yesB);
			panel.add(noB);
			panel.add(cancelB);
		}
		add("South", panel);
		
//		depthSeek = new Checkbox("Auto-sense specimen depth?", false);
//		modeFractionSpinnerA = new JSpinner(new SpinnerNumberModel(/*1.011*/ 1, -1.0, 2.0, 0.001));  
//		modeFractionSpinnerA.setToolTipText("Fraction of image Mode to use for autocrop \nand background subtraction on Camera A");
//		modeFractionSpinnerB = new JSpinner(new SpinnerNumberModel(/*1.012*/ 1, -1.0, 2.0, 0.001));  
//		modeFractionSpinnerB.setToolTipText("Fraction of image Mode to use for autocrop \nand background subtraction on Camera B");
//		iterationSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));  
//		iterationSpinner.setToolTipText("Iterations of Deconvolution");
//		optPanel = new Panel();
//		optPanel.add("West", depthSeek);
//		optPanel.add("West", modeFractionSpinnerA);
//		optPanel.add("Center", modeFractionSpinnerB);
//		optPanel.add("East", iterationSpinner);
//		add("East", optPanel);

		pack();
		GUI.center(this);
		show();
	}
    
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==cancelB)
			cancelPressed = true;
		else if (e.getSource()==yesB) {
			yesPressed = true;
			keyChannel = channelChoices.getSelectedIndex()+1;
			abRelOriValue = abRelOriChoices.getSelectedIndex()-1;
			regDeconMethod = methodChoices.getSelectedItem();	
			matPrimMethod = matrixPriming.getSelectedItem();
//			subFractA = ((Double)modeFractionSpinnerA.getValue());
//			subFractB = ((Double)modeFractionSpinnerB.getValue());
//			iterations = ((Integer)iterationSpinner.getValue());
		}
		closeDialog();
	}
	
	/** Returns true if the user dismissed dialog by pressing "Cancel". */
	public boolean cancelPressed() {
		return cancelPressed;
	}

	/** Returns true if the user dismissed dialog by pressing "Yes". */
	public boolean yesPressed() {
		return yesPressed;
	}
	
	void closeDialog() {
		dispose();
	}

	public void keyPressed(KeyEvent e) { 
		int keyCode = e.getKeyCode(); 
		IJ.setKeyDown(keyCode); 
		if (keyCode==KeyEvent.VK_ENTER||keyCode==KeyEvent.VK_Y||keyCode==KeyEvent.VK_S) {
			yesPressed = true;
			closeDialog(); 
		} else if (keyCode==KeyEvent.VK_N || keyCode==KeyEvent.VK_D) {
			closeDialog(); 
		} else if (keyCode==KeyEvent.VK_ESCAPE||keyCode==KeyEvent.VK_C) { 
			cancelPressed = true; 
			closeDialog(); 
			IJ.resetEscape();
		} 
	} 

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode(); 
		IJ.setKeyUp(keyCode); 
	}
	
	public void keyTyped(KeyEvent e) {}

    public void paint(Graphics g) {
    	super.paint(g);
      	if (firstPaint) {
    		yesB.requestFocus();
    		firstPaint = false;
    	}
    }

	public int getKeyChannel() {
		return keyChannel;
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == channelChoices) {
			keyChannel = channelChoices.getSelectedIndex()+1;		
			IJ.log("keyChannel "+ keyChannel);
		}
		if (e.getSource()==depthSeek) {
			autodepth = false;
		}
		if (e.getSource() == methodChoices) {
			regDeconMethod = methodChoices.getSelectedItem();		
			IJ.log("regDeconMethod "+ regDeconMethod);
			if (regDeconMethod.contains("GPU") ) {
				if (optPanel!=null) {
					optPanel.setVisible(true);
				}
				this.pack();
			} else {
				if (optPanel != null) {
					optPanel.setVisible(false);
					this.pack();
				}
			}
		}
	}

	public boolean isAutodepth() {
		return autodepth;
	}

	public void setAutodepth(boolean autodepth) {
		this.autodepth = autodepth;
	}

	public int getAbRelOriValue() {
		// TODO Auto-generated method stub
		return abRelOriValue;
	}

}
