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

    String classbnf[] = {
            "<import declarations> ::= <import declaration> | <import declarations> <import declaration>",
            "<import declaration> ::= <single type import declaration> | <type import on demand declaration>",
            "<single type import declaration> ::= import <type import name> ;",
            "<type import on demand declaration> ::= import <package import name>.* ;",
            "<type import name> ::= <import identifier> | <package import name>.<import identifier>",
            "<package import name> ::= <import identifier> | <package import name>.<import identifier>",
            "<import identifier> ::= java|lang|util",


            "<type declarations> ::= <type declaration> | <type declarations> <type declaration>",
            "<type declaration> ::= <class declaration> |  ",
            "<class declaration> ::= <class modifiers>? class <type identifier> <super>? <interfaces>? <class body>",
            "<class modifiers> ::= <class modifier> | <class modifiers> <class modifier>",
            //"<class modifiers> ::= <class modifier> | <class modifiers> <class modifier>",
            //"<class modifier> ::= public | abstract | final",
            "<super> ::= extends <class type>",
            "<class type> ::= <type name>",
            "<type name> ::= <type identifier> | <package class name>.<type identifier>",
            "<package class name> ::= <type identifier> | <package name>.<type identifier>",
            "<interfaces> ::= implements <interface type list>",
            "<interface type list> ::= <interface type> | <interface type list> , <interface type>",
            "<class body> ::= { <class body declarations>? }",
            "<class body declarations> ::= <class body declaration> | <class body declarations> <class body declaration>",
            "<class body declaration> ::= <class member declaration> | <static initializer> | <constructor declaration>",
            "<type identifier> ::= A | B | RandomCodeGenerator"
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


            if (production_rules.get(found) != null && found!="<type declarations>") {

                ArrayList<String> rules_associated = production_rules.get(found);
                if (!rules_associated.isEmpty()) {
                    Random r = new Random();
                    String replace = rules_associated.get(r.nextInt(rules_associated.size()));
                    System.out.println("replace:" + replace);
                    if (found.equals("<import identifier>")) {
                        rules_associated.remove(replace);
                        for (String str : rules_associated) {
                            System.out.println("------------>>>>>>" + str);
                        }
                        production_rules.put(found, rules_associated);
                        import_declaration = import_declaration.replaceFirst(found, replace);
                        import_declaration = generateImport(import_declaration);

                    } else if (!found.equals("<import identifier>")) {
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
                    if (found.equals("<type identifier>")) {
                        rules_associated.remove(replace);
                    /*for (String str : rules_associated) {
                        System.out.println("------------>>>>>>" + str);
                    }*/
                        production_rules.put(found, rules_associated);
                        type_declarations = type_declarations.replaceFirst(found, replace);
                        type_declarations = generateClass(type_declarations).trim();
                    } else {
                        type_declarations = type_declarations.replaceFirst(found, replace).trim();
                        type_declarations = generateClass(type_declarations).trim();
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
        String got = "";
        s = s.replace("<package declaration>",jcg.generatePackage("<package declaration>"));
        s = s.replace("<import declarations>",jcg.generateImport("<import declarations>"));
        got = s.replace("<type declarations>",jcg.generateClass("<type declarations>"));
        //got = jcg.generateClass(s);
        got = got.replaceAll("\\?", "");
        System.out.println("\n\n\n\n\n\nGOT: " + got);
        File f = new File("/Users/sharandec7/Desktop/vishalkumar_malli_guneshekaran_saisharan_nagulapalli_reenamary/src/main/java", "B" + ".java");
        if (!f.exists())//check if the file already exists
            f.createNewFile();
        //FileUtils.writeStringToFile(f, got);
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