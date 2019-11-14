package sample;

import javafx.scene.chart.XYChart;

class Euler extends Controller {

    //function for finding maximum global errors for the given amount of steps
    static double findMaxGlobal(int numberOfSteps){

        //calculating step length, defining initial maximum global error, and initial y-value of approximation at previous step
        double stepLength = (X - x0) / numberOfSteps;
        double maxGlobalError = 0.0;
        double y = y0, x = x0;

        //jumping through number of steps and calculate error by the formula
        while(x + stepLength <= X){
            y += stepLength * derivativeAtPoint(x, y);
            x += stepLength;
            maxGlobalError = Math.max(Math.abs(exactSolution.calculate(x) - y), maxGlobalError);
        }
        //returning result
        return maxGlobalError;
    }

    static void compute(){

        //creating series for approximation and local error of Euler's computational method
        XYChart.Series<Number, Number> euler = new XYChart.Series<>();
        XYChart.Series<Number, Number> eulerLocalError = new XYChart.Series<>();

        //inserting into series values at 0-th step
        euler.getData().add(new XYChart.Data<>(x0, y0));
        eulerLocalError.getData().add(new XYChart.Data<>(x0, 0.0));

        //looping through N steps (N - user inserted value)
        for (int i = 1; i <= N; ++i){
            //retrieving x and y-approximation at previous step
            double prevX = euler.getData().get(i - 1).getXValue().doubleValue();
            double prevY = euler.getData().get(i - 1).getYValue().doubleValue();

            //calculating current x, and current y-approximation
            double x = x0 + h * i;
            double y = prevY + h * derivativeAtPoint(prevX, prevY);

            //inserting to the approximation series pair of correspondent values
            euler.getData().add(new XYChart.Data<>(x, y));

            //calculating previous and current step exact solutions
            double prevExactY = exactSolution.calculate(x - h);
            double curExactY = exactSolution.calculate(x);

            //using formula for local error, calculating it and putting it into local error series for Euler's computational method
            double A = derivativeAtPoint(x - h, prevExactY);
            eulerLocalError.getData().add(new XYChart.Data<>(x, Math.abs(curExactY - prevExactY - h * A)));
        }
        //defining the name of the series
        euler.setName("Euler method");
        eulerLocalError.setName("Euler method");

        //inserting these series into their correspondent charts
        localErrorsChart.getData().add(eulerLocalError);
        approximationsAndExactSolutionChart.getData().add(euler);
    }

}
