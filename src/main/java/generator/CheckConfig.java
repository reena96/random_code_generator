package generator;

public class CheckConfig {
    Config config = new Config();
    public boolean checkConfig(String found, String replace) {

        switch (found) {

            case "<statements>":
                if (replace.contains("<statement>")) {
                    config.maxNoOfStatementsPerMethod -= 1;
                    if (config.maxNoOfStatementsPerMethod < 0) {
                        return false;
                    } else return true;
                }
                break;
            case "<type declarations>":
                if (replace.contains("<type declaration>")) {
                    config.maxNoOfTypes -= 1;
                    if (config.maxNoOfTypes < 0) {
                        return false;
                    } else return true;
                }
                break;
            case "<import declarations>":
                if (replace.contains("<import declaration>")) {
                    config.maxNoOfImports -= 1;
                    if (config.maxNoOfImports < 0) {
                        return false;
                    } else return true;
                }
                break;
            case "<type declaration>":
                if (replace.contains("<class declaration>")) {
                    config.noOfClasses -= 1;
                    if (config.noOfClasses < 0) {
                        return false;
                    } else return true;
                } else if (replace.contains("<interface declaration>")) {
                    config.noOfInterfaces -= 1;
                    if (config.noOfInterfaces < 0) {
                        return false;
                    } else return true;
                }
                break;
            case "<method declaration>":
                if (replace.contains("<import declaration>")) {
                    config.maxNoOfMethods -= 1;
                    if (config.maxNoOfMethods< 0) {
                        return false;
                    } else return true;
                }
                break;
            case "<method invocation>":
                if (replace.contains("<method invocation>")) {
                    config.maxAllowedMethodCalls -= 1;
                    if (config.maxAllowedMethodCalls < 0) {
                        return false;
                    } else return true;
                }
                break;
            default:
                return true;
        }
        return true;
    }
}
