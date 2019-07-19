package sausage_core.api.core.atp;

public class ATPScale implements IATPProvider {
	private IATPProvider underlying;
	private final int scale;

	public ATPScale(IATPProvider underlying, int scale) {
		this.underlying = underlying;
		this.scale = scale;
	}

	@Override
	public int provide(int goal) {
		if (scale > 0)
			return underlying.provide((goal + scale - 1) / scale) * scale;
		if (scale < 0)
			return underlying.provide(goal * -scale) / -scale;
		return 0;
	}
}
