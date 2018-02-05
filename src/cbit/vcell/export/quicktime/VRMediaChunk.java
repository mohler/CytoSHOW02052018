package cbit.vcell.export.quicktime;
import cbit.vcell.export.quicktime.atoms.*;
/**
 * Insert the type's description here.
 * Creation date: (11/8/2005 10:54:49 PM)
 * @author: Ion Moraru
 */
public class VRMediaChunk implements MediaChunk {
	private VRMediaSample[] samples;
	private long offset;
	private String dataReference = "self";
	private String dataReferenceType = "alis";

/**
 * Insert the method's description here.
 * Creation date: (11/8/2005 10:58:05 PM)
 * @param vrWorld cbit.vcell.export.quicktime.VRWorld
 */
public VRMediaChunk(VRWorld vrWorld) {
	samples = new VRMediaSample[vrWorld.getNumberOfNodes()];
	for (int i = 0; i < samples.length; i++){
		samples[i] = new VRMediaSample(vrWorld, i);
	}
}


/**
 * This method was created in VisualAge.
 * @return java.lang.Byte[]
 */
public byte[] getDataBytes() {
	byte[] bytes = new byte[getSize()];
	int idx = 0;
	for (int i = 0; i < samples.length; i++){
		byte[] sampleBytes = samples[i].getDataBytes();
		for (int j = 0; j < sampleBytes.length ; j++){
			bytes[idx] = sampleBytes[j];
			idx++;
		}
	}
	return bytes;
}


/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public String getDataFormat() {
	return samples[0].getDataFormat();
}


/**
 * This method was created in VisualAge.
 * @return java.lang.Object
 */
public String getDataReference() {
	return dataReference;
}


/**
 * This method was created in VisualAge.
 * @return java.lang.Object
 */
public String getDataReferenceType() {
	return dataReferenceType;
}


/**
 * This method was created in VisualAge.
 * @return int
 */
public int getDuration() {
	return samples[0].getDuration();
}


/**
 * This method was created in VisualAge.
 * @return SampleDescriptionEntry
 */
public cbit.vcell.export.quicktime.MediaSampleInfo[] getMediaSampleInfos() {
	return samples;
}


/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public String getMediaType() {
	return samples[0].getMediaType();
}


/**
 * This method was created in VisualAge.
 * @return int
 */
public int getNumberOfSamples() {
	return samples.length;
}


/**
 * This method was created in VisualAge.
 * @return int
 */
public long getOffset() {
	return offset;
}


/**
 * This method was created in VisualAge.
 * @return SampleDescriptionEntry
 */
public cbit.vcell.export.quicktime.atoms.SampleDescriptionEntry getSampleDescriptionEntry() {
	return samples[0].getSampleDescriptionEntry();
}


/**
 * This method was created in VisualAge.
 * @return int
 */
public int getSize() {
	int size = 0;
	for (int i = 0; i < samples.length; i++){
		size += samples[i].getSize();
	}
	return size;
}


/**
 * Insert the method's description here.
 * Creation date: (11/26/2005 7:25:39 PM)
 * @return boolean
 * @param file java.io.File
 */
public boolean isDataInFile(java.io.File file) {
	return false;
}


/**
 * This method was created in VisualAge.
 * @param dataReference java.lang.String
 */
public void setDataReference(String dataReference) {
	this.dataReference = dataReference;
}


/**
 * This method was created in VisualAge.
 * @param dataReference java.lang.String
 */
public void setDataReferenceType(String dataReferenceType) {
	this.dataReferenceType = dataReferenceType;
}


/**
 * This method was created in VisualAge.
 * @param offset int
 */
public void setOffset(long offset) {
	this.offset = offset;
}
}