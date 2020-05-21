package main;

public class Solution {
    private double[][] gradX; //f(x)
    private double[][] H;
    private double[][] X;
    private final static double e = 0.000001;
    private final static double alpha = 0.9;


    public Solution(double[] xi) {
        gradX = new double[xi.length][1];
        H = new double[xi.length][xi.length];
        X = new double[xi.length][1];
        for (int i = 0; i < xi.length; i++) {
            X[i][0] = xi[i];
        }

    }

    private double function(double x, double y) {
        /* double result = Math.pow(2 * x + y + 1, 4) + 0.1 * Math.pow(x + 2 * y - 1, 4);
        return result;*/

        double result = 100 * Math.pow(y - x * x, 4) + Math.pow(1 - x, 4);
        return result;
    }


    private double[][] zFunction(double x, double y) {
       /* double part1 = Math.pow(2 * x + y + 1, 3);
        double part2 = Math.pow(x + 2 * y - 1, 3);
        double dx = 8 * part1 + 0.4 * part2;
        double dy = 4 * part1 + 0.8 * part2;*/
        double part =  Math.pow(-Math.pow(x,2) + y,3);
        double dx = -800 * x * part - 4 * Math.pow(1-x,3);
        double dy = 400 * part;
        return new double[][]{{dx}, {dy}};

    }



    private double[][] hessianMatrix(double x, double y) {
        /*double dx = 1.2 * Math.pow(x + 2 * y - 1,2) + 48 * Math.pow(2 * x + y + 1,2);
        double dy = 4.8 * Math.pow(x + 2 * y - 1,2) + 12 * Math.pow(2 * x + y + 1,2);
        double dxDy = 2.4 * Math.pow(x + 2 * y - 1, 2) + 24 * Math.pow(2 * x + y + 1, 2);*/

        /*double dx = 12 * (1 - x*x) + 800 * Math.pow(x * x - y, 3)
               + 4800 * Math.pow(x*x*x - x * y, 2);
       double part = Math.pow( y - x * x,2);
       double dy = - 2400 * x * part;
       double dxDy = 1200 * part;*/


        double dx = 4800 * Math.pow(x,2) * Math.pow(-x*x + y,2) + 12 * Math.pow(-x + 1,2) - 800 * Math.pow(-Math.pow(x,2) + y,3);
        double part = Math.pow(-Math.pow(x,2) + y,2);
        double dy = 1200 * part;
        double dxDy = -2400 * x * part;


        return new double[][]{{dx, dxDy}, {dxDy, dy}};
    }

    private double modGrad(double[][] deltaF) {
        return Math.sqrt(Math.pow(deltaF[0][0], 2) + Math.pow(deltaF[1][0], 2));
    }

    public double[][] method() {

        double[][] s;
        double gradMod;
        double lamda = alpha;
        int iter = 0;
        double[][] invertPart;
        double[] funcX = new double[2];
        double[][] I = Matrix.unitMatrix(2);

        gradX = zFunction(X[0][0], X[1][0]);
        gradMod = modGrad(gradX);
        funcX[0] = function(X[0][0], X[1][0]);

        while (!(gradMod < e)) {
            iter++;
            System.out.printf("\n----------Iter: %d --------------\n", iter);
            System.out.printf("Grad. F(X%d)", iter - 1);
            Matrix.printMartix(gradX);

            H = hessianMatrix(X[0][0], X[1][0]);
            System.out.print("\nH: ");
            Matrix.printMartix(H);

            invertPart = Matrix.invert(Matrix.sumMatrix(
                    Matrix.prodMatrix(I, lamda), H));

            s = Matrix.prodMatrix(Matrix.prodMatrix(invertPart, -1), gradX);
            System.out.printf("\nS%d: ", iter);
            Matrix.printMartix(s);

            X = Matrix.sumMatrix(X, s);
            System.out.printf("\nX%d: ", iter);
            Matrix.printMartix(X);

            funcX[1] = function(X[0][0], X[1][0]);
            System.out.printf("\nf(x%d): %.6f \nf(x%d): %.6f\n", iter - 1, funcX[0], iter, funcX[1]);

            if (funcX[1] < funcX[0]) {
                lamda /= 2;
                System.out.printf("lamda: %.6f\n", lamda);
            }
            gradX = zFunction(X[0][0], X[1][0]);
            gradMod = modGrad(gradX);
        }
        return X;
    }
}
