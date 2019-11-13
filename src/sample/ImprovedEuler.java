package sample;

import javafx.scene.chart.XYChart;

class ImprovedEuler extends Controller {

    //helping function used in formula for improved Euler method
    private static double k2(double x, double y, double k){
        return derivativeAtPoint(x + h, y + h * k);
    }

    //function for finding maximum global errors for the given amount of steps
    static double findMaxGlobal(int numberOfSteps){

        //calculating step length, defining initial maximum global error, and initial y-value of approximation at previous step
        double stepLength = (X - x0) / numberOfSteps;
        double maxGlobalError = 0.0;
        double prevY = 0.0;

        //jumping through number of steps
        for (int i = 1; i <= numberOfSteps; ++i){

            //calculating x, previous step x, and current y-approximation
            double x = x0 + stepLength * i;
            double prevX = x - stepLength;
            double K1 = derivativeAtPoint(prevX, prevY);
            double K2 = k2(prevX, prevY, K1);
            double y = prevY + stepLength * 0.5 * (K1 + K2);

            //updating maximum global errors in each step
            maxGlobalError = Math.max(maxGlobalError, Math.abs(exactSolution.calculate(x) - y));

            //also updating previous y-approximation
            prevY = y;
        }
        //returning result
        return maxGlobalError;
    }

    static void compute(){
        //creating series for approximation and local error of Euler's improved computational method
        XYChart.Series<Number, Number> improvedEuler = new XYChart.Series<>();
        XYChart.Series<Number, Number> improvedEulerLocalError = new XYChart.Series<>();

        //inserting into series values at 0-th step
        improvedEuler.getData().add(new XYChart.Data<>(x0, y0));
        improvedEulerLocalError.getData().add(new XYChart.Data<>(x0, 0.0));

        //looping through N steps (N - user inserted value)
        for (int i = 1; i <= N; ++i){
            double K1, K2;

            //retrieving x and y-approximation at previous step
            double prevX = improvedEuler.getData().get(i - 1).getXValue().doubleValue();
            double prevY = improvedEuler.getData().get(i - 1).getYValue().doubleValue();

            //calculating current x, and current y-approximation
            K1 = derivativeAtPoint(prevX, prevY);
            K2 = k2(prevX, prevY, K1);
            double x = x0 + h * i;
            double y = prevY + h * 0.5 * (K1 + K2);

            //inserting to the approximation series pair of correspondent values
            improvedEuler.getData().add(new XYChart.Data<>(x, y));

            //calculating previous and current step exact solutions
            double prevExactY = exactSolution.calculate(x - h);
            double curExactY = exactSolution.calculate(x);

            //using formula for local error, calculating it and putting it into local error series for Euler's improved computational method
            K1 = derivativeAtPoint(prevX, prevExactY);
            K2 = k2(prevX, prevExactY, K1);
            double A = 0.5 * (K1 + K2);
            improvedEulerLocalError.getData().add(new XYChart.Data<>(x, Math.abs(curExactY - prevExactY - h * A)));
        }
        //defining the name of the series
        improvedEuler.setName("Improved Euler method");
        improvedEulerLocalError.setName("Improved Euler method");

        //inserting these series into their correspondent charts
        localErrorsChart.getData().add(improvedEulerLocalError);
        approximationsAndExactSolutionChart.getData().add(improvedEuler);
    }
}
