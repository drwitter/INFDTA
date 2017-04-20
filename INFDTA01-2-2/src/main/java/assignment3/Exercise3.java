package assignment3;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davey on 4/20/17.
 */
public class Exercise3 extends JFrame {
    private static final String file = "resources/forecastingWalmart.csv";
    private static File files = new File(file);
    private static List<Double> data;
    private static final String appTitle = "Forecasting Walmart";
    private static String chartTitle = "Chart";
    private static Double a;
    private static Double b;

    public Exercise3 () throws IOException {
        super(appTitle);
        data = getData();

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, "Weeks", "Weekly purchases", createDataSet(data));
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
        System.out.println("Best smoothing factor a (des): " + a + " Because error is: "+exponentialSmoothing.getDesError());
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
        int i = 0;
        while ((line = in.readLine()) != null) {
            String[] splittedLine = line.split(",");
            if(i!=0){
                if(Integer.valueOf(splittedLine[0]) == 1 && Integer.valueOf(splittedLine[1]) == 2){
                    result.add(Double.valueOf(splittedLine[3]));
                    System.out.println(splittedLine[3]);
                }else{
                    //System.out.println(splittedLine[0]+"--->"+splittedLine[1]);
                }
            }
            i++;
        }

        return result;
    }
}
