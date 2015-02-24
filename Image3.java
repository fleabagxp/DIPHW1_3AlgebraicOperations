/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image3;

/**
 *
 * @author fleabag
 */
import java.awt.Color;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;
import java.awt.*;

/**
 *
 * @author fleabag
 */
public class Image3 extends JFrame {

    /**
     * @param args the command line arguments
     */
    static int[] histogram_red = new int[256];
    static int[] histogram_green = new int[256];
    static int[] histogram_blue = new int[256];

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        String red = "D:\\Users\\fleabag\\Documents\\NetBeansProjects\\Image1\\src\\image1\\Image\\3\\SanFranPeak_red.pgm";
        String green = "D:\\Users\\fleabag\\Documents\\NetBeansProjects\\Image1\\src\\image1\\Image\\3\\SanFranPeak_green.pgm";
        String blue = "D:\\Users\\fleabag\\Documents\\NetBeansProjects\\Image1\\src\\image1\\Image\\3\\SanFranPeak_blue.pgm";
        FileInputStream fileInputStream_red = new FileInputStream(red);
        FileInputStream fileInputStream_green = new FileInputStream(green);
        FileInputStream fileInputStream_blue = new FileInputStream(blue);

        Scanner scan = new Scanner(fileInputStream_red);
        scan.nextLine(); //Header Line 1
        scan.nextLine();//Header Line 2

        int picHeight = scan.nextInt(); //Header Line 3
        int picWidth = scan.nextInt();  //Header Line 3
        int maxvalue = scan.nextInt(); //Header Line 4

        fileInputStream_red.close();

        fileInputStream_red = new FileInputStream(red);
        fileInputStream_green = new FileInputStream(green);
        fileInputStream_blue = new FileInputStream(blue);
        DataInputStream dis_red = new DataInputStream(fileInputStream_red);
        DataInputStream dis_green = new DataInputStream(fileInputStream_green);
        DataInputStream dis_blue = new DataInputStream(fileInputStream_blue);

        int numnewlines = 4;
        while (numnewlines > 0) {
            char c;
            do {
                c = (char) (dis_red.readUnsignedByte());
                System.out.print(c);
            } while (c != '\n');
            numnewlines--;
        }

        int a;
        int[][] data2D = new int[picWidth][picHeight];
        int[][] data_red = new int[picWidth][picHeight];
        int[][] data_green = new int[picWidth][picHeight];
        int[][] data_blue = new int[picWidth][picHeight];
        for (int row = 0; row < picWidth; row++) {
            for (int col = 0; col < picHeight; col++) {
                data_red[row][col] = dis_red.readUnsignedByte();
                data_green[row][col] = dis_green.readUnsignedByte();
                data_blue[row][col] = dis_blue.readUnsignedByte();

                /*(2*g)-r-b*/
                data2D[row][col] = 2 * (data_green[row][col]) - data_red[row][col] - data_blue[row][col];

                /*r-b*/
                //data2D[row][col]=data_red[row][col]-data_blue[row][col];
                /*(r+b+g)/3*/
                //data2D[row][col]=(data_red[row][col]+data_blue[row][col]+data_green[row][col])/3;
                a = data2D[row][col];
                if (a < 0) {
                    a = 0;
                } else if (a > 255) {
                    a = 255;
                }
                histogram_red[a]++;
                System.out.print(data_red[row][col] + " ");
            }
            System.out.println();
        }

        fileInputStream_red.close();

        Image3 image = new Image3();
        showPGM pgm = new showPGM(red, green, blue);
    }

    public Image3() {
        super("Histogram");
        Dimension size = new Dimension(300, 350);
        Toolkit tk = getToolkit();
        Dimension screen = tk.getScreenSize();
        setBounds((screen.width - size.width) / 2, (screen.height - size.height) / 2, size.width, size.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.GRAY);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        g.setColor(Color.WHITE);

        if (histogram_red != null) {
            int widths = 45;
            int height = 350;
            int HhPos = (widths - (widths / 2));
            int HvPos = 342;
            for (int i = 0; i <= 255; i++) {
                int[] r = histogram_red;

                r[i] = (r[i] * 100) / 300;
                g.fillRect(i + HhPos, HvPos, 1, -r[i]);
            }
        }
    }
}
