package assignment3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davey on 3/30/17.
 */
public class ExponentialSmoothing {
    private List<Double> data;

    private List<Double> ses;

    private List<Double> des;
    private List<Double> trend;
    private List<Double> forecast;

    private double a;
    private double b;

    private double sesError;
    private double desError;

    public ExponentialSmoothing(List<Double> data, double a) {
        this.data = data;
        this.a = a;
        this.ses = new ArrayList<>();
    }

    public ExponentialSmoothing(List<Double> data, double a, double b) {
        this.data = data;
        this.a = a;
        this.b = b;
        this.ses = new ArrayList<>();
        this.des = new ArrayList<>();
        this.trend = new ArrayList<>();
        this.forecast = new ArrayList<>();
    }

    public List<Double> simpleExponentialSmoothing() {
        ses.clear();
        sesError = 0;

        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                double average = getMovingAverage(data, 12);
                ses.add(average);
                continue;
            }

            double result = a * data.get(i - 1) + (1 - a) * ses.get(i - 1);
            ses.add(result);

            double error = Math.pow(data.get(i) - result, 2);
            sesError += error;
        }

        sesError = sesError / (data.size() - 1);

        ses = forecastSes(ses, data, 37, 48);

        return ses;
    }

    private List<Double> forecastSes(List<Double> ses, List<Double> data, int from, int to) {
        for (int i = from - 1; i <= to - 1; i++) {
            try {
                double value = a * data.get(i - 1) + (1 - a) * ses.get(i - 1);
                ses.add(value);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(ses.size());
                ses.add(ses.get(i - 1));
            }
        }

        return ses;
    }

    public double getBestSesSmoothingFactor() {
        double bestSmoothing = 0.0;
        double lowestSesError = 99999999;

        for ( double currentSmoothing = 0.05; currentSmoothing < 1 ; currentSmoothing += 0.05 ) {
            setA(currentSmoothing);
            simpleExponentialSmoothing();

            if ( sesError < lowestSesError ) {
                lowestSesError = sesError;
                bestSmoothing = currentSmoothing;
            }
        }
        return bestSmoothing;
    }

    public List<Double> doubleExponentialSmoothing() {
        des.clear();
        trend.clear();
        forecast.clear();
        desError = 0;

        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                des.add(null);
                trend.add(null);
                forecast.add(null);
                continue;
            } else if (i == 1) {
                des.add(Double.valueOf(data.get(i)));
                trend.add((double) (data.get(i) - data.get(i - 1)));
                forecast.add(null);
                continue;
            }

            double result = a * data.get(i) + (1 - a) * (des.get(i - 1) + trend.get(i - 1));
            des.add(result);

            double trendValue = getTrend(des, this.trend, i);
            trend.add(trendValue);

            double forecastValue = des.get(i - 1) + trend.get(i - 1);
            forecast.add(forecastValue);

            double error = Math.pow(data.get(i) - forecast.get(i), 2);
            desError += error;
        }

        desError = Math.sqrt(desError / getAmountOfForecasts() - 2);

        forecast = forecastDes(des, trend, 37, 48);

        return forecast;
    }

    public Double[] getBestSesAndDesFactors() {
        double bestSesSmoothingFactor = 0.0;
        double lowestSesError = 999999999;

        double bestDesSmoothingFactor = 0.0;
        double lowestDesError = 999999999;

        for (double currentSesSmoothingFactor = 0.05; currentSesSmoothingFactor < 1; currentSesSmoothingFactor += 0.05) {
            setA(currentSesSmoothingFactor);
            doubleExponentialSmoothing();

            //System.out.println(currentSesSmoothingFactor+"  -----   >    ----  "+ sesError);
            if (sesError < lowestSesError) {
                lowestSesError = sesError;
                bestSesSmoothingFactor = currentSesSmoothingFactor;
            }

            for (double currentDesSmoothingFactor = 0.05; currentDesSmoothingFactor < 1; currentDesSmoothingFactor += 0.05) {
                setB(currentDesSmoothingFactor);
                doubleExponentialSmoothing();

                System.out.println("Ses: "+ currentSesSmoothingFactor + "   <---> " + sesError + " <=====> Des: " + currentDesSmoothingFactor+" <---> "+ desError);

                if (desError < lowestDesError) {
                    lowestDesError = desError;
                    bestDesSmoothingFactor = currentDesSmoothingFactor;
                }
            }
        }

        return new Double[]{bestSesSmoothingFactor, bestDesSmoothingFactor};
    }

    private double getAmountOfForecasts() {
        int result = 0;
        for (Double value : forecast) {
            if (value != null) {
                result++;
            }
        }
        return result;
    }

    private List<Double> forecastDes(List<Double> s, List<Double> trend, int from, int to) {
        for (int i = from - 1; i <= to - 1; i++) {
            if (i == s.size()) {
                forecast.add(s.get(i - 1) + trend.get(i - 1));
            } else {
                Double aDouble = s.get(s.size() - 1);
                Double a2 = (double) (i + 1 - s.size());
                Double a3 = trend.get(trend.size() - 1);
                forecast.add(aDouble + a2 * a3);
            }
        }

        return forecast;
    }

    private double getTrend(List<Double> s, List<Double> t, int i) {
        return b * (s.get(i) - s.get(i - 1)) + (1 - b) * t.get(i - 1);
    }

    private double getMovingAverage(List<Double> data, int k) {
        if (k > data.size()) {
            throw new IllegalArgumentException("k cannot be bigger than the size of the data");
        }

        double sum = 0.0;
        for (int i = 0; i < k; i++) {
            sum += data.get(i);
        }

        return sum / k;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getSesError() {
        return sesError;
    }

    public double getDesError() {
        return desError;
    }
}
