package gui;

import javax.swing.*;
import data.DataContainer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A graphical user interface (GUI) for displaying and processing GreenEr data.
 * This class allows the user to select a date range, choose variables to plot (e.g., consumption, production, temperature, radiation),
 * and display relevant plots or calculate autonomous energy percentages.
 */

public class GreenErDataScreen extends JFrame {
    private JTextField startDateField, endDateField;
    private JCheckBox consumptionCheckbox, productionCheckbox, temperatureCheckbox, radiationCheckbox;
    private JComboBox<String> samplingTimeComboBox;

    /**
     * Constructs the GreenErDataScreen GUI.
     * 
     * @param parent the parent JFrame that will be shown after closing this screen.
     */

    public GreenErDataScreen(JFrame parent) {
        super("Green-Er Data");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(1200, 600));
        setResizable(false);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        JPanel variablePanel = new JPanel();
        variablePanel.setLayout(new BoxLayout(variablePanel, BoxLayout.Y_AXIS));

        consumptionCheckbox = new JCheckBox("Green_Er_Consumption_kW");
        productionCheckbox = new JCheckBox("Green_Er_Production_kW");
        temperatureCheckbox = new JCheckBox("Outdoor Temperature");
        radiationCheckbox = new JCheckBox("Global Radiation");

        variablePanel.add(consumptionCheckbox);
        variablePanel.add(productionCheckbox);
        variablePanel.add(temperatureCheckbox);
        variablePanel.add(radiationCheckbox);

        consumptionCheckbox.addActionListener(e -> adjustCheckboxSelection());
        productionCheckbox.addActionListener(e -> adjustCheckboxSelection());
        temperatureCheckbox.addActionListener(e -> adjustCheckboxSelection());
        radiationCheckbox.addActionListener(e -> adjustCheckboxSelection());

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 3;
        contentPane.add(variablePanel, c);

        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;
        contentPane.add(new JLabel("Please indicate the dates you wish to view:"), c);

        c.gridy = 1;
        contentPane.add(new JLabel("Dates must be between 2022-09-01 00:00:00 and 2023-08-31 23:00:00."), c);

        c.gridy = 2;
        contentPane.add(new JLabel("Initial date (format: yyyy-MM-dd HH:mm:ss)"), c);

        startDateField = new JTextField(20);
        c.gridy = 3;
        contentPane.add(startDateField, c);

        c.gridx = 2;
        c.gridy = 2;
        contentPane.add(new JLabel("Final date (format: yyyy-MM-dd HH:mm:ss)"), c);

        endDateField = new JTextField(20);
        c.gridy = 3;
        contentPane.add(endDateField, c);

        c.gridx = 1;
        c.gridy = 4;
        contentPane.add(new JLabel("Select Sampling Time:"), c);

        String[] samplingTimes = {"1 Hour", "1 Day", "1 Month"};
        samplingTimeComboBox = new JComboBox<>(samplingTimes);
        c.gridx = 2;
        c.gridy = 4;
        contentPane.add(samplingTimeComboBox, c);

        JButton plotButton = new JButton("Plot");
        plotButton.addActionListener(e -> handlePlot());
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        contentPane.add(plotButton, c);

        JButton energyPercentageButton = new JButton("Calculate Autonomous Energy Percentage");
        energyPercentageButton.addActionListener(e -> calculateAutonomousEnergyPercentage());
        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        contentPane.add(energyPercentageButton, c);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        contentPane.add(backButton, c);

