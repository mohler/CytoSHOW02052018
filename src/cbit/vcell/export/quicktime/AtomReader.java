package cbit.vcell.export.quicktime;
/**
 * Insert the type's description here.
 * Creation date: (11/5/2005 12:03:57 PM)
 * @author: Ion Moraru
 */
public class AtomReader {
	private String fileName = "";
	private javax.swing.tree.DefaultMutableTreeNode rootNode = null;
	java.util.Hashtable nodeHash = new java.util.Hashtable(); // key = AtomInfo; element = TreeNode
	private java.io.DataInputStream din = null;
	public static final String[] containerAtoms = new String[] {
		"moov",
		"trak",
		"edts",
		"tref",
		"mdia",
		"minf",
		"dinf",
		"stbl",
		"gmhd"
	};
	class AtomInfo {
		private long offset = 0;
		private long size = 0;
		private String type = "";
		private boolean atom64 = false;
		private AtomInfo(long argOffset, long argSize, String argType, boolean largeAtom) {
			offset = argOffset;
			size = argSize;
			type = argType;
			atom64 = largeAtom;
		}
		public String toString() {
			return type+" "+Long.toHexString(offset)+"--"+Long.toHexString(offset+size-1)+" "+size;
		}
	}

/**
 * Insert the method's description here.
 * Creation date: (11/5/2005 12:04:35 PM)
 * @param din java.io.DataInputStream
 */
public AtomReader(String fileName) {
	this.fileName = fileName;
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2005 12:07:18 PM)
 * @return int
 */
public AtomReader.AtomInfo[] getAtoms(String atomType) throws java.io.IOException {
	java.util.Enumeration en = nodeHash.keys();
	java.util.Vector v = new java.util.Vector();
	while (en.hasMoreElements()) {
		AtomInfo info = (AtomInfo)en.nextElement();
		if (info.type.equals(atomType)) {
			v.addElement(info);
		}
	}
	return (AtomInfo[])cbit.util.BeanUtils.getArray(v, AtomInfo.class);
}


/**
 * Insert the method's description here.
 * Creation date: (11/27/2005 1:02:30 PM)
 * @return int
 */
public short getColorDepth(int trackNumber) throws java.io.IOException {
	AtomInfo[] stsd = getAtoms(cbit.vcell.export.quicktime.atoms.SampleTableDescription.type);
	java.io.File f = new java.io.File(fileName);
	din = new java.io.DataInputStream(new java.io.FileInputStream(f));
	din.skip(stsd[trackNumber - 1].offset + 16); // go to first entry
	int sampleSize = din.readInt();
	byte[] b4 = new byte[4];
	din.read(b4);
	String sampleType = new String(b4);
	if (sampleSize != cbit.vcell.export.quicktime.atoms.VideoSampleDescriptionEntryRaw.SIZE ||
		!sampleType.equals(cbit.vcell.export.quicktime.atoms.VideoSampleDescriptionEntryRaw.DATA_FORMAT)
		)
	{
		throw new RuntimeException("Only raw video media sample entry descriptions are supported");
	}
	din.skip(74);
	short colorDepth = din.readShort();
	din.close();
	return colorDepth;
}


/**
 * Insert the method's description here.
 * Creation date: (11/27/2005 1:01:16 PM)
 * @return int
 */
public long getDataLength() throws java.io.IOException {
	AtomInfo[] mdat = getAtoms(cbit.vcell.export.quicktime.atoms.MediaData.type);
	return mdat[0].size - 8;
}


/**
 * Insert the method's description here.
 * Creation date: (11/27/2005 1:00:09 PM)
 * @return long
 */
public long getDataOffset() throws java.io.IOException {
	AtomInfo[] mdat = getAtoms(cbit.vcell.export.quicktime.atoms.MediaData.type);
	return mdat[0].offset + 8;
}


/**
 * Insert the method's description here.
 * Creation date: (11/27/2005 12:59:28 PM)
 * @return int
 */
public int getHeight(int trackNumber) throws java.io.IOException {
	AtomInfo[] tkhd = getAtoms(cbit.vcell.export.quicktime.atoms.TrackHeader.type);
	java.io.File f = new java.io.File(fileName);
	din = new java.io.DataInputStream(new java.io.FileInputStream(f));
	din.skip(tkhd[trackNumber - 1].offset + tkhd[trackNumber - 1].size - 4);
	int height = din.readShort();
	din.close();
	return height;
}


/**
 * Insert the method's description here.
 * Creation date: (11/27/2005 12:58:36 PM)
 * @return int
 */
public int getNumberOfTracks() throws java.io.IOException {
	return getAtoms(cbit.vcell.export.quicktime.atoms.TrackAtom.type).length;
}


/**
 * Insert the method's description here.
 * Creation date: (11/27/2005 12:59:28 PM)
 * @return int
 */
public int getSampleNumber(int trackNumber) throws java.io.IOException {
	AtomInfo[] stsz = getAtoms(cbit.vcell.export.quicktime.atoms.SampleSize.type);
	java.io.File f = new java.io.File(fileName);
	din = new java.io.DataInputStream(new java.io.FileInputStream(f));
	din.skip(stsz[trackNumber - 1].offset + 16);
	int sampleNumber = din.readInt();
	din.close();
	return sampleNumber;
}


/**
 * Insert the method's description here.
 * Creation date: (11/27/2005 12:59:28 PM)
 * @return int
 */
public int getWidth(int trackNumber) throws java.io.IOException {
	AtomInfo[] tkhd = getAtoms(cbit.vcell.export.quicktime.atoms.TrackHeader.type);
	java.io.File f = new java.io.File(fileName);
	din = new java.io.DataInputStream(new java.io.FileInputStream(f));
	din.skip(tkhd[trackNumber - 1].offset + tkhd[trackNumber - 1].size - 8);
	int width = din.readShort();
	din.close();
	return width;
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2005 12:35:50 PM)
 * @return boolean
 * @param atomType java.lang.String
 */
public boolean isContainerAtom(String atomType) {
	return cbit.util.BeanUtils.arrayContains(containerAtoms, atomType);
}


/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {
	// Insert code to start the application here.
	try {
		AtomReader reader1 = new AtomReader(args[0]);
		AtomReader reader2 = args.length > 1 ? new AtomReader(args[1]) : null;
		reader1.readAtoms();
		if (reader2 != null) reader2.readAtoms();
		javax.swing.JFrame  frame = showTree(reader1, reader2);
		frame.pack();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.setVisible(true);
	} catch (Exception exc) {
		exc.printStackTrace();
	}
}


/**
 * Insert the method's description here.
 * Creation date: (11/9/2005 7:52:24 PM)
 */
public void printSelectionDetails(AtomInfo info) {
	try {
		java.io.File f = new java.io.File(fileName);
		din = new java.io.DataInputStream(new java.io.FileInputStream(f));
		long offset = info.offset + 8;
		din.skip(offset);
		System.out.println(info);
		int bytesToPrint = 0;
		if (info.size < 1008) {
			bytesToPrint = (int)info.size-8;
		} else {
			System.out.print("large atom, printing first 1000 bytes only");
			bytesToPrint = 1000;
		}
		byte[] bytes = new byte[bytesToPrint];
		din.read(bytes);
		String s = "";
		for (int i = 0; i < bytes.length; i++){
			s += " " + Integer.toHexString(bytes[i] < 0 ? bytes[i]+256 : bytes[i]);
		}
		System.out.println(s);
		din.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2005 12:07:18 PM)
 * @return int
 */
public void readAtoms() throws java.io.IOException {
	AtomInfo[] rootAtoms = readRootAtoms();
	rootNode = new javax.swing.tree.DefaultMutableTreeNode(fileName);
	for (int i = 0; i < rootAtoms.length; i++){
		javax.swing.tree.DefaultMutableTreeNode treeNode = new javax.swing.tree.DefaultMutableTreeNode(rootAtoms[i]);
		rootNode.add(treeNode);
		nodeHash.put(rootAtoms[i], treeNode);
	}
	java.util.Vector contAtoms = new java.util.Vector();
	for (int i = 0; i < rootAtoms.length; i++){
		if (isContainerAtom(rootAtoms[i].type)) {
			contAtoms.add(rootAtoms[i]);
		}
	}
	while (!contAtoms.isEmpty()) {
		java.util.Vector moreContAtoms = new java.util.Vector();
		java.util.Enumeration en = contAtoms.elements();
		while (en.hasMoreElements()) {
			Object obj = en.nextElement();
			AtomInfo parentInfo = (AtomInfo)obj;
			AtomInfo[] childAtoms = readChildAtoms(parentInfo);
			for (int i = 0; i < childAtoms.length; i++){
				javax.swing.tree.DefaultMutableTreeNode parentNode = (javax.swing.tree.DefaultMutableTreeNode)nodeHash.get(parentInfo);
				javax.swing.tree.DefaultMutableTreeNode childNode = new javax.swing.tree.DefaultMutableTreeNode(childAtoms[i]);
				parentNode.add(childNode);
				nodeHash.put(childAtoms[i], childNode);
				if (isContainerAtom(childAtoms[i].type)) {
					moreContAtoms.add(childAtoms[i]);
				}
			}
		}
		contAtoms.clear();
		contAtoms.addAll(moreContAtoms);
	}
	readSampleDetails();
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2005 12:07:18 PM)
 * @return int
 */
public AtomReader.AtomInfo[] readChildAtoms(AtomReader.AtomInfo parentInfo) throws java.io.IOException {
	java.io.File f = new java.io.File(fileName);
	din = new java.io.DataInputStream(new java.io.FileInputStream(f));
	long offset = parentInfo.offset + 8;
	din.skip(offset);
	AtomInfo[] childAtoms = new AtomInfo[0];
	while (offset < parentInfo.offset + parentInfo.size) {
		AtomInfo info = readOneAtomSig(offset);
		din.skip(info.size - 8);
		offset += info.size;
		childAtoms = (AtomInfo[])cbit.util.BeanUtils.addElement(childAtoms, info);
	}
	din.close();
	return childAtoms;
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2005 12:23:09 PM)
 */
public AtomInfo readOneAtomSig(long offset) throws java.io.IOException {
	long size = din.readInt();
	byte[] b4 = new byte[4];
	din.read(b4);
	String type = new String(b4);
	boolean largeAtom = false;
	if (size == 1) { // 64-bit atom, read real size, 8 extra bytes after type
		largeAtom = true;
		size = din.readLong();
	}
	AtomInfo info = new AtomInfo(offset, size, type, largeAtom);
	System.out.println(info);
	return info;
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2005 12:07:18 PM)
 * @return int
 */
public AtomReader.AtomInfo[] readRootAtoms() throws java.io.IOException {
	java.io.File f = new java.io.File(fileName);
	din = new java.io.DataInputStream(new java.io.FileInputStream(f));
	long offset = 0;
	AtomInfo[] rootAtoms = new AtomInfo[0];
	while (offset < f.length()) {
		AtomInfo info = readOneAtomSig(offset);
		if (info.atom64) {
			din.skip(info.size - 16);			
		} else {
			din.skip(info.size - 8);
		}
		offset += info.size;
		rootAtoms = (AtomInfo[])cbit.util.BeanUtils.addElement(rootAtoms, info);
	}
	din.close();
	return rootAtoms;
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2005 12:07:18 PM)
 * @return int
 */
public void readSampleDetails() throws java.io.IOException {
	AtomInfo[] infos = (AtomInfo[])cbit.util.BeanUtils.getArray(nodeHash.keys(), AtomInfo.class);
	java.util.Comparator infoComp = new java.util.Comparator() {
		public int compare(Object o1, Object o2) {
			return Long.valueOf(((AtomInfo)o1).offset).compareTo(Long.valueOf(((AtomInfo)o2).offset));
		}
		public boolean equals(Object o) {
			return (this == o);
		}
	};
	java.util.Arrays.sort(infos, infoComp);
	for(int j = 0; j < infos.length; j++) {
		AtomInfo info = infos[j];
		if (info.type.equals("stsc")) {
			java.io.File f = new java.io.File(fileName);
			din = new java.io.DataInputStream(new java.io.FileInputStream(f));
			din.skip(info.offset+12);
			int entries = din.readInt();
			System.out.println("stsc table");
			for (int i = 0; i < entries; i++){
				System.out.println(din.readInt()+" "+din.readInt()+" "+din.readInt());
			}
			din.close();
		}
		if (info.type.equals("stsz")) {
			java.io.File f = new java.io.File(fileName);
			din = new java.io.DataInputStream(new java.io.FileInputStream(f));
			din.skip(info.offset+12);
			int size = din.readInt();
			int entries = din.readInt();
			System.out.println("stsz table");
			if (size != 0) {
				System.out.println(entries+" samples of size "+size);
			} else {
				System.out.println(entries+" samples of variable size, printing up to the first 20...");
				if (entries > 100) entries = 20;
				for (int i = 0; i < entries; i++){
					System.out.println(din.readInt());
				}
			}
			din.close();
		}
		if (info.type.equals("stco")) {
			java.io.File f = new java.io.File(fileName);
			din = new java.io.DataInputStream(new java.io.FileInputStream(f));
			din.skip(info.offset+12);
			int entries = din.readInt();
			System.out.println("stco table");
			System.out.println(entries+" chunk offsets, printing up to the first 20...");
			if (entries > 100) entries = 20;
			for (int i = 0; i < entries; i++){
				System.out.println(Integer.toHexString(din.readInt()));
			}
			din.close();
		}
		if (info.type.equals("co64")) {
			java.io.File f = new java.io.File(fileName);
			din = new java.io.DataInputStream(new java.io.FileInputStream(f));
			din.skip(info.offset+12);
			int entries = din.readInt();
			System.out.println("stco table");
			System.out.println(entries+" chunk offsets, printing up to the first 20...");
			if (entries > 100) entries = 20;
			for (int i = 0; i < entries; i++){
				System.out.println(Long.toHexString(din.readLong()));
			}
			din.close();
		}
	}
}


/**
 * Insert the method's description here.
 * Creation date: (11/6/2005 12:25:52 AM)
 */
public static javax.swing.JFrame showTree(final AtomReader a1, final AtomReader a2) {
	javax.swing.tree.DefaultTreeModel treeModel1 = a1 == null ? null : new javax.swing.tree.DefaultTreeModel(a1.rootNode);
	javax.swing.tree.DefaultTreeModel treeModel2 = a2 == null ? null : new javax.swing.tree.DefaultTreeModel(a2.rootNode);
	final javax.swing.JTree tree1 = new javax.swing.JTree(treeModel1);
	final javax.swing.JTree tree2 = new javax.swing.JTree(treeModel2);
	javax.swing.JFrame frame = new javax.swing.JFrame();
	frame.getContentPane().setLayout(new java.awt.BorderLayout());
	frame.getContentPane().add(tree1, java.awt.BorderLayout.WEST);
	frame.getContentPane().add(tree2, java.awt.BorderLayout.EAST);
	javax.swing.event.TreeSelectionListener tcl = new javax.swing.event.TreeSelectionListener() {
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			if (e.getSource().equals(tree1)) {
				javax.swing.tree.DefaultMutableTreeNode node = (javax.swing.tree.DefaultMutableTreeNode)e.getNewLeadSelectionPath().getLastPathComponent();
				a1.printSelectionDetails((AtomInfo)node.getUserObject());
			}
			if (e.getSource().equals(tree2)) {
				javax.swing.tree.DefaultMutableTreeNode node = (javax.swing.tree.DefaultMutableTreeNode)e.getNewLeadSelectionPath().getLastPathComponent();
				a2.printSelectionDetails((AtomInfo)node.getUserObject());
			}
		}
	};
	tree1.addTreeSelectionListener(tcl);
	tree2.addTreeSelectionListener(tcl);
	return frame;
}
}