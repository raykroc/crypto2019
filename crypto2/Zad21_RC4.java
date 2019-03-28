package crypto2;

public class Zad21_RC4 {

	private static Byte[] KSA(int N, int T, Byte[] key) {
		int L = key.length;
		Byte S[] = new Byte[N];

		for (int i = 0; i < N; i++) {
			S[i] = (byte) (i);
		}

		int j = 0;

		for (int i = 0; i < T; i++) {
			int iN = i % N;
			j = Common.mod(j + S[iN] + key[i % L], N);
			Common.swap(S, iN, j);
		}

		return S;
	}

	public static void RC4mdrop(int N, int T, int L, int D) {
		Byte key[] = Common.newKey(L);
		// Byte key[] = { 'K', 'e', 'y' };
		Byte S[] = KSA(N, T, key);
		Common.PRGAmdrop(S, N, D);
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		int N = 16;// 16, 64, 256
		int L = 40;// 40, 64, 128
		int D = 2;// 0, 1, 2, 3

		RC4mdrop(N, N, L, D);
		// System.out.println(Arrays.asList(x));
		// for (byte b : x) {
		// System.out.printf("%02X ", b);
		// }
		// Common.writeToFile(x, "RC4_N" + N + "_L" + L + "_D" + D);
		long stopTime = System.currentTimeMillis();
		System.err.println("Execution time: " + (stopTime - startTime));
	}

}
