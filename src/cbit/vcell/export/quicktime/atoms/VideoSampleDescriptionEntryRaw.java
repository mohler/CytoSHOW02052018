package cbit.vcell.export.quicktime.atoms;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import cbit.vcell.export.quicktime.*;
import java.io.*;
/**
 * This type was created in VisualAge.
 */
public class VideoSampleDescriptionEntryRaw extends VideoSampleDescriptionEntry {
	public final static int SIZE = 86;
	public final static String DATA_FORMAT = "raw ";

/**
 * This method was created in VisualAge.
 * @param frameWidth int
 * @param frameHeight int
 * @param frameResolution int
 * @param colorDepth int
 */
public VideoSampleDescriptionEntryRaw(int frameWidth, int frameHeight) {
	this((short)frameWidth, (short)frameHeight, defaultFrameResolution, defaultColorDepth, (short)0);
}


/**
 * This method was created in VisualAge.
 * @param frameWidth int
 * @param frameHeight int
 * @param frameResolution int
 * @param colorDepth int
 */
public VideoSampleDescriptionEntryRaw(int frameWidth, int frameHeight, int referenceIndex) {
	this((short)frameWidth, (short)frameHeight, defaultFrameResolution, defaultColorDepth, (short)referenceIndex);
}


/**
 * This method was created in VisualAge.
 * @param frameWidth int
 * @param frameHeight int
 * @param frameResolution int
 * @param colorDepth int
 */
public VideoSampleDescriptionEntryRaw(int frameWidth, int frameHeight, int frameResolution, int colorDepth, int referenceIndex) {
	this((short)frameWidth, (short)frameHeight, frameResolution, (short)colorDepth, (short)referenceIndex);
}


/**
 * This method was created in VisualAge.
 * @param width short
 * @param height short
 * @param resolution short
 * @param depth short
 */
public VideoSampleDescriptionEntryRaw(short frameWidth, short frameHeight, int frameResolution, short colorDepth, short referenceIndex) {
	size = SIZE;
	dataFormat = DATA_FORMAT;
	dataReferenceIndex = referenceIndex;
	version = (short)0;
	revisionLevel = (short)0;
	vendor = "appl";
	temporalQuality = lossLessQuality;
	spatialQuality = lossLessQuality;
	width = frameWidth;
	height = frameHeight;
	horizontalResolution = frameResolution;
	verticalResolution = frameResolution;
	dataSize = defaultDataSize;
	frameCount = (short)1;
	compressorName[0] = (byte)4; for (int i=0;i<4;i++) compressorName[i+1] = "None".getBytes()[i];
	depth = colorDepth;
	colorTableID = defaultColorTableID;
}


/**
 * writeData method comment.
 */
public boolean writeData(DataOutputStream out) {
	try {
		out.writeInt(size);
		out.writeBytes(dataFormat);
		out.write(reserved);
		out.writeShort(dataReferenceIndex);
		out.writeShort(version);
		out.writeShort(revisionLevel);
		out.writeBytes(vendor);
		out.writeInt(temporalQuality);
		out.writeInt(spatialQuality);
		out.writeShort(width);
		out.writeShort(height);
		out.writeInt(horizontalResolution);
		out.writeInt(verticalResolution);
		out.writeInt(dataSize);
		out.writeShort(frameCount);
		out.write(compressorName);
		out.writeShort(depth);
		out.writeShort(colorTableID);
		return true;
	} catch (IOException e) {
		System.out.println("Unable to write: " + e.getMessage());
		e.printStackTrace();
		return false;
	}
}
}