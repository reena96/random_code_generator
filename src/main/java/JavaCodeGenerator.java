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

    static int maxNoOfClasses = 1;
    static int maxNoOfInterfaces = 1;
    static int maxNoOfImports = 2;
    static int maxNoOfTypes = 2;

    String classbnf[] = {

            "<import declarations> ::= <import declaration>\n | <import declarations> <import declaration>",
            "<import declaration> ::= <single type import declaration> | <type import on demand declaration>",
            "<single type import declaration> ::= import <type import name> ;",
            "<type import on demand declaration> ::= import <package import name>.* ;",
            "<type import name> ::= <import identifier> | <package import name>.<import identifier>",
            "<package import name> ::= <import identifier> | <package import name>.<import identifier>",
            "<import identifier> ::= java|lang|util",

            //"<interface declaration> ::= <interface modifiers>? interface <identifier> <extends interfaces>? <interface body>",
            "<interface declaration> ::= <interface modifiers>? interface <interface type identifier> <interface body>",
            //"<interface modifiers> ::= <interface modifier> | <interface modifiers> <interface modifier>",
            "<interface modifiers> ::= <interface modifier>",
            "<interface modifier> ::= public | abstract",
            "<extends interfaces> ::= extends <interface type> | <extends interfaces> , <interface type>",
            "<interface body> ::= { <interface member declarations>? }",
            "<interface member declarations> ::= <interface member declaration> | <interface member declarations> <interface member declaration>",
            "<interface member declaration> ::= <constant declaration> | <abstract method declaration>",
            "<constant declaration> ::= <constant modifiers> <type> <variable declarator> ;",
            "<constant modifiers> ::= public | static | final",
            "<abstract method declaration>::= <abstract method modifiers>? <result type> <method declarator> <throws>? ;",
            "<abstract method modifiers> ::= <abstract method modifier> | <abstract method modifiers> <abstract method modifier>",
            "<abstract method modifier> ::= public | abstract",
            "<array initializer> ::= { <variable initializers>? , ? }",
            "<variable initializers> ::= <variable initializer> | <variable initializers> , <variable initializer>",
            "<variable initializer> ::= <expression> | <array initializer>",
            "<variable declarators> ::= <variable declarator> | <variable declarators> , <variable declarator>",
            "<variable declarator> ::= <variable declarator id> | <variable declarator id> = <variable initializer>",
            "<variable declarator id> ::= <identifier> | <variable declarator id> [ ]",
            "<variable initializer> ::= <expression> | <array initializer>",

            "<interface type identifier> ::= Interface1 | Interface2 | Interface3",

            "<type declarations> ::= <type declaration>\n | <type declarations> <type declaration>",
            "<type declaration> ::= <class declaration> |  <interface declaration>",
            //"<class declaration> ::= <class access specifiers>? <class modifiers>? class <type identifier> <super>? <interfaces>? <class body>",
            "<class declaration> ::= <class access specifiers>? <class modifiers>? class <class type identifier> <super>? <class body>",
            "<class modifiers> ::= <class modifier>",
            "<class access specifiers> ::= <class access specifier>",
            "<class access specifier> ::= public | private | protected ",
            "<class modifier> ::= abstract | final | static",
            "<super> ::= extends <class type>",
            "<class type> ::= <type name>",
            "<type name> ::= <type identifier> | <package class name>.<type identifier>",
            "<package class name> ::= <type identifier> | <package name>.<type identifier>",
            "<interfaces> ::= implements <interface type list>",
            "<interface type list> ::= <interface type> | <interface type list> , <interface type>",
            "<class body> ::= { \n<class body declarations> }",
            "<class body declarations> ::= <class body declaration> | <class body declarations> <class body declaration>",
            "<class body declaration> ::= <class member declaration> | <static initializer> | <constructor declaration>",

            "<class type identifier> ::= Class1 | Class2 | Class3",

            "<class member declaration> ::= <field declaration> | <method declaration>",
            "<static initializer> ::= static <block>",
            "<constructor declaration> ::= <constructor modifiers>? <constructor declarator> <throws>? <constructor body>",
            "<constructor modifiers> ::= <constructor modifier> | <constructor modifiers> <constructor modifier>",
            "<constructor modifier> ::= public | protected | private",
            "<constructor declarator> ::= <simple constructor name> ( <formal parameter list>? )",
            "<formal parameter list> ::= <formal parameter> | <formal parameter list> , <formal parameter>",
            "<formal parameter> ::= <type> <variable declarator id>",
            "<throws> ::= throws <class type list>",
            "<class type list> ::= <class type> | <class type list> , <class type>",
            "<constructor body> ::= { <explicit constructor invocation>? <block statements>? }",
            "<simple constructor name> ::= <constructor identifier>",

            "<constructor identifier> ::= ",

            "<method declaration> ::= <method header> <method body>",
            "<method header> ::= <method modifiers>? <result type> <method declarator> <throws>?",
            "<result type> ::= <type> | void",
            "<method modifiers> ::= <method modifier> | <method modifiers> <method modifier>",
            //"<method modifier> ::= public | protected | private | static | abstract | final | synchronized | native",
            "<method modifier> ::= public | protected | private | static | abstract | final | synchronized",
            "<method declarator> ::= <method identifier> ( <formal parameter list>? )",
            "<method body> ::= <block> ",
            "<block> ::= { <block statements>? }",

            "<method identifier> ::= method1 | method2 | method3 | method4 | method5",


            "<field declaration> ::= <field modifiers>? <type> <variable declarators> ;",
            "<field modifiers> ::= <field modifier> | <field modifiers> <field modifier>",
            "<field modifier> ::= public | protected | private | static | final | transient | volatile",

            "<type> ::= <primitive type> | <reference type>",
            "<primitive type> ::= <numeric type> | boolean",
            "<numeric type> ::= <integral type> | <floating-point type>",
            "<integral type> ::= byte | short | int | long | char",
            "<floating-point type> ::= float | double",
            "<reference type> ::= <class or interface type> | <array type>",
            "<class or interface type> ::= <class type> | <interface type>",
            "<class type> ::= <type name>",
            "<interface type> ::= <type name>",
            "<array type> ::= <type> [ ]",

            "<simple type name> ::= <identifier>",
            "<expression name> ::= <identifier> | <ambiguous name> . <identifier>",
            "<method name> ::= <identifier> | <ambiguous name>. <identifier>",
            "<ambiguous name>::= <identifier> | <ambiguous name>. <identifier>",

    };

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

    void randomGrammarPicker() {
        Random randomNumberGenerator = new Random();
        String grammarRule = classbnf[randomNumberGenerator.nextInt(5)];
        //System.out.println(randomNumberGenerator.nextInt(5) + " : " + grammarRule);
    }

    String generateImport(String import_declaration) throws ParseException {

        System.out.println("actual:" + import_declaration);
        String regex = "[<][a-z\\s]*[>]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(import_declaration);

        while (matcher.find()) {

            String found = matcher.group(0).trim();
            System.out.println("found:" + found);

            if (production_rules.get(found) != null && !found.equals("<type declarations>")) {

                ArrayList<String> rules_associated = production_rules.get(found);

                if (!rules_associated.isEmpty()) {

                    Random r = new Random();
                    String replace = rules_associated.get(r.nextInt(rules_associated.size()));

                    //check if allowed to replace and form
                    if (checkConfig(found, replace)) {

                        //if yes then replace
                        if (found.equals("<import identifier>")) {

                            System.out.println("replace:" + replace);

                            rules_associated.remove(replace);
                            for (String str : rules_associated) {
                                System.out.println(">>>>>>------------" + str);
                            }
                            production_rules.put(found, rules_associated);
                            import_declaration = import_declaration.replaceFirst(found, replace);
                            import_declaration = generateImport(import_declaration);

                        } else if (!found.equals("<import identifier>")) {
                            import_declaration = import_declaration.replaceFirst(found, replace);
                            import_declaration = generateImport(import_declaration);
                        }

                    } else {
                        import_declaration = import_declaration.replaceFirst(found, "");
                        import_declaration = generateImport(import_declaration);
                    }
                }

            }
        }
        return import_declaration;
    }

    private boolean checkConfig(String found, String replace) {

        switch (found) {
            case "<type declarations>":
                if (replace.contains("<type declaration>")) {
                    maxNoOfTypes = maxNoOfTypes - 1;
                    if (maxNoOfTypes < 0) {
                        return false;
                    } else return true;
                }
                break;
            case "<import declarations>":
                if (replace.contains("<import declaration>")) {
                    maxNoOfImports = maxNoOfImports - 1;
                    if (maxNoOfImports < 0) {
                        return false;
                    } else return true;
                }
                break;
            case "<type declaration>":
                if (replace.contains("<class declaration>")) {
                    maxNoOfClasses = maxNoOfClasses - 1;
                    if (maxNoOfClasses < 0) {
                        return false;
                    } else return true;
                } else if (replace.contains("<interface declaration>")) {
                    maxNoOfInterfaces = maxNoOfInterfaces - 1;
                    if (maxNoOfInterfaces < 0) {
                        return false;
                    } else return true;
                }
                break;
            default:
                return true;
        }
        return true;
    }

    String generateClass(String type_declarations) throws ParseException {

        System.out.println("actual:" + type_declarations);
        String regex = "[<][a-z\\s]*[>]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(type_declarations);

        while (matcher.find()) {

            String found = matcher.group(0).trim();
            System.out.println("found:" + found);

            if (production_rules.get(found) != null) {

                type_declarations = optionalCFG(type_declarations);
                ArrayList<String> rules_associated = production_rules.get(found);
                Random r = new Random();
                String replace = "";

                if (rules_associated.size() > 0) {

                    replace = rules_associated.get(r.nextInt(rules_associated.size())).trim();
                    System.out.println("replace: " + replace);

                    if (checkConfig(found, replace)) {
                        System.out.println(maxNoOfTypes);

                        if (found.equals("<interface type identifier>") || found.equals("<class type identifier>")) {

                            rules_associated.remove(replace);
                            /*for (String str : rules_associated) {
                                System.out.println("------------>>>>>>" + str);
                            }*/
                            production_rules.put(found, rules_associated);

                            //add class names to constructors
                            if (found.equals("<class type identifier>")) {
                                ArrayList<String> constructor_list = production_rules.get("<constructor identifier>");
                                constructor_list.add(replace);
                                for (String str : constructor_list) {
                                    System.out.println("------------>>>>>>" + str);
                                }
                                production_rules.put("<constructor identifier>", constructor_list);
                            }
                            type_declarations = type_declarations.replaceFirst(found, replace);
                            type_declarations = generateClass(type_declarations).trim();

                        } else {

                            type_declarations = type_declarations.replaceFirst(found, replace).trim();
                            type_declarations = generateClass(type_declarations).trim();
                        }
                    } else {
                        type_declarations = type_declarations.replaceFirst(found, "");
                        type_declarations = generateImport(type_declarations);
                    }
                }
            }
        }
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

    public static void main(String args[]) throws ParseException, IOException {

        JavaCodeGenerator jcg = new JavaCodeGenerator();
        jcg.createHM();
        jcg.randomGrammarPicker();
        String s = "<package declaration> <import declarations>  <type declarations> ";

        //s = "<type declarations>";

        s = s.replace("<package declaration>", jcg.generatePackage(s));
        s = jcg.generateImport(s);
        s = jcg.generateClass(s);
        //s = jcg.generateClassBody(s);

        s = s.replaceAll("\\?", "");
        s = s.replaceAll(";", ";\\\n");

        System.out.println("\n\n\nGENERATED CODE: " + s);

        String publicClassname = getFileNameFromCodeGenerated(s);
        System.out.println("\n\n\n\n\nFile Name: " + publicClassname);

        jcg.printToFile(s, publicClassname);
    }

    private static String getFileNameFromCodeGenerated(String generatedCode) {

        ArrayList<String> t = new ArrayList<String>();
        for (String r : generatedCode.split(" ")) {
            if (!r.trim().isEmpty()) {
                t.add(r.trim());
                System.out.println("TOKEN: " + r.trim());
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

        File f = new File("src/main/java", filename + ".java");

        if (!f.exists())//check if the file already exists
            f.createNewFile();
        FileUtils.writeStringToFile(f, generatedCode);
    }


    private String generateClassBody(String class_body_declaration) {

        return "";
    }

    private String generatePackage(String s) {
        return "";
    }

    /*
    public static void main(String args[]) {

        String regex = "[<][a-z]*[>][?]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("<abc> <abc>?");

        while (matcher.find()) {
            matcher.group();
        }
    }*/
}