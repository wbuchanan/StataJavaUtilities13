package org.paces.Stata.DataRecords;

import com.stata.sfi.Data;
import org.paces.Stata.MetaData.Meta;

import java.util.*;

/***
 * Creates an Array of Integers for Individual Observation
 *
 * @author Billy Buchanan
 * @version 0.0.0
 *
 */
public class DataRecordIntArray implements Record {

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
	private Integer[] observation;

	/**
	 * Default value used to represent missing values
	 */
	private Integer missing = -1;

	/***
	 * Constructor method for DataRecordIntArray class
	 * @param id The observation index value for which to retrieve the data for
	 * @param metaobject A Meta class object containing metadata from the
	 *                      Stata dataset
	 *
	 */
	public DataRecordIntArray(Integer id, Meta metaobject) {

		// The metadata object
		this.metaob = metaobject;

		// Set the observation ID variable
		setObid(id);

		// Set the data (observation) variable
		setData(id);

	} // End declaration of constructor method

	/***
	 * Constructor method for DataRecordIntArray class
	 * @param id The observation index value for which to retrieve the data for
	 * @param metaobject A Meta class object containing metadata from the
	 *                      Stata dataset
	 * @param missingValue The value used to represent missing values for
	 *                        numeric dataset objects
	 */
	public DataRecordIntArray(Integer id, Meta metaobject, Integer
		missingValue) {
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

		Integer[] values = new Integer[metaob.varindex.size()];

		// Loop over the variable indices
		for (Integer i : metaob.getVarindex()) {

			// Check to see if value is missing
			if (Data.isValueMissing(Data.getNum(i, obid))) {

				// If value is missing, set value to -1.0
				values[i] = this.missing;

			} else {

				// Convert numeric variables to string
				values[i] = (int) Math.round(Data.getNum(i, obid) / 1);

			} // End ELSE Block for non-missing values

		} // End of Loop

		// Set the observation value
		this.observation = values;

	} // End of setData method declaration

	/***
	 * Retrieves the data for a given record
	 * @return An object with the values for variables of interest on a given
	 * observation
	 */
	@Override
	public Integer[] getData() {

		// Returns the array for the observation
		return this.observation;

	} // End of getData method declaration

	/**
	 * Method to retrieve the data as a List object
	 * @return The data in a List object
	 */
	public List<Integer> toList() {

		// Converts the array of objects into a List of the same objects
		return Arrays.asList(this.observation);

	} // End of the method declaration



} // End of Class declaration
