package co.edu.javeriana.desarrollo.editor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;

public class VideoTools {
	public static void extractFrames(String folderDestiny, String videoFileName, String imagesFormat) {
		IMediaReader videoReader = ToolFactory.makeReader(videoFileName);
		videoReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		File folder = new File(folderDestiny);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		videoReader.addListener(new MediaToolAdapter() {
			@Override
			public void onVideoPicture(IVideoPictureEvent event) {
				try {
					ImageIO.write(event.getImage(), imagesFormat, new File("./Frames/" + event.getTimeStamp() + "." + imagesFormat));
				}
				catch (IOException event1) {
					System.out.println("Error: [" + event1.getMessage() + "]");
				}
			}
		});
		while (videoReader.readPacket() == null) {
			;
		}
		System.out.println("listo");
	}
}
