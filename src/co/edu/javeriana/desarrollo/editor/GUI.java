package co.edu.javeriana.desarrollo.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private File imageFile;
	private VideoPanel videoPanel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					GUI frame = new GUI();
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public GUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 892, 487);
		this.setLocationRelativeTo(null);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(Color.BLACK);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));
		
		this.videoPanel = new VideoPanel();
		this.videoPanel.setBorder(null);
		this.videoPanel.setBackground(Color.GRAY);
		this.contentPane.add(this.videoPanel, BorderLayout.CENTER);
		this.videoPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelControl = new JPanel();
		panelControl.setBorder(new LineBorder(new Color(128, 128, 128), 20));
		panelControl.setBackground(Color.DARK_GRAY);
		this.contentPane.add(panelControl, BorderLayout.EAST);
		panelControl.setLayout(new MigLayout("", "[]", "[][][][][]"));
		
		JButton btnVideo = new JButton("Subir video");
		btnVideo.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.this.videoPanel.loadVideo();
			}
		});
		panelControl.add(btnVideo, "cell 0 0,growx");
		
		JButton btnNewButton_1 = new JButton("Subir imagen");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.this.cargarArchivoImagen();
			}
		});
		panelControl.add(btnNewButton_1, "cell 0 1,growx");
		
		JButton btnSubirAudio = new JButton("Subir audio");
		btnSubirAudio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.this.videoPanel.cargarAudio();
			}
		});
		btnSubirAudio.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelControl.add(btnSubirAudio, "cell 0 2,growx");
		
		JButton btnChange = new JButton("Cambiar");
		btnChange.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnChange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.this.videoPanel.changeVizualization();
			}
		});
		panelControl.add(btnChange, "cell 0 3,growx");
		
		JButton btnGenerarVideo = new JButton("Generar video");
		btnGenerarVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.this.videoPanel.saveVideo();
			}
		});
		btnGenerarVideo.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelControl.add(btnGenerarVideo, "cell 0 4,growx");
		
	}
	
	protected void cargarArchivoImagen() {
		JFileChooser chooser = new JFileChooser("./");
		chooser.setFileFilter(new FileNameExtensionFilter("Imagenes *.jpg *.png", "jpg", "png"));
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			this.imageFile = chooser.getSelectedFile();
			this.videoPanel.setImage(this.imageFile);
		}
	}
}