        setVisible(true);
    }

    /**
     * Adjusts the state of checkboxes based on the user's selection to ensure correct variable selection.
     */

    private void adjustCheckboxSelection() {
        boolean consumptionSelected = consumptionCheckbox.isSelected();
        boolean productionSelected = productionCheckbox.isSelected();
        boolean temperatureSelected = temperatureCheckbox.isSelected();
        boolean radiationSelected = radiationCheckbox.isSelected();

        if (temperatureSelected || radiationSelected) {
            consumptionCheckbox.setEnabled(false);
            productionCheckbox.setEnabled(false);
            if (temperatureSelected) radiationCheckbox.setEnabled(false);
            if (radiationSelected) temperatureCheckbox.setEnabled(false);
        } else {
            consumptionCheckbox.setEnabled(true);
            productionCheckbox.setEnabled(true);
            temperatureCheckbox.setEnabled(true);
            radiationCheckbox.setEnabled(true);
        }

        if (consumptionSelected || productionSelected) {
            temperatureCheckbox.setEnabled(false);
            radiationCheckbox.setEnabled(false);
        }
    }

    /**
     * Calculates and displays the percentage of autonomous energy based on the selected date range.
     */

    private void calculateAutonomousEnergyPercentage() {
        try {
            String startDate = startDateField.getText().trim();
            String endDate = endDateField.getText().trim();
    
            startDate = appendTimeZone(startDate);
            endDate = appendTimeZone(endDate);
    
            if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
                JOptionPane.showMessageDialog(this, "Wrong date format, please try again!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (!isDateInRange(startDate) || !isDateInRange(endDate)) {
                JOptionPane.showMessageDialog(this, "Date must be within the range: 2022-09-01 00:00:00 to 2023-08-31 23:00:00", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (isStartDateAfterEndDate(startDate, endDate)) {
                JOptionPane.showMessageDialog(this, "Start date must be before the end date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            DataContainer dataContainer = new DataContainer("GreenEr_data.csv");
            DataContainer filteredData = dataContainer.filterByDateRange(startDate, endDate);
    
            Double totalEnergy = 0.0;
            Double autonomousEnergy = 0.0;
    
            for (String variable : filteredData.getAvailableVariables()) {
                if (variable.startsWith("Green_Er_Consumption_kW")) {
                    Double[] consumptionValues = filteredData.getData(variable);
                    for (Double value : consumptionValues) {
                        totalEnergy += value;
                    }
                }
                if (variable.startsWith("Green_Er_Production_kW")) {
                    Double[] autonomousValues = filteredData.getData(variable);
                    for (Double value : autonomousValues) {
                        autonomousEnergy += value;
                    }
                }
            }
    
            if (totalEnergy == 0) {
                JOptionPane.showMessageDialog(this, "Total energy is zero, unable to calculate percentage.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            double percentage = (autonomousEnergy / totalEnergy) * 100;
    
            JOptionPane.showMessageDialog(this, "Autonomous Energy Percentage: " + String.format("%.2f", percentage) + "%", "Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     /**
     * Handles the plot request by validating the date range, selected variables, and sampling time.
     */

    private void handlePlot() {
        try {
            String startDate = startDateField.getText().trim();
            String endDate = endDateField.getText().trim();
            String samplingTime = (String) samplingTimeComboBox.getSelectedItem();

            startDate = appendTimeZone(startDate);
            endDate = appendTimeZone(endDate);

            if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
                JOptionPane.showMessageDialog(this, "Wrong date format, please try again!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isDateInRange(startDate) || !isDateInRange(endDate)) {
                JOptionPane.showMessageDialog(this, "Date must be within the range: 2022-09-01 00:00:00 to 2023-08-31 23:00:00", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isStartDateAfterEndDate(startDate, endDate)) {
                JOptionPane.showMessageDialog(this, "Start date must be before the end date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DataContainer dataContainer = new DataContainer("GreenEr_data.csv");
            DataContainer filteredData = dataContainer.filterByDateRange(startDate, endDate);
            DataContainer resampledData = filteredData.resampleData(samplingTime);

            String selectedVariable = null;
            if (consumptionCheckbox.isSelected() && productionCheckbox.isSelected()) {
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] consumptionValues = resampledData.getData("Green_Er_Consumption_kW");
                Double[] productionValues = resampledData.getData("Green_Er_Production_kW");
                plotTimeChart.createTwoPlots(timeStrings, consumptionValues, productionValues);
                return;
            } else if (consumptionCheckbox.isSelected()) {
                selectedVariable = "Green_Er_Consumption_kW";
            } else if (productionCheckbox.isSelected()) {
                selectedVariable = "Green_Er_Production_kW";
            } else if (temperatureCheckbox.isSelected()) {
                selectedVariable = "Outdoor Temperature";
            } else if (radiationCheckbox.isSelected()) {
                selectedVariable = "Global Radiation";
            }

            if (selectedVariable == null) {
                JOptionPane.showMessageDialog(this, "Please select at least one variable to plot.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PlotTimeChart plotTimeChart = new PlotTimeChart();
            String[] timeStrings = resampledData.getTimeStrings();
            Double[] values = resampledData.getData(selectedVariable);
            plotTimeChart.createChart(timeStrings, values, selectedVariable);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Helper method to append time zone information to a date string.
     *
     * @param date the date string to which time zone information will be appended.
     * @return the modified date string.
     */

    private String appendTimeZone(String date) {
        if (!date.endsWith("+00:00")) {
            return date + "+00:00";
        }
        return date;
    }

    private boolean isValidDateFormat(String date) {
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2}$";
        return date.matches(regex);
    }

    /**
     * Checks if a date is within the valid range.
     *
     * @param date the date string to check.
     * @return true if the date is within range, otherwise false.
     */

    private boolean isDateInRange(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");
            Date minDate = sdf.parse("2022-09-01 00:00:00+00:00");
            Date maxDate = sdf.parse("2023-08-31 23:00:00+00:00");
            Date inputDate = sdf.parse(date);
            return !inputDate.before(minDate) && !inputDate.after(maxDate);
        } catch (Exception e) {
            return false;
        }
    }

      /**
     * Compares two dates to check if the start date is safter the end date.
     *
     * @param startDate the start date string.
     * @param endDate   the end date string.
     * @return true if the start date is after the end date, otherwise false.
     */

    private boolean isStartDateAfterEndDate(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return start.after(end);
        } catch (Exception e) {
            return false;
        }
    }
}
