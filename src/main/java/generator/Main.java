package generator;

import generator.elements.Type;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public void callClass() {

        Config config = new Config();
        for (int i = 0; i < Config.noOfClasses; i++) {
            boolean createClass;
            Random rn = new Random();
            createClass = rn.nextBoolean();
            if (createClass == true) {

                Type type = new Type();
                String generated_code = type.createClassLine();
                JavaCodeGenerator jcg = new JavaCodeGenerator();
                generated_code = jcg.generatePackage("<package declaration>")
                        + jcg.generateImport("<import declaration>")
                        + generated_code;

                Main main = new Main();
                main.printToFile(generated_code, type.name);
            }
        }
    }

    private void callInterface() throws ParseException {

        boolean createInterface;
        Random rn = new Random();
        createInterface = rn.nextBoolean();
        if (createInterface == true) {
            Type type = new Type();
            String generated_code = type.createInterfaceLine();
            generated_code = "package newlyGeneratedCode ;" + generated_code;
            Main main = new Main();
            main.printToFile(generated_code, type.name);
        }
    }


    public void printToFile(String generatedCode, String filename) {

        String regex = "[<][a-z\\s]*[>]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(generatedCode);

        while (matcher.find()) {

            String found = matcher.group().trim();
            System.out.println("found in last:" + found);
            generatedCode.replace(found, "");
        }
        File f = new File("src/main/java/newlyGeneratedCode", filename + ".java");

        if (!f.exists())//check if the file already exists
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            FileUtils.writeStringToFile(f, generatedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws ParseException {
        Main main = new Main();
        main.callClass();
        main.callInterface();

    }

}