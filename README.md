We have created a generator that creates syntactically correct but semantically meaningless Java application.
We have attempted to build a grammar based off on which we have programmed our Random Code Generator.
Generating random code 

Existing Random Code Base generators: https://blog.takipi.com/java-bullshifier-generate-massive-random-code-bases/

## How to Run 
#### Run /src/main/java/generator/Main.java ####
#### The newly created classes and interfaces will be created in the package of /src/main/java/newlyGeneratedCode ####

## Config ##
 
#### ProgGen ####

/src/main/java/generator/ProgGen
ProgGen is JSON file that is configurable to the needs of what kind of a Random Code is required and helps us place the restriction on how much code is generated.

#### Config  ####
Config file is a Java class that obtains all values from the ProgGen JSON file.

#### CheckConfig ####
CheckConfig file checks the number of import declarations, type declarations- classes and interfaces, method declarations etc that have been generated. Since the configuration values are obtained from the ProgGen file in Config file have been made static, they have only one copy and then they are decremented each time and code is generated until these values reach zero or terminals are reached in the code string that we are generating, whichever happens earlier.

## BNF_Grammar ##
BNF_Grammar is a Java Class that contains a set of production rules that we store in order to use them everytime a non-terminal is encountered. 
BNF_Grammar contains rules defined for package declarations, import declarations, type declarations, method declarations, statements etc.

## 1) Building the Hash Map of production_rules: ##
We begin by storing the production rules into a hash map.
Each key of the hash map contains the left hand side of the grammar rules, which is a non-terminal of the production rule.
The right hand side of the production rule contains the set of corresponding possible states that the non-terminal can have.
These states/rules are stored in the value of the hash map.

## 2) Initializing the Code with a starting set of Non-terminals: ##
We start off with a string that contains the non terminals that we would like to start off our code with:
String s = "<package declaration> <import declarations> <type declarations> "

## 3) Building blocks of Java Code: ##
### (i) Generating Package Statements: ###
We use regex to look for non terminals in the string which are all enclosed by angular braces
Ex: < package declaration 
The non terminal in the production is replaced by its associated right hand side value associated with it. We recursively call generatePackage() with the parameter containing the updated string until we generate a meaningful package statement.

### (ii) Generating Import Statements: ###
 We again use regex to look for non terminals in the string which are enclosed by angular braces.
 < import declaration >
Each time, we check the Config file if we have exceeded the number of import declaration statements that we are allowed to declare. And 
So, we once again replace <import declaration> statements by its corresponding declaration rules. 
During the recursive calls, when we find an <import identifier>, we replace it by some or all of the imports as specified by the <import identifier> rule.
We keep checking for the number of allowed imports using the checkConfig()

Also, we ensure that an import declaration statement that has already been declared is not declared twice. We do so by deleting from the set of rules-
the set of imports that we have already generated in the code.

### (iii) Generating Classes ###
We have classes defined for each type of node- Class, Interface, Field Declaration and Method Declaration.
We also have a class called State which defines the current state of any node.
Like name of the class, parent class node, field declarations in the block, methods declarations, modifier set and lists of primitive constants that include int,boolean and String.
The whole sequence of generating classes is trigerred by the Main class.
/src/main/java/generator/Main.java
The Main.java, abides by the configuration values of the Config file with respect to the values of the number of classes, number of interfaces etc.
It makes a decision of "flipTheCoin()" to decide at each iteration of whether the class/interface should be created or not.
Then it invokes the methods which autogenerate classes and interfaces.
The whole flow of generating the Java code for the class deals with using the above mentioned classes which describe the nodes. 
The Class Node creates the Class Declaration, which includes the creation of Global variables. It then passes the control over to atiothe Method
Declaration node, which takes care of the creating the method declarations along with its local varibales.
The body of the method declaration is handled by the Java Code Generator, which is a BNF Grammar parser, involving a block of statements.
The critical part of the scopes of variables is maintained by the hierarchy of classes, State, Clazz declarations, MethodDeclarations and FieldDeclarations, 
which are connected to each other by the parent attribute of the object of type State.
The State attribute at each level of the hierarchy holds the global and local variables in the block of scope.
Using this, we generate and maintain the scope of variables.
