import java.util.*;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {


        Map<String, Integer> solution = distinctCharactersCount(NAMES,"aa");

        System.out.println("solution="+solution);



    }


    private static final List<String> NAMES = Arrays.asList("aayanna","airriana","aayannay");



    public static Map<String, Integer> distinctCharactersCount(List<String> listString, String prefix){


        Map<String, Integer> MyMap = new HashMap<>();

        //1.  Identify the List of string starting with prefix => List<String>

        List<String>  validPrefixList  = listString.stream()
                .filter( x-> x.startsWith(prefix))
                .collect(Collectors.toList());

        //System.out.println("validPrefixList="+validPrefixList);


        //  2.  A) Convert the String to ArrayChar   B)count the duplicate ( object Set and count the difference)

        Set<String> distinctChar;

        for (String e : validPrefixList){

            distinctChar = new HashSet();

            char[] array = e.toCharArray();
            distinctChar.add(   e+""  );

            for(char mychar : array){
                distinctChar.add(mychar+"");
            }
            //System.out.println("distinctChar="+distinctChar);
            MyMap.put(e,distinctChar.size());
        }





        return MyMap;
    }

    // ['aayanna',3];






}
