public class ShowEnergy {

    public static void main(String[] args)
    {
        Picture inputImg = new Picture(args[0]);
        System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        inputImg.show();
        SeamCarver sc = new SeamCarver(inputImg);

        System.out.printf("Displaying energy calculated for each pixel.\n");
        SCUtility.showEnergy(sc);

    }

}