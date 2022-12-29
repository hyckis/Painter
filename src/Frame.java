// 105403506���3A��y��

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
	static final String[] toolNames = {"����", "���u", "����", "�x��", "�ꨤ�x��"};

	// Radio Button -> Brush sizes
	final JRadioButton[] brushJRadioButton;	// selects medium brush		
	final ButtonGroup brushRadioGroup;	//holds radio buttons

	// CheckBox -> �O�_��
	final JCheckBox fillJCheckBox;

	// Button -> foreground/background/clean up/eraser
	final JButton foregroundJButton;
	final JButton backgroundJButton;
	final JButton cleanJButton;
	final JButton eraserJButton;
	
	// Variables to implement the functions
	public static int brushSize = 1;	// Brush size	
	public static Stroke brush;	// �e����Ĳ
	public static int penMode = 0;		// Different Painting Tools
	public static Color foregroundColor = Color.BLACK;	// �e����w�]���¦�
	public static Color backgroundColor = Color.white;	// �I����w�]���զ�
	public static int fill = 0;		// Fill in: 0���Ť�/��u, 1�����/��u
	public static int erase = 0;	// Eraser: 1�������
	public static int temp;	// �������������x�sfill���A
	
	public Frame() {
		
		super("�p�e�a");
		
		// Layout
		interfaceLayout = new BorderLayout();
		toolsLayout = new JPanel();
		
		// ComboBox -> Brush Modes		
		toolsJComboBox = new JComboBox<String>(toolNames);	
		// toolsJComboBox's Listener
		ComboBoxHandler handlerC = new ComboBoxHandler();	// register ItemListeners for ComboBox
		toolsJComboBox.addItemListener(handlerC);
		// add ComboBox
		toolsLayout.add(new JLabel("ø�Ϥu��"));
		toolsLayout.add(toolsJComboBox);
				
		// Radio Button -> Brush sizes				
		// create radio buttons
		brushJRadioButton = new JRadioButton[3];
		brushJRadioButton[0] = new JRadioButton("�p", true);
		brushJRadioButton[1] = new JRadioButton("��", false);
		brushJRadioButton[2] = new JRadioButton("�j", false);					
		brushRadioGroup = new ButtonGroup();	// create logical relationship between JRadioButtons	
		RadioButtonHandler handlerRB = new RadioButtonHandler();	// register ActionListeners for RadioButtons
		toolsLayout.add(new JLabel("����j�p"));
		for (int i=0; i<brushJRadioButton.length; i++) {
			toolsLayout.add(brushJRadioButton[i]);	// add radio buttons
			brushRadioGroup.add(brushJRadioButton[i]);	// create logical relationship between JRadioButton
			brushJRadioButton[i].addActionListener(handlerRB);	// register ActionListeners for RadioButtons
		}			
				
		// CheckBox -> �O�_��
		fillJCheckBox = new JCheckBox("");
		fillJCheckBox.setEnabled(false);
		// add CheckBox
		toolsLayout.add(new JLabel("��"));
		toolsLayout.add(fillJCheckBox);				
		// register listeners for JCheckBoxes
		CheckBoxHandler handlerCB = new CheckBoxHandler();
		fillJCheckBox.addItemListener(handlerCB);
				
		// Button -> foreground/background/clean up
		foregroundJButton = new JButton("�e����");
		backgroundJButton = new JButton("�I����");
		cleanJButton = new JButton("�M���e��");
		eraserJButton = new JButton("�����");
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
					fill = temp;	// �Nfill�����^�쥻�Ҧ�
				erase = 0;	// if�쥻����ܾ����>>�q����������^����
				
				// �U�ص���Ϯ�
				if (toolsJComboBox.getSelectedIndex() == 0) {
					penMode = 0;	// ����
					fillJCheckBox.setEnabled(false);	// ������
				} else if (toolsJComboBox.getSelectedIndex() == 1) {
					penMode = 1;	// ���u
					fillJCheckBox.setEnabled(true);
				} else if (toolsJComboBox.getSelectedIndex() == 2) {
					penMode = 2;	// ����
					fillJCheckBox.setEnabled(true);
				} else if (toolsJComboBox.getSelectedIndex() == 3) {
					penMode = 3;	// �x��
					fillJCheckBox.setEnabled(true);
				} else if (toolsJComboBox.getSelectedIndex() == 4) {
					penMode = 4;	// �ꨤ�x��
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
				fill = 1;	// ��/��u
			else 
				fill = 0;	// �Ť�/��u
		}
	}
			
	// register ActionListeners for JButtons
	public class ButtonHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == foregroundJButton) {
				foregroundColor = JColorChooser.showDialog(null, "��ܵ����C��", foregroundColor);
				if (foregroundColor == null)
					foregroundColor = Color.black;
			}
			if (event.getSource() == backgroundJButton) {
				backgroundColor = JColorChooser.showDialog(null, "��ܶ��C��", backgroundColor);
				if (backgroundColor == null)
					backgroundColor = Color.white;
			}
			if (event.getSource() == eraserJButton) {
				erase = 1;	// ������Ҧ�
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
