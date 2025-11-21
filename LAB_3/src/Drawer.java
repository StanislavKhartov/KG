import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Drawer {
    private static Random random = new Random();
    private static Desktop desktop = Desktop.getDesktop();

    public void Save(BufferedImage img){
        try {
            ImageIO.write(img, "png", new File("outputFileName.png"));
            File f = new File("outputFileName.png");
            desktop.open(f);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage drawLineStep(BufferedImage img, int x1, int x2, int y1, int y2){
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        boolean xORy = (Math.abs(x1 - x2) - Math.abs(y1 - y2)) > 0;
        if(xORy){
            Double slope = (double) ((double)(y1 - y2) / (double) (x1 - x2));
            for(int i = 0; i < Math.abs(x1 - x2); i++){
               img.setRGB((int)(i + Math.min(x1, x2)), (int)(slope * i + Math.min(y1, y2)), color.getRGB());
            }
        }else{
            Double slope = ((double)(x1 - x2)) /((double) (y1 - y2));
            for(int i = 1; i < Math.abs(y1 - y2); i++){
               img.setRGB((int)(slope * i + Math.min(x1, x2)), (int)(i + Math.min(y1, y2)), color.getRGB());
            }
        }
        Save(img);
        return img;
    }

    public BufferedImage drawLineCDA(BufferedImage img, int x1, int x2, int y1, int y2){
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Double slopeX = (double) (Math.max(x1, x2) - Math.min(x1, x2));
        Double slopeY = (double) (Math.max(y1, y2) - Math.min(y1, y2));
        img.setRGB(x1, y1, color.getRGB());
        Double l = Math.max(slopeX, slopeY);
        Double i = 0.0;
        Double x = (double) Math.min(x1, x2);
        Double y = (double) Math.min(y1, y2);
        img.setRGB(Math.min(x1, x2), Math.min(y1, y2), color.getRGB());
        while (i <= l)
        {
            x += slopeX / l;
            y += slopeY / l;
            img.setRGB(x.intValue(), y.intValue(), color.getRGB());
            i++;
        }
        Save(img);
        return img;
    }

    public BufferedImage drawLineBR(BufferedImage img, int x1, int x2, int y1, int y2){

        Double mistake = -0.5;
        boolean xORy = (Math.abs(x1 - x2) - Math.abs(y1 - y2)) > 0;
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        img.setRGB(x1, y1, color.getRGB());
        img.setRGB(x2, y2, color.getRGB());
        int delta = 0;
        if (xORy){
            Double slope = (double) ((double)(y1 - y2) / (double) (x1 - x2));
            for(int i = 1; i < Math.abs(x1 - x2); i++){
                mistake += slope;
                if(mistake <= 0){
                    img.setRGB(x1 + i, y1 + delta, color.getRGB());
                } else {
                    delta++;
                    mistake = mistake - 1.0;
                    img.setRGB(x1 + i, y1 + delta, color.getRGB());
                }

            }
        }else{
            Double slope = ((double)(x1 - x2)) /((double) (y1 - y2));
            for(int i = 1; i < Math.abs(y1 - y2); i++){
                mistake += slope;
                if(mistake <= 0){
                    img.setRGB(x1 + delta, y1 + i, color.getRGB());
                } else {
                    delta++;
                    mistake = mistake - 1.0;
                    img.setRGB(x1 + delta, y1 + i, color.getRGB());
                }

            }
        }
        Save(img);
        return img;
    }

    public void draw8 (BufferedImage img, int x, int y, int x1, int y1, Color color){
        img.setRGB(x + x1, y - y1, color.getRGB());
        img.setRGB(x + y1, y - x1, color.getRGB());
        img.setRGB(x + y1, y + x1, color.getRGB());
        img.setRGB(x + x1, y + y1, color.getRGB());
        img.setRGB(x - x1, y + y1, color.getRGB());
        img.setRGB(x - y1, y + x1, color.getRGB());
        img.setRGB(x - y1, y - x1, color.getRGB());
        img.setRGB(x - x1, y - y1, color.getRGB());
    }

    public void draw4 (BufferedImage img, int x, int y, int x1, int y1, Color color){
        img.setRGB(x + x1, y - y1, color.getRGB());
        img.setRGB(x + x1, y + y1, color.getRGB());
        img.setRGB(x - x1, y + y1, color.getRGB());
        img.setRGB(x - x1, y - y1, color.getRGB());
    }

    public BufferedImage drawCircle (BufferedImage img, int x, int y, int r){
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int d = 3 - 2 * r;
        int u = 6;
        int w = 10 - 4 * r;
        int x1 = 0;
        int y1 = r;
        draw8(img, x, y, x1, y1, color);
        while (w < 10){
            if (d < 0){
                d = d + u;
                u = u + 4;
                w = w + 4;
                x1 = x1 + 1;
                draw8(img, x, y, x1, y1, color);
            } else {
                d = d + w;
                u = u + 4;
                w = w + 8;
                x1 = x1 + 1;
                y1 = y1 - 1;
                draw8(img, x, y, x1, y1, color);
            }
        }

        try {
            ImageIO.write(img, "png", new File("outputFileName.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public BufferedImage drawEllipse (BufferedImage img, int x, int y, int a, int b){
        if (a == b){
            return drawCircle(img, x, y, b);
        }
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        /*int l = a*b;
        int d = 0;
        int u = 12 * b;
        int w = 12 * b + 8 * a; 
        int x1 = 0;
        int y1 = b;
        while(l >= 0){
            if(d < 0){
                draw8(img, x, y, x1, y1, color);
                x1 = x1 + 1;
                d = d + u;
                u = u + 8 * b;
                w = w + 8 * a;
                l = l - b;
            } else {
                draw8(img, x, y, x1, y1, color);
                x1 = x1 + 1;
                y1 = y1 - 1;
                d = d + w;
                u = u + 8 * b;
                w = w + 8 * (b + a);
                l = l - b - a;
            }
        }*/
        // https://libeldoc.bsuir.by/bitstream/123456789/33241/1/RAk.pdf
        double p = b * b - a * a * b  + 0.25 * a * a;
        int x1 = 0;
        int y1 = b;
        draw4(img, x, y, x1, y1, color);
        while(a * a * y1 > b * b * x1){
            if (p < 0){
                x1 = x1 + 1;
                p = p + 2 * b * b * x1 + b * b;
                draw4(img, x, y, x1, y1, color);
            }else{
                x1 = x1 + 1;
                y1 = y1 - 1;
                p = p + 2 * b* b * x1 - 2 * a * a * y1 + b * b;
                draw4(img, x, y, x1, y1, color);
            }
        }
        p = b * b * (x1 + 0.5) * (x1 + 0.5) + a * a * (y1 - 1) * (y1 - 1) - a * a * b * b;
        while (y1 != 0){
            if (p <= 0){
                x1 = x1 + 1;
                y1 = y1 - 1;
                p = p + 2 * b* b * x1 - 2 * a * a * y1 + a * a;
                draw4(img, x, y, x1, y1, color);
            }else{
                y1 = y1 - 1;
                p = p - 2 * a * a * y1 + a * a;
                draw4(img, x, y, x1, y1, color);
            }
        }
        Save(img);
        return img;
    }
}
