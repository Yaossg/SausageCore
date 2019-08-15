package sausage_core.api.core.atp;

/**
 * alternative transient provisional potential power
 * or simply, alias to power
 * structure formula: [A-T-P~P-P]
 *
 * ATP helps manage working progress of machines
 * give ATP to the holder means energy is cost
 * want ATP from the holder to send a request
 * and finally take ATP away from holder to work
 *
 * This api is under construction
 * Please use with care
 */
public interface ATP {
	static ATPHolder holder() {
		return new ATPHolder();
	}
}
