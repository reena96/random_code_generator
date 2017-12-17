package generator.elements;

import generator.NameGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Type extends State {
    NameGenerator nameGenerator = new NameGenerator();
    List<String> field_declaration_list = new ArrayList<>();
    List<String> constructor_declaration_list = new ArrayList<>();
    List<String> method_declaration_list = new ArrayList<>();
    public List<String> modifiers_list = new ArrayList<>();

    public List<MethodDeclaration> method_list;

    static SecureRandom r;

    static {
        try {
            r = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public Type() {
        modifiers_list.add("public");
        modifiers_list.add("abstract");
    }

    // METHODS REQUIRED FOR CREATING CLASS:

    public String createClassLine() {

        List<String> complete_class = new ArrayList<>();
        String class_declration = "";
        if (rollTheDice()) {
            String modifier = modifiers_list.get(r.nextInt(modifiers_list.size()));
            modifiers.add(modifier);
            class_declration += modifier;
        }
        // CREATE A STRING : MODIFIER + CLASS + GENERATENAME
        class_declration += " class ";
        String class_name = nameGenerator.formClassName();
        name = class_name;
        parent = this;
        class_declration += class_name + " { ";
        //System.out.println(class_declration);
        field_declaration_list = createFieldItems();
        for (String s : field_declaration_list) class_declration += s;
        constructor_declaration_list = createConstructor();
        for (String s : constructor_declaration_list) class_declration += s;
        method_declaration_list = createMethodItems();
        for (String s : method_declaration_list) class_declration += s;
        class_declration += "}\n";
        System.out.println(class_declration);

        String regex = "[<][a-z\\s]*[>]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(class_declration);

        while (matcher.find()) {

            String found = matcher.group().trim();
            System.out.println("found in last:" + found);
            class_declration.replace(found, "");
        }

        System.out.println(class_declration);
        return class_declration;
    }

    public List<String> createConstructor() {
        List<String> constructor_declaration_list = new ArrayList<>();
        String constructor_declaration = "public " + name + "() {";
        constructor_declaration_list.add(constructor_declaration);
        List<String> constructor_body = createConstructorBody();
        for (String s : constructor_body) constructor_declaration_list.add(s);
        constructor_declaration_list.add("} ");
        return constructor_declaration_list;
    }

    public List<String> createConstructorBody() {
        List<String> constructor_statements = new ArrayList<>();
        String constructor_body = "";
        for (FieldDeclaration fd : fieldDeclarations) {
            constructor_body = fd.name;
            if (fd.type == "String") {
                if (!fd.assignment.isEmpty()) constructor_body += " = " + "\"" + fd.assignment + "\"" + " ; ";
            } else {
                if (!fd.assignment.isEmpty()) constructor_body += " = " + fd.assignment + " ; ";
            }
            constructor_statements.add(constructor_body);
        }
        return constructor_statements;
    }

    public List<String> createFieldItems() {

        List<String> field_declaration_list = new ArrayList<>();
        Random r = new Random();
        for (int i = 1; i < 5; i++) {
            if (rollTheDice()) {
                FieldDeclaration fd = new FieldDeclaration();
                fd.name = nameGenerator.formIdentifierName();
                fd.type = types_list.get(r.nextInt(types_list.size()));
                fd.assignment = getConstantList(fd.type);
                String field_item = fd.type + " " + fd.name;
                if (fd.type == "String") {
                    if (!fd.assignment.isEmpty()) field_item += " = " + "\"" + fd.assignment + "\"" + " ; ";
                } else {
                    if (!fd.assignment.isEmpty()) field_item += " = " + fd.assignment + " ; ";
                }
                //System.out.println(field_item);
                field_declaration_list.add(field_item);
                fieldDeclarations.add(fd);
            }
        }
        return field_declaration_list;
    }

    public List<String> createMethodItems() {

        List<String> method_declaration_list = new ArrayList();
        for (int i = 1; i < 5; i++) {
            if (rollTheDice()) {
                MethodDeclaration md = new MethodDeclaration(nameGenerator);
                md.parent = this;
                method_declaration_list.add(md.createMethod());
            }
        }
        return method_declaration_list;
    }

    /*
    public List<String> createClassMethodItems() {

        List<String> method_declaration_list = new ArrayList();
        for (int i = 1; i < 5; i++) {
            if (rollTheDice()) {
                MethodDeclaration md = new MethodDeclaration(nameGenerator);
                md.parent = this;
                method_declaration_list.add(md.createMethod());
            }
        }
        return method_declaration_list;
    }*/
    // METHODS REQUIRED FOR CREATING INTERFACE:

    public String createInterfaceLine() throws ParseException {

        List<String> complete_class = new ArrayList<>();
        String interface_declaration = "";
        if (rollTheDice()) {
            String modifier = modifiers_list.get(r.nextInt(modifiers_list.size()));
            modifiers.add(modifier);
            interface_declaration += modifier;
        }
        // CREATE A STRING : MODIFIER + CLASS + GENERATENAME
        interface_declaration += " interface ";
        String interface_name = nameGenerator.formClassName();
        name = interface_name;
        parent = this;
        interface_declaration += interface_name + " { ";
        //System.out.println(class_declaration);
        field_declaration_list = createFieldItems();
        for (String s : field_declaration_list) interface_declaration += s;
        method_declaration_list = createInterfaceMethodItems();
        for (String s : method_declaration_list) interface_declaration += s;
        interface_declaration += "}\n";
        System.out.println(interface_declaration);
        return interface_declaration;
    }


    public List<String> createInterfaceMethodItems() throws ParseException {

        List<String> method_declaration_list = new ArrayList();
        for (int i = 1; i < 5; i++) {
            if (rollTheDice()) {
                MethodDeclaration md = new MethodDeclaration(nameGenerator);
                md.parent = this;
                method_declaration_list.add(md.createAbstractMethodDeclaration());
            }
        }
        return method_declaration_list;
    }

    public String getConstantList(String type) {

        List<?> identifier_list;
        switch (type) {
            case "int":
                identifier_list = integer_list;
                break;
            case "String":
                identifier_list = string_list;
                break;
            case "boolean":
                identifier_list = boolean_list;
                break;
            default:
                identifier_list = null;
        }
        if (!identifier_list.isEmpty())
            return identifier_list.get(r.nextInt(identifier_list.size())).toString();
        else
            return "";

        // return identifier_list.get(r.nextInt(identifier_list.size())).toString();
        // return identifier_list.get(0).toString();
    }

    public boolean rollTheDice() {

        if (r.nextInt(100) > 50) return false;
        else return true;
    }

    public static void main(String[] args) throws ParseException {
        Type typeObject = new Type();
        typeObject.createClassLine();
        typeObject.createInterfaceLine();

    }
}