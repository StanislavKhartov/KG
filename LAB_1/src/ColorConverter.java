public class ColorConverter {
    
    public double[] rgbToCmyk(int r, int g, int b) {

        double k = (double) Math.min(Math.min(1.0 - (double) (r / 255.0), 1.0 - (double) (g / 255.0)), 1.0 - (double) (b / 255.0));
        
        if (k == 1) {
            return new double[]{0.0, 0.0, 0.0, 1.0};
        }

        double c = (1 - (double)(r / 255.0) - k) / (double)(1 - k);
        double m = (1 - (double)(g / 255.0) - k) / (double)(1 - k);
        double y = (1 - (double)(b / 255.0) - k) / (double)(1 - k);

        return new double[]{c, m, y, k};
    }

    public int[] cmykToRgb(double c, double m, double y, double k) {

        if (k == 1){
            return new int[]{0, 0, 0};
        }

        double r = 255 * (1 - c) * (1 - k);
        double g = 255 * (1 - m) * (1 - k);
        double b = 255 * (1 - y) * (1 - k);

        return new int[]{(int) Math.round(r),(int) Math.round(g),(int) Math.round(b)};
    }

    // https://www.rapidtables.com/convert/color/hsv-to-rgb.html
    public int[] hsvToRgb(double h, double s, double v){
    
    double c = v * s;
    double x = c * (1 - Math.abs((h / 60) % 2 - 1));
    double m = v - c;
    
    double rPrime, gPrime, bPrime;
    
    if (h < 60) {
        rPrime = c; gPrime = x; bPrime = 0;
    } else if (h < 120) {
        rPrime = x; gPrime = c; bPrime = 0;
    } else if (h < 180) {
        rPrime = 0; gPrime = c; bPrime = x;
    } else if (h < 240) {
        rPrime = 0; gPrime = x; bPrime = c;
    } else if (h < 300) {
        rPrime = x; gPrime = 0; bPrime = c;
    } else {
        rPrime = c; gPrime = 0; bPrime = x;
    }
    
    int r = (int) Math.round((rPrime + m) * 255);
    int g = (int) Math.round((gPrime + m) * 255);
    int b = (int) Math.round((bPrime + m) * 255);
    
    return new int[]{r, g, b};
    }

    // https://math.stackexchange.com/questions/556341/rgb-to-hsv-color-conversion-algorithm
    public double[] rgbToHsv(int r, int g, int b){
    double rd = (double)r / 255.0;
    double gd = (double)g / 255.0;
    double bd = (double)b / 255.0;
    
    double maxc = Math.max(rd, Math.max(gd, bd));
    double minc = Math.min(rd, Math.min(gd, bd));
    double v = maxc;
    
    if (minc == maxc) {
        return new double[]{0.0, 0.0, v * 100};
    }
    
    double s = (maxc - minc) / maxc;
    double rc = (maxc - rd) / (maxc - minc);
    double gc = (maxc - gd) / (maxc - minc);
    double bc = (maxc - bd) / (maxc - minc);
    
    double h;
    if (rd == maxc) {
        h = 0.0 + bc - gc;
    } else if (gd == maxc) {
        h = 2.0 + rc - bc;
    } else {
        h = 4.0 + gc - rc;
    }
    
    h = (h / 6.0);
    if (h < 0) {
        h += 1.0;
    }
    
    return new double[]{Math.round(h * 360), s, v};
    };
}