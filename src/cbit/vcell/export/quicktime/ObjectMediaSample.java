package cbit.vcell.export.quicktime;

import cbit.vcell.export.quicktime.atoms.*;
/**
 * Insert the type's description here.
 * Creation date: (11/8/2005 10:06:44 PM)
 * @author: Ion Moraru
 */
public class ObjectMediaSample implements MediaSample {
	private VRWorld vrWorld;
	private int nodeIndex;
/**
 * Insert the method's description here.
 * Creation date: (11/8/2005 10:16:04 PM)
 */
public ObjectMediaSample(VRWorld vrWorld, int nodeIndex) {
	this.vrWorld = vrWorld;
	this.nodeIndex = nodeIndex;
}
/**
 * This method was created in VisualAge.
 * @return byte[]
 */
public byte[] getDataBytes() {
	java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
	java.io.DataOutputStream dout = new java.io.DataOutputStream(bout);
	try {
		vrWorld.getVRObjectInfoContainer(nodeIndex).writeData(dout);
	} catch (java.io.IOException exc) {
		exc.printStackTrace();
	}
	return bout.toByteArray();
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public java.lang.String getDataFormat() {
	return VRAtom.VR_OBJECT_NODE_TYPE;
}
/**
 * This method was created in VisualAge.
 * @return int
 */
public int getDuration() {
	return vrWorld.getDuration();
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public java.lang.String getMediaType() {
	return AtomConstants.MEDIA_TYPE_OBJECT;
}
/**
 * This method was created in VisualAge.
 * @return SampleDescriptionEntry
 */
public SampleDescriptionEntry getSampleDescriptionEntry() {
	return new ObjectSampleDescriptionEntry();
}
/**
 * This method was created in VisualAge.
 * @return int
 */
public int getSize() {
	return vrWorld.getVRObjectInfoContainer(nodeIndex).getSize();
}
/**
 * This method was created in VisualAge.
 * @return boolean
 */
public boolean isKeyFrame() {
	return false;
}
}
