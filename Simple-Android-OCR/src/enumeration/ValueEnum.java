package enumeration;

public enum ValueEnum {
	
	ZERO("0"),
	UM("1"), 
	DEZMIL("10000"),
	POR_WORD_USER("por.user-words");
	
	public String value;
	ValueEnum(String valor) {
		value = valor;
	}

}
