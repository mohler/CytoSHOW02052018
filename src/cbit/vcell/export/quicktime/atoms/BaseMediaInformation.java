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
public class BaseMediaInformation extends MediaInformation {

	protected BaseMediaHeader baseMediaHeader;
	protected HandlerReference handlerReference;
	protected DataInformation dataInformation;
	protected SampleTable sampleTable;
	
/**
 * This method was created in VisualAge.
 * @param vmhd cbit.vcell.export.quicktime.atoms.VideoMediaInformationHeader
 * @param hdlr cbit.vcell.export.quicktime.atoms.HandlerReference
 * @param dinf cbit.vcell.export.quicktime.atoms.DataInformation
 * @param stbl cbit.vcell.export.quicktime.atoms.SampleTable
 */
public BaseMediaInformation(BaseMediaHeader gmhd, HandlerReference hdlr, DataInformation dinf, SampleTable stbl) {
	baseMediaHeader = gmhd;
	handlerReference = hdlr;
	dataInformation = dinf;
	sampleTable = stbl;
	size = 8 + gmhd.size + hdlr.size;
	if (dinf != null) size += dinf.size;
	if (stbl != null) size += stbl.size;
}


/**
 * This method was created in VisualAge.
 * @param out java.io.DataOutputStream
 */
public boolean writeData(DataOutputStream out) {
	try {
		out.writeInt(size);
		out.writeBytes(type);
		baseMediaHeader.writeData(out);
		handlerReference.writeData(out);
		if (dataInformation != null) dataInformation.writeData(out);
		if (sampleTable != null) sampleTable.writeData(out);
		return true;
	} catch (IOException e) {
		System.out.println("Unable to write: " + e.getMessage());
		e.printStackTrace();
		return false;
	}
}
}