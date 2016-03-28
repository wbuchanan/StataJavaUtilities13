package org.paces.Stata.DataRecords;

import com.stata.sfi.*;
import org.paces.Stata.MetaData.Meta;

import java.util.*;

/***
 * Creates an Array of Bytes for Individual Observation
 *
 * @author Billy Buchanan
 * @version 0.0.0
 *
 */
public class DataRecordByteArray implements Record {

	/***
	 * A Stata Metadata object
	 */
	private Meta metaob;

	/***
	 * Observation ID variable
	 */
	private Integer obid;

	/***
	 * Variable containing the data for a given observation
	 */
	private Byte[] observation;

	/**
	 * Default value to use for representing missing values
	 */
	private Byte missing = (byte) -1;

	/***
	 * Constructor method for DataRecordByteArray class
	 * @param id The observation index value for which to retrieve the data for
	 * @param metaobject A Meta class object containing metadata from the
	 *                      Stata dataset
	 */
	public DataRecordByteArray(Integer id, Meta metaobject) {
		this.metaob = metaobject;
		setObid(id);
		setData(id);
	}

	/***
	 * Constructor method for DataRecordByteArray class
	 * @param id The observation index value for which to retrieve the data for
	 * @param metaobject A Meta class object containing metadata from the
	 *                      Stata dataset
	 * @param missingValue The value used to represent missing values for
	 *                        numeric dataset objects
	 */
	public DataRecordByteArray(Integer id, Meta metaobject, Byte missingValue) {
		this(id, metaobject);
		this.missing = missingValue;
	}

	/***
	 * Method to set the observation index value for the record
	 * @param observationNumber An observation index value
	 */
	@Override
	public void setObid(Integer observationNumber) {

		// Sets the observation index value for the object
		this.obid = observationNumber;

	} // End of setObid method declaration

	/***
	 * Constructs the object containing the data for the record
	 * @param obid The observation ID for which the data are to be extracted
	 */
	@Override
	public void setData(Integer obid) {

		Byte[] values = new Byte[metaob.varindex.size()];

		// Loop over the variable indices
		for (Integer i : metaob.getVarindex()) {

			// Check to see if value is missing
			if (Data.isValueMissing(Data.getNum(i, obid))) {

				// If value is missing, set value to -1.0
				values[i] = this.missing;

			} else {

				// Convert numeric variables to string
				values[i] = (byte) Math.round(Data.getNum(i, obid) / 1);

			} // End ELSE Block for non-missing values

		} // End of Loop

		// Set the observation value
		this.observation = values;

	} // End of setData method declaration

	/***
	 * Retrieves the data for a given record
	 *
	 * @return An object with the values for variables of interest on a given
	 * observation
	 */
	@Override
	public Byte[] getData() {

		// Returns the array for the observation
		return this.observation;

	} // End of getData method declaration

	/**
	 * Method to retrieve the data as a List of Byte objects
	 * @return The array as a List of Byte elements
	 */
	public List<Byte> toList() {

		return Arrays.asList(this.observation);

	} // End Method declaration

} // End of Class declaration
