import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TestFileRunner {

    public static class ExtensionFilter implements FilenameFilter  {
        ArrayList<String> whitelist;

        public ExtensionFilter(String... exts) {
            whitelist = new ArrayList<String>();
            whitelist.addAll(Arrays.asList(exts));
        }

        @Override
        public boolean accept(File dir, String name) {
            if (whitelist.size() == 0) return true;
            for (String ext : whitelist) {
                if (name.toLowerCase().contains(ext.toLowerCase())) { return true; }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            File here = new File("./Algs4/src/Assignments/burrows/tests");
            String[] tests = new String[] { "abra.txt" };

            InputStream origInput = System.in;

            for (File f : here.listFiles(new ExtensionFilter(tests))) {
                StdOut.println("executing file " + f.getCanonicalPath());

                //if your test class reads a file directly from in, instead of via its name

                System.setIn(new FileInputStream(f));
                //StdIn.resync();

                //if your test class needs to direct its output to a file instead of the console
                //PrintStream origOutput = System.out;
                //String outFileName = f.getCanonicalPath().replaceFirst(".png", ".printseams_RJ.txt");
                //System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(outFileName)), true));

                //PrintSeams.main(new String[]{f.getCanonicalPath()});
                //ShowSeams.main(new String[]{f.getCanonicalPath()});
                //ShowEnergy.main(new String[]{f.getCanonicalPath()});
                //ResizeDemo.main(new String[] {f.getCanonicalPath(), "50", "50"});
                //SeamCarverObjectRemover.main(new String[] { f.getCanonicalPath() });
                //BaseballElimination.main(new String[] {f.getCanonicalPath()});
                //BoggleSolver.main(new String[] { here.getCanonicalPath() + File.separator + "dictionary-yawl.txt", f.getCanonicalPath()});

                //if this is clear
                String whatToDo = isTransformed(f.getCanonicalPath()) ? "+" : "-";
                BurrowsWheeler.main(new String[] { whatToDo });

                //reset in and out, if you need to?
                //StdDraw.clear();
                if (origInput != null) { System.setIn(origInput); }
                //if (origOutput != null) { System.setOut(origOutput); }
            }
        } catch (Exception e) {
            StdOut.println(e.toString() + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isTransformed(String s) {
        return (s.endsWith(".bwt") || s.endsWith(".mtf") || s.endsWith(".huf"));
    }
}
