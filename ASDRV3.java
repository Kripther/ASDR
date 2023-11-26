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

    //TERM_2 -> - FACTOR TERM_2 | + FACTOR TERM_2 | /e
    private void TERM_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.MINUS){
        	match(TipoToken.MINUS);
            FACTOR();
       	 	TERM_2();
        }
        else if(preanalisis.tipo == TipoToken.PLUS){
        	match(TipoToken.PLUS);
            FACTOR();
       	 	TERM_2();
        }

    }
    //FACTOR -> UNARY FACTOR_2
    private void FACTOR(){
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
            UNARY();
       	 	FACTOR_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }

    }
    //FACTOR_2 -> / UNARY FACTOR_2 | * UNARY FACTOR_2 | /e
    private void FACTOR_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.SLASH){
        	match(TipoToken.SLASH);
            UNARY();
       	 	FACTOR_2();
        }
        else if(preanalisis.tipo == TipoToken.STAR){
        	match(TipoToken.STAR);
            UNARY();
       	 	FACTOR_2();
        }
    }
    //UNARY -> ! UNARY | - UNARY | CALL
    private void UNARY(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG){
        	match(TipoToken.BANG);
            UNARY();
        }
        else if(preanalisis.tipo == TipoToken.MINUS){
        	match(TipoToken.MINUS);
            UNARY();
        }
        else if(preanalisis.tipo == TipoToken.TRUE || 
        		preanalisis.tipo == TipoToken.FALSE || 
        		preanalisis.tipo == TipoToken.NULL || 
        		preanalisis.tipo == TipoToken.NUMBER || 
        		preanalisis.tipo == TipoToken.STRING || 
        		preanalisis.tipo == TipoToken.IDENTIFIER || 
        		preanalisis.tipo == TipoToken.LEFT_PAREN){
            CALL();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //CALL -> PRIMARY CALL_2
    private void CALL(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
        	PRIMARY();
        	CALL_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //CALL_2 -> ( ARGUMENTS_OPC ) CALL_2 | /e
    private void CALL_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
        	match(TipoToken.LEFT_PAREN);
        	ARGUMENTS_OPC();
        	match(TipoToken.RIGHT_PAREN);
        	CALL_2();
        }
    }
    //PRIMARY -> true | false | null | number | string | id | (EXPRESSION)
    private void PRIMARY(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.TRUE ){
        	match(TipoToken.TRUE);
        }
        else if(preanalisis.tipo == TipoToken.FALSE ){
        	match(TipoToken.FALSE);
        }
        else if(preanalisis.tipo == TipoToken.NULL ){
        	match(TipoToken.NULL);
        }
        else if(preanalisis.tipo == TipoToken.NUMBER ){
        	match(TipoToken.NUMBER);
        }
        else if(preanalisis.tipo == TipoToken.STRING){
        	match(TipoToken.STRING);
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFIER){
        	match(TipoToken.IDENTIFIER);
        }
        else if(preanalisis.tipo == TipoToken.LEFT_PAREN ){
        	match(TipoToken.LEFT_PAREN);
        	EXPRESSION();
        	match(TipoToken.RIGHT_PAREN);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //FUNCTION -> id ( PARAMETERS_OPC ) BLOCK
    private void FUNCTION(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IDENTIFIER ){
        	match(TipoToken.IDENTIFIER);
        	match(TipoToken.LEFT_PAREN);
        	PARAMETERS_OPC();
        	match(TipoToken.RIGHT_PAREN);
        	BLOCK();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'id' ");
        }
    }
    //FUNCTIONS -> FUN_DECL FUNCTIONS | /e
    private void FUNCTIONS(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.FUN ){
        	FUN_DECL();
        	FUNCTIONS();
        }
    }
    //PARAMETERS_OPC -> PARAMETERS | /e
    private void PARAMETERS_OPC(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IDENTIFIER ){
        	PARAMETERS();
        }
    }
    //PARAMETERS -> id PARAMETERS_2
    private void PARAMETERS(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IDENTIFIER ){
        	match(TipoToken.IDENTIFIER);
        	PARAMETERS_2();
        }
    }
    //PARAMETERS_2 -> , id PARAMETERS_2 | /e
    private void PARAMETERS_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.COMMA ){
        	match(TipoToken.COMMA);
        	match(TipoToken.IDENTIFIER);
        	PARAMETERS_2();
        }
    }
    //ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | /e
    private void ARGUMENTS_OPC(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
        	EXPRESSION();
        	ARGUMENTS();
        }
    }
    //ARGUMENTS -> , EXPRESSION ARGUMENTS | /e
    private void ARGUMENTS(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.COMMA){
        	match(TipoToken.COMMA);
        	EXPRESSION();
        	ARGUMENTS();
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
