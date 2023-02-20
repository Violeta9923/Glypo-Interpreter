package src;

import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Executor;

class DecodeInstruction {
    public String aux = new String("");
    public String decodeInstr = new String("");
    HashMap<String, BigInteger> decodeTable = new HashMap<>();
    public DecodeInstruction(String aux){
        this.aux = aux;
    }

    String decode(String aux) {
        int cont = 0;
        BigInteger nr = new BigInteger("0");
        for(int i = 0; i < 4; i++){
            if((decodeTable.containsKey(aux.substring(i, i + 1))) == false){
                nr = new BigInteger(cont + "");
                decodeTable.put(aux.substring(i, i + 1), nr);
                cont++;
            } else {
                continue;
            }
        }
        for(int i = 0; i < 4; i++){
            if(decodeTable.containsKey(aux.substring(i, i + 1))){
                decodeInstr = decodeInstr + ((decodeTable.get(aux.substring(i, i + 1))) + "");
            }
        }
        return decodeInstr;
    }
}

class CheckBalance {
    int check = -1;
    Stack<Character> stack = new Stack<Character>();
    String instructions = new String("");
    String currentInstruction = new String("");
    String aux = new String("");

    CheckBalance(String instructions){
        this.instructions = instructions;
    }

    int check(String instructions){
        int numberOfInstructions = instructions.length() / 4;
        for(int i = 0; i < numberOfInstructions; i++){
            currentInstruction = "";
            for(int j = 0; j < 4; j++){
                currentInstruction = currentInstruction + instructions.substring(i * 4 + j, i * 4 + j + 1);
            }
            if(currentInstruction.equals("0110")){
                stack.push('[');
            }
            if(currentInstruction.equals("0123")){
                if(stack.isEmpty()){
                    check = i;
                    return check;
                } else {
                    stack.pop();
                }
            }
        }
        if(stack.isEmpty() == false){
            check = numberOfInstructions;
        }
        return check;
    }
}

class ExecuteDecode{
    BigInteger a;
    BigInteger b;
    BigInteger c;
    BigInteger d;
    String instr = new String("");

    ExecuteDecode(BigInteger a, BigInteger b, BigInteger c, BigInteger d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    String decode(BigInteger a, BigInteger b, BigInteger c, BigInteger d) {
        HashMap<BigInteger, String> decodeTable = new HashMap<>();
        int count = 0;
        String aux = "0123";
        BigInteger nr = new BigInteger("0");

        if (!decodeTable.containsKey(a)) {
            decodeTable.put(a, aux.substring(count, count + 1));
            count++;
        }
        if (!decodeTable.containsKey(b)) {
            decodeTable.put(b, aux.substring(count, count + 1));
            count++;
        }
        if (!decodeTable.containsKey(c)) {
            decodeTable.put(c, aux.substring(count, count + 1));
            count++;
        }
        if (!decodeTable.containsKey(d)) {
            decodeTable.put(d, aux.substring(count, count + 1));
            count++;
        }

        instr = instr + (decodeTable.get(a) + "");
        instr = instr + (decodeTable.get(b) + "");
        instr = instr + (decodeTable.get(c) + "");
        instr = instr + (decodeTable.get(d) + "");
        return instr;
    }

}

public class Main {
    public static int lBracesNumber = 0;
    public static void main(String args[]){
        String fileName = args[0];
        int baseInt;
        //System.out.print(args.length);
        //Integer baseInt = Integer.parseInt(args[1]);
        //String base = args[1];
        if(args.length > 1){
            baseInt = Integer.parseInt(args[1]);
        } else {
            baseInt = 10;
        }
        String line = new String("");
        String instructions = new String("");
        String aux = new String("");
        String decodedInstructions = new String("");
        String decodeInstr = new String("");
        ArrayList<BigInteger> myStack= new ArrayList<BigInteger>();
        int numberOfInstructions = 0;
        String executeSpecialInstr = new String("");
        boolean executeInstructions = false;
        Scanner scannerObject = new Scanner(System.in);
        boolean findRBrace = false;
        Stack<Integer> bracesStack = new Stack<>();
        int rBracesNumber = 0;
        //BigInteger valStack = new BigInteger("0");
        Stack<BigInteger> valStack = new Stack<BigInteger>();

        //base = Integer.valueOf(args[1]);


        // citire fisier
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                instructions = instructions + line;

            }
            bufferedReader.close();
        } catch(FileNotFoundException exc) {
            ////System.out.println("Error : unable to open file '" + fileName + "'");
        } catch(IOException exc) {
            ////System.out.println("Error : unable to read file '" + fileName + "'");
        }
        ////System.out.println(instructions);

