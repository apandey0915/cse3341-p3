class Main {
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java Main <program.code> <data.data>");
			return;
		}
		Parser.scanner = new CoreScanner(args[0]);
		DataInput.init(args[1]);

		Procedure p = new Procedure();
		p.parse();
		p.execute();
	}
}