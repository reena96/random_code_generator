package generator.elements;

import generator.JavaCodeGenerator;
import generator.NameGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MethodDeclaration extends State {

    public NameGenerator nameGenerator;
    public List<String> local_declaration_list = new ArrayList<>();
    public List<FieldDeclaration> complete_declaration_list = new ArrayList<>();
    public List<String> modifiers_list = new ArrayList<>();

    static SecureRandom r;

    static {
        try {
            r = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public MethodDeclaration(NameGenerator nameGenerator) {
        modifiers_list.add("public");
        modifiers_list.add("static");
        modifiers_list.add("private");
        modifiers_list.add("protected");
        this.nameGenerator = nameGenerator;
    }

    public String createMethod() {

        //List<String> complete_class = new ArrayList<>();
        String method_declration = "";
        String modifier = "";
        if (rollTheDice()) {
            modifier = modifiers_list.get(r.nextInt(modifiers_list.size()));
            modifiers.add(modifier);
            method_declration += modifier;
        }
        // CREATE A STRING : MODIFIER + CLASS + GENERATENAME
        method_declration += " void ";
        String method_name = nameGenerator.formIdentifierName();
        method_declration += method_name + "() {";
        name = method_name;
        if (!modifier.equals("static"))
            complete_declaration_list.addAll(parent.fieldDeclarations);
        local_declaration_list = createLocalItems();
        //for (String s : local_declaration_list) System.out.println("statement in method: "+method_name+" : "+s);
        String method_body = createMethodBody();


        List<FieldDeclaration> updated_local_list = complete_declaration_list;
        List<String> updated_local_dec_list = new ArrayList<>();
        updated_local_list.removeAll(parent.fieldDeclarations);

        for (FieldDeclaration fd : updated_local_list) {
            String field_item = fd.type + " " + fd.name;
            if (fd.type == "String") {
                if (!fd.assignment.isEmpty()) field_item += " = " + "\"" + fd.assignment + "\"" + " ;";
            } else {
                if (!fd.assignment.isEmpty()) field_item += " = " + fd.assignment + " ;";
            }
            System.out.println("$$$$$$$$: " + field_item);
            updated_local_dec_list.add(field_item);
        }

        for (String s : updated_local_dec_list) method_declration += s;
        method_declration += method_body;
        method_declration += " } ";
        return method_declration;
    }


    public List<String> createLocalItems() {

        List<String> local_declaration_list = new ArrayList<>();
        Random r = new Random();
        for (int i = 1; i < 5; i++) {
            if (rollTheDice()) {
                FieldDeclaration fd = new FieldDeclaration();
                fd.name = nameGenerator.formIdentifierName();
                fd.type = types_list.get(r.nextInt(types_list.size()));
                fd.assignment = getConstantList(fd.type);
                String field_item = fd.type + " " + fd.name;
                if (fd.type == "String") {
                    if (!fd.assignment.isEmpty()) field_item += " = " + "\"" + fd.assignment + "\"" + " ;";
                } else {
                    if (!fd.assignment.isEmpty()) field_item += " = " + fd.assignment + " ;";
                }
                //System.out.println(field_item);
                local_declaration_list.add(field_item);
                complete_declaration_list.add(fd);
            }
        }
        return local_declaration_list;
    }

    public String createMethodBody() {

        String block_statements = "<statements>";
        JavaCodeGenerator jcg = new JavaCodeGenerator();
        block_statements = jcg.generateClass(block_statements, complete_declaration_list, nameGenerator);
        //complete_declaration_list.addAll(jcg.updated_declaration_list);
        return block_statements;
    }
    public String createAbstractMethodDeclaration() {

        //List<String> complete_class = new ArrayList<>();
        String method_declration = "abstract ";
        if (rollTheDice()) {
            String modifier = modifiers_list.get(r.nextInt(modifiers_list.size()));
            modifiers.add(modifier);
            method_declration += modifier;
        }
        // CREATE A STRING : MODIFIER + INTERFACE + GENERATENAME
        method_declration += " void ";
        String method_name = nameGenerator.formIdentifierName();
        method_declration += method_name + "();";
        name = method_name;
        complete_declaration_list.addAll(parent.fieldDeclarations);

        return method_declration;
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

    boolean rollTheDice() {

        if (r.nextInt(100) > 50) return false;
        else return true;
    }
}