        for(int i = 0; i < instructions.length(); i++){
            int character = (int) instructions.charAt(i);
            if(character >= 33 && character <= 126){
                aux = aux + instructions.charAt(i);
            }
        }
        instructions = aux;
        ////System.out.println(instructions);


        if(instructions.length() % 4 != 0){
            int numError = instructions.length() / 4;
            System.err.println("Error:" + numError);
            System.exit(-1);
        } else {
            numberOfInstructions = instructions.length() / 4;
            ////System.out.println("Nr instructiuni " + numberOfInstructions);
        }


        for(int i = 0; i < numberOfInstructions; i++){
            //String instr = new String("");
            aux = "";
            decodeInstr = "";
            for(int j = 0; j < 4; j++){
                aux = aux + instructions.charAt(i * 4 + j);
            }
            DecodeInstruction decoder = new DecodeInstruction(aux);
            decodeInstr = decoder.decode(aux);
            decodedInstructions = decodedInstructions + decodeInstr;
        }
        //System.out.println("Instructiunea decodata " + decodedInstructions);

        CheckBalance checker = new CheckBalance(decodedInstructions);
        int balanced = checker.check(decodedInstructions);
        if(balanced != -1){
            System.err.println("Error:" + balanced);
            System.exit(-1);
        }

        // parcurgere instructiuni
        int i = 0;
        while(i < numberOfInstructions){
            aux = "";
            if(executeInstructions == false) {
                for (int j = 0; j < 4; j++) {
                    aux = aux + decodedInstructions.substring(i * 4 + j, i * 4 + j + 1);
                }
                if(findRBrace == true){
                    if(aux.equals("0110")){
                        lBracesNumber++;
                    }
                    //System.out.println("intrai");
                    if(aux.equals("0123")){
                        rBracesNumber++;
                        if(lBracesNumber > 0){
                            if(rBracesNumber <= lBracesNumber){
                                //System.out.println("L" + lBracesNumber);
                                //System.out.println(("R" + rBracesNumber));
                                i++;
                                continue;
                            } else {
                                findRBrace = false;
                                rBracesNumber = 0;
                                lBracesNumber = 0;
                            }
                        } else {
                            if(rBracesNumber != 0){
                                findRBrace = false;
                                rBracesNumber = 0;
                                lBracesNumber = 0;
                            }
                        }

                    } else {
                        i++;
                        continue;
                    }
                }
            } else {
                aux = executeSpecialInstr;
                executeInstructions = false;
                int character = (int) aux.charAt(0);
                if(character != 48){
                    System.err.println("Exception:" + i);
                    System.exit(-2);
                }
            }
            BigInteger numberAux;
            //System.out.println(myStack);
            switch (aux) {

                case "0000":
                    //System.out.println( "NOP");
                    break;
                case "0001":
                    //System.out.println( "INPUT");
                    String input = scannerObject.nextLine();
                    //System.out.println(input);
                    aux = "";

                    int character;
                    //boolean mul = false;
                    if(baseInt == 10) {
                        for(int idx = 1; idx < input.length(); idx++){
                            character = (int) input.charAt(idx);
                            if(character < 48 || character > 57){
                                System.err.println("Exception:" + i);
                                System.exit(-2);
                            }
                        }
                        if((int) input.charAt(0) != 45){
                            if ((int) input.charAt(0) < 48 || (int) input.charAt(0) > 57) {
                                System.err.println("Exception:" + i);
                                System.exit(-2);
                            }
                        }
                        character = (int) input.charAt(0);
                        if(character == 48 && input.length() > 1){
                            System.err.println("Exception:" + i);
                            System.exit(-2);
                        }
                        BigInteger number = new BigInteger(input);

                        myStack.add(myStack.size(), number);
                    } else {
                        System.out.println(baseInt);
                        String number = new BigInteger(input, baseInt).toString();
                        System.out.println(number);
                        myStack.add(new BigInteger(number));
                        System.out.println(number);
                    }



                    ////System.out.println(myStack);
                    break;
                case "0010":
                    //System.out.println( "ROT");
                    if(!myStack.isEmpty()){
                        numberAux = myStack.get(myStack.size() - 1);
                        myStack.remove(myStack.size() - 1);
                        myStack.add(0, numberAux);
                        ////System.out.println(myStack);
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }

                    break;
                case "0011":
                    //System.out.println( "SWAP");
                    if(myStack.size() >= 2){
                        numberAux = myStack.get(myStack.size() - 1);
                        myStack.set(myStack.size() - 1, myStack.get(myStack.size() - 2));
                        myStack.set(myStack.size() - 2, numberAux);
                        ////System.out.println(myStack);
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }
                    break;
                case "0012":
                    //System.out.println( "PUSH");
                    myStack.add(myStack.size(), new BigInteger("1"));
                    ////System.out.println(myStack);
                    break;
                case "0100":
                    //System.out.println( "RROT");
                    //////System.out.println(myStack.size());
                    if(!myStack.isEmpty()){
                        numberAux = myStack.get(0);
                        myStack.remove(0);
                        myStack.add(myStack.size(), numberAux);
                        ////System.out.println(myStack);
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }

                    break;
                case "0101":
                    //System.out.println( "DUP");
                    if(!myStack.isEmpty()){
                        numberAux = myStack.get(myStack.size() - 1);
                        myStack.add(myStack.size(), numberAux);
                        ////System.out.println(myStack);
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }
                    break;
                case "0102":
                    //System.out.println( "ADD");
                    if(myStack.size() >= 2){
                        numberAux = myStack.get(myStack.size() - 1);
                        numberAux = numberAux.add(myStack.get(myStack.size() - 2));
                        myStack.remove(myStack.size() - 1);
                        myStack.remove(myStack.size() - 1);
                        myStack.add(myStack.size(), numberAux);
                        ////System.out.println(myStack);
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }
                    break;
                case "0110":
                    //System.out.println( "L-BRACE");
                    if(executeInstructions == false){
                        if(!myStack.isEmpty()){
                            valStack.push(myStack.get(myStack.size() - 1));
                            //System.out.println(bracesStack);
                            //System.out.println(valStack);
                            if(!bracesStack.contains(i)){
                                bracesStack.push(i);
                            }
                            numberAux = myStack.get(myStack.size() - 1);
                            //System.out.println(bracesStack);
                            if(numberAux.equals(new BigInteger("0"))){
                                findRBrace = true;
                            }

                        } else {
                            System.err.println("Exception:" + i);
                            System.exit(-2);
                        }
                    }
                    break;
                case "0111":
                    //System.out.println( "OUTPUT");
                    if(!myStack.isEmpty()){
                        numberAux = myStack.get(myStack.size() - 1);
                        myStack.remove(myStack.size() - 1);
                        if(baseInt == 10){
                            System.out.println(numberAux);
                        } else {
                            System.out.println(numberAux.toString(baseInt).toUpperCase());
                        }

                        //System.out.println(numberAux);
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }
                    break;
                case "0112":
                    //System.out.println( "MULTIPLY");
                    if(myStack.size() >= 2){
                        numberAux = myStack.get(myStack.size() - 1);
                        numberAux = numberAux.multiply(myStack.get(myStack.size() - 2));
                        myStack.remove(myStack.size() - 1);
                        myStack.remove(myStack.size() - 1);
                        myStack.add(myStack.size(), numberAux);
                        ////System.out.println(myStack);
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }
                    break;
                case "0120":
                    //System.out.println( "EXECUTE");
                    if(myStack.size() >= 4){
                        BigInteger a = myStack.get(myStack.size() - 1);
                        myStack.remove(myStack.size() - 1);
                        BigInteger b = myStack.get(myStack.size() - 1);
                        myStack.remove(myStack.size() - 1);
                        BigInteger c = myStack.get(myStack.size() - 1);
                        myStack.remove(myStack.size() - 1);
                        BigInteger d = myStack.get(myStack.size() - 1);
                        myStack.remove(myStack.size() - 1);
                        ExecuteDecode dec = new ExecuteDecode(a, b, c, d);
                        executeSpecialInstr = dec.decode(a, b, c, d);

                        executeInstructions = true;
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }
                    break;
                case "0121":
                    //System.out.println( "NEGATE");
                    if(!myStack.isEmpty()){
                        numberAux = myStack.get(myStack.size() - 1);
                        myStack.set(myStack.size() - 1, numberAux.negate());
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }

                    ////System.out.println(myStack);
                    break;
                case "0122":
                    //System.out.println( "POP");
                    if(!myStack.isEmpty()){
                        myStack.remove(myStack.size() - 1);
                        ////System.out.println(myStack);
                    } else {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }
                    break;
                case "0123":
                    //System.out.println( "R-BRACE");
                    if(executeInstructions == false){
                        //System.out.println(valStack);
                        //System.out.println(bracesStack);
                        BigInteger auxNum = new BigInteger("0");
                        auxNum = valStack.pop();
                        if(auxNum.equals(new BigInteger("0"))){
                            bracesStack.pop();
                        } else {
                            i = bracesStack.pop();
                            bracesStack.push(i);
                            i--;
                        }
                        //System.out.println(bracesStack);

                    }
                    break;

            }
            if(executeInstructions == false){
                i++;
            }
        }
    }
}

