/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image3;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author fleabag
 */
public class showPGM extends Component {

    private BufferedImage img;
    private int[][] pixels;

    private void pix2img() {
        int g;
        img = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_ARGB);
        for (int row = 0; row < pixels.length; ++row) {
            for (int col = 0; col < pixels[row].length; ++col) {
                g = pixels[row][col];
                img.setRGB(col, row, ((255 << 24) | (g << 16) | (g << 8) | g));
            }
        }
    }

    public showPGM(String red, String green, String blue) {
        pixels = null;
        readFile(red, green, blue);
        if (pixels != null) {
            pix2img();
        }

        JFrame f = new JFrame("PGM");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.add(this);
        f.pack();
        f.setVisible(true);
    }

    public void readFile(String red, String green, String blue) {
        try {

            FileInputStream fileInputStream_red = new FileInputStream(red);
            FileInputStream fileInputStream_green = new FileInputStream(green);
            FileInputStream fileInputStream_blue = new FileInputStream(blue);

            Scanner scan_red = new Scanner(fileInputStream_red);
            Scanner scan_green = new Scanner(fileInputStream_green);
            Scanner scan_blue = new Scanner(fileInputStream_blue);

            //Header Line1
            scan_red.nextLine();
            scan_green.nextLine();
            scan_blue.nextLine();

            //Header Line2
            scan_red.nextLine();
            scan_green.nextLine();
            scan_blue.nextLine();

            //Header Line3
            int picHeight = scan_red.nextInt();
            int picWidth = scan_red.nextInt();
            int maxvalue = scan_red.nextInt();

            //Header Line3
            scan_green.nextLine();
            scan_green.nextLine();
            scan_blue.nextLine();
            scan_blue.nextLine();

            //Header Line4
            fileInputStream_red.close();
            fileInputStream_green.close();
            fileInputStream_blue.close();

            fileInputStream_red = new FileInputStream(red);
            fileInputStream_green = new FileInputStream(green);
            fileInputStream_blue = new FileInputStream(blue);
            DataInputStream dis_red = new DataInputStream(fileInputStream_red);
            DataInputStream dis_green = new DataInputStream(fileInputStream_green);
            DataInputStream dis_blue = new DataInputStream(fileInputStream_blue);

            int numnewlines = 4;
            while (numnewlines > 0) {
                char c, d, e;
                do {
                    c = (char) (dis_red.readUnsignedByte());
                    d = (char) (dis_green.readUnsignedByte());
                    d = (char) (dis_blue.readUnsignedByte());
                } while (c != '\n');
                numnewlines--;
            }

            int a;
            int[][] data_red = new int[picWidth][picHeight];
            int[][] data_green = new int[picWidth][picHeight];
            int[][] data_blue = new int[picWidth][picHeight];
            pixels = new int[picWidth][picHeight];
            for (int row = 0; row < picWidth; row++) {
                for (int col = 0; col < picHeight; col++) {
                    data_red[row][col] = dis_red.readUnsignedByte();
                    data_green[row][col] = dis_green.readUnsignedByte();
                    data_blue[row][col] = dis_blue.readUnsignedByte();

                    /*(2*g)-r-b*/
                    pixels[row][col] = 2 * (data_green[row][col]) - data_red[row][col] - data_blue[row][col];

                    /*r-b*/
                    //pixels[row][col]=data_red[row][col]-data_blue[row][col];
                    /*(r+b+g)/3*/
                    //pixels[row][col]=(data_red[row][col]+data_blue[row][col]+data_green[row][col])/3;
                    if (pixels[row][col] < 0) {
                        pixels[row][col] = 0;
                    } else if (pixels[row][col] > 255) {
                        a = pixels[row][col];
                    }
                }
            }

            fileInputStream_red.close();
        } catch (FileNotFoundException fe) {
            System.out.println("Had a problem opening a file.");
        } catch (Exception e) {
            System.out.println(e.toString() + " caught in readPPM.");
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

    public Dimension getPreferredSize() {
        if (img == null) {
            return new Dimension(100, 100);
        } else {
            return new Dimension(Math.max(100, img.getWidth(null)),
                    Math.max(100, img.getHeight(null)));
        }
    }
}
