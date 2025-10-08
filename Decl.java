class Decl implements Stmt {
	DeclInteger declInt;
	DeclObj declObj;
	
	public void parse() {
		if (Parser.scanner.currentToken() == Core.INTEGER) {
			declInt = new DeclInteger();
			declInt.parse();
		} else {
			declObj = new DeclObj();
			declObj.parse();
		}
	}
	
	public void print(int indent) {
		if (declInt != null) {
			declInt.print(indent);
		} else {
			declObj.print(indent);
		}
	}

	public void execute(boolean global) {
		if (declInt != null) {
			declInt.execute(global);
		} else {
			declObj.execute(global);
		}
	}

	@Override
	public void execute() {
		// When a <decl> appears as a <stmt>, it should allocate in the current scope (local)
		if (declInt != null) {
			declInt.execute(false);
		} else {
			declObj.execute(false);
		}
	}
}