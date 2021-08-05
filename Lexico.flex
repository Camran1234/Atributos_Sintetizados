package camran.atributos_sintetizados.Parser;
import java_cup.runtime.*;
import static camran.atributos_sintetizados.Parser.sym.*;
%%

%class Lexico
%cup
%unicode
%line
%column
%public

%{

%}

/* Regular Expressions */
    LineTerminator = \r|\n|\r\n
    WhiteSpace     = {LineTerminator} | [ \t\f]
    Numbers = [0-9]+
    Id = [aA-zZ][aA-zZ|0-9|"$"|"_"]+
%%

    <YYINITIAL>{
        "*"         {
           System.out.println("*");
            return new Symbol(MULT, yyline+1, yycolumn+1, yytext());
                    }
        "+"         {
           System.out.println("+");
            return new Symbol(ADD, yyline+1, yycolumn+1, yytext());
                    }
        "("         {
           System.out.println("(");
            return new Symbol(OPEN_PARENTHESIS, yyline+1, yycolumn+1, yytext());
                    }
        ")"         {
           System.out.println(")");
            return new Symbol(CLOSE_PARENTHESIS, yyline+1, yycolumn+1, yytext());
                    }
        
        {Numbers}   {
           System.out.println("Number "+yytext());
            return new Symbol(NUMBER, yyline + 1, yycolumn +1, yytext());
                    }
        {Id}        {
           System.out.println("ID = "+yytext());
            return new Symbol(ID, yyline+1, yycolumn+1, yytext());
                    }
        
          
        {WhiteSpace} {/*ignore*/}
    }

    [^] {System.out.println("Error en el lexema se encontro: "+yytext());}