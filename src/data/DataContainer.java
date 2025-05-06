package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The DataContainer class represents a container for time series data stored in a CSV file.
 * It provides methods for reading data from a CSV file, filtering data by date range,
 * resampling data at different time intervals, and calculating sums or averages of energy variables.
 * The data is organized by time and variable, with methods for accessing and manipulating it.
 */
public class DataContainer {

    private ArrayList<String> timeStrings;
    private ArrayList<String> orderedVariableNames;
    private TreeMap<String, ArrayList<Double>> data;
    private int numberOfSamples = 0;

    /**
     * Constructs a DataContainer by reading data from a CSV file.
     * The CSV file is expected to have a header row followed by time series data.
     *
     * @param csvFileName the name of the CSV file containing the data
     * @throws IOException if an error occurs while reading the file
     */
    public DataContainer(String csvFileName) throws IOException {
        orderedVariableNames = new ArrayList<>();
        data = new TreeMap<>();
        timeStrings = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFileName));
        String line = bufferedReader.readLine();
        String[] tokens = line.split(",");

        for (int i = 1; i < tokens.length; i++) {
            orderedVariableNames.add(tokens[i]);
            data.put(tokens[i], new ArrayList<>());
        }
        while ((line = bufferedReader.readLine()) != null) {
            String[] values = line.split(",");
            timeStrings.add(values[0]);
            for (int i = 1; i < values.length; i++) {
                data.get(orderedVariableNames.get(i - 1)).add(Double.parseDouble(values[i]));
            }
        }
        bufferedReader.close();
        numberOfSamples = timeStrings.size();
    }

    /**
     * Gets the number of data samples in the container.
     *
     * @return the number of samples
     */
    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    /**
     * Gets the names of all available variables.
     *
     * @return an array of variable names
     */
    public String[] getAvailableVariables() {
        return orderedVariableNames.toArray(new String[0]);
    }

    /**
     * Gets the time strings corresponding to the data samples.
     *
     * @return an array of time strings
     */
    public String[] getTimeStrings() {
        return timeStrings.toArray(new String[0]);
    }

    /**
     * Gets the data for a specific variable.
     *
     * @param variableName the name of the variable
     * @return an array of values for the specified variable
     */
    public Double[] getData(String variableName) {
        return data.get(variableName).toArray(new Double[0]);
    }

    /**
     * Filters the data by the specified date range.
     * Only data within the given start and end dates (inclusive) will be included.
     *
     * @param start the start date in the format "yyyy-MM-dd HH:mm:ss"
     * @param end   the end date in the format "yyyy-MM-dd HH:mm:ss"
     * @return a new DataContainer containing the filtered data
     * @throws ParseException if the date format is invalid
     */
    public DataContainer filterByDateRange(String start, String end) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");
        Date startDate = format.parse(start + "+00:00");
        Date endDate = format.parse(end + "+00:00");

        ArrayList<String> filteredTimeStrings = new ArrayList<>();
        TreeMap<String, ArrayList<Double>> filteredData = new TreeMap<>();

        for (String variable : orderedVariableNames) {
            filteredData.put(variable, new ArrayList<>());
        }

        for (int i = 0; i < timeStrings.size(); i++) {
            Date current = format.parse(timeStrings.get(i) + "+00:00");
            if (!current.before(startDate) && !current.after(endDate)) {
                filteredTimeStrings.add(timeStrings.get(i));
                for (String variable : orderedVariableNames) {
                    filteredData.get(variable).add(data.get(variable).get(i));
                }
            }
        }

        DataContainer filteredContainer = new DataContainer();
        filteredContainer.timeStrings = filteredTimeStrings;
        filteredContainer.data = filteredData;
        filteredContainer.orderedVariableNames = orderedVariableNames;
        filteredContainer.numberOfSamples = filteredTimeStrings.size();
        return filteredContainer;
    }

    /**
     * Resamples the data at a specified sampling interval.
     * The available intervals are "1 Hour", "1 Day", and "1 Month".
     *
     * @param samplingInterval the desired sampling interval
     * @return a new DataContainer containing the resampled data
     * @throws ParseException if the date format is invalid
     * @throws IllegalArgumentException if the sampling interval is not valid
     */
    public DataContainer resampleData(String samplingInterval) throws ParseException {
        if (!Arrays.asList("1 Hour", "1 Day", "1 Month").contains(samplingInterval)) {
            throw new IllegalArgumentException("Invalid sampling interval. Valid options are: 1 Hour, 1 Day, 1 Month.");
        }

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");
        DateFormat outputFormat;

        Calendar calendar = Calendar.getInstance();
        ArrayList<String> resampledTimeStrings = new ArrayList<>();
        TreeMap<String, ArrayList<Double>> resampledData = new TreeMap<>();

        for (String variable : orderedVariableNames) {
            resampledData.put(variable, new ArrayList<>());
        }

        HashMap<String, ArrayList<Double>> tempData = new HashMap<>();
        for (String variable : orderedVariableNames) {
            tempData.put(variable, new ArrayList<>());
        }

        String currentBucket = null;

        for (int i = 0; i < timeStrings.size(); i++) {
            Date currentDate = inputFormat.parse(timeStrings.get(i) + "+00:00");
            calendar.setTime(currentDate);

            switch (samplingInterval) {
                case "1 Hour":
                    outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00XXX");
                    break;
                case "1 Day":
                    outputFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00XXX");
                    break;
                case "1 Month":
                    outputFormat = new SimpleDateFormat("yyyy-MM-01 00:00:00XXX");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + samplingInterval);
            }

            String bucket = outputFormat.format(currentDate);

            // bucket changes, compute averages and reset
            if (currentBucket != null && !bucket.equals(currentBucket)) {
                resampledTimeStrings.add(currentBucket);
                for (String variable : orderedVariableNames) {
                    ArrayList<Double> values = tempData.get(variable);
                    double average = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    resampledData.get(variable).add(average);
                }
                for (String variable : orderedVariableNames) {
                    tempData.get(variable).clear();
                }
            }

            currentBucket = bucket;
            for (String variable : orderedVariableNames) {
                tempData.get(variable).add(data.get(variable).get(i));
            }
        }

        // Last bucket
        if (currentBucket != null) {
            resampledTimeStrings.add(currentBucket);
            for (String variable : orderedVariableNames) {
                ArrayList<Double> values = tempData.get(variable);
                double average = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                resampledData.get(variable).add(average);
            }
        }

        // crate a new DataContainer with the resampled data
        DataContainer resampledContainer = new DataContainer();
        resampledContainer.timeStrings = resampledTimeStrings;
        resampledContainer.data = resampledData;
        resampledContainer.orderedVariableNames = orderedVariableNames;
        resampledContainer.numberOfSamples = resampledTimeStrings.size();
        return resampledContainer;
    }

    /**
     * computes the sum of all electric power variables from the comptuers and stores the result
     * in a new variable called "puissance_electrique_sum".
     */
    public void computePuissanceElectriqueSum() {
        String sumVariableName = "puissance_electrique_sum";
        ArrayList<Double> sumValues = new ArrayList<>();
    
        for (int i = 0; i < numberOfSamples; i++) {
            double sum = 0.0;
            for (String variable : orderedVariableNames) {
                if (variable.startsWith(" puissance_electrique")) {
                    sum += data.get(variable).get(i);
                }
            }
            sumValues.add(sum);
        }
    
        // Add the new variable to the container
        orderedVariableNames.add(sumVariableName);
        data.put(sumVariableName, sumValues);
    }

    /**
     * Private constructor for creating an empty DataContainer.
     * This is used when creating filtered or resampled containers.
     */
    private DataContainer() {
    }
}
