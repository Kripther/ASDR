import java.util.List;

public class ASDR implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        PROGRAM();

        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    //RETURN_STMT_OPC -> EXPRESSION | /e
    private void RETURN_STMT_OPC(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        }
    }
    //WHILE_STMT -> while (EXPRESSION) STATEMENT
    private void WHILE_STMT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            match(TipoToken.LEFT_PAREN);
            EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            STATEMENT();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'while' ");
        }
    }
    //BLOCK -> { DECLARATION }
    private void BLOCK(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            match(TipoToken.LEFT_BRACE);
            DECLARATION();
            match(TipoToken.RIGHT_BRACE);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '{' ");
        }
    }
    //EXPRESSION -> ASSIGNMENT
    private void EXPRESSION(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            ASSIGNMENT();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //ASSIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private void ASSIGNMENT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            LOGIC_OR();
       	 	ASSIGNMENT_OPC();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //ASSIGNMENT_OPC -> = EXPRESSION | /e
    private void ASSIGNMENT_OPC(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
       	 	EXPRESSION();
        }
    }
    //LOGIC_OR -> LOGIC_AND LOGIC_OR_2
    private void LOGIC_OR(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            LOGIC_AND();
       	 	LOGIC_OR_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2
    private void LOGIC_OR_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.OR){
        	match(TipoToken.OR);
            LOGIC_AND();
       	 	LOGIC_OR_2();
        }
    }
    //LOGIC_AND -> EQUALITY LOGIC_AND_2
    private void LOGIC_AND(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            EQUALITY();
       	 	LOGIC_AND_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 | /e
    private void LOGIC_AND_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.AND){
        	match(TipoToken.AND);
            EQUALITY();
       	 	LOGIC_AND_2();
        }
    }
    //EQUALITY -> COMPARISON EQUALITY_2
    private void EQUALITY(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            COMPARISON();
       	 	EQUALITY_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //EQUALITY_2 -> != COMPARISON EQUALITY_2 | == COMPARISON EQUALITY_2 | /e
    private void EQUALITY_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG_EQUAL){
        	match(TipoToken.BANG_EQUAL);
            COMPARISON();
       	 	EQUALITY_2();
        }
        else if(preanalisis.tipo == TipoToken.EQUAL_EQUAL){
        	match(TipoToken.EQUAL_EQUAL);
            COMPARISON();
       	 	EQUALITY_2();
        }
    }
    //COMPARISON -> TERM COMPARISON_2
    private void COMPARISON(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            TERM();
       	 	COMPARISON_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //COMPARISON_2 -> > TERM COMPARISON_2 | >= TERM COMPARISON_2 | < TERM COMPARISON_2 | <= TERM COMPARISON_2
    private void COMPARISON_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.GREATER){
        	match(TipoToken.GREATER);
            TERM();
       	 	COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.GREATER_EQUAL){
        	match(TipoToken.GREATER_EQUAL);
            TERM();
       	 	COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.LESS){
        	match(TipoToken.LESS);
            TERM();
       	 	COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.LESS_EQUAL){
        	match(TipoToken.LESS_EQUAL);
            TERM();
       	 	COMPARISON_2();
        }
    }
    //TERM -> FACTOR TERM_2
    private void TERM(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            FACTOR();
       	 	TERM_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }

    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error encontrado");
        }

    }

}
