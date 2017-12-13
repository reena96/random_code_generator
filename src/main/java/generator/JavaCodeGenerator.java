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
    static int maxNoOfClasses = 1;
    static int maxNoOfInterfaces = 1;
    static int maxNoOfImports = 2;
    static int maxNoOfTypes = 2;
    static String code = "";

    String classbnf[] = {

            "<package declaration> ::= package <package folder name>;",
            "<import declarations> ::= <import declaration>|<import declarations><import declaration>",
            "<import declaration> ::= <type import on demand declaration>",
            // "<single type import declaration> ::= import <type name> ;",
            "<type import on demand declaration> ::= import java.<import identifier>.*;",
            // "<type name> ::= <import identifier>|<package name>.<import identifier>",
            //"<package name> ::= <import identifier>",
            // "<package declaration> ::= package <package name> ;",
            "<import identifier> ::= lang|util|io|net",
            "<package folder name> ::= newlyGeneratedCode",


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
            "<abstract method declaration>::= <abstract method modifiers>? <result type> <method declarator> ;",
            //"<abstract method modifiers> ::= <abstract method modifier> | <abstract method modifiers> <abstract method modifier>",
            "<abstract method modifiers> ::= <abstract method modifier>",
            "<abstract method modifier> ::= public | abstract",

            "<array initializer> ::= { <variable initializers>? }",
            "<variable initializers> ::= <variable initializer> | <variable initializers> , <variable initializer>",
            "<variable initializer> ::= <expression> ",
            "<array variable initializer> ::= <array initializer>",
            "<variable declarators> ::= <variable declarator> | <variable declarators> , <variable declarator>",
            "<variable declarator> ::= <variable declarator id> | <variable declarator id> = <variable initializer> | <array variable declarator id> = <array variable initializer> | ",
            "<variable declarator id> ::= <identifier> ",
            "<array variable declarator id> ::= <identifier> [ ] ",


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
            "<type name> ::= <class type identifier>|<package class name>.<class type identifier>",
            "<package class name> ::= <class type identifier> | <package folder name>.<class type identifier>",

            "<interfaces> ::= implements <interface type list>",
            "<interface type list> ::= <interface type> | <interface type list> , <interface type>",
            "<class body> ::= { \n<class body declarations> }",
            "<class body declarations> ::= <class body declaration> | <class body declarations> <class body declaration>",
            //"<class body declaration> ::= <class member declaration> | <static initializer> | <constructor declaration>",
            "<class body declaration> ::= <class member declaration> | <constructor declaration>",

            "<class type identifier> ::= Class1 | Class2 | Class3",

            "<class member declaration> ::= <field declaration> | <method declaration>",
            "<static initializer> ::= static <block>",
            "<constructor declaration> ::= <constructor modifiers>? <constructor declarator> <constructor body>",
            "<constructor modifiers> ::= <constructor modifier> | <constructor modifiers> <constructor modifier>",
            "<constructor modifier> ::= public | protected | private",
            "<constructor declarator> ::= <simple constructor name> ( <formal parameter list>? )",
            "<formal parameter list> ::= <formal parameter> | <formal parameter list> , <formal parameter>",
            "<formal parameter> ::= <type> <variable declarator id>",
            //"<throws> ::= throws <class type list>",
            "<class type list> ::= <class type> | <class type list> , <class type>",
            "<constructor body> ::= { <block statements>? }",
            "<simple constructor name> ::= <constructor identifier>",

            "<constructor identifier> ::= ",

            "<method declaration> ::= <method header> <method body>",
            "<method header> ::= <method modifiers>? <result type> <method declarator>",
            "<result type> ::= <type> | void",
            "<method modifiers> ::= <method modifier> | <method modifiers> <method modifier>",
            //"<method modifier> ::= public | protected | private | static | abstract | final | synchronized | native",
            "<method modifier> ::= public | protected | private | static | abstract | final | synchronized",
            "<method declarator> ::= <method identifier> ( <formal parameter list>? )",
            "<method body> ::= <block> ",
            "<block> ::= { <block statements>? }",

            "<method identifier> ::= method1 | method2 | method3 | method4 | method5",

            "<block> ::= { <block statements>? }",
            "<block statements> ::= <block statement> | <block statements> <block statement>",
            "<block statement> ::= <local variable declaration statement> | <statement>",
            "<local variable declaration statement> ::= <local variable declaration> ;",
            "<local variable declaration> ::= <type> <variable declarators>",


            "<statement> ::= System.out.println(\"something is working, need to add statements\");",
            //"<statement> ::= <statement without trailing substatement> | <labeled statement> | <if then statement> | <if then else statement> | <while statement> | <for statement>",
            "<statement no short if> ::= <statement without trailing substatement> | <labeled statement no short if> | <if then else statement no short if> | <while statement no short if> | <for statement no short if>",
            "<statement without trailing substatement> ::= <block> | <empty statement> | <expression statement> | <switch statement> | <do statement> | <break statement> | <continue statement> | <return statement> ",
            "<empty statement> ::= ;",
            "<labeled statement> ::= <identifier> : <statement>",
            "<labeled statement no short if> ::= <identifier> : <statement no short if>",
            "<expression statement> ::= <statement expression> ;",
            "<statement expression> ::= <assignment> | <preincrement expression> | <postincrement expression> | <predecrement expression> | <postdecrement expression> | <method invocation> | <class instance creation expression>",

            "<field declaration> ::= <field modifiers>? <type> <variable declarators> ;",
            "<field modifiers> ::= <field modifier> | <field modifiers> <field modifier>",
            "<field modifier> ::= public | protected | private | static | final | transient | volatile",

            "<type> ::= <primitive type> | <reference type> | Object",
            "<primitive type> ::= <numeric type> | String | char | boolean",
            "<numeric type> ::= <integral type> | <floatingpoint type>",
            "<integral type> ::= byte | short | int | long | char",
            "<floatingpoint type> ::= float | double",
            "<reference type> ::= <class or interface type>",
            "<class or interface type> ::= <class type> | <interface type>",
            "<class type> ::= <type name>",
            "<interface type> ::= <type name>",
            //"<array type> ::= <type>[]",

            "<simple type name> ::= <identifier>",
            "<expression name> ::= <identifier> | <ambiguous name>.<identifier>",
            "<method name> ::= <identifier> | <ambiguous name>.<identifier>",
            "<ambiguous name>::= <identifier> | <ambiguous name>.<identifier>",
            "<identifier>::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z",

            "<if then statement>::= if ( <expression> ) <statement>",
            "<if then else statement>::= if ( <expression> ) <statement no short if> else <statement>",
            "<if then else statement no short if> ::= if ( <expression> ) <statement no short if> else <statement no short if>",
            /*
            "<switch statement> ::= switch ( <expression> ) <switch block>",
            "<switch block> ::= { <switch block statement groups>? <switch labels>? }",
            "<switch block statement groups> ::= <switch block statement group> | <switch block statement groups> <switch block statement group>",
            "<switch block statement group> ::= <switch labels> <block statements>",
            "<switch labels> ::= <switch label> | <switch labels> <switch label>",
            "<switch label> ::= case <constant expression> : | default :",
            */
            "<while statement> ::= while ( <expression> ) <statement>",
            "<while statement no short if> ::= while ( <expression> ) <statement no short if>",
            "<do statement> ::= do <statement> while ( <expression> ) ;",
            "<for statement> ::= for ( <for init>? ; <expression>? ; <for update>? ) <statement>",
            "<for statement no short if> ::= for ( <for init>? ; <expression>? ; <for update>? ) <statement no short if>",
            "<for init> ::= <statement expression list> | <local variable declaration>",
            "<for update> ::= <statement expression list>",
            "<statement expression list> ::= <statement expression> | <statement expression list> , <statement expression>",
            "<break statement> ::= break <identifier>? ;",
            "<continue statement> ::= continue <identifier>? ;",
            "<return statement> ::= return <expression>? ;",
            /*
            "<throws statement> ::= throw <expression> ;",
            "<synchronized statement> ::= synchronized ( <expression> ) <block>",
            "<try statement> ::= try <block> <catches> | try <block> <catches>? <finally>",
            "<catches> ::= <catch clause> | <catches> <catch clause>",
            "<catch clause> ::= catch ( <formal parameter> ) <block>",
            "<finally > ::= finally <block>",
            */
            "<constant expression> ::= <expression>",
            "<expression> ::= <assignment expression>",
            "<assignment expression> ::= <conditional expression> | <assignment>",
            "<assignment> ::= <left hand side> <assignment operator> <assignment expression>",
            "<left hand side> ::= <expression name> | <field access> | <array access>",
            "<assignment operator> ::= = | *= | /= | %= | += | -= | <<= | >>= | >>>= | &= | ^= | |=",
            "<conditional expression> ::= <conditional or expression> | <conditional or expression> ? <expression> : <conditional expression>",
            "<conditional or expression> ::= <conditional and expression> | <conditional or expression> || <conditional and expression>",
            "<conditional and expression> ::= <inclusive or expression> | <conditional and expression> && <inclusive or expression>",
            "<inclusive or expression> ::= <exclusive or expression> | <inclusive or expression> | <exclusive or expression>",
            "<exclusive or expression> ::= <and expression> | <exclusive or expression> ^ <and expression>",
            "<and expression> ::= <equality expression> | <and expression> & <equality expression>",
            "<equality expression> ::= <relational expression> | <equality expression> == <relational expression> | <equality expression> != <relational expression>",

            "<relational expression> ::= <shift expression> | <relational expression> < <shift expression> | <relational expressio> > <shift expression> | <relational expression> <= <shift expression> | <relational expression> >= <shift expression> | <relational expression> instanceof <reference type>",

            "<shift exression> ::= <additive expression> | <shift expression> << <additive expression> | <shift expression> >> <additive expression> | <shift expression> >>> <additive expression>",
            "<additive expression> ::= <multiplicative expression> | <additive expression> + <multiplicative expression> | <additive expression> - <multiplicative expression>",

            "<multiplicative expression> ::= <unary expression> | <multiplicative expression> * <unary expression> | <multiplicative expression> / <unary expression> | <multiplicative expression> % <unary expression>",
            "<cast expression> ::= ( <primitive type> ) <unary expression> | ( <reference type> ) <unary expression not plus minus>",
            "<unary expression> ::= <preincrement expression> | <predecrement expression> | + <unary expression> | - <unary expression> | <unary expression not plus minus>",
            "<predecrement expression> ::= -- <unary expression>",
            "<preincrement expression> ::= ++ <unary expression>",
            "<unary expression not plus minus> ::= <postfix expression> | ~ <unary expression> | ! <unary expression> | <cast expression>",
            "<postdecrement expression> ::= <postfix expression> --",
            "<postincrement expression> ::= <postfix expression> ++",
            "<postfix expression> ::= <primary> | <expression name> | <postincrement expression> | <postdecrement expression>",
            "<method invocation> ::= <method name> ( <argument list>? ) | <primary> . <identifier> ( <argument list>? ) | super . <identifier> ( <argument list>? )",
            "<field access> ::= <primary> . <identifier> | super . <identifier>",
            "<primary> ::= <primary no new array> | <array creation expression>",
            "<primary no new array> ::= <literal> | this | ( <expression> ) | <class instance creation expression> | <field access> | <method invocation> | <array access>",
            "<class instance creation expression> ::= new <class type> ( <argument list>? )",
            "<argument list> ::= <expression> | <argument list> , <expression>",
            "<array creation expression> ::= new <primitive type> <dim exprs> <dims>? | new <class or interface type> <dim exprs> <dims>?",
            "<dim exprs> ::= <dim expr> | <dim exprs> <dim expr>",
            "<dim expr> ::= [ <expression> ]",
            "<dims> ::= [ ] | <dims> [ ]",
            "<array access> ::= <expression name> [ <expression> ] | <primary no new array> [ <expression>]",
            "<package name> ::= <identifier> | <package name> . <identifier>",
            "<type name> ::= <identifier> | <package name> . <identifier>",
            "<simple type name> ::= <identifier>",
            "<expression name> ::= <identifier> | <ambiguous name> . <identifier>",
            "<method name> ::= <identifier> | <ambiguous name>. <identifier>",
            "<ambiguous name>::= <identifier> | <ambiguous name>. <identifier>",
            "<literal> ::= <integer literal> | <floating-point literal> | <boolean literal> | <character literal> | <string literal> | <null literal>",
            "<integer literal> ::= <decimal integer literal> | <hex integer literal> | <octal integer literal>",
            "<decimal integer literal> ::= <decimal numeral> <integer type suffix>?",
            "<hex integer literal> ::= <hex numeral> <integer type suffix>?",
            "<octal integer literal> ::= <octal numeral> <integer type suffix>?",
            "<integer type suffix> ::= l | L",
            "<decimal numeral> ::= 0 | <non zero digit> <digits>?",
            "<digits> ::= <digit> | <digits> <digit>",
            "<digit> ::= 0 | <non zero digit>",
            "<non zero digit> ::= 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9",
            "<hex numeral> ::= 0 x <hex digit> | 0 X <hex digit> | <hex numeral> <hex digit>",
            "<hex digit> ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | a | b | c | d | e | f | A | B | C | D | E | F",
            "<octal numeral> ::= 0 <octal digit> | <octal numeral> <octal digit>",
            "<octal digit> ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7",
            "<floating-point literal> ::= <digits> . <digits>? <exponent part>? <float type suffix>?",
            "<digits> ::= <exponent part>? <float type suffix>?",
            "<exponent part> ::= <exponent indicator> <signed integer>",
            "<exponent indicator> ::= e | E",
            "<signed integer> ::= <sign>? <digits>",
            "<sign> ::= + | -",
            "<float type suffix> ::= f | F | d | D",
            "<boolean literal> ::= true | false",
            "<character literal> ::= ' <single character> ' | ' <escape sequence> '",
            //"<single character> ::= <input character> except ' and \\",
            //"<string literal> ::= \" <string characters>?\" ",
            "<string characters> ::= <string character> | <string characters> <string character>",
            //"<string character> ::= <input character> except \" and \\ | <escape character>",
            //"<null literal> ::= null",
            "<keyword> ::= abstract | boolean | break | byte | case | catch | char | class | const | continue | default | do | double | else | extends | final | finally | float | for | goto | if | implements | import | instanceof | int | interface | long | native | new | package | private | protected | public | return | short | static | super | switch | synchronized | this | throw | transient | try | void | volatile | while",
            "<identifier> ::= sharan | x | y | z"
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
                    if (checkConfig(found, replace)) {

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
                    if (checkConfig(found, replace)) {

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


    private boolean checkConfig(String found, String replace) {


        switch (found) {
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
                    config.maxNoOfImports -= 1;
                    if (config.maxNoOfImports < 0) {
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

                    if (checkConfig(found, replace)) {

                        System.out.println(config.maxNoOfTypes);
                        if (found.equals("<interface type identifier>") || found.equals("<class type identifier>")) {

                            replace = nameGenerator.formClassName();
                            System.out.println("Class NAME GENERATED: " + replace);

                            /*rules_associated.remove(replace);
                            for (String str : rules_associated) {
                                System.out.println("------------>>>>>>" + str);
                            }
                            production_rules.put(found, rules_associated);*/

                            //add class names to constructors
                            if (found.equals("<class type identifier>")) {
                                ArrayList<String> constructor_list = production_rules.get("<constructor identifier>");
                                constructor_list.add(replace);
                                constructor_list.remove(0);
                                for (String str : constructor_list) {
                                    System.out.println("------------>>>>>>" + str);
                                }
                                production_rules.put("<constructor identifier>", constructor_list);
                            }


                            /*type_declarations = type_declarations.replaceFirst(found, replace).trim();
                            type_declarations = generateClass(type_declarations).trim();*/
                        }


                        if (found.equals("<method identifier>")) {
                            replace = nameGenerator.formMethodName();

                        }

                        type_declarations = type_declarations.replaceFirst(found, replace).trim();
                        type_declarations = generateClass(type_declarations).trim();

                    } else {
                        type_declarations = type_declarations.replaceFirst(found, "");
                        type_declarations = generateClass(type_declarations);
                    }
                }
            }
        }
        System.out.println("Constructor List: " + production_rules.get("<constructor identifier>"));

        return type_declarations;
    }


/*

    void generateClass(String type_declarations) throws ParseException {


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
                            *//*for (String str : rules_associated) {
                                System.out.println("------------>>>>>>" + str);
                            }*//*
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
                            type_declarations = type_declarations.replaceFirst(found, replace).trim();
                            code = type_declarations;
                            generateClass(type_declarations);//.trim();
                        }
                        type_declarations = type_declarations.replaceFirst(found, replace).trim();
                        code = type_declarations;
                        generateClass(type_declarations);//.trim();
                    } else {
                        type_declarations = type_declarations.replaceFirst(found, "");
                        code = type_declarations;
                        generateClass(type_declarations);
                    }
                }
            }
        }

    }*/

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

        //s = s.replace("<package declaration>", jcg.generatePackage(s));
        s = jcg.generatePackage(s);
        s = jcg.generateImport(s);
        s = jcg.generateClass(s);
        //s = jcg.generateClassBody(s);

        s = s.replaceAll("\\?", "");
        s = s.replaceAll(";", ";\\\n").replaceAll("\\{","{\\\n").replaceAll("}","\\\n}");

        System.out.println("\n\n\nGENERATED CODE: \n" + s);

        String publicClassname = getFileNameFromCodeGenerated(s);
        System.out.println("\n\n\n\n\nFile Name: " + publicClassname);

        jcg.printToFile(s, publicClassname);
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

        File f = new File("src/main/java", filename + ".java");

        if (!f.exists())//check if the file already exists
            f.createNewFile();
        FileUtils.writeStringToFile(f, generatedCode);
    }


    private String generateClassBody(String class_body_declaration) {

        return "";
    }

    /*private String generatePackage(String s) {
        return "";
    }*/

    /*
    public static void main(String args[]) {

        String regex = "[<][a-z]*[>][?]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("<abc> <abc>?");`

        while (matcher.find()) {
            matcher.group();
        }
    }*/
}