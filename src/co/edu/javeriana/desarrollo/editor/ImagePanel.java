package co.edu.javeriana.desarrollo.editor;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	protected BufferedImage image;
	protected double x;
	protected double y;
	protected double width;
	protected double height;
	
	public ImagePanel() {
		this.image = null;
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ImagePanel.this.x = e.getX();
				ImagePanel.this.y = e.getY();
				ImagePanel.this.repaint();
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				ImagePanel.this.x = e.getX();
				ImagePanel.this.y = e.getY();
				ImagePanel.this.repaint();
			}
		});
		
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (ImagePanel.this.image != null) {
					if ((e.getWheelRotation() > 0) && (ImagePanel.this.width > 20)) {
						ImagePanel.this.setWidth(ImagePanel.this.width + (e.getWheelRotation() * -20));
					} else if ((e.getWheelRotation() < 0) && (ImagePanel.this.width < 500)) {
						ImagePanel.this.setWidth(ImagePanel.this.width + (e.getWheelRotation() * -20));
					}
					ImagePanel.this.repaint();
				}
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.image != null) {
			g.drawImage(this.image, (int) Math.ceil(this.x - (this.width / 2)), (int) Math.ceil(this.y - (this.height / 2)), (int) this.width, (int) this.height, null);
		}
	}
	
	public void setImage(File image) {
		try {
			this.image = ImageIO.read(image);
			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
			if (this.width > 500) {
				this.setWidth(500);
			}
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
	
	public void setWidth(double f) {
		double proportion = this.height / this.width;
		this.height = proportion * f;
		this.width = f;
	}
	
	public void setHeight(double height) {
		double proportion = this.width / this.height;
		this.width = proportion * height;
		this.height = height;
	}
}
