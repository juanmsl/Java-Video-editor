package co.edu.javeriana.desarrollo.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaTool;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class VideoPanel extends ImagePanel {
	
	private static final long serialVersionUID = 1L;
	private Player playerVideo;
	private boolean videoOn;
	private Map<Long, Pair> coords;
	private long actualTime;
	private File videoFile;
	private int videoWidth;
	private int videoHeight;
	private File audioFile;
	private Player playerAudio;
	
	public VideoPanel() {
		this.videoOn = false;
		this.actualTime = 0;
		this.coords = new HashMap<>();
		this.coords.put(0L, new Pair(50, 50));
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(new BorderLayout(0, 0));
	}
	
	public void saveVideo() {
		if (this.videoFile == null) {
			JOptionPane.showMessageDialog(this, "Debe cargar un archivo de video para poder exportarlo");
			return;
		}
		
		if (this.image == null) {
			JOptionPane.showMessageDialog(this, "Debe cargar una imagen para poder agregarla al video exportado");
			return;
		}
		
		System.out.println("Times");
		List<Long> times = new ArrayList<>(this.coords.keySet());
		Collections.sort(times);
		for (long time : times) {
			Pair r = this.coords.get(time);
			System.out.println(time + ": (" + r.getPx() + " , " + r.getPy() + ")");
		}
		
		File saveFile = null;
		JFileChooser chooser = new JFileChooser("./");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			saveFile = chooser.getSelectedFile();
			
			if (!saveFile.getName().matches("[a-z0-9()-_]+((.mp4)|(.avi))")) {
				JOptionPane.showMessageDialog(this, "Extención de archivo no valida, debe ser .mp4 ó .avi");
				return;
			}
			
			if (saveFile.getPath().contains(" ") || this.videoFile.getPath().contains("á") || this.videoFile.getPath().contains("é") || this.videoFile.getPath().contains("í") || this.videoFile.getPath().contains("ó") || this.videoFile.getPath().contains("ú")) {
				JOptionPane.showMessageDialog(this, "Ruta del video invalida, la ruta no debe contener espacios ni tildes");
				return;
			}
			
			this.generateVideo(this.videoFile.getPath(), saveFile.getPath());
		}
	}
	
	public void generateVideo(String videoFileName, String outputFile) {
		IMediaReader videoReader = ToolFactory.makeReader(videoFileName);
		videoReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		IMediaWriter videoWriter = ToolFactory.makeWriter(outputFile, videoReader);
		int imagesIndex = videoWriter.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, this.videoWidth, this.videoHeight);
		IMediaTool imageMediaTool = new StaticImageMediaTool(this.image, this.width, this.height, this.coords, imagesIndex, videoWriter);
		videoReader.addListener(imageMediaTool);
		
		if (this.audioFile == null) {
			this.getAudioStream(this.videoFile, videoWriter);
		} else {
			this.getAudioStream(this.audioFile, videoWriter);
		}
		
		while (videoReader.readPacket() == null) {
			;
		}
		videoWriter.close();
		JOptionPane.showMessageDialog(this, "Video exportado correctamente");
		System.out.println("listo");
	}
	
	public class StaticImageMediaTool extends MediaToolAdapter {
		private BufferedImage logoImage;
		private double width;
		private double height;
		private Map<Long, Pair> coords;
		private List<Long> times;
		private int index;
		private double px;
		private double py;
		private int imagesIndex;
		private IMediaWriter videoWriter;
		private int i;
		private Pair factor;
		private long time;
		private int x;
		private int y;
		
		public StaticImageMediaTool(BufferedImage imageFile, double width, double height, Map<Long, Pair> coords, int imagesIndex, IMediaWriter videoWriter) {
			this.logoImage = imageFile;
			this.width = width;
			this.height = height;
			this.coords = coords;
			this.times = new ArrayList<>(coords.keySet());
			Collections.sort(this.times);
			this.index = 0;
			this.px = (int) this.coords.get(this.times.get(this.index)).getPx();
			this.py = (int) this.coords.get(this.times.get(this.index)).getPy();
			this.imagesIndex = imagesIndex;
			this.videoWriter = videoWriter;
			this.i = 0;
			this.calculateFactor();
		}
		
		private void calculateFactor() {
			System.out.println("Factor");
			if ((this.index + 1) < this.times.size()) {
				long in1 = this.times.get(this.index + 1);
				long in = this.times.get(this.index);
				this.time = in1 - in;
				double deltaPx = this.coords.get(in1).getPx() - this.coords.get(in).getPx();
				double deltaPy = this.coords.get(in1).getPy() - this.coords.get(in).getPy();
				double TIME = this.time;
				this.factor = new Pair(deltaPx / TIME, deltaPy / TIME);
			} else {
				this.factor = new Pair(0, 0);
			}
		}
		
		private void calculateChangeIndex(Long nanosecond) {
			if ((this.index + 1) < this.times.size()) {
				if (this.times.get(this.index + 1) <= nanosecond) {
					this.index++;
					this.px = (int) this.coords.get(this.times.get(this.index)).getPx();
					this.py = (int) this.coords.get(this.times.get(this.index)).getPy();
					
					this.x = (int) (((VideoPanel.this.videoWidth * this.px) / 100.0f) - (this.width / 2));
					this.y = (int) (((VideoPanel.this.videoHeight * this.py) / 100.0f) - (this.height / 2));
					this.calculateFactor();
					System.out.println("Cambio");
				}
			}
		}
		
		private float ant = 0;
		
		@Override
		public void onVideoPicture(IVideoPictureEvent event) {
			float diff = event.getTimeStamp(TimeUnit.NANOSECONDS) - this.ant;
			this.ant = event.getTimeStamp(TimeUnit.NANOSECONDS);
			
			BufferedImage image = event.getImage();
			Graphics2D g = image.createGraphics();
			
			this.px += this.factor.getPx() * diff;
			this.py += this.factor.getPy() * diff;
			
			this.x = (int) (((VideoPanel.this.videoWidth * this.px) / 100.0f) - (this.width / 2));
			this.y = (int) (((VideoPanel.this.videoHeight * this.py) / 100.0f) - (this.height / 2));
			g.drawImage(this.logoImage, this.x, this.y, (int) this.width, (int) this.height, null);
			
			this.calculateChangeIndex(event.getTimeStamp(TimeUnit.NANOSECONDS));
			
			BufferedImage finalFrame = new BufferedImage(VideoPanel.this.videoWidth, VideoPanel.this.videoHeight, BufferedImage.TYPE_3BYTE_BGR);
			finalFrame.getGraphics().drawImage(image, 0, 0, null);
			this.videoWriter.encodeVideo(this.imagesIndex, finalFrame, (1000 / 24) * this.i++, TimeUnit.MILLISECONDS);
			this.videoWriter.flush();
		}
	}
	
	public void changeVizualization() {
		if (this.playerVideo != null) {
			if (this.videoOn) {
				this.playerVideo.stop();
				this.remove(this.playerVideo.getVisualComponent());
				this.remove(this.playerVideo.getControlPanelComponent());
				if (this.playerAudio != null) {
					this.remove(this.playerAudio.getControlPanelComponent());
				}
				this.actualTime = this.playerVideo.getMediaNanoseconds();
			} else {
				double px = (this.x * 100.0f) / this.getWidth();
				double py = (this.y * 100.0f) / this.getHeight();
				System.out.println(this.actualTime + ": (" + px + " , " + py + ")");
				this.coords.put(this.actualTime, new Pair(px, py));
				this.setVisualComponent(this.playerVideo.getVisualComponent());
				this.setControlPanelComponent(this.playerVideo.getControlPanelComponent());
				if (this.playerAudio != null) {
					this.setAudioControlPanelComponent(this.playerAudio.getControlPanelComponent());
				}
				this.playerVideo.start();
			}
			this.videoOn = !this.videoOn;
			this.doLayout();
		}
	}
	
	public void loadVideo() {
		JFileChooser chooser = new JFileChooser("./");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new FileNameExtensionFilter("Videos *.mpg", "mpg"));
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File videoFile = chooser.getSelectedFile();
			if (videoFile.getPath().contains(" ") || videoFile.getPath().contains("á") || videoFile.getPath().contains("é") || videoFile.getPath().contains("í") || videoFile.getPath().contains("ó") || videoFile.getPath().contains("ú")) {
				JOptionPane.showMessageDialog(this, "Ruta del video invalida, la ruta no debe contener espacios ni tildes");
				return;
			}
			this.setVideo(videoFile);
			this.videoOn = true;
		}
	}
	
	private void setVideo(File video) {
		try {
			this.videoFile = video;
			URL videoURL = video.toURI().toURL();
			this.playerVideo = Manager.createRealizedPlayer(new MediaLocator(videoURL));
			this.setVisualComponent(this.playerVideo.getVisualComponent());
			this.setControlPanelComponent(this.playerVideo.getControlPanelComponent());
			this.getVideoDimentions(video);
		}
		catch (Exception event) {
			System.out.println("Error: [" + event.getMessage() + "]");
			event.printStackTrace();
		}
		this.doLayout();
	}
	
	public void cargarAudio() {
		JFileChooser chooser = new JFileChooser("./");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new FileNameExtensionFilter("Musica *.mp3", "mp3"));
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File audioFile = chooser.getSelectedFile();
			if (audioFile.getPath().contains(" ") || audioFile.getPath().contains("á") || audioFile.getPath().contains("é") || audioFile.getPath().contains("í") || audioFile.getPath().contains("ó") || audioFile.getPath().contains("ú")) {
				JOptionPane.showMessageDialog(this, "Ruta del audio invalida, la ruta no debe contener espacios ni tildes");
				return;
			}
			this.setAudio(audioFile);
		}
	}
	
	private void setAudio(File audioFile) {
		try {
			this.audioFile = audioFile;
			URL videoURL = audioFile.toURI().toURL();
			this.playerAudio = Manager.createRealizedPlayer(new MediaLocator(videoURL));
			this.setAudioControlPanelComponent(this.playerAudio.getControlPanelComponent());
		}
		catch (Exception event) {
			System.out.println("Error: [" + event.getMessage() + "]");
			event.printStackTrace();
		}
		this.doLayout();
	}
	
	private void setAudioControlPanelComponent(Component controlPanelComponent) {
		this.add(controlPanelComponent, BorderLayout.NORTH);
	}
	
	private void setVisualComponent(Component visualComponent) {
		this.add(visualComponent, BorderLayout.CENTER);
	}
	
	private void setControlPanelComponent(Component controlPanelComponent) {
		this.add(controlPanelComponent, BorderLayout.SOUTH);
	}
	
	private void getVideoDimentions(File video) {
		IContainer container = IContainer.make();
		int result = container.open(video.getPath(), IContainer.Type.READ, null);
		if (result >= 0) {
			int numStreams = container.getNumStreams();
			for (int i = 0; i < numStreams; i++) {
				IStream stream = container.getStream(i);
				IStreamCoder coder = stream.getStreamCoder();
				if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
					this.videoWidth = coder.getWidth();
					this.videoHeight = coder.getHeight();
				}
			}
		}
	}
	
	public void getAudioStream(File video, IMediaWriter videoWriter) {
		IContainer container = IContainer.make();
		int result = container.open(video.getPath(), IContainer.Type.READ, null);
		if (result >= 0) {
			int numStreams = container.getNumStreams();
			int audiostreamt = -1;
			
			for (int i = 0; i < numStreams; i++) {
				IStream stream = container.getStream(i);
				IStreamCoder coder = stream.getStreamCoder();
				
				if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
					audiostreamt = i;
					break;
				}
			}
			
			if (audiostreamt != -1) {
				IStreamCoder audioCoder = container.getStream(audiostreamt).getStreamCoder();
				if (audioCoder.open() < 0) { throw new RuntimeException("Cant open audio coder"); }
				videoWriter.addAudioStream(1, 1, audioCoder.getChannels(), audioCoder.getSampleRate());
				
				IPacket packetaudio = IPacket.make();
				
				while (container.readNextPacket(packetaudio) >= 0) {
					
					if (packetaudio.getStreamIndex() == audiostreamt) {
						IAudioSamples samples = IAudioSamples.make(512, audioCoder.getChannels(), IAudioSamples.Format.FMT_S32);
						int offset = 0;
						while (offset < packetaudio.getSize()) {
							int bytesDecodedaudio = audioCoder.decodeAudio(samples, packetaudio, offset);
							if (bytesDecodedaudio < 0) { throw new RuntimeException("could not detect audio"); }
							offset += bytesDecodedaudio;
							
							if (samples.isComplete()) {
								videoWriter.encodeAudio(1, samples);
							}
						}
					}
				}
			}
		}
	}
	
	private class Pair {
		private double px;
		private double py;
		
		public Pair(double px, double py) {
			this.px = px;
			this.py = py;
		}
		
		public double getPx() {
			return this.px;
		}
		
		public double getPy() {
			return this.py;
		}
	}
}
