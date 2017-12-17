package generator.elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class State {
    public List<String> types_list = new ArrayList<>();
    public List<Integer> integer_list = new ArrayList<>();
    public List<String> string_list = new ArrayList<>();
    public List<Boolean> boolean_list = new ArrayList<>();
    public String[] alphabets = {"a","b","c"};


    public State parent;
    public String name;
    public HashSet<String> modifiers;
    public List<FieldDeclaration> fieldDeclarations;
    public List<MethodDeclaration> methodDeclarations;

    public State() {
        types_list.add("int");
        types_list.add("String");
        types_list.add("boolean");

        for (int i=0; i<100; i++)
            integer_list.add(i);
        for (int i=0; i<3; i++)
            string_list.add(alphabets[i]);

        boolean_list.add(true);
        boolean_list.add(false);

        modifiers = new HashSet<>();
        fieldDeclarations = new ArrayList<>();
        methodDeclarations = new ArrayList<>();
    }
}