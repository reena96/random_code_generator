package generator;

import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Random;

        import org.apache.commons.lang3.ArrayUtils;
        import org.apache.commons.lang3.ArrayUtils;

        import java.util.Random;

class NameGenerator {
    Config config = new Config();
    HashMap<String ,Integer> classNames = new HashMap<>();

    //long =config.getNoOfClasses();


    HashMap<String,Integer> methodNames = new HashMap<>();
    int noOfMethods = 10;


    String[] nouns = {
            "Factory", "Bean", "Wrapper", "Visitor", "Model", "Singleton",
            "Method", "Configuration", "Exception", "Error", "Property", "Value",
            "Identifier", "Attribute", "Authentication", "Policy", "Container",
            "Order", "Info", "Parameter", "Request", "Adapter", "Bridge",
            "Decorator", "Facade", "Proxy", "Worker",
            "Interpreter", "Iterator", "Observer",
            "State", "Strategy", "Template", "Comparator", "Clone", "Task",
            "Resolver", "Candidate", "Expression", "Predicate",
            "Thread", "Pool", "Descriptor", "Interceptor", "Definition",
            "Getter", "Setter", "Listener", "Proccesor", "Printer",
            "Prototype", "Composer", "Event", "Helper", "Utils",
            "Invocation", "Exporter", "Importer", "Serializer", "Callback",
            "Tag", "Context", "Mapping", "Advisor", "Filter", "Field", "Test",
            "Tests", "Connection", "Annotation", "Service", "Repository",
            "Stub", "Mock", "Instance", "Dispatcher", "Client", "Server",
            "Message", "Map", "List", "Collection", "Queue", "Manager",
            "Database", "Reponse", "Broadcaster",
            "Watcher", "Schema", "Mapper", "Publisher", "Consumer", "Producer"
    };

    String[] inWords = {
            "Composite", "Invalid", "Supported", "Focus", "Traversal", "Abstract",
            "Transformer", "Common", "Concrete", "Autowire", "Simple", "Aware",
            "Aspect", "Principal", "Driven", "Interruptible", "Batch",
            "Prepared", "Statement", "Remote", "Stateless", "Session",
            "Transaction", "Transactional", "Based", "Meta", "Data", "Jms",
            "Readable", "Literal", "Reflective", "Scope", "Multipart", "Xml",
            "Generic", "Interface", "Advisable", "Observable", "Identifiable",
            "Iterable", "Distributed", "Notification", "Failure", "Type",
            "Http", "Jdbc"};

    String[] verbs = { "begin", "initiate" , "generate" , "complete", "display", "print", "find", "replace", "process","compare","mutate","finalize","put","get","request"

    };


    String classWord = "Class";
    String methodWord = "method";
    String generatedClassName="";
    String generatedMethodName="";

    public String formClassName() {
            Random random = new Random();
            generatedClassName= generate(9, 1, classWord, nouns, inWords) + inWords[random.nextInt(5)];
            while (classNames.containsKey(generatedClassName)){
                generatedClassName= generate(9, 1, classWord, nouns, inWords) + inWords[random.nextInt(5)];
                //System.out.println("repeated AND therefore IGNORED!");
            }
            classNames.put(generatedClassName,0);

            return generatedClassName;
    }

    public String formMethodName() {

            Random random = new Random();
            generatedMethodName= generate(8, 1, methodWord, verbs, nouns) + inWords[random.nextInt(5)];
            while (classNames.containsKey(generatedMethodName) && noOfMethods!=0){
                generatedMethodName= generate(8, 1, methodWord, verbs, nouns) + inWords[random.nextInt(5)];
                //System.out.println("repeated AND therefore IGNORED!");
            }
            methodNames.put(generatedMethodName,0);
            return generatedMethodName;
    }

    public String generate(int min, int max, String word,String[] nouns, String[] inWords) {
        String[] allWords = ArrayUtils.addAll(nouns, inWords);
        String w;
        for (int i = 0, l = (int) (rand(min, max) - 1); i < l; i++) {

            do {
                w = allWords[(int) rand(0, nouns.length- 1)];
            } while (word.indexOf(w) != -1);
            word += w;
        }
        return nouns[(int) rand(0, nouns.length-1)];
        //return word;
    }

    public long rand(int min, int max) {
        return Math.round(Math.random() * (max - min)) + min;
    }

    public static void main(String[] args){
        NameGenerator nameGenerator = new NameGenerator();
        System.out.println(nameGenerator.formClassName());
        System.out.println(nameGenerator.formMethodName());


    }



}
