package javaapplication3;
import java.util.Scanner;
import java.math.*;
import java.util.*;

public class JavaApplication3 {
 ArrayList<String> contents;
    String item;
    JavaApplication3 check;
    
    public static void main(String[] args) {
          Scanner input = new Scanner(System.in);
        System.out.print("Input: ");
        String a = input.nextLine();
        JavaApplication3 go = new JavaApplication3();
        a = go.brackets(a);
        System.out.println(a);
    }
    public String brackets(String s){            
        check = new JavaApplication3();
        while(s.contains(Character.toString('('))||s.contains(Character.toString(')'))){
            for(int o=0; o<s.length();o++){
                try{                                                       
                    if((s.charAt(o)==')' || Character.isDigit(s.charAt(o))) 
                            && s.charAt(o+1)=='('){                         
                        s=s.substring(0,o+1)+"*"+(s.substring(o+1));       
                    }

                }catch (Exception ignored){}                               
                if(s.charAt(o)==')'){                                
                    for(int i=o; i>=0;i--){
                        if(s.charAt(i)=='('){                         
                            String in = s.substring(i+1,o);
                            in = check.recognize(in);
                            s=s.substring(0,i)+in+s.substring(o+1);
                            i=o=0;
                        }
                    }
                }
            }
            
            if(s.contains(Character.toString('('))||s.contains(Character.toString(')'))||
                    s.contains(Character.toString('('))||s.contains(Character.toString(')'))){
                System.out.println("Error: incorrect brackets placement");
                return "Error: incorrect brackets placement";
            }
        }
        s=check.recognize(s);
        return s;
    }
    public String recognize(String s){              
        PutIt putIt = new PutIt();
        contents = new ArrayList<String>();         
        item = "";
        for(int i=s.length()-1;i>=0;i--){           
            if(Character.isDigit(s.charAt(i))){    
                item=s.charAt(i)+item;              
                if(i==0){
                    putIt.put();
                }
            }else{
                if(s.charAt(i)=='.'){
                    item=s.charAt(i)+item;
                }else if(s.charAt(i)=='-' && (i==0 || (!Character.isDigit(s.charAt(i-1))))){
                    item=s.charAt(i)+item;          
                    putIt.put();                   
                }else{
                    putIt.put();                
                    item+=s.charAt(i);         
                    putIt.put();               
                if(s.charAt(i)=='|'){       
                        item+=" ";          
                        putIt.put();        
                    }
                }
            }
        }
        
        contents = putIt.result(contents, "^", "|");    
        
        contents = putIt.result(contents, "*", "/");   
        
        contents = putIt.result(contents, "+", "-");    
        
        return contents.get(0);
    }
    public class PutIt{
        public void put(){
            if(!item.equals("")){
                contents.add(0,item);
                item="";
            }
        }
        public ArrayList<String>result(ArrayList<String> arrayList, String op1, String op2){
            int scale = 10;                             
            BigDecimal result = new BigDecimal(0);
            for(int c = 0; c<arrayList.size();c++){
                if(arrayList.get(c).equals(op1)|| arrayList.get(c).equals(op2)){
                    if(arrayList.get(c).equals("^")){
                        result = new BigDecimal(arrayList.get(c-1)).pow(Integer.parseInt(arrayList.get(c+1)));
                    }else if(arrayList.get(c).equals("|")){
                        result = new BigDecimal(Math.sqrt(Double.parseDouble(arrayList.get(c+1))));
                    }else if(arrayList.get(c).equals("*")){
                        result = new BigDecimal(arrayList.get(c-1)).multiply
                                (new BigDecimal(arrayList.get(c+1)));
                    }else if(arrayList.get(c).equals("/")){
                        result = new BigDecimal(arrayList.get(c-1)).divide
                                (new BigDecimal(arrayList.get(c+1)),scale,BigDecimal.ROUND_DOWN);
                    }else if(arrayList.get(c).equals("+")){
                        result = new BigDecimal(arrayList.get(c-1)).add(new BigDecimal(arrayList.get(c+1)));
                    }else if(arrayList.get(c).equals("-")){
                        result = new BigDecimal(arrayList.get(c-1)).subtract(new BigDecimal(arrayList.get(c+1)));
                    }
                    try{       //in a case of to "out of range" ex
                        arrayList.set(c, (result.setScale(scale, RoundingMode.HALF_DOWN).
                                stripTrailingZeros().toPlainString()));
                        arrayList.remove(c + 1);            
                        arrayList.remove(c-1);             
                    }catch (Exception ignored){}
                }else{
                    continue;            
                }
                c=0;
                System.out.println(arrayList);
                //loop reset, as arrayList changed size
            }
             
            return arrayList;
        }
    }
}