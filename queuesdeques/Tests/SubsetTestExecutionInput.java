public class SubsetTestExecutionInput {
    int subsetDesired;
    String inputData;

    public SubsetTestExecutionInput(String[] args) {
        subsetDesired = Integer.parseInt(args[0]);
        inputData = args[1];
    }
}