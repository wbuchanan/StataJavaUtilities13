package org.paces.Stata.DataSets;

import com.stata.sfi.Data;
import com.stata.sfi.Macro;
import org.paces.Stata.MetaData.Meta;

import java.util.ArrayList;
import java.util.List;

/**
 * A 2d Array of Bytes containing the data from the active data set in memory.
 *
 * @author Billy Buchanan
 * @version 0.0.0
 *
 */
public class DataSetByteArrays implements StataData {

	/***
	 * A new Meta object
	 */
	private Meta metaob;

	/***
	 * The name of the data set in memory to be converted to a JSON object
	 */
	private String filename;

	/***
	 * A 2d array of Byte objects
	 */
	private Byte[][] stataDataSet;

	/**
	 * Default value used to represent missing data
	 */
	private Byte missing = (byte) -1;

	/***
	 * Constructor method for DataSetByteArrays class
	 * @param metaobject A Meta class object containing metadata from the
	 *                      Stata dataset
	 */
	public DataSetByteArrays(Meta metaobject) {

		// Set the meta object to the value passed to the constructor
		this.metaob = metaobject;

		// Set the name variable
		setFileName();

		// Builds the data object
		setData();

	} // End constructor declaration

	/***
	 * Constructor method for DataSetByteArrays class
	 * @param metaobject A Meta class object containing metadata from the
	 *                      Stata dataset
	 * @param missingValue The value used to represent missing values for
	 *                        numeric dataset objects
	 */
	public DataSetByteArrays(Meta metaobject, Byte missingValue) {
		this(metaobject);
		this.missing = missingValue;
	}

	/***
	 * Generic Setter method for the name of the dataset object
	 */
	public void setFileName() {

		// Store the value of `"`c(filename)'"' as a Java string
		String nm = Macro.getLocalSafe("c(filename)");

		// If the dataset name is not empty
		if (!nm.isEmpty()) {

			// Assign the Stata file name as the name of the object
			this.filename = nm;

		} else {

			// Otherwise set a generic name
			this.filename = "Stata Data Set";

		} // End IF/ELSE Block for object name

	} // End declaration of setname method

	/***
	 * Method to store Stata dataset as a 2d Array of Byte objects.  This is
	 * used for passing the data set to objects/methods in the com.itemanalysis
	 * .psychometrics package to expand IRT modeling options in Stata.
	 */
	@Override
	public void setData() {

		// Initialize container to ID the observation and contains a Map
		// object with key/value pairs
		Byte[][] obs = new Byte[this.metaob.getObsindex().size()][this.metaob.getVarindex().size()];

		for (Integer i = 0; i < this.metaob.getObsindex().size(); i++) {

			// Loop over the variable indices
			for (Integer j = 0; j < this.metaob.getVarindex().size(); j++) {

				// Check to see if value is missing
				if (Data.isValueMissing(Data.getNum(this.metaob.getVarindex(j), i))) {

					// If value is missing, set value to this.missing
					// (defaults to  -1)
					obs[i][j] = this.missing;

				} else {

					// Make sure the value is truncated to a byte type
					obs[i][j] = ((byte)Math.round(Data.getNum(this.metaob.getVarindex(j), i) / 1.0D));

				} // End ELSE Block for non-missing values

			} // End of Loop over variable

		} // End of Loop over observations

		// Set the member variable to this value
		this.stataDataSet = obs;

	} // End method declaration to set data value of class

	/***
	 * Getter method to access the POJO representation of the Stata dataset
	 * @return A POJO representation of the Stata Dataset
	 */
	@Override
	public Byte[][] getData() {

		// Returns the sole member variable of the class
		return this.stataDataSet;

	} // End getter method declaration

	/***
	 * Method to retrieve a single record of values
	 * @param record The integer value of the first index in the 2d Array
	 * @return An array of bytes
	 */
	@Override
	public Byte[] getData(Integer record) {

		// Returns a single row of data from the dataset object
		return this.getData()[record];

	} // End overloaded getter method declaration

	/***
	 * Method to retrieve a datum from the dataset object
	 * @param record The integer value of the first index of the 2d array
	 * @param var The integer value of the second index of the 2d array
	 * @return An object containing the datum[i, j] of the data set
	 */
	@Override
	public Byte getData(Integer record, Integer var) {

		// Returns a single datum[i, j] from the dataset object
		// where i is passed as the parameter record and
		// j is passed as the parameter var
		return this.getData()[record][var];

	} // End overloaded getter method declaration

	/***
	 * Getter method to the name of the Object
	 * @return The name of the Stata Data object
	 */
	public String getFileName() {

		// Returns the name of the Stata dataset (or generic placeholder)
		// used to construct a JSON object
		return this.filename;

	} // End of getName method declaration

	/***
	 * Converts the dataset into a list of variable values nested within a
	 * list of records
	 * @return A List of Objects containing the datum[i, j] nested within a
	 * List containing the data[i, j...n]
	 */
	@Override
	public List<List<Byte>> toList() {

		// Initializes the object to store the data that will be returned by
		// this method
		List<List<Byte>> dataset = new ArrayList<>();

		// Starts loop over the record indices
		for (Integer i = 0; i < this.metaob.getObsindex().size(); i++) {

			// Initializes an object to store the values for a given record
			List<Byte> record = new ArrayList<>();

			// Starts loop over the individual variables
			for (Integer j = 0; j < this.metaob.getVarindex().size(); j++) {

				// Adds the datum to the record object using the getData
				// method with the row and column indices passed as arguments
				record.add(j, getData(i, j));

			} // Ends the loop over the variables

			// Adds the complete record to the object initialized at the
			// beginning of the method
			dataset.add(i, record);

		} // Ends the loop over the observations

		// Returns the List object
		return dataset;

	} // End of Method declaration

} // End Class declaration


