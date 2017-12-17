package generator;

import generator.elements.FieldDeclaration;
import generator.elements.Type;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JavaCodeGenerator {


    public List<FieldDeclaration> updated_declaration_list = new ArrayList<>();
    BNF_Grammar_Statements bnf_grammar = new BNF_Grammar_Statements();
    String type = "";
    Config config = new Config();
    public NameGenerator nameGenerator = new NameGenerator();
    static final int MAX_HOPS = 1000;
    CheckConfig cc = new CheckConfig();
    String[] classbnf = bnf_grammar.classbnf;
    static int hopCount = 0;
    public int maxNoOfStatementsPerMethod = 10;

    HashMap<String, ArrayList<String>> production_rules = new HashMap<>();
    static SecureRandom r;

    static {
        try {
            r = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public JavaCodeGenerator() {
        createHM();
    }

    public boolean checkConfig(String found, String replace) {

        switch (found) {

            case "<statements>":
                if (replace.contains("<statement>")) {
                    maxNoOfStatementsPerMethod -= 1;
                    if (maxNoOfStatementsPerMethod < 0) {
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
                    if (config.maxNoOfMethods < 0) {
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

    public static String getFileNameFromCodeGenerated(String generatedCode) {

        ArrayList<String> t = new ArrayList<String>();
        for (String r : generatedCode.split(" ")) {
            if (!r.trim().isEmpty()) {
                t.add(r.trim());
                //System.out.println("TOKEN: " + r.trim());
            }
        }

        if (t.indexOf("public") != -1) {
            int Position = t.indexOf("public");
            if (t.get(Position + 1) == "class" || t.get(Position + 1) == "interface") {
                return t.get(Position + 2);
            }
        }

        return "NotFound";
    }

    public static void main(String args[]) throws IOException {

        List<FieldDeclaration> fieldDeclarationsList = new ArrayList<>();
        FieldDeclaration fieldDeclaration = new FieldDeclaration();
        fieldDeclaration.type = "int";
        fieldDeclaration.name = "variable_1";
        fieldDeclaration.assignment = "1";
        fieldDeclarationsList.add(fieldDeclaration);
        JavaCodeGenerator jcg = new JavaCodeGenerator();
        jcg.createHM();
        String s = "<package declaration> <import declarations>  <type declarations> ";
        s = "<statements>";
        //s = s.replace("<package declaration>", jcg.generatePackage(s));
        //s = jcg.generatePackage(s);
        //s = jcg.generateImport(s);
        s = jcg.generateClass(s, fieldDeclarationsList, new NameGenerator());
        //s = jcg.generateClassBody(s);

        s = s.replaceAll("\\?", "");
        s = s.replaceAll(";", ";\\\n").replaceAll("\\{", "{\\\n").replaceAll("}", "\\\n }");

        System.out.println("\n\n\nGENERATED CODE: \n" + s);

        String publicClassname = getFileNameFromCodeGenerated(s);
        System.out.println("\n\n\n\n\nFile Name: " + publicClassname);

        //jcg.printToFile(s, publicClassname);
    }

    public void createHM() {
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

    public String generatePackage(String package_declaration) {

        System.out.println("actual:" + package_declaration);
        package_declaration = "package newlyGeneratedCode;";
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

    public String generateImport(String import_declaration) {

        System.out.println("actual:" + import_declaration);
        import_declaration = "import java.*";
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

    public String generateClass(String type_declarations, List<FieldDeclaration> field_declaration_list, NameGenerator nameGenerator) {

        updated_declaration_list = field_declaration_list;
        System.out.println(hopCount++);
        if (hopCount > MAX_HOPS) {
            hopCount /= 2;
        }
        //Clazz node = new Clazz();

        System.out.println("actual:" + type_declarations);
        String regex = "[<][a-z\\s]*[>]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(type_declarations);

        if (matcher.find()) {

            String found = matcher.group(0).trim();
            System.out.println("found:" + found);

            if (production_rules.get(found) != null && hopCount < MAX_HOPS) {

                type_declarations = optionalCFG(type_declarations);
                ArrayList<String> rules_associated = production_rules.get(found);

                if (!rules_associated.isEmpty()) {

                    String replace = rules_associated.get(r.nextInt(rules_associated.size())).trim();
                    System.out.println("replace: " + replace);

                    if (checkConfig(found, replace)) {

                        System.out.println(config.maxNoOfTypes);

                        if (found.equals("<method identifier>")) {
                            // get method name from list of method names in clazz node
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
                        type_declarations = generateClass(type_declarations, field_declaration_list, nameGenerator).trim();

                    } else {
                        type_declarations = type_declarations.replaceFirst(found, "");
                        type_declarations = generateClass(type_declarations, field_declaration_list, nameGenerator);
                    }
                }


            } else if (found.equals("<expression int>") || found.equals("<expression String>") || found.equals("<expression boolean>") ||
                    found.equals("<expressionLH int>") || found.equals("<expressionLH String>") || found.equals("<expressionLH boolean>")) {
                String[] expression = found.split(" ");
                type = expression[1].replaceFirst(">", "");
                type_declarations.replaceFirst(found, expression[0] + ">");
                String replace = expression[0] + ">";
                type_declarations = type_declarations.replaceFirst(found, replace);
                type_declarations = generateClass(type_declarations, field_declaration_list, nameGenerator);
            }
            // <identifier> is getting replaced

            else if (found.equals("<identifier>")) {

                List<FieldDeclaration> filtered_identifiers = new ArrayList<>();

                if (!type.isEmpty()) {
                    for (FieldDeclaration fd : field_declaration_list) {
                        System.out.println("FD: " + fd.type + " : " + type + " : " + fd.type.equals(type));
                        if (fd.type.equals(type)) {
                            filtered_identifiers.add(fd);
                            //type = fd.type;
                        }
                    }
                    if (filtered_identifiers.isEmpty()) {

                        System.out.println("FD: found nothing so creating: " + type);
                        FieldDeclaration fd = new FieldDeclaration();
                        fd.type = type;
                        fd.name = nameGenerator.formIdentifierName();
                        switch (type) {
                            case "int":
                                fd.assignment = "7865";
                                break;
                            case "String":
                                fd.assignment = " \" added \"";
                                break;
                            case "boolean":
                                fd.assignment = "true";
                                break;
                        }
                        for (FieldDeclaration f : field_declaration_list)
                            System.out.println("/////////////" + f.type + " " + f.name + " " + f.assignment);
                        filtered_identifiers.add(fd);
                        for (FieldDeclaration f : field_declaration_list)
                            System.out.println("/////////////" + f.type + " " + f.name + " " + f.assignment);
                        field_declaration_list.add(fd);
                    }
                } else {
                    filtered_identifiers = field_declaration_list;
                }
                int f = r.nextInt(filtered_identifiers.size());
                String identifier_name = filtered_identifiers.get(f).name;

                System.out.println();
                type_declarations = type_declarations.replaceFirst(found, identifier_name);
                type_declarations = generateClass(type_declarations, field_declaration_list, nameGenerator);
            } else if (found.equals("<value>")) {

                String replace = "";
                System.out.println(" TYPE EVERYTIME: " + type);
                if (!type.isEmpty()) {
                    if (type.equals("String")) {
                        replace = "\"" + new Type().getConstantList(type) + "\"";
                    } else {
                        replace = new Type().getConstantList(type);
                    }
                } else {
                    replace = "null";
                }
                type_declarations = type_declarations.replaceFirst(found, replace);
                type_declarations = generateClass(type_declarations, field_declaration_list, nameGenerator);
            }
        }
        //System.out.println("Constructor List: " + production_rules.get("<constructor identifier>"));
        return type_declarations;
    }

    public String optionalCFG(String type_declarations) {

        System.out.println(hopCount++);

        System.out.println("Optional: " + type_declarations);
        String regex = "[<][a-z\\s]*[>][\\?]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(type_declarations);

        while (matcher.find()) {

            String found = matcher.group(0).trim().replace("?", "\\?");
            System.out.println("found optional: " + found);
            int check = r.nextInt(100);
            if (check > 70 && hopCount < MAX_HOPS) {
                if (found.equals("<formal parameter>?"))
                    type_declarations = type_declarations.replaceFirst(", <formal parameter>\\?", "");
                else
                    type_declarations = type_declarations.replaceFirst(found, "");
            } else {
                String replace = found.replace("\\?", "");
                System.out.println("NOT released OPTIONAL: " + found + " - " + replace);
                type_declarations = type_declarations.replaceFirst(found, replace);
            }
            System.out.println("replaced Optional: " + check + " : " + type_declarations);
        }
        return type_declarations;
    }

    public void printToFile(String generatedCode, String filename) throws IOException {

        File f = new File("src/main/java/newlyGeneratedCode", filename + ".java");

        if (!f.exists())//check if the file already exists
            f.createNewFile();
        FileUtils.writeStringToFile(f, generatedCode);
    }

}


