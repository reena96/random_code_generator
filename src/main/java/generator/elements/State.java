package generator.elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class State {

    public State parent;
    public String name;
    public HashSet<String> modifiers;
    public List<FieldDeclaration> fieldDeclarations;
    public List<MethodDeclaration> methodDeclarations;

    public State() {
        modifiers = new HashSet<>();
        fieldDeclarations = new ArrayList<>();
        methodDeclarations = new ArrayList<>();
    }



}
