package generator;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCodeGenerator {

    static Config config = new Config();
    NameGenerator nameGenerator = new NameGenerator();
    BNF_Grammar bnf_grammar = new BNF_Grammar();
    CheckConfig cc = new CheckConfig();
    String[] classbnf = bnf_grammar.classbnf;

    HashMap<String, ArrayList<String>> production_rules = new HashMap<>();

    void createHM() {
        for (String rule : classbnf) {
            String rules = rule.split("::=")[1].trim();
            ArrayList<String> t = new ArrayList<>();
            for (String r : rules.split("\\|")) {
                t.add(r.trim());
            }
            production_rules.put(rule.split("::=")[0].trim(), t);
        }
        for (String name : production_rules.keySet()) {

            String key = name.toString();
            String value = production_rules.get(name).toString();
            //System.out.println("key:" + key + "value:" + value);
        }
    }
    
    private String generatePackage(String package_declaration) {
        System.out.println("actual:" + package_declaration);
        String regex = "[<][a-z\\s]*[>]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(package_declaration);

        if (matcher.find()) {

            String found = matcher.group(0).trim();
            System.out.println("found:" + found);

            if (production_rules.get(found) != null && (found.equals("<package declaration>") || found.equals("<package folder name>"))) {

                ArrayList<String> rules_associated = production_rules.get(found);

                if (!rules_associated.isEmpty()) {

                    Random r = new Random();
                    String replace = rules_associated.get(r.nextInt(rules_associated.size()));

                    //check if allowed to replace and form
                    if (cc.checkConfig(found, replace)) {

                        //if yes then replace
                        if (found.equals("<package folder name>")) {

                            System.out.println("replace:" + replace);
                            rules_associated.remove(replace);

                            production_rules.put(found, rules_associated);

                        }
                        package_declaration = package_declaration.replaceFirst(found, replace);
                        package_declaration = generatePackage(package_declaration);
                    } else {
                        package_declaration = package_declaration.replaceFirst(found, "");
                        package_declaration = generatePackage(package_declaration);
                    }
                }

            }

        }
        return package_declaration;
    }


    String generateImport(String import_declaration) throws ParseException {

        System.out.println("actual:" + import_declaration);
        String regex = "[<][a-z\\s]*[>]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(import_declaration);

        if (matcher.find()) {

            String found = matcher.group(0).trim();
            System.out.println("found:" + found);
            if (found.contains("?"))
                found.replace("?", "");

            if (production_rules.get(found) != null && !found.equals("<type declarations>")) {

                ArrayList<String> rules_associated = production_rules.get(found);

                if (!rules_associated.isEmpty()) {

                    Random r = new Random();
                    String replace = rules_associated.get(r.nextInt(rules_associated.size()));

                    //check if allowed to replace and form
                    if (cc.checkConfig(found, replace)) {

                        //if yes then replace
                        if (found.equals("<import identifier>")) {

                            System.out.println("replace:" + replace);

                            rules_associated.remove(replace);
                            rules_associated.remove("");
                            for (String str : rules_associated) {
                                System.out.println(">>>>>>------------" + str);
                            }
                            production_rules.put(found, rules_associated);
                            /*import_declaration = import_declaration.replaceFirst(found, replace);
                            import_declaration = generateImport(import_declaration);*/
                        }
                        import_declaration = import_declaration.replaceFirst(found, replace);
                        import_declaration = generateImport(import_declaration);

                    } else {
                        replace = "";
                        import_declaration = import_declaration.replaceFirst(found, replace);
                        import_declaration = generateImport(import_declaration);
                    }
                }
            }
        }
        return import_declaration;
    }


    String generateClass(String type_declarations) throws ParseException {


        System.out.println("actual:" + type_declarations);
        String regex = "[<][a-z\\s]*[>]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(type_declarations);

        if (matcher.find()) {

            String found = matcher.group(0).trim();
            System.out.println("found:" + found);

            if (production_rules.get(found) != null) {

                type_declarations = optionalCFG(type_declarations);
                ArrayList<String> rules_associated = production_rules.get(found);
                Random r = new Random();

                if (!rules_associated.isEmpty()) {

                    String replace = rules_associated.get(r.nextInt(rules_associated.size())).trim();
                    System.out.println("replace: " + replace);


                    if (cc.checkConfig(found, replace)) {

                        System.out.println(config.maxNoOfTypes);

                        if (found.equals("<class access specifier>") || found.equals("<constructor modifier>")) {



                        }



                        if (found.equals("<method identifier>")) {
                            replace = nameGenerator.formMethodName();

                        }

                        if (found.equals("<interface type identifier>") || found.equals("<class type identifier>")) {

                            replace = nameGenerator.formClassName();
                            System.out.println("Class NAME GENERATED: " + replace);


                            if (found.equals("<class type identifier>")) {
                                ArrayList<String> constructor_list = production_rules.get("<constructor identifier>");
                                constructor_list.add(replace);
                                constructor_list.remove(0);
                                for (String str : constructor_list) {
                                    System.out.println("------------>>>>>>" + str);
                                }
                                production_rules.put("<constructor identifier>", constructor_list);
                            }

       }

                        type_declarations = type_declarations.replaceFirst(found, replace).trim();
                        type_declarations = generateClass(type_declarations).trim();

                    } else {
                        type_declarations = type_declarations.replaceFirst(found, "");
                        type_declarations = generateClass(type_declarations);
                    }
                }
            }

            /*

//ensures that modifiers are not repeated
            if (production_rules.get(found) != null && found.equals("<modifier>")) {

                ArrayList<String> rules_associated = production_rules.get(found);

                if (!rules_associated.isEmpty()) {

                    Random r = new Random();
                    String replace = rules_associated.get(r.nextInt(rules_associated.size()));

                    //check if allowed to replace and form

             System.out.println("replace:" + replace);

                      rules_associated.remove(replace);
                      rules_associated.remove("");

                       production_rules.put(found, rules_associated);

                        type_declarations = type_declarations.replaceFirst(found, replace).trim();
                        type_declarations = generateClass(type_declarations).trim();



                }
            }

            */


        }
        System.out.println("Constructor List: " + production_rules.get("<constructor identifier>"));

        return type_declarations;
    }


    private String optionalCFG(String type_declarations) {

        System.out.println("Optional: " + type_declarations);
        String regex = "[<][a-z\\s]*[>][\\?]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(type_declarations);

        while (matcher.find()) {

            String found = matcher.group(0).trim().replace("?", "\\?");
            System.out.println("found optional: " + found);
            Random r = new Random();
            int check = r.nextInt(2);
            if (check == 0) {
                if (found.equals("<formal parameter>?"))
                    type_declarations = type_declarations.replaceFirst(", <formal parameter>\\?", "");
                else
                    type_declarations = type_declarations.replaceFirst(found, "");
            }
            System.out.println("replaced Optional: " + check + " : " + type_declarations);
        }
        return type_declarations;
    }


    private static String getFileNameFromCodeGenerated(String generatedCode) {

        ArrayList<String> t = new ArrayList<String>();
        for (String r : generatedCode.split(" ")) {
            if (!r.trim().isEmpty()) {
                t.add(r.trim());
                //System.out.println("TOKEN: " + r.trim());
            }
        }
        if (t.indexOf("class") != -1) {
            int Position = t.indexOf("class");
            return t.get(Position + 1);
        } else if (t.indexOf("interface") != -1) {
            int Position = t.indexOf("interface");
            return t.get(Position + 1);
        }
        return "NotFound";
    }

    void printToFile(String generatedCode, String filename) throws IOException {

        File f = new File("src/main/java/newlyGeneratedCode", filename + ".java");

        if (!f.exists())//check if the file already exists
            f.createNewFile();
        FileUtils.writeStringToFile(f, generatedCode);
    }

    public static void main(String args[]) throws ParseException, IOException {


        JavaCodeGenerator jcg = new JavaCodeGenerator();
        jcg.createHM();
        String s = "<package declaration> <import declarations>  <type declarations> ";
        //s = "<type declarations>";
        //s = s.replace("<package declaration>", jcg.generatePackage(s));
        s = jcg.generatePackage(s);
        s = jcg.generateImport(s);
        s = jcg.generateClass(s);
        //s = jcg.generateClassBody(s);

        s = s.replaceAll("\\?", "");
        s = s.replaceAll(";", ";\\\n").replaceAll("\\{","{\\\n").replaceAll("}","\\\n }");

        System.out.println("\n\n\nGENERATED CODE: \n" + s);

        String publicClassname = getFileNameFromCodeGenerated(s);
        System.out.println("\n\n\n\n\nFile Name: " + publicClassname);

        jcg.printToFile(s, publicClassname);
    }


/* void randomGrammarPicker() {
        Random randomNumberGenerator = new Random();
        String grammarRule = classbnf[randomNumberGenerator.nextInt(5)];
        //System.out.println(randomNumberGenerator.nextInt(5) + " : " + grammarRule);
    }

*/


}


