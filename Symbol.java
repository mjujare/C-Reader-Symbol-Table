import java.util.Scanner;
import java.io.File;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;

public class Symbol
{

	static Symbol SymbolTable[] = new Symbol[100]; // initializes an array of symbols
	static int STsize = 0; // initialization of size of symbol table

    // instance variables

	int lineNumber;
   	String name;
	String datatype;
	String scope;
	String value;

    /**
     * Constructor for objects of class Symbol
     */
    public Symbol(int lineNumber, String name, String datatype, String scope, String value)
    {
		this.lineNumber = lineNumber;
		this.name = name;
		this.datatype = datatype;
		this.scope = scope;
		this.value = value;
    }

// creates symbol table

public static void createSymbolTable() throws FileNotFoundException
{
		
	File file = new File("input.txt"); // Creates file that takes input.txt as the argument (the file to read)
	Scanner Fin = new Scanner(file);
	String scope = ""; // initialization of scope
	int lineNumber = 0; // initialization of line number
	int curlyBrace = 0; // initialization of curly brace (to keep track of when it appears in the specific sample of code provided in input.txt
	while (Fin.hasNextLine()) { // while input.txt has next line
		String line = Fin.nextLine(); // set line to the next line
		lineNumber++; // increment line number

		// System.out.println("");

		String name = ""; // initialization of name
		String datatype = ""; // initialization of datatype
		String value = ""; // initialization of value

		StringTokenizer st = new StringTokenizer(line); // initialize StringTokenizer to tokenize each line in input.txt
		String prevToken = ""; // before reading the very first token in the very first line of input.txt, token is null
		while (st.hasMoreTokens()) { // while there are more tokens
			String token = st.nextToken(); // set token to the next token

			// System.out.println(lineNumber + " " + token);


			if (token.equals("int") || token.equals("char") || token.equals("void")) // if token is set to any of these data types
				datatype = token; // the respective datatype is set to token
			else if (token.equals("{")) { // if token is set to an open curly brace
				curlyBrace++; // increment curly brace
				scope = name; // scope subject to whichever symbol (name) is present after open curly brace
			}
			else if (token.equals("}")) { // if token is set to closing curly brace
				curlyBrace--; // decrement curly brace
				if (curlyBrace == 0) // if no curly brace exists
					scope = ""; // then scope set to no symbol (name)
			}
			else if (token.matches("\\p{Alpha}+\\p{Alnum}*")) {
				name = token; // name is symbol which is a in this case
				// System.out.println("name = " + name);
			}
			else if (token.equals("="))
				prevToken = token;
			else if (token.matches("\\d+;")) {
				if (prevToken.equals("=")) { // token is value only if the previous token is "="
					String[] spw = token.split(";");
					value = "" + Integer.parseInt(spw[0]);
				}
				SymbolTable[STsize++] = new Symbol(lineNumber, name, datatype, scope, value); // increments size of symbol table 
			}
			else if (token.matches("\\p{Alpha}+\\p{Alnum}*;")) {
				String[] spw = token.split(";");	// removes trailing ; from variable name
				name = spw[0];
				value = "-"; // name is only defined not initialized in input.txt, so c is set to a dash 
				SymbolTable[STsize++] = new Symbol(lineNumber, name, datatype, scope, value); // increments size of symbol table 

			}
			else if (token.matches("\\p{Alpha}+\\p{Alnum}*\\(\\)")) { 
				String[] spw = token.split("\\(\\)");	// removes trailing () from function name
				name = spw[0];
				//System.out.println("name = " + name);
				datatype += " function";
				SymbolTable[STsize++] = new Symbol(lineNumber, name, datatype, scope, value); // increments size of symbol table 

			}
		}
	}
}


// prints symbol table

public static void printSymbolTable(int lineNumber)
{
	for (int i = 0; i < STsize; i++) {
		if (SymbolTable[i].lineNumber <= lineNumber) {
			System.out.print(SymbolTable[i].name);
			System.out.print(" -> ");

			System.out.print("data type: ");
			if (SymbolTable[i].datatype.equals("int"))
				System.out.print("integer");
			else if (SymbolTable[i].datatype.equals("char"))
				System.out.print("character");
			else
				System.out.print(SymbolTable[i].datatype);

			System.out.print(", scope: ");
			if (SymbolTable[i].scope.equals(""))
				System.out.print("global");
			else
				System.out.print("local to " + SymbolTable[i].scope);

			if (!SymbolTable[i].value.equals("")) {	// print only non-null values
				System.out.print(", value: ");
				System.out.print(SymbolTable[i].value);
			}
			System.out.println();
		}
	}
}

// main method

public static void main(String[] args) throws FileNotFoundException
{
	createSymbolTable(); // invokes method to create symbol table

	Scanner in = new Scanner(System.in); 

	System.out.println("Input line: ");
	//while (in.hasNextLine()) {
		String line = in.nextLine();
		int lineNumber = Integer.parseInt(line); // returns line which is a string as an integer
		printSymbolTable(lineNumber); // invokes method to print symbol table along with line number
	//System.out.println("Input line: ");
	//}

}

}
