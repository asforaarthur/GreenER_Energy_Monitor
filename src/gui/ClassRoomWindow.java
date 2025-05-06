package gui;

import javax.swing.*;
import data.DataContainer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents the classroom data viewer window.
 * This window allows users to select different temperature-related variables
 * and define a date range to view specific data from a CSV file classRoom_4A020_data
 * Users can also specify the sampling time for data visualization and interact with the UI
 * elements to control the plot generation.
 */

public class ClassRoomWindow extends JFrame {
    private JTextField startDateField, endDateField;
    private JCheckBox chaudCheckbox, froidCheckbox, ambianteCheckbox, outdoorTempCheckbox, radiationCheckbox, puissanceSumCheckbox;
    private JComboBox<String> samplingTimeComboBox;

    /**
     * Constructs a ClassRoomWindow object.
     *
     * @param parent The parent frame that the window will return to when closed.
     */

    public ClassRoomWindow(JFrame parent) {
        super("Classroom Data Viewer");

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

        chaudCheckbox = new JCheckBox("Consigne Température Chaude");
        froidCheckbox = new JCheckBox("Consigne Température Froide");
        ambianteCheckbox = new JCheckBox("Température Ambiante");
        outdoorTempCheckbox = new JCheckBox("Current Outdoor Temperature");
        radiationCheckbox = new JCheckBox("Global Radiation");
        puissanceSumCheckbox = new JCheckBox("Puissance Electrique Sum");

        variablePanel.add(chaudCheckbox);
        variablePanel.add(froidCheckbox);
        variablePanel.add(ambianteCheckbox);
        variablePanel.add(outdoorTempCheckbox);
        variablePanel.add(radiationCheckbox);
        variablePanel.add(puissanceSumCheckbox);

        chaudCheckbox.addActionListener(e -> adjustCheckboxSelection());
        froidCheckbox.addActionListener(e -> adjustCheckboxSelection());
        ambianteCheckbox.addActionListener(e -> adjustCheckboxSelection());
        outdoorTempCheckbox.addActionListener(e -> adjustCheckboxSelection());
        radiationCheckbox.addActionListener(e -> adjustCheckboxSelection());
        puissanceSumCheckbox.addActionListener(e -> adjustCheckboxSelection());

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
     * Adjusts the enabled state of checkboxes based on selected values.
     */

    private void adjustCheckboxSelection() {
        boolean radiationSelected = radiationCheckbox.isSelected();
        boolean puissanceSumSelected = puissanceSumCheckbox.isSelected();

        if (radiationSelected || puissanceSumSelected) {
            chaudCheckbox.setEnabled(false);
            froidCheckbox.setEnabled(false);
            ambianteCheckbox.setEnabled(false);
            outdoorTempCheckbox.setEnabled(false);
        } else {
            chaudCheckbox.setEnabled(true);
            froidCheckbox.setEnabled(true);
            ambianteCheckbox.setEnabled(true);
            outdoorTempCheckbox.setEnabled(true);
        }

        boolean anyTempSelected = chaudCheckbox.isSelected() || froidCheckbox.isSelected() || ambianteCheckbox.isSelected() || outdoorTempCheckbox.isSelected();
        if (anyTempSelected) {
            radiationCheckbox.setEnabled(false);
            puissanceSumCheckbox.setEnabled(false);
        } else {
            radiationCheckbox.setEnabled(true);
            puissanceSumCheckbox.setEnabled(true);
        }
    }

    /**
     * Handles the plot button click event, performing input validation and creating the appropriate plot
     * based on selected variables and the date range. I think I did more if-elses than necessary in this method (it can be optimized)
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

            DataContainer dataContainer = new DataContainer("classRoom_4A020_data.csv");
            dataContainer.computePuissanceElectriqueSum();
            DataContainer filteredData = dataContainer.filterByDateRange(startDate, endDate);
            DataContainer resampledData = filteredData.resampleData(samplingTime);

            String selectedVariable = null;

            if (ambianteCheckbox.isSelected() && chaudCheckbox.isSelected() && outdoorTempCheckbox.isSelected() && froidCheckbox.isSelected()) {
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] consigneChaudValues = resampledData.getData(" consigne_temperature_chaude");
                Double[] consigneFroidValues = resampledData.getData(" consigne_temperature_froide");
                Double [] outdoorTempValues = resampledData.getData(" Current Outdoor Temperature");
                Double [] temperatureAmbienteValues = resampledData.getData(" temperature_ambiante");
                plotTimeChart.createFourPlots(timeStrings, consigneChaudValues, consigneFroidValues, temperatureAmbienteValues, outdoorTempValues);
                return;      
            }

            if (chaudCheckbox.isSelected() && froidCheckbox.isSelected() && ambianteCheckbox.isSelected()) {
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] consigneChaudValues = resampledData.getData(" consigne_temperature_chaude");
                Double[] consigneFroidValues = resampledData.getData(" consigne_temperature_froide");
                Double [] temperatureAmbienteValues = resampledData.getData(" temperature_ambiante");
                plotTimeChart.createThreePlots(timeStrings, consigneChaudValues, consigneFroidValues, temperatureAmbienteValues);
                return;
            
            }

            if (chaudCheckbox.isSelected() && froidCheckbox.isSelected() && outdoorTempCheckbox.isSelected()) {
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] consigneChaudValues = resampledData.getData(" consigne_temperature_chaude");
                Double[] consigneFroidValues = resampledData.getData(" consigne_temperature_froide");
                Double [] outdoorTempValues = resampledData.getData(" Current Outdoor Temperature");
                plotTimeChart.createThreePlots2(timeStrings, consigneChaudValues, consigneFroidValues, outdoorTempValues);
                return;      
            }

            if (ambianteCheckbox.isSelected() && froidCheckbox.isSelected() && outdoorTempCheckbox.isSelected()) {
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double [] temperatureAmbienteValues = resampledData.getData(" temperature_ambiante");
                Double[] consigneFroidValues = resampledData.getData(" consigne_temperature_froide");
                Double [] outdoorTempValues = resampledData.getData(" Current Outdoor Temperature");
                plotTimeChart.createThreePlots3(timeStrings, temperatureAmbienteValues, consigneFroidValues, outdoorTempValues);
                return;      
            }

            if (ambianteCheckbox.isSelected() && chaudCheckbox.isSelected() && outdoorTempCheckbox.isSelected()) {
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] consigneChaudValues = resampledData.getData(" consigne_temperature_chaude");
                Double[] consigneFroidValues = resampledData.getData(" consigne_temperature_froide");
                Double [] outdoorTempValues = resampledData.getData(" Current Outdoor Temperature");
                plotTimeChart.createThreePlots4(timeStrings, consigneChaudValues, consigneFroidValues, outdoorTempValues);
                return;      
            }
                
            else if (chaudCheckbox.isSelected()) {
                selectedVariable = " consigne_temperature_chaude";
            } else if (froidCheckbox.isSelected()) {
                selectedVariable = " consigne_temperature_froide";
            } else if (ambianteCheckbox.isSelected()) {
                selectedVariable = " temperature_ambiante";
            } else if (outdoorTempCheckbox.isSelected()) {
                selectedVariable = " Current Outdoor Temperature";
            } else if (radiationCheckbox.isSelected()) {
                selectedVariable = " Global Radiation";
            } else if (puissanceSumCheckbox.isSelected()) {
                selectedVariable = "puissance_electrique_sum";
            }

            if (puissanceSumCheckbox.isSelected() && radiationCheckbox.isSelected()){
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] puissanceValues = resampledData.getData("puissance_electrique_sum");
                Double[] radiationValues = resampledData.getData(" Global Radiation");
                plotTimeChart.createTwoPlots2(timeStrings, puissanceValues, radiationValues);
                return;
            }

            if (outdoorTempCheckbox.isSelected() && ambianteCheckbox.isSelected()){
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] outdoorTempValues = resampledData.getData(" Current Outdoor Temperature");
                Double [] temperatureAmbienteValues = resampledData.getData(" temperature_ambiante");
                plotTimeChart.createTwoPlots3(timeStrings, outdoorTempValues, temperatureAmbienteValues);
                return;
            }

            if (outdoorTempCheckbox.isSelected() && chaudCheckbox.isSelected()){
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] outdoorTempValues = resampledData.getData(" Current Outdoor Temperature");
                Double[] consigneChaudValues = resampledData.getData(" consigne_temperature_chaude");
                plotTimeChart.createTwoPlots4(timeStrings, outdoorTempValues, consigneChaudValues);
                return;
            }

            if (outdoorTempCheckbox.isSelected() && froidCheckbox.isSelected()){
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] outdoorTempValues = resampledData.getData(" Current Outdoor Temperature");
                Double[] consigneFroidValues = resampledData.getData(" consigne_temperature_froide");
                plotTimeChart.createTwoPlots5(timeStrings, outdoorTempValues, consigneFroidValues);
                return;
            }

            if (chaudCheckbox.isSelected() && froidCheckbox.isSelected()){
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] consigneChaudValues = resampledData.getData(" consigne_temperature_chaude");
                Double[] consigneFroidValues = resampledData.getData(" consigne_temperature_froide");
                plotTimeChart.createTwoPlots6(timeStrings, consigneChaudValues, consigneFroidValues);
                return;
            }

            if (chaudCheckbox.isSelected() && ambianteCheckbox.isSelected()){
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] consigneChaudValues = resampledData.getData(" consigne_temperature_chaude");
                Double [] temperatureAmbienteValues = resampledData.getData(" temperature_ambiante");
                plotTimeChart.createTwoPlots7(timeStrings, consigneChaudValues, temperatureAmbienteValues);
                return;
            }

            if (froidCheckbox.isSelected() && ambianteCheckbox.isSelected()){
                PlotTimeChart plotTimeChart = new PlotTimeChart();
                String[] timeStrings = resampledData.getTimeStrings();
                Double[] consigneFroidValues = resampledData.getData(" consigne_temperature_froide");
                Double [] temperatureAmbienteValues = resampledData.getData(" temperature_ambiante");
                plotTimeChart.createTwoPlots8(timeStrings, consigneFroidValues, temperatureAmbienteValues);
                return;
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
     * Appends the time zone to the date string if not already present.
     *
     * @param date The date string to check and modify.
     * @return The date string with the time zone appended.
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
     * Checks if the provided date is within the acceptable range.
     *
     * @param date The date string to check.
     * @return True if the date is within range, otherwise false.
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
     * Checks if the start date is after the end date.
     *
     * @param startDate The start date string.
     * @param endDate The end date string.
     * @return True if the start date is after the end date, otherwise false.
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
