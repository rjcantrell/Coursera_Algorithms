import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    protected Picture p;
    protected int[][] energy;

    public SeamCarver(Picture picture) {
        p = new Picture(picture);
        calcEnergies();
    }
    
    public Picture picture() {                       // current picture
        return p;
    }

    public int width() {                         // width  of current picture
        return p.width();
    }

    public int height() {                        // height of current picture
        return p.height();
    }

    // Computing the energy of a pixel. We will use the dual gradient energy function: The energy of pixel (x, y) is
    // Δx2(x, y) + Δy2(x, y), where the square of the x-gradient Δx2(x, y) = Rx(x, y)2 + Gx(x, y)2 + Bx(x, y)2, and where
    // the central differences Rx(x, y), Gx(x, y), and Bx(x, y) are the absolute value in differences of red, green, and
    // blue components between pixel (x + 1, y) and pixel (x − 1, y). The square of the y-gradient Δy2(x, y) is defined
    // in an analogous manner. We define the energy of pixels at the border of the image to be 2552 + 2552 + 2552 = 195075.
    public double energy(int x, int y) {            // energy of pixel at column x and row y in current picture
        if (!isValidPixel(x, y)) { throw new IndexOutOfBoundsException(); }
        return energy[x][y];
    }

    public int[] findHorizontalSeam() {            // sequence of indices for horizontal seam in current picture
        return findSeam(false);
    }

    public int[] findVerticalSeam() {              // sequence of indices for vertical   seam in current picture
        return findSeam(true);
    }

    public void removeHorizontalSeam(int[] a) {   // remove horizontal seam from current picture
        if (!isValidSeam(a, false)) { throw new IllegalArgumentException(); }
        removeSeam(a, false);
    }

    public void removeVerticalSeam(int[] a) {     // remove vertical   seam from current picture
        if (!isValidSeam(a, true)) { throw new IllegalArgumentException(); }
        removeSeam(a, true);
    }

    protected boolean isValidPixel(int x, int y) {
        return (x >= 0 || x <= width() - 1 || y >= 0 || y <= height() - 1);
    }

    protected boolean isBorderPixel(int x, int y) {
        return (x == 0 || x == width() - 1 || y == 0 || y == height() - 1);
    }

    protected void calcEnergies() {
        energy = new int[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energy[x][y] = getEnergyInt(x, y);
            }
        }
    }

    protected double getEnergy(int x, int y) {
        if (isBorderPixel(x, y)) { return 195075d; }
        double Xenergy = pixelDiff(x - 1, y, x + 1, y);
        double Yenergy = pixelDiff(x, y - 1, x, y + 1);
        return Xenergy + Yenergy;
    }

    protected int getEnergyInt(int x, int y) {
        Double retVal = getEnergy(x, y);
        return retVal.intValue();
    }

    protected double pixelDiff(int x1, int y1, int x2, int y2) {
        Color a = p.get(x1, y1);
        Color b = p.get(x2, y2);

        double retVal = 0d;
        retVal += numberDiff(a.getRed(), b.getRed());
        retVal += numberDiff(a.getGreen(), b.getGreen());
        retVal += numberDiff(a.getBlue(), b.getBlue());

        return retVal;
    }

    protected double numberDiff(int a, int b) {
        return Math.pow(b - a, 2);
    }

    protected int[] findSeam(boolean vertical) {
        int thisDimension = vertical ? height() : width();
        int thatDimension = vertical ? width() : height();
        int[] retVal = new int[thisDimension];

        if (thatDimension < 3) {
            for (int i = 0; i < thisDimension; i++) { retVal[i] = 0; }
            return retVal;
        }

        int newWidth = vertical ? width() : width() - 1;
        int newHeight = vertical ? height() - 1 : height();
        int[][] weights = new int[newWidth][newHeight];
        int[][] vertexTo = new int[newWidth][newHeight];

        int min = Integer.MAX_VALUE;
        for (int ths = 0; ths < thisDimension - 1; ths++) {
            for (int that = 0; that < thatDimension; that++) {
                int x = vertical ? that : ths;
                int y = vertical ? ths : that;

                if (ths == 0) {
                    vertexTo[x][y] = 0;
                    weights[x][y] = 0;
                } else {
                    if (that - 1 >= 0) {
                        min = weights[x - 1][y - 1];
                        vertexTo[x][y] = that - 1;
                    }

                    int beforeX = vertical ? x : x - 1;
                    int beforeY = vertical ? y - 1 : y;
                    if (weights[beforeX][beforeY] < min) {
                        min = weights[beforeX][beforeY];
                        vertexTo[x][y] = that;
                    }

                    int afterX = vertical ? x + 1 : x - 1;
                    int afterY = vertical ? y - 1 : y + 1;
                    if (that + 2 < thatDimension && weights[afterX][afterY] < min) {
                        min = weights[afterX][afterY];
                        vertexTo[x][y] = that + 1;
                    }

                    weights[x][y] = min + getEnergyInt(x, y);
                }
            }
        }

        int finalLayer = thisDimension - 2;
        min = Integer.MAX_VALUE;
        Integer minThat = null;
        for (int that = 0; that < thatDimension - 1; that++) {
            int x = vertical ? that : finalLayer;
            int y = vertical ? finalLayer : that;
            if (weights[x][y] < min) {
                min = weights[x][y];
                minThat = that;
            }
        }

        if (minThat - 1 >= 0) { retVal[thisDimension - 1] = minThat - 1; }
        else { retVal[thisDimension - 1] = minThat; }

        retVal[thisDimension - 2] = minThat;

        for (int i = thisDimension - 3; i >= 0; i--) {
            int x = vertical ? retVal[i +1] : i + 1;
            int y = vertical ? i + 1 : retVal[i + 1];
            retVal[i] = vertexTo[x][y];
        }

        return retVal;
    }

    protected void removeSeam(int[] seam, boolean vertical) {
        if (!isValidSeam(seam, vertical)) { throw new IllegalArgumentException(); }

        int thisDimension = vertical ? height() : width();
        int thatDimension = vertical ? width() : height();
        int newWidth = vertical ? width() - 1 : width();
        int newHeight = vertical ? height() : height() - 1;

        Picture tmpPic = new Picture(newWidth, newHeight);
        for (int ths = 0; ths < thisDimension; ths++) {
            int thatCount = 0;
            for (int that = 0; that < thatDimension; that++) {
                if (that == seam[ths]) { continue; }
                //StdOut.println(String.format("Before tmpPic.set(thatCount++, ths, p.get(that, ths)) -> this = %d, that = %d, thatCount = %d", ths, that, thatCount));
                if (vertical) { tmpPic.set(thatCount++, ths, p.get(that, ths)); }
                else { tmpPic.set(ths, thatCount++, p.get(ths, that)); }
            }
        }
        p = tmpPic;

        int[][] tmpEnergy = new int[width()][height()];
        for (int ths = 0; ths < thisDimension; ths++) {
            for (int that = 0; that < thatDimension; that++) {
                int x = vertical ? that : ths;
                int y = vertical ? ths : that;

                //vertical:
                //1) x = seam[ths] - 1  y = ths
                //2) x = seam[ths]      y = ths
                //3) x = that + 1       y = ths
                //
                //horizontal:
                //1) x = ths            y = seam[ths] - 1
                //2) x = ths            y = seam[ths]
                //3) x = ths            y = that + 1
                int Xbefore = vertical ? seam[ths] - 1 : ths;
                int Xhere = vertical ? seam[ths] : ths;
                int Xafter = vertical ? that + 1 : ths;

                int Ybefore = vertical ? ths : seam[ths] - 1;
                int Yhere = vertical ? ths : seam[ths];
                int Yafter = vertical ? ths : that + 1;

                //TODO: define this shit in real terms of UDLN, before, and after. jeez.
                if (seam[ths] - 1 == that) { tmpEnergy[Xbefore][Ybefore] = getEnergyInt(Xbefore, Ybefore); }
                else if (seam[ths] == that) { tmpEnergy[Xhere][Yhere] = getEnergyInt(Xhere, Yhere); }
                else if (seam[ths] < that && that + 1 < thatDimension) { tmpEnergy[x][y] = energy[Xafter][Yafter]; }
                else if (x < tmpEnergy.length && y < tmpEnergy[x].length) { tmpEnergy[x][y] = energy[x][y]; }
            }
        }
        energy = tmpEnergy;
    }

    // Throw a java.lang.IllegalArgumentException if removeVerticalSeam() or removeHorizontalSeam() is called with an
    // array of the wrong length or if the array is not a valid seam (i.e., either an entry is outside its prescribed
    // range or two adjacent entries differ by more than 1).

    // Throw a java.lang.IllegalArgumentException if either removeVerticalSeam() or removeHorizontalSeam() is called
    // when either the width or height is less than or equal to 1.
    protected boolean isValidSeam(int[] seam, boolean vertical) {
        int thisDimension = vertical ? height() : width();
        if (width() <= 1 || height() <= 1) { return false; }
        if (seam.length != thisDimension) { return false; }

        Integer prev = seam[0];
        for (int i = 0; i < thisDimension; i++) {
            if (Math.abs(seam[i] - prev) > 1) { return false; }
            prev = seam[i];
        }
        return true;
    }
}