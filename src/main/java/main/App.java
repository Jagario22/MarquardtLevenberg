package main;


public class App {
    private static double e = 0.000001;


    public static void main(String[] args) {
        double[] xi = {-1.2, 1};
        double[][] result =  new Solution(xi).method();
        System.out.println("\n-------Result--------");
        Matrix.printMartix(result);
    }
}