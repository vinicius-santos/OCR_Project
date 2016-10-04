package enumeration;

	
	/**
	 * Enum para variaveis para base api
	 * @author vinicius
	 *
	 */
	public enum VariablesEnum {
		POR_LOAD_UNAMBIG_DAWG("por.DangAmbigs"),// CARREGAR PALVRAS ANBIGUAS CHAVE: 1LIGADO 0 DESL
		POR_LOAD_FREQ_DAWG("por.freq-dawg"),// CARREGAR PALVRAS FREQUENTES CHAVE: 1LIGADO 0 DESL
		POR_LOAD_WORD_DAWG("por.word-dawg"),//CARREGAR PALAVRAS CHAVE: 1 LIGADO 0 DESL
		POR_INTTEMP("por.inttemp"),
		POR_NORMOPROTO("por.normproto"),
		POR_UNICHARSET("por.unicharset"),
		POR_USER_WORD_SUFFIX("por.user-words");// HABILITAR PALVRAS FORNECIDAS PELO USUÁRIO
		
		public String value;

		VariablesEnum(String valor) {
			value = valor;
		}
	}

