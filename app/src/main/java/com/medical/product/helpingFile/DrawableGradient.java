package com.medical.product.helpingFile;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class DrawableGradient extends GradientDrawable {
    public DrawableGradient(int[] colors, int cornerRadius) {
        super(Orientation.LEFT_RIGHT, colors);

        try {
            this.setShape(GradientDrawable.LINEAR_GRADIENT);
            this.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            this.setCornerRadius(cornerRadius);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Drawable SetTransparency(int transparencyPercent) {
        this.setAlpha(255 - ((255 * transparencyPercent) / 100));

        return this;
    }
}
