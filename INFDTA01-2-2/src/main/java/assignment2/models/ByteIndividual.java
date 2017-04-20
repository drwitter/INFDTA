package assignment2.models;

public class ByteIndividual extends Individual<Byte> {
	public ByteIndividual(int bits) {
		super(bits);
	}

	protected ByteIndividual(Byte value) {
		super(value);
	}

	@Override
	protected Byte randomize(int bits) {
		byte b = 0;
		for(int p = 0; p < bits; p++) {
			b |= getRandomBit(p);
		}
		return b;
	}

	@Override
	public Individual<Byte> breed(Individual<Byte> parent) {
		byte f = getValue();
		byte p2 = parent.getValue();
		for(int i = 0; i < Byte.SIZE; i++) {
			if(Math.random() < 0.5) {
				f |= (p2 & (1 << i));
			}
		}
		return new ByteIndividual(Byte.valueOf(f));
	}

	@Override
	public Individual<Byte> mutate(double mutationRate, int bits) {
		byte value = getValue();
		for(int i = 0; i < bits; i++) {
			if(Math.random() < mutationRate) {
				value ^= (1 << i);
			}
		}
		return new ByteIndividual(Byte.valueOf(value));
	}

	@Override
	public String toString() {
		return "<" + getValue() + ">";
	}
}
