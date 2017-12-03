import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCodeGenerator {

    String classbnf[] = {
            "<type declarations> ::= <type declaration> | <type declarations> <type declaration>",
            "<type declaration> ::= <class declaration> | <interface declaration> | ;",
            "<class declaration> ::= <class modifiers>? class <identifier> <super>? <interfaces>? <class body>",
            "<class modifiers> ::= <class modifier> | <class modifiers> <class modifier>",
            "<class modifier> ::= public | abstract | final",
            "<super> ::= extends <class type>",
            "<interfaces> ::= implements <interface type list>",
            "<interface type list> ::= <interface type> | <interface type list> , <interface type>",
            "<class body> ::= { <class body declarations>? }",
            "<class body declarations> ::= <class body declaration> | <class body declarations> <class body declaration>",
            "<class body declaration> ::= <class member declaration> | <static initializer> | <constructor declaration>"
    };
    HashMap<String, ArrayList<String>> production_rules = new HashMap<>();


    void createHM() {
        for (String rule : classbnf) {
            String rules = rule.split("::=")[1];
            ArrayList<String> t = new ArrayList<>();
            for (String r : rules.split("\\|")) {
                t.add(r);
            }
            production_rules.put(rule.split("::=")[0], t);
        }
        for (String name : production_rules.keySet()) {

            String key = name.toString();
            String value = production_rules.get(name).toString();
            System.out.println("key : " + key + " value :" + value);
        }

    }

    void randomGrammarPicker() {
        Random randomNumberGenerator = new Random();
        String grammarRule = classbnf[randomNumberGenerator.nextInt(5)];
        System.out.println(randomNumberGenerator.nextInt(5) + " : " + grammarRule);
    }

    File createNewFile() {
        return null;
    }

    String generateClass(String type_declarations) throws ParseException {

        //String type_declarations = classbnf[0];
        System.out.println(type_declarations);
        Pattern pattern = Pattern.compile("<(.*?)>");
        Matcher matcher = pattern.matcher(type_declarations);

        System.out.println(matcher.find());

        while (matcher.find()) {
            String found = matcher.group(0);
            System.out.println(found);
            ArrayList<String> rules_associated = production_rules.get(found);
            Random r = new Random();
            //String s = rules_associated.get(r.nextInt(rules_associated.size()));
            System.out.println(rules_associated);
        }
        return "";
    }

    public static void main(String args[]) throws ParseException {
        JavaCodeGenerator jcg = new JavaCodeGenerator();
        jcg.createHM();
        jcg.randomGrammarPicker();
        jcg.generateClass(jcg.classbnf[0]);

    }
}

/*
import <identifier> . <identifier>. <identifier> . * ;

 */
