class Read implements Stmt {
	Id argument;
	
	public void parse() {
		Parser.expectedToken(Core.READ);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.LPAREN);
		Parser.scanner.nextToken();
		argument = new Id();
		argument.parse();
		Parser.expectedToken(Core.RPAREN);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.SEMICOLON);
		Parser.scanner.nextToken();
	}
	
	public void print(int indent) {
		for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.print("read(");
		argument.print();
		System.out.println(");");
	}
}