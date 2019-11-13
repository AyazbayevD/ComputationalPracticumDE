package sample;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;

class RungeKutta extends Controller {

    private static double k23 (double x, double y, double k){
        return derivativeAtPoint(x + h * 0.5, y + h * k * 0.5);
    }
    private static double k4 (double x, double y, double k){
        return derivativeAtPoint(x + h, y + h * k);
    }

    static double findMaxGlobal(int numberOfSteps){

        double stepLength = (X - x0) / numberOfSteps;
        double maxGlobalError = 0.0;
        double prevY = 0.0;

        for (int i = 1; i <= numberOfSteps; ++i){

            double x = x0 + stepLength * i;

            double K1 = derivativeAtPoint(x - stepLength, prevY);
            double K2 = k23(x - stepLength, prevY, K1);
            double K3 = k23(x - stepLength, prevY, K2);
            double K4 = k4(x - stepLength, prevY, K3);

            double y = prevY + stepLength / 6.0 * (K1 + K2 * 2.0 + K3 * 2.0 + K4);

            maxGlobalError = Math.max(maxGlobalError, Math.abs(exactSolution.calculate(x) - y));

            prevY = y;
        }
        return maxGlobalError;
    }

    static void compute(){
        XYChart.Series<Number, Number> rungeKutta = new XYChart.Series<>();
        XYChart.Series<Number, Number> rungeKuttaLocalError = new XYChart.Series<>();

        rungeKutta.getData().add(new XYChart.Data<>(x0, y0));
        rungeKuttaLocalError.getData().add(new XYChart.Data<>(x0, 0.0));

        for (int i = 1; i <= N; ++i){
            double K1, K2, K3, K4;
            double prevX = rungeKutta.getData().get(i - 1).getXValue().doubleValue();
            double prevY = rungeKutta.getData().get(i - 1).getYValue().doubleValue();

            K1 = derivativeAtPoint(prevX, prevY);
            K2 = k23(prevX, prevY, K1);
            K3 = k23(prevX, prevY, K2);
            K4 = k4(prevX, prevY, K3);

            double x = x0 + h * i;

            double y = prevY + h / 6.0 * (K1 + K2 * 2.0 + K3 * 2.0 + K4);

            rungeKutta.getData().add(new XYChart.Data<>(x, y));

            double prevExactY = exactSolutionY.get(i - 1);

            K1 = derivativeAtPoint(prevX, prevExactY);
            K2 = k23(prevX, prevExactY, K1);
            K3 = k23(prevX, prevExactY, K2);
            K4 = k4(prevX, prevExactY, K3);

            double A = (K1 + K2 * 2.0 + K3 * 2.0 + K4) / 6.0;

            rungeKuttaLocalError.getData().add(new XYChart.Data<>(x, Math.abs(exactSolutionY.get(i) - exactSolutionY.get(i - 1) - h * A)));
        }
        rungeKutta.setName("Runge-Kutta method");
        rungeKuttaLocalError.setName("Runge-Kutta method");

        localErrorsChart.getData().add(rungeKuttaLocalError);
        methodsChart.getData().add(rungeKutta);
    }
}
