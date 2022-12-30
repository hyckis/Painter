// Author: YICHIN HO

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Frame extends JFrame {

	// Layout
	final BorderLayout interfaceLayout;
	final JPanel toolsLayout;
	final PaintingField paintingField;
	public static JLabel statusField;
	
	// ComboBox -> Brush Mode
	final JComboBox<String> toolsJComboBox;	//holds tools' names
	static final String[] toolNames = {"筆刷", "直線", "橢圓形", "矩形", "圓角矩形"};

	// Radio Button -> Brush sizes
	final JRadioButton[] brushJRadioButton;	// selects medium brush		
	final ButtonGroup brushRadioGroup;	//holds radio buttons

	// CheckBox -> 是否填滿
	final JCheckBox fillJCheckBox;

	// Button -> foreground/background/clean up/eraser
	final JButton foregroundJButton;
	final JButton backgroundJButton;
	final JButton cleanJButton;
	final JButton eraserJButton;
	
	// Variables to implement the functions
	public static int brushSize = 1;	// Brush size	
	public static Stroke brush;	// 畫筆筆觸
	public static int penMode = 0;		// Different Painting Tools
	public static Color foregroundColor = Color.BLACK;	// 前景色預設為黑色
	public static Color backgroundColor = Color.white;	// 背景色預設為白色
	public static int fill = 0;		// Fill in: 0為空心/虛線, 1為實心/實線
	public static int erase = 0;	// Eraser: 1為橡皮擦
	public static int temp;	// 切換到橡皮擦時儲存fill狀態
	
	public Frame() {
		
		super("小畫家");
		
		// Layout
		interfaceLayout = new BorderLayout();
		toolsLayout = new JPanel();
		
		// ComboBox -> Brush Modes		
		toolsJComboBox = new JComboBox<String>(toolNames);	
		// toolsJComboBox's Listener
		ComboBoxHandler handlerC = new ComboBoxHandler();	// register ItemListeners for ComboBox
		toolsJComboBox.addItemListener(handlerC);
		// add ComboBox
		toolsLayout.add(new JLabel("繪圖工具"));
		toolsLayout.add(toolsJComboBox);
				
		// Radio Button -> Brush sizes				
		// create radio buttons
		brushJRadioButton = new JRadioButton[3];
		brushJRadioButton[0] = new JRadioButton("小", true);
		brushJRadioButton[1] = new JRadioButton("中", false);
		brushJRadioButton[2] = new JRadioButton("大", false);					
		brushRadioGroup = new ButtonGroup();	// create logical relationship between JRadioButtons	
		RadioButtonHandler handlerRB = new RadioButtonHandler();	// register ActionListeners for RadioButtons
		toolsLayout.add(new JLabel("筆刷大小"));
		for (int i=0; i<brushJRadioButton.length; i++) {
			toolsLayout.add(brushJRadioButton[i]);	// add radio buttons
			brushRadioGroup.add(brushJRadioButton[i]);	// create logical relationship between JRadioButton
			brushJRadioButton[i].addActionListener(handlerRB);	// register ActionListeners for RadioButtons
		}			
				
		// CheckBox -> 是否填滿
		fillJCheckBox = new JCheckBox("");
		fillJCheckBox.setEnabled(false);
		// add CheckBox
		toolsLayout.add(new JLabel("填滿"));
		toolsLayout.add(fillJCheckBox);				
		// register listeners for JCheckBoxes
		CheckBoxHandler handlerCB = new CheckBoxHandler();
		fillJCheckBox.addItemListener(handlerCB);
				
		// Button -> foreground/background/clean up
		foregroundJButton = new JButton("前景色");
		backgroundJButton = new JButton("背景色");
		cleanJButton = new JButton("清除畫面");
		eraserJButton = new JButton("橡皮擦");
		// add buttons
		toolsLayout.add(foregroundJButton);
		toolsLayout.add(backgroundJButton);
		toolsLayout.add(cleanJButton);
		toolsLayout.add(eraserJButton);
		// register ActionListener for JButtons
		ButtonHandler handlerB = new ButtonHandler();
		foregroundJButton.addActionListener(handlerB);
		backgroundJButton.addActionListener(handlerB);
		cleanJButton.addActionListener(handlerB);
		eraserJButton.addActionListener(handlerB);
		
		// add components above to layout
		paintingField = new PaintingField();
		statusField = new JLabel();
		add(toolsLayout, BorderLayout.NORTH);
		add(paintingField, BorderLayout.CENTER);
		add(statusField, BorderLayout.SOUTH);
		
	}
	
	// register ItemListeners for JComboBox
	public class ComboBoxHandler implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				if(erase==1) 
					fill = temp;	// 將fill切換回原本模式
				erase = 0;	// if原本有選擇橡皮擦>>從橡皮擦切換回筆刷
				
				// 各種筆刷圖案
				if (toolsJComboBox.getSelectedIndex() == 0) {
					penMode = 0;	// 筆刷
					fillJCheckBox.setEnabled(false);	// 不准選填滿
				} else if (toolsJComboBox.getSelectedIndex() == 1) {
					penMode = 1;	// 直線
					fillJCheckBox.setEnabled(true);
				} else if (toolsJComboBox.getSelectedIndex() == 2) {
					penMode = 2;	// 橢圓形
					fillJCheckBox.setEnabled(true);
				} else if (toolsJComboBox.getSelectedIndex() == 3) {
					penMode = 3;	// 矩形
					fillJCheckBox.setEnabled(true);
				} else if (toolsJComboBox.getSelectedIndex() == 4) {
					penMode = 4;	// 圓角矩形
					fillJCheckBox.setEnabled(true);
				}
			}
		}
	}
	
	// register ActionListeners for RadioButtons
	public class RadioButtonHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			if (brushJRadioButton[0].isSelected())
				brushSize = 1;
			else if (brushJRadioButton[1].isSelected())
				brushSize = 10;
			else if (brushJRadioButton[2].isSelected())
				brushSize = 20;
		}
	}
	
	// register ItemListeners for JCheckBoxes
	public class CheckBoxHandler implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent event) {
			if(fillJCheckBox.isSelected())
				fill = 1;	// 填滿/實線
			else 
				fill = 0;	// 空心/虛線
		}
	}
			
	// register ActionListeners for JButtons
	public class ButtonHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == foregroundJButton) {
				foregroundColor = JColorChooser.showDialog(null, "選擇筆刷顏色", foregroundColor);
				if (foregroundColor == null)
					foregroundColor = Color.black;
			}
			if (event.getSource() == backgroundJButton) {
				backgroundColor = JColorChooser.showDialog(null, "選擇填滿顏色", backgroundColor);
				if (backgroundColor == null)
					backgroundColor = Color.white;
			}
			if (event.getSource() == eraserJButton) {
				erase = 1;	// 橡皮擦模式
				fillJCheckBox.setEnabled(false);
				temp = fill;
				fill = 1;
			}
			if (event.getSource() == cleanJButton) {
				PaintingField.currentImg = new BufferedImage(PaintingField.panelWid, PaintingField.panelHei, BufferedImage.TYPE_INT_ARGB);
				PaintingField.bufferGraphics = (PaintingField.currentImg).createGraphics();
				PaintingField.x1 = -1;
				PaintingField.y1 = -1;
				PaintingField.x2 = -1;
				PaintingField.y2 = -1;
				repaint();
			}
		}
	}	
}
