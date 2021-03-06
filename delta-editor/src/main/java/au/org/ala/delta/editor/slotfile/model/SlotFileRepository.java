/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.editor.slotfile.model;

import au.org.ala.delta.editor.DeltaFileReader;
import au.org.ala.delta.editor.slotfile.DeltaVOP;
import au.org.ala.delta.editor.slotfile.SlotFile;
import au.org.ala.delta.io.BinFileMode;
import au.org.ala.delta.model.DeltaDataSetRepository;
import au.org.ala.delta.model.MutableDeltaDataSet;
import au.org.ala.delta.util.IProgressObserver;

import java.io.File;

/**
 * Provides access to DELTA Data sets via a slot file implementation.
 */
public class SlotFileRepository implements DeltaDataSetRepository {

	/** 
	 * Tracks the number of new data sets created so each can be given
	 * a unique name
	 */
	private int _count;

	/** 
	 * Saves the supplied data set to permanent storage
	 * @param dataSet the DELTA data set to save.
	 * @param observer allows the progress of the save to be tracked if required.
	 * @see au.org.ala.delta.model.DeltaDataSetRepository#save(au.org.ala.delta.model.MutableDeltaDataSet, IProgressObserver)
	 */
	@Override
	public void save(MutableDeltaDataSet dataSet, IProgressObserver observer) {
		getVOP(dataSet).commit(null);
	}
	
	/** 
	 * Saves the supplied data set to permanent storage
	 * @param dataSet the DELTA data set to save.
	 * @param name the new file name for the data set.
	 * @param observer allows the progress of the save to be tracked if required.
	 * @see au.org.ala.delta.model.DeltaDataSetRepository#save(au.org.ala.delta.model.MutableDeltaDataSet, IProgressObserver)
	 */
	@Override
	public void saveAsName(MutableDeltaDataSet dataSet, String name, boolean overwriteExisting, IProgressObserver observer) {
		
		File f = new File(name);
		if (f.exists()) {
			if (!overwriteExisting) {
				throw new RuntimeException("File already exists! " + f.getAbsolutePath());
			} else {
				if (!f.delete()) {
					throw new RuntimeException("Could not overwrite existing file: " + f.getAbsolutePath());					
				}
			}
		}
		
		SlotFile newFile = new SlotFile(name, BinFileMode.FM_NEW);
		getVOP(dataSet).commit(newFile);
		
	}

	/**
	 * This implementation expects the supplied name to be a filename.
	 * 
	 * @param name the absolute path of the DELTA file.
	 * @param observer allows the progress of the file load to be tracked if required.
	 *  
	 * @see au.org.ala.delta.model.DeltaDataSetRepository#findByName(java.lang.String, IProgressObserver)
	 */
	@Override
	public MutableDeltaDataSet findByName(String name, IProgressObserver observer) {

		MutableDeltaDataSet dataSet = DeltaFileReader.readDeltaFile(name, observer);
		return dataSet;
	}
	
	/**
	 * Creates a new DeltaDataSet backed by a new DeltaVOP.
	 */
	@Override
	public MutableDeltaDataSet newDataSet() {
		DeltaVOP vop = new DeltaVOP();
		SlotFileDataSetFactory _factory = new SlotFileDataSetFactory(vop);
		return _factory.createDataSet("Document"+_count++);
	}

	private DeltaVOP getVOP(MutableDeltaDataSet dataSet) {
		SlotFileDataSet vop = (SlotFileDataSet)dataSet;
		return vop.getVOP();
	}
	
}
