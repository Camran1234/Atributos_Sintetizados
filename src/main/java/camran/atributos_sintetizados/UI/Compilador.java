/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camran.atributos_sintetizados.UI;

import java.io.StringReader;
import camran.atributos_sintetizados.Parser.*;
import java.util.ArrayList;
/**
 *
 * @author camran1234
 */
public class Compilador {
    int result = 0;
    ArrayList<String> lista = new ArrayList();
    public String calcular(String entrada){        
        try {
            StringReader string = new StringReader(entrada);
            Lexico lexic = new Lexico(string);
            parser parser = new parser (lexic);
            parser.parse();
            String resultado = "";
            ArrayList<String> list = parser.getList();
            this.lista = list;
            for(int index=0; index<list.size(); index++){
                resultado += list.get(index);
            }
            calcularResultado(list);
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR"+e.getMessage();
        }
    }
    
    public int getRespuesta(){
        return result;
    }
    
    public void calcularResultado(ArrayList<String> lista){
        ArrayList<String> auxiliar = new ArrayList();
        
        
        for(int index=0; index<lista.size(); index++){
            String simbolo = lista.get(index);
            if(!isNumber(simbolo) && !simbolo.equalsIgnoreCase("+")
                    && !simbolo.equalsIgnoreCase("*")){
                simbolo = "0";
            }
            auxiliar.add(simbolo);
            if(!isNumber(simbolo)){
                if(simbolo.equalsIgnoreCase("+")){
                    int actual = auxiliar.size();
                    int numero1 = Integer.parseInt(auxiliar.get(actual-2));
                    int numero2 = Integer.parseInt(auxiliar.get(actual-3));
                    int numero = numero1 + numero2;
                    result = numero;
                    
                    auxiliar.remove(actual-1);
                    auxiliar.remove(actual-2);
                    auxiliar.remove(actual-3);
                    auxiliar.add(Integer.toString(numero));
                    System.out.println(numero);
                }else if(simbolo.equalsIgnoreCase("*")){
                    int actual = auxiliar.size();
                    int numero1 = Integer.parseInt(auxiliar.get(actual-2));
                    int numero2 = Integer.parseInt(auxiliar.get(actual-3));
                    int numero = numero1 * numero2;
                    result = numero;
                    
                    auxiliar.remove(actual-1);
                    auxiliar.remove(actual-2);
                    auxiliar.remove(actual-3);
                    auxiliar.add(Integer.toString(numero));
                    System.out.println(numero);
                }
                
            }
        }
        System.out.println("Hola");
    }
    //Here goes the code to create the
    
    
    public boolean isNumber(String string){
        try {
            int number = Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    
}
