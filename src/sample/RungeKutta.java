package sample;

import javafx.scene.chart.XYChart;

class RungeKutta extends Controller {

    //helping functions used in formula for Runge-Kutta method
    private static double k23 (double x, double y, double k, double h){
        return derivativeAtPoint(x + h * 0.5, y + h * k * 0.5);
    }
    private static double k4 (double x, double y, double k, double h){
        return derivativeAtPoint(x + h, y + h * k);
    }

    //function for finding maximum global errors for the given amount of steps
    static double findMaxGlobal(int numberOfSteps){

        //calculating step length, defining initial maximum global error, and initial y-value of approximation at previous step
        double stepLength = (X - x0) / numberOfSteps;
        double maxGlobalError = 0.0;
        double y = y0, x = x0;

        //jumping through number of steps and calculate error by the formula
        while(x + stepLength <= X){
            double K1 = derivativeAtPoint(x, y);
            double K2 = k23(x, y, K1, stepLength);
            double K3 = k23(x, y, K2, stepLength);
            double K4 = k4(x, y, K3, stepLength);

            y += stepLength / 6.0 * (K1 + K2 * 2.0 + K3 * 2.0 + K4);
            x += stepLength;
            maxGlobalError = Math.max(maxGlobalError, Math.abs(exactSolution.calculate(x) - y));
        }
        //returning result
        return maxGlobalError;
    }

    static void compute(){
        //creating series for approximation and local error of Runge-Kutta computational method
        XYChart.Series<Number, Number> rungeKutta = new XYChart.Series<>();
        XYChart.Series<Number, Number> rungeKuttaLocalError = new XYChart.Series<>();

        //inserting into series values at 0-th step
        rungeKutta.getData().add(new XYChart.Data<>(x0, y0));
        rungeKuttaLocalError.getData().add(new XYChart.Data<>(x0, 0.0));

        //looping through N steps (N - user inserted value)
        for (int i = 1; i <= N; ++i){
            double K1, K2, K3, K4;

            //retrieving x and y-approximation at previous step
            double prevX = rungeKutta.getData().get(i - 1).getXValue().doubleValue();
            double prevY = rungeKutta.getData().get(i - 1).getYValue().doubleValue();

            //calculating current x, and current y-approximation
            K1 = derivativeAtPoint(prevX, prevY);
            K2 = k23(prevX, prevY, K1, h);
            K3 = k23(prevX, prevY, K2, h);
            K4 = k4(prevX, prevY, K3, h);
            double x = x0 + h * i;
            double y = prevY + h / 6.0 * (K1 + K2 * 2.0 + K3 * 2.0 + K4);

            //inserting to the approximation series pair of correspondent values
            rungeKutta.getData().add(new XYChart.Data<>(x, y));

            //calculating previous and current step exact solutions
            double prevExactY = exactSolution.calculate(x - h);
            double curExactY = exactSolution.calculate(x);

            //using formula for local error, calculating it and putting it into local error series for Runge-Kutta computational method
            K1 = derivativeAtPoint(prevX, prevExactY);
            K2 = k23(prevX, prevExactY, K1, h);
            K3 = k23(prevX, prevExactY, K2, h);
            K4 = k4(prevX, prevExactY, K3, h);
            double A = (K1 + K2 * 2.0 + K3 * 2.0 + K4) / 6.0;
            rungeKuttaLocalError.getData().add(new XYChart.Data<>(x, Math.abs(curExactY - prevExactY - h * A)));
        }
        //defining the name of the series
        rungeKutta.setName("Runge-Kutta method");
        rungeKuttaLocalError.setName("Runge-Kutta method");

        //inserting these series into their correspondent charts
        localErrorsChart.getData().add(rungeKuttaLocalError);
        approximationsAndExactSolutionChart.getData().add(rungeKutta);
    }
}
