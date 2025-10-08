class Term {
	Factor factor;
	Term term;
	int option;
	
	void parse() {
		factor  = new Factor();
		factor.parse();
		if (Parser.scanner.currentToken() == Core.MULTIPLY) {
			option = 1;
		} else if (Parser.scanner.currentToken() == Core.DIVIDE) {
			option = 2;
		}
		if (option != 0) {
			Parser.scanner.nextToken();
			term = new Term();
			term.parse();
		}						
	}
	
	void print() {
		factor.print();
		if (option == 1) {
			System.out.print("*");
			term.print();
		} else if (option == 2) {
			System.out.print("/");
			term.print();
		}
	}

	int execute() {
		if (option == 0) {
			return factor.execute();
		} else if (option == 1) {
			return factor.execute() * term.execute();
		} else {
			int divisor = term.execute();
			if (divisor == 0) {
				throw new RuntimeException("Division by zero");
			}
			return factor.execute() / divisor;
		}
	}
}