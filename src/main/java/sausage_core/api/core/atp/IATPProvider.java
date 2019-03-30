package sausage_core.api.core.atp;

import java.util.function.IntUnaryOperator;

public interface IATPProvider extends IntUnaryOperator {
	int provide(int goal);

	@Override
	default int applyAsInt(int operand) {
		return provide(operand);
	}
}
