/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camran.atributos_sintetizados.UI;

/**
 *
 * @author camran1234
 */
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Precedencia{
    private ArrayList<String> arbol = new ArrayList();
    private ArrayList<String> declaraciones = new ArrayList();

    public void imprimir(String text){
        
        String path=text;
        try(BufferedWriter br = new BufferedWriter(new FileWriter(path))){
            //Reading file
            String string = graphvizCode();
            br.append(string);
            br.close();
        }catch(Throwable e){
            System.out.println("Error: "+e.getMessage());
            e.printStackTrace();
        }
        if(new File(path).getName().contains(".png")){
            executeGraphviz(new File(path), new File(path).getName());
        }else{
            executeGraphviz(new File(path), new File(path).getName()+".png");
        }
    }

    private void executeGraphviz(File file, String outPutName){
        try {
            ProcessBuilder pbuilder;
            pbuilder = new ProcessBuilder( "dot", "-Tpng",file.getAbsolutePath(), "-o",   file.getParent()+"/"+outPutName);
            pbuilder.redirectErrorStream( true );
            //Ejecuta el proceso
            pbuilder.start();
            
            System.out.println("\nSe creo tu imagen "+ outPutName);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error en executeGraphviz: "+ex.getMessage());
        }
    } 



    public String graphvizCode(){
        StringBuffer string = new StringBuffer();
        string.append("digraph g{\n");
        for(int index=declaraciones.size()-1; index>=0; index--){
            string.append(declaraciones.get(index));
        }
        for(int index=arbol.size()-1; index>=0; index--){
            string.append(arbol.get(index));
        }
        string.append("}");
        return string.toString();
        
    }

    // npi = notacion polaca inversa
    public void calcularArbol(ArrayList<String> npi){
        int numero=0;
        String operador="";
        String operadorPrevio="";
        int nivelProfundidad=0;
        String nombreOperador="";
        int flag=0;
        for (int index=0; index<npi.size(); index++){

            if(isNumber(npi.get(index))){
                numero++;
                if(numero==3){
                    numero--;
                }
            }else{
                if(npi.get(index).equalsIgnoreCase("+")){
                    nombreOperador = "suma"+(nivelProfundidad+1)+""+index;
                }else if(npi.get(index).equalsIgnoreCase("*")){
                    nombreOperador = "multiplicacion"+(nivelProfundidad+1)+""+index;
                }

                if(npi.get(index).equalsIgnoreCase("+")
                || npi.get(index).equalsIgnoreCase("*")){
                    if(numero==2){
                        String declaracionOperador = nombreOperador+" [label=\""+npi.get(index)+"\"];\n";
                        String declaracionNumeroRight = "num"+npi.get(index-1)+nivelProfundidad+""+(index-1)+" [label=\""+npi.get(index-1)+"\"];\n";
                        String declaracionNumeroLeft = "num"+npi.get(index-2)+nivelProfundidad+""+(index-2)+" [label=\""+npi.get(index-2)+"\"];\n";
                        declaraciones.add(declaracionOperador);
                        declaraciones.add(declaracionNumeroRight);
                        declaraciones.add(declaracionNumeroLeft);
                        String arbolRight = nombreOperador+" -> "+"num"+npi.get(index-1)+nivelProfundidad+""+(index-1)+";\n";
                        String arbolLeft = nombreOperador+" -> "+"num"+npi.get(index-2)+nivelProfundidad+""+(index-2)+";\n";
                        arbol.add(arbolLeft);
                        arbol.add(arbolRight);
                        operador = nombreOperador;
                        int actualIndex = index;
                        //no es nulo
                        if(index+1<npi.size()){
                            if(!isNumber(npi.get(index+1))){
                                index-=4;
                            }else{
                                index-=3;
                            }
                        }
                        //removemos los valores que ya ingresamos
                        npi.remove(actualIndex);
                        npi.remove(actualIndex-1);
                        npi.remove(actualIndex-2);

                        
                        if(flag==0){
                            operadorPrevio=operador;
                        }
                        flag++;
                    }else if(numero==1){
                        String declaracionOperador = nombreOperador + " [label=\""+npi.get(index)+"\"];\n";
                        String declaracionNumeroRight = "num"+npi.get(index-1)+nivelProfundidad+""+(index-1)+" [label=\""+npi.get(index-1)+"\"];\n";
                        declaraciones.add(declaracionOperador);
                        declaraciones.add(declaracionNumeroRight);
                        String arbolRight = nombreOperador+" -> "+operador+"\n";
                        String arbolLeft = nombreOperador +" -> "+"num"+npi.get(index-1)+nivelProfundidad+""+(index-1)+";\n";
                        arbol.add(arbolLeft);
                        arbol.add(arbolRight);
                        int actualIndex=index;
                        if(index+1<npi.size()){
                            if(!isNumber(npi.get(index+1))){
                                index-=3;
                            }else{
                                index-=2;
                            }
                        }
                        npi.remove(actualIndex);
                        npi.remove(actualIndex-1);
                        operador = nombreOperador;

                        //no es nulo
                        
                        if(npi.size()==2){
                            index-=1;
                        }
                         if(flag==0){
                            operadorPrevio=operador;
                        }
                        flag++;
                    }else{
                        String declaracionOperador = nombreOperador + " [label=\""+npi.get(index)+"\"];\n";
                        declaraciones.add(declaracionOperador);
                        String arbolRight = nombreOperador+" -> "+operador+"\n";
                        String arbolLeft = nombreOperador + " -> "+operadorPrevio+"\n";    
                        arbol.add(arbolRight);
                        int actualIndex=index;
                        npi.remove(actualIndex);
                        System.out.println(npi.size());
                        
                            if(flag!=0){
                                arbol.add(arbolLeft);
                                flag=0;
                            }
                        
                        operador = nombreOperador;
                        index-=2;
                    }
                    numero=0;
                    nivelProfundidad++;
                }else{
                    //Significa que encontro un id
                    numero++;
                }                
                if(index<0){
                    if(npi.size()==0){
                        break;
                    }else{
                        index=-1;
                    }
                    
                }
                
            }
        }
    }

    public boolean isNumber(String text){
        try{
            int number = Integer.parseInt(text);
            return true;
        }catch(Exception ex){
            return false;
        }


    }
}
