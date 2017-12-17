We have created a generator that creates syntactically correct but semantically meaningless Java application.
 
We have attempted to build a grammar based off on which we have programmed our Random Code Generator.

# Config #
 
#### ProgGen ####

/src/main/java/generator/ProgGen
ProgGen is JSON file that is configurable to the needs of what kind of a Random Code is required and helps us place the restriction on how much code is generated.

#### Config File ####
Config file is a Java class that obtains all values from the ProgGen JSON file.

#### CheckConfig File ####
CheckConfig file checks the number of import declarations, type declarations- classes and interfaces, method declarations etc that have been generated. Since the configuration values are obtained from the ProgGen file in Config file have been made static, they have only one copy and then they are decremented each time and code is generated until these values reach zero or terminals are reached in the code string that we are generating, whichever happens earlier.

## Building the Hash Map of production_rules: ##
We begin by storing the production rules into a hash map.
Each key of the hash map contains the left hand side of the grammar rules, which is a non-terminal of the production rule.
The right hand side of the production rule contains the set of corresponding possible states that the non-terminal can have.
These states/rules are stored in the value of the hash map.

### Initializing the Code with a starting set of Non-terminals: ###
We start off with a string that contains the non terminals that we would like to start off our code with:
String s = "<package declaration> <import declarations>  <type declarations> �

# Building blocks of Java Code: #
	### Generating Package Statements: ###
We use regex to look for non terminals in the string which are all enclosed by angular braces.
 < package declaration >
Each time, we check the Config file if we have exceeded the number of package declaration statements that we are allowed to declare. 
So, we replace package declaration by its corresponding production rules.
We update the replaced String and we recursively call generatePackage() with the parameter containing the updated string until we generate meaningful package statements.

    ### Generating Import Statements: ###
 We again use regex to look for non terminals in the string which are enclosed by angular braces.
 < import declaration >
Each time, we check the Config file if we have exceeded the number of import declaration statements that we are allowed to declare. And 
So, we once again replace <import declaration> statements by its corresponding declaration rules. 







