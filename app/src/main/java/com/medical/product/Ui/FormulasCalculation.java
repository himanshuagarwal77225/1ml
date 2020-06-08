package com.medical.product.Ui;

import java.text.DecimalFormat;

public class FormulasCalculation {


    public static double CalculateProductDiscount(double mrpprice, double saleprice)
    {

      double  pricedistc=mrpprice-saleprice;


        double discpercentage= ((pricedistc/mrpprice)*100);

        return Double.parseDouble(new DecimalFormat("##.##").format(discpercentage));
    }

}
