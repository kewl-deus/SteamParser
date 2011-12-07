
/*
 * This class is for scanning hl2 attack log
 */


/* example
L 10/03/2005 - 21:42:50: "Eric<8><BOT><CT>" attacked "Harold<11><BOT><TERRORIST>" with "m3" (damage "25") (damage_armor "0") (health "75") (armor "0") (hitgroup "stomach")
*/

import java_cup.runtime.*;

%%

%class HL2LogScanner
%implements HL2LogSymbols


%line
%column
%char
%cup
%cupdebug


%{
  StringBuffer string = new StringBuffer();
  
  private Symbol symbol(int type)
  {
    return new SymbolWithPosition(type, yyline+1, yycolumn+1, yychar);
  }

  private Symbol symbol(int type, Object value)
  {
    return new SymbolWithPosition(type, yyline+1, yycolumn+1, yychar, value);
  }
  
%}

/* main character classes */
LineTerminator = \r | \n | \r\n

WhiteSpace = {LineTerminator} | [ \t\f ] | " "


/* numbers */
Number = [0-9]*

/* date */
Date = {Number} "/" {Number} "/" {Number}

/* time */
Time = {Number} ":" {Number} ":" {Number} ":"

/* string and character literals */
StringCharacter = [^\r\n\"\\]

%state STRING


%%

<YYINITIAL> {


  /* keywords */
  "attacked"                       { return symbol(ATTACKED); }
  "with"                        { return symbol(WITH); }
  "damage"                        { return symbol(DAMAGE); }
  "damage_armor"                        { return symbol(DAMAGE_ARMOR); }
  "health"                        { return symbol(HEALTH); }
  "armor"                        { return symbol(ARMOR); }
  "hitgroup"                        { return symbol(HITGROUP); }
  "killed"                        { return symbol(KILLED); }
  "headshot"                        { return symbol(HEADSHOT); }
    
  /* separators */
  "("                            { return symbol(OPEN_BRACKET); }
  ")"                            { return symbol(CLOSE_BRACKET); }

  "L"				{ /* ignore */ }
  "-"				{ /* ignore */ }
  
  /* string literal */
  \"                             { yybegin(STRING); string.setLength(0); }
   
  /* numeric literals */
  {Number}            		 { return symbol(NUMBER, new Integer(yytext())); }
  
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
  

 /* date & time */
  {Date}		 { return symbol(DATE, yytext()); }
  {Time}		 { return symbol(TIME, yytext()); }
  
}

<STRING> {
  \"                             { yybegin(YYINITIAL); return symbol(STRING_LITERAL, string.toString()); }
  
  {StringCharacter}+             { string.append( yytext() ); }
  
  /* escape sequences */
  "\\b"                          { string.append( '\b' ); }
  "\\t"                          { string.append( '\t' ); }
  "\\n"                          { string.append( '\n' ); }
  "\\f"                          { string.append( '\f' ); }
  "\\r"                          { string.append( '\r' ); }
  "\\\""                         { string.append( '\"' ); }
  "\\'"                          { string.append( '\'' ); }
  "\\\\"                         { string.append( '\\' ); }
  
  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
}

/* error fallback */
.|\n                             { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+yyline+", column "+yycolumn); }
<<EOF>>                          { return symbol(EOF); }