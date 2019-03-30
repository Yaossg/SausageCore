package sausage_core.api.core.atp;

/**
 * alternative transient periodic provisional power
 * or simply, alias to power
 * structure formula: [A-T-P~P-P]
 */
public interface ATP {
	static ATPHolder holder() {
		return new ATPHolder();
	}
}
