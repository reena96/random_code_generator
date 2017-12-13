package generator;



import java.io.*;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {

    public static long maxNoOfImports;
    public static long maxNoOfTypes;
    public static long noOfInterfaces;
    public static long maxNoOfMethodsPerInterface;
    public static long noOfClasses;
    public static long maxAllowedMethodCalls;
    public static long maxValueForLoop;
    public static String allowIndirectRecursion;
    public static long maximumArraySize;
    public static long noOfInheritanceChains;
    public static long maxNestedIfs;
    public static long minNoOfClassFields;
    public static String allowArray;
    public static long maxInheritanceDepth;
    public static long maxNoOfParametersPerMethod;
    public static long minInheritanceDepth;
    public static long totalLOC;
    public static long maxInterfacesToImplement;
    public static long maxRecursionDepth;
    public static String classNamePrefix;
    public static long maxNoOfClassFields;
    public static long maxNoOfMethodsPerClass;
    public static Double recursionProbability;
    public static long minNoOfParametersPerMethod;
    public static long intMaxValue;
    JSONArray allowedTypesJSONArray;
    public static ArrayList<String> allowedTypesList;


    public Config() {
        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader("/Users/sharandec7/Desktop/vishalkumar_malli_guneshekaran_saisharan_nagulapalli_reenamary/src/main/java/generator/ProgGen"));

            JSONObject jsonObject = (JSONObject) obj;

            maxNoOfImports = (long) jsonObject.get("maxNoOfImports");
            maxNoOfTypes = (long) jsonObject.get("maxNoOfTypes");
            noOfInterfaces = (long) jsonObject.get("noOfInterfaces");
            maxNoOfMethodsPerInterface = (long) jsonObject.get("maxNoOfMethodsPerInterface");
            noOfClasses = (long)jsonObject.get("noOfClasses");
            maxAllowedMethodCalls = (long)jsonObject.get("maxAllowedMethodCalls");
            maxValueForLoop = (long)jsonObject.get("maxValueForLoop");
            allowIndirectRecursion = (String)jsonObject.get("allowIndirectRecursion");
            maximumArraySize = (long)jsonObject.get("maximumArraySize");
            noOfInheritanceChains = (long)jsonObject.get("noOfInheritanceChains");
            maxNestedIfs = (long)jsonObject.get("maxNestedIfs");
            minNoOfClassFields = (long)jsonObject.get("minNoOfClassFields");
            allowArray = (String)jsonObject.get("allowArray");
            maxInheritanceDepth = (long)jsonObject.get("maxInheritanceDepth");
            maxNoOfParametersPerMethod = (long)jsonObject.get("maxNoOfParametersPerMethod");
            minInheritanceDepth = (long)jsonObject.get("minInheritanceDepth");
            totalLOC = (long)jsonObject.get("totalLOC");
            maxInterfacesToImplement = (long)jsonObject.get("maxInterfacesToImplement");
            maxRecursionDepth = (long)jsonObject.get("maxRecursionDepth");
            classNamePrefix = (String) jsonObject.get("classNamePrefix");
            maxNoOfClassFields = (long)jsonObject.get("maxNoOfClassFields");
            maxNoOfMethodsPerClass = (long)jsonObject.get("maxNoOfMethodsPerClass");
            recursionProbability = (Double)jsonObject.get("recursionProbability");
            minNoOfParametersPerMethod = (long)jsonObject.get("minNoOfParametersPerMethod");
            intMaxValue = (long)jsonObject.get("intMaxValue");
            //allowedTypes JSON Array {"char","byte","short", "int","long", "float", "double", "String", "Object" }
            allowedTypesJSONArray = (JSONArray)jsonObject.get("allowedTypes");
            int j = 0;
            JSONArray allowedTypesJSON = (JSONArray)jsonObject.get("allowedTypes");

            allowedTypesList = new ArrayList<String>();
            if (allowedTypesJSON != null) {
                for (int i=0;i<allowedTypesJSON.size();i++){
                    allowedTypesList.add(allowedTypesJSON.get(i).toString());
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

/*
    public  long getNoOfInterfaces(){ return noOfInterfaces;}
    public  long getMaxNoOfMethodsPerInterface(){ return maxNoOfMethodsPerInterface;}
    public  long getNoOfClasses(){ return noOfClasses;}
    public  long getMaxAllowedMethodCalls(){ return maxAllowedMethodCalls;}
    public  long getMaxValueForLoop(){ return maxValueForLoop;}
    public  String getAllowIndirectRecursion(){ return allowIndirectRecursion;}
    public  long getMaximumArraySize(){ return maximumArraySize;}
    public  long getNoOfInheritanceChains(){ return noOfInheritanceChains;}
    public  long getMaxNestedIfs(){ return maxNestedIfs;}
    public  long getMinNoOfClassFields(){ return minNoOfClassFields;}
    public  String getAllowArray(){ return getAllowArray();}
    public  long getMaxInheritanceDepth(){ return maxInheritanceDepth;}
    public  long getMaxNoOfParametersPerMethod(){ return maxNoOfParametersPerMethod;}
    public  long getMinInheritanceDepth(){ return minInheritanceDepth;}
    public  long getTotalLOC(){ return totalLOC;}
    public  long getMaxInterfacesToImplement(){ return maxInterfacesToImplement;}
    public  long getMaxRecursionDepth(){ return maxRecursionDepth;}
    public  String getClassNamePrefix(){ return classNamePrefix;}
    public  long getMaxNoOfClassFields(){ return maxNoOfClassFields;}
    public  long getMaxNoOfMethodsPerClass(){ return maxNoOfMethodsPerClass;}
    public  Double getRecursionProbability(){ return getRecursionProbability();}
    public  long getMinNoOfParametersPerMethod(){ return minNoOfParametersPerMethod;}
    public  long getIntGetMaxValue(){ return intMaxValue;}



    public static void main(String[] args) {

        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader("/Users/reenamaryputhota/Desktop/vishalkumar_malli_guneshekaran_saisharan_nagulapalli_reenamary/src/main/java/generator/ProgGen"));

            JSONObject jsonObject = (JSONObject) obj;


            JSONArray allowedTypesJSON = (JSONArray)jsonObject.get("allowedTypes");

            ArrayList<String> listdata = new ArrayList<String>();
            if (allowedTypesJSON != null) {
                for (int i=0;i<allowedTypesJSON.size();i++){
                    listdata.add(allowedTypesJSON.get(i).toString());
                }
            }

            for(String u:listdata){
                System.out.println(u);
            }

            Config config = new Config();
            System.out.println(config.getNoOfClasses());


        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    */

}