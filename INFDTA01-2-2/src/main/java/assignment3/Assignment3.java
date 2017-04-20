package assignment3;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davey on 3/30/17.
 */
public class Assignment3 extends JFrame {
    static final String file = "resources/SwordForecasting.csv";
    File files = new File(file);
    List<Double> data;

    Assignment3(String appTitle, String chartTitle) throws IOException {
        super(appTitle);
        System.out.println(files.getAbsolutePath());
        data = getData();

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, "Months", "Demand", createDataSet(data));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 540));
        setContentPane(chartPanel);
    }

    private XYDataset createDataSet(List<Double> data) {
        XYSeries originalData = new XYSeries("Original data");

        for (int i = 0; i < data.size(); i++) {
            originalData.add(i + 1, data.get(i));
        }

        XYSeries ses = new XYSeries("SES");
        List<Double> sesData = getSesData();

        for (int i = 0; i < sesData.size(); i++) {
            ses.add(i + 1, sesData.get(i));
        }


        XYSeries des = new XYSeries("DES forecast");
        List<Double> desData = getDesData();

        for (int i = 0; i < desData.size(); i++) {
            des.add(i + 1, desData.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(originalData);
        dataset.addSeries(ses);
        dataset.addSeries(des);
        return dataset;
    }

    private List<Double> getSesData() {
        ExponentialSmoothing exponentialSmoothing = new ExponentialSmoothing(data, 0.5);
        double a = exponentialSmoothing.getBestSesSmoothingFactor();
        System.out.println("Best smoothing factor a (ses): " + a + ", because error is: " + exponentialSmoothing.getSesError());

        exponentialSmoothing.setA(a);
        List<Double> sesData = exponentialSmoothing.simpleExponentialSmoothing();

        return sesData;
    }

    private List<Double> getDesData() {
        ExponentialSmoothing exponentialSmoothing = new ExponentialSmoothing(data, 0.5, 0.5);

        Double[] factors = exponentialSmoothing.getBestSesAndDesFactors();

        double a = factors[0];
        System.out.println("Best smoothing factor a (des): " + a);
        exponentialSmoothing.setA(a);

        double b = factors[1];
        System.out.println("Best smoothing factor b (des): " + b);
        exponentialSmoothing.setB(b);

        List<Double> desData = exponentialSmoothing.doubleExponentialSmoothing();

        return desData;
    }

    static List<Double> getData() throws IOException {
        BufferedReader in = null;
        try{
            in = new BufferedReader(new FileReader(file));
        } catch(FileNotFoundException ex){
            try{
                in = new BufferedReader(new FileReader("src/main/"+file));
            }catch(FileNotFoundException e){
                System.out.println("File not found, try again in other path.");
                File filePath = new File("src/main/"+file);
                System.out.println("Current path: "+filePath.getAbsolutePath());
            }catch(Exception e){
                System.out.println("Exception on reading the file.");
                e.printStackTrace();
            }
        }catch(Exception ex){
            System.out.println("Exception on reading the file.");
            ex.printStackTrace();
        }


        List<Double> result = new ArrayList<>();

        String line;
        while ((line = in.readLine()) != null) {
            result.add(Double.valueOf(line));
        }

        return result;
    }
}
