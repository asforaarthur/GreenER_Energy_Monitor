package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides methods to create and display time series plots using JFreeChart.
 * It supports creating single, two, or three time series plots with custom data.
 */

public class PlotTimeChart {

    /**
     * Creates a single time series plot with data provided as time strings and corresponding values.
     * The plot is displayed in a new JFrame.
     *
     * @param timeStrings An array of time strings in the format "yyyy-MM-dd HH:mm:ssXXX"
     * @param values      An array of Double values corresponding to the times
     * @param variableName The name of the variable represented by the time series
     */

    public void createChart(String[] timeStrings, Double[] values, String variableName) {
        try {
            TimeSeries series = new TimeSeries(variableName);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

            for (int i = 0; i < timeStrings.length; i++) {
                Date date = format.parse(timeStrings[i]);
                series.addOrUpdate(new Hour(date), values[i]);
            }

            TimeSeriesCollection dataset = new TimeSeriesCollection(series);
            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: " + variableName,
                    "Time",
                    variableName,
                    dataset,
                    true,
                    true,
                    false
            );

            JFrame frame = new JFrame("Plot");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * Creates a time series plot with two series: one for consumption and one for production.
     * The plot is displayed in a new JFrame.
     *
     * @param timeStrings An array of time strings in the format "yyyy-MM-dd HH:mm:ssXXX"
     * @param consumptionValues An array of Double values representing consumption at the given times
     * @param productionValues  An array of Double values representing production at the given times
     */

    public void createTwoPlots(String[] timeStrings, Double[] consumptionValues, Double[] productionValues) {
        try {
            TimeSeries consumptionSeries = new TimeSeries("Green_Er_Consumption_kW");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

            for (int i = 0; i < timeStrings.length; i++) {
                Date date = format.parse(timeStrings[i]);
                consumptionSeries.addOrUpdate(new Hour(date), consumptionValues[i]);
            }

            TimeSeries productionSeries = new TimeSeries("Green_Er_production_kW");

            for (int i = 0; i < timeStrings.length; i++) {
                Date date = format.parse(timeStrings[i]);
                productionSeries.addOrUpdate(new Hour(date), productionValues[i]);
            }

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(consumptionSeries);
            dataset.addSeries(productionSeries);

            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Consumption and Production",
                    "Time",
                    "Values (kW)",
                    dataset,
                    true,
                    true,
                    false
            );

            JFrame frame = new JFrame("Plot");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a time series plot with three series: one for "Consigne Temperature Chaude",
     * one for "Consigne Temperature Froide", and one for "Temperature Ambiante".
     * The plot is displayed in a new JFrame.
     *
     * @param timeStrings An array of time strings in the format "yyyy-MM-dd HH:mm:ssXXX"
     * @param consigneTempChaude An array of Double values for "Consigne Temperature Chaude"
     * @param consigneTempFroide An array of Double values for "Consigne Temperature Froide"
     * @param temperatureAmbiante An array of Double values for "Temperature Ambiante"
     */

    public void createThreePlots(String[] timeStrings, Double[] consigneTempChaude, Double[] consigneTempFroide, Double[] temperatureAmbiante) {
        try {
            TimeSeries tempChaudeSeries = new TimeSeries("Consigne Temperature Chaude");
            TimeSeries tempFroideSeries = new TimeSeries("Consigne Temperature Froide");
            TimeSeries tempAmbianteSeries = new TimeSeries("Température Ambiante");

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

            for (int i = 0; i < timeStrings.length; i++) {
                Date date = format.parse(timeStrings[i]);
                tempChaudeSeries.addOrUpdate(new Hour(date), consigneTempChaude[i]);
                tempFroideSeries.addOrUpdate(new Hour(date), consigneTempFroide[i]);
                tempAmbianteSeries.addOrUpdate(new Hour(date), temperatureAmbiante[i]);
            }

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(tempChaudeSeries);
            dataset.addSeries(tempFroideSeries);
            dataset.addSeries(tempAmbianteSeries);

            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
            );

            JFrame frame = new JFrame("Plot");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createThreePlots2(String[] timeStrings, Double[] consigneTempChaude, Double[] consigneTempFroide, Double[] outDoorTemperature) {
        try {
            TimeSeries tempChaudeSeries = new TimeSeries("Consigne Temperature Chaude");
            TimeSeries tempFroideSeries = new TimeSeries("Consigne Temperature Froide");
            TimeSeries outdoorTempSeries = new TimeSeries("Current Outdoor Temperature");

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

            for (int i = 0; i < timeStrings.length; i++) {
                Date date = format.parse(timeStrings[i]);
                tempChaudeSeries.addOrUpdate(new Hour(date), consigneTempChaude[i]);
                tempFroideSeries.addOrUpdate(new Hour(date), consigneTempFroide[i]);
                outdoorTempSeries.addOrUpdate(new Hour(date), outDoorTemperature[i]);
            }

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(tempChaudeSeries);
            dataset.addSeries(tempFroideSeries);
            dataset.addSeries(outdoorTempSeries);

            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
            );

            JFrame frame = new JFrame("Plot");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createThreePlots3(String[] timeStrings, Double[] temperatureAmbiante, Double[] consigneTempFroide, Double[] outDoorTemperature) {
        try {
            TimeSeries tempAmbianteSeries = new TimeSeries("Température Ambiante");
            TimeSeries tempFroideSeries = new TimeSeries("Consigne Temperature Froide");
            TimeSeries outdoorTempSeries = new TimeSeries("Current Outdoor Temperature");

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

            for (int i = 0; i < timeStrings.length; i++) {
                Date date = format.parse(timeStrings[i]);
                tempAmbianteSeries.addOrUpdate(new Hour(date), temperatureAmbiante[i]);
                tempFroideSeries.addOrUpdate(new Hour(date), consigneTempFroide[i]);
                outdoorTempSeries.addOrUpdate(new Hour(date), outDoorTemperature[i]);
            }

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(tempAmbianteSeries);
            dataset.addSeries(tempFroideSeries);
            dataset.addSeries(outdoorTempSeries);

            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
            );

            JFrame frame = new JFrame("Plot");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createThreePlots4(String[] timeStrings, Double[] temperatureAmbiante, Double[] consigneTempChaude, Double[] outDoorTemperature) {
        try {
            TimeSeries tempAmbianteSeries = new TimeSeries("Température Ambiante");
            TimeSeries tempChaudSeries = new TimeSeries("Consigne Temperature Chaude");
            TimeSeries outdoorTempSeries = new TimeSeries("Current Outdoor Temperature");

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

            for (int i = 0; i < timeStrings.length; i++) {
                Date date = format.parse(timeStrings[i]);
                tempAmbianteSeries.addOrUpdate(new Hour(date), temperatureAmbiante[i]);
                tempChaudSeries.addOrUpdate(new Hour(date), consigneTempChaude[i]);
                outdoorTempSeries.addOrUpdate(new Hour(date), outDoorTemperature[i]);
            }

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(tempAmbianteSeries);
            dataset.addSeries(tempChaudSeries);
            dataset.addSeries(outdoorTempSeries);

            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
            );

            JFrame frame = new JFrame("Plot");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public void createTwoPlots2(String[] timeStrings, Double[] puissanceValues, Double[] radiationValues) {
    try {
        TimeSeries puissanceSeries = new TimeSeries("puissance_electrique_sum");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            puissanceSeries.addOrUpdate(new Hour(date), puissanceValues[i]);
        }

        TimeSeries radiationSeries = new TimeSeries(" Global Radiation");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            radiationSeries.addOrUpdate(new Hour(date), radiationValues[i]);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(puissanceSeries);
        dataset.addSeries(radiationSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Data Plot: Radiation and Puissance",
                "Time",
                "Values (kW)",
                dataset,
                true,
                true,
                false
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createTwoPlots3(String[] timeStrings, Double[] outdoorTemperature, Double[] temperatureAmbiante) {
    try {
        TimeSeries outdoorTemperatureSeries = new TimeSeries(" Current Outdoor Temperature");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            outdoorTemperatureSeries.addOrUpdate(new Hour(date), outdoorTemperature[i]);
        }

        TimeSeries temperatureAmbienteSeries = new TimeSeries(" temperature_ambiante");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            temperatureAmbienteSeries.addOrUpdate(new Hour(date), temperatureAmbiante[i]);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(outdoorTemperatureSeries);
        dataset.addSeries(temperatureAmbienteSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createTwoPlots4(String[] timeStrings, Double[] outdoorTemperature, Double[] consigneTempChaude) {
    try {
        TimeSeries outdoorTemperatureSeries = new TimeSeries(" Current Outdoor Temperature");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            outdoorTemperatureSeries.addOrUpdate(new Hour(date), outdoorTemperature[i]);
        }

        TimeSeries consigneTempChaudeSeries = new TimeSeries(" consigne_temperature_chaude");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            consigneTempChaudeSeries.addOrUpdate(new Hour(date), consigneTempChaude[i]);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(outdoorTemperatureSeries);
        dataset.addSeries(consigneTempChaudeSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createTwoPlots5(String[] timeStrings, Double[] outdoorTemperature, Double[] consigneTempFroid) {
    try {
        TimeSeries outdoorTemperatureSeries = new TimeSeries(" Current Outdoor Temperature");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            outdoorTemperatureSeries.addOrUpdate(new Hour(date), outdoorTemperature[i]);
        }

        TimeSeries consigneTempFroidSeries = new TimeSeries(" consigne_temperature_froide");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            consigneTempFroidSeries.addOrUpdate(new Hour(date), consigneTempFroid[i]);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(outdoorTemperatureSeries);
        dataset.addSeries(consigneTempFroidSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createTwoPlots6(String[] timeStrings, Double[] consigneTempChaude, Double[] consigneTempFroid) {
    try {
        TimeSeries consigneTempChaudeSeries = new TimeSeries(" consigne_temperature_chaude");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            consigneTempChaudeSeries.addOrUpdate(new Hour(date), consigneTempChaude[i]);
        }

        TimeSeries consigneTempFroidSeries = new TimeSeries(" consigne_temperature_froide");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            consigneTempFroidSeries.addOrUpdate(new Hour(date), consigneTempFroid[i]);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(consigneTempChaudeSeries);
        dataset.addSeries(consigneTempFroidSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createTwoPlots7(String[] timeStrings, Double[] consigneTempChaude, Double[] temperatureAmbiante) {
    try {
        TimeSeries consigneTempChaudeSeries = new TimeSeries(" consigne_temperature_chaude");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            consigneTempChaudeSeries.addOrUpdate(new Hour(date), consigneTempChaude[i]);
        }

        TimeSeries temperatureAmbienteSeries = new TimeSeries(" temperature_ambiante");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            temperatureAmbienteSeries.addOrUpdate(new Hour(date), temperatureAmbiante[i]);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(consigneTempChaudeSeries);
        dataset.addSeries(temperatureAmbienteSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createTwoPlots8(String[] timeStrings, Double[] consigneTempFroide, Double[] temperatureAmbiante) {
    try {
        TimeSeries consigneTempFroideSeries = new TimeSeries(" consigne_temperature_froide");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            consigneTempFroideSeries.addOrUpdate(new Hour(date), consigneTempFroide[i]);
        }

        TimeSeries temperatureAmbienteSeries = new TimeSeries(" temperature_ambiante");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            temperatureAmbienteSeries.addOrUpdate(new Hour(date), temperatureAmbiante[i]);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(consigneTempFroideSeries);
        dataset.addSeries(temperatureAmbienteSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Data Plot: Temperatures",
                    "Time",
                    "Temperature (°C)",
                    dataset,
                    true,
                    true,
                    false
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createFourPlots(String[] timeStrings, Double[] consigneTempChaude, Double[] consigneTempFroide, Double[] temperatureAmbiante, Double[] outdoorTempValues) {
    try {
        TimeSeries tempChaudeSeries = new TimeSeries(" consigne_temperature_chaude");
        TimeSeries tempFroideSeries = new TimeSeries(" consigne_temperature_froide");
        TimeSeries tempAmbianteSeries = new TimeSeries(" temperature_ambiante");
        TimeSeries outdoorTempSeries = new TimeSeries(" Current Outdoor Temperature");

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");

        for (int i = 0; i < timeStrings.length; i++) {
            Date date = format.parse(timeStrings[i]);
            tempChaudeSeries.addOrUpdate(new Hour(date), consigneTempChaude[i]);
            tempFroideSeries.addOrUpdate(new Hour(date), consigneTempFroide[i]);
            tempAmbianteSeries.addOrUpdate(new Hour(date), temperatureAmbiante[i]);
            outdoorTempSeries.addOrUpdate(new Hour(date), outdoorTempValues[i]);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(tempChaudeSeries);
        dataset.addSeries(tempFroideSeries);
        dataset.addSeries(tempAmbianteSeries);
        dataset.addSeries(outdoorTempSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Data Plot: Temperatures",
                "Time",
                "Temperature (°C)",
                dataset,
                true,
                true,
                false
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
