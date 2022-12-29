import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;


public class PaintingField extends JPanel {

	static int x1, x2, y1, y2;	// Mouse Location
	boolean isEnd = false;	// monitor whether painting is end
	
	private final ArrayList<Point> points = new ArrayList<>();	// 儲存滑鼠經過的點
	
	static int panelWid, panelHei;	// BufferedImage長寬
	static BufferedImage currentImg;	// 存取目前畫面
	static Graphics2D bufferGraphics;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	// 獲取視窗大小
	
	public PaintingField() {
		
		panelWid = (int) screenSize.getWidth();
		panelHei = (int) screenSize.getHeight();
		currentImg = new BufferedImage(panelWid, panelHei, BufferedImage.TYPE_INT_ARGB);
		bufferGraphics = currentImg.createGraphics();
		
		addMouseListener(
				new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						x1 = e.getX();
						y1 = e.getY();
					}			
					@Override
					public void mouseClicked(MouseEvent e) {}				
					@Override
					public void mouseReleased(MouseEvent e) {						
						isEnd = true;	// painting end	
						repaint();
					}
					@Override
					public void mouseEntered(MouseEvent e) {}
					@Override
					public void mouseExited(MouseEvent e) {
						(Frame.statusField).setText(String.format("離開畫布"));
					}
		});
		
		addMouseMotionListener(
				new MouseAdapter() {
					@Override
					public void mouseDragged(MouseEvent e) {					
						// 筆刷
						if (Frame.penMode == 0) {
							points.add(e.getPoint());
							repaint();
						}	
						if (Frame.penMode != 0) {
							x2 = e.getX();
							y2 = e.getY();				
							repaint();
						}
					}
					@Override
					public void mouseMoved(MouseEvent e) {
						(Frame.statusField).setText(String.format("游標位置: {%d, %d}", e.getX(), e.getY()));						
					}
		});
		
	}
	
	// 設定畫布
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		setBackground(Color.white);
		saveBufferedImg(g);			
	}
	
	// 工具>>畫在BufferedImage上
	public void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;		
		
		// brush color
		if (Frame.erase == 0)	// 各種筆刷模式 
			g.setColor(Frame.foregroundColor);	
		else if (Frame.erase == 1)  	// 橡皮擦模式
			g.setColor(Color.white);
				
		// 實線筆刷粗細
		Frame.brush = new BasicStroke(Frame.brushSize, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10);
		g2.setStroke(Frame.brush);
			
		// 筆刷模式
		// 筆刷
		if (Frame.penMode == 0) {		
			for(int i=0; i<points.size()-1; i++) {
				x1 = points.get(i).x;
				y1 = points.get(i).y;
				x2 = points.get(i+1).x;
				y2 = points.get(i+1).y;			
				g.drawLine(x1, y1, x2, y2);
			}
		}
		// 直線
		if (Frame.penMode == 1) {
			if (Frame.fill == 0) {	// 虛線筆刷
				Frame.brush = new BasicStroke(Frame.brushSize, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] {3,10}, 10);
				g2.setStroke(Frame.brush);
			}
			g.drawLine(x1, y1, x2, y2);
		}
		// 橢圓形
		if (Frame.penMode == 2) {		
			int width = Math.abs(x1 - x2);	// Math.abs取絕對值
			int height = Math.abs(y1 - y2);
			if (Frame.fill == 1) {	// 填滿
				if (Frame.erase == 1)
					g.fillOval(Math.min(x1, x2), Math.min(y1, y2), width, height);
				else if (Frame.erase == 0) {
					g.setColor(Frame.backgroundColor);
					g.fillOval(Math.min(x1, x2), Math.min(y1, y2), width, height);				
					g.setColor(Frame.foregroundColor);
				}
			}
			g.drawOval(Math.min(x1, x2), Math.min(y1, y2), width, height);	
			
		}
		// 矩形
		if (Frame.penMode == 3) {
			int width = Math.abs(x1 - x2);
			int height = Math.abs(y1 - y2);	
			if (Frame.fill == 1) {	// 填滿
				if (Frame.erase == 1)
					g.fillRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
				else if (Frame.erase == 0) {
					g.setColor(Frame.backgroundColor);
					g.fillRect(Math.min(x1, x2), Math.min(y1, y2), width, height);				
					g.setColor(Frame.foregroundColor);
				}
			}
			g.drawRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
		}
		// 圓角矩形
		if (Frame.penMode == 4) {	// 填滿		
			int width = Math.abs(x1 - x2);
			int height = Math.abs(y1 - y2);
			if (Frame.fill == 1) {
				if (Frame.erase == 1)
					g.fillRoundRect(Math.min(x1, x2), Math.min(y1, y2), width, height, 15, 15);
				else if (Frame.erase == 0) {
					g.setColor(Frame.backgroundColor);
					g.fillRoundRect(Math.min(x1, x2), Math.min(y1, y2), width, height, 15, 15);				
					g.setColor(Frame.foregroundColor);
				}
			}
			g.drawRoundRect(Math.min(x1, x2), Math.min(y1, y2), width, height, 15, 15);					
		}

	}

	// 儲存畫面
	public void saveBufferedImg(Graphics g) {
		Graphics2D saveGraphics = (Graphics2D) g;
		saveGraphics.drawImage(currentImg, null, 0, 0);
		draw(saveGraphics);
		if (isEnd) {	// 滑鼠事件結束(released)後把currentImg丟到畫面上
			draw(bufferGraphics);
			points.clear();
			isEnd = false;
		}
	}

}
