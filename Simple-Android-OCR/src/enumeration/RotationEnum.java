package enumeration;

/**
 * Enum para valores de rota��o.
 * @author vinicius
 *
 */
public enum RotationEnum {

	ZERO(0),
	NOVENTA(90), 
	CENTOEOITENTA(180), 
	DUZENTOSESETENTA(270), A(14);
	
	public int value;

	RotationEnum(int valor) {
		value = valor;
	}
}
