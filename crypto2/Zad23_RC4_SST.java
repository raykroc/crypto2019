package crypto2;

public class Zad23_RC4_SST {

	private static Byte[] KSA_SST(int N, Byte[] key) {
		int L = key.length;
		Byte S[] = new Byte[N];
		Boolean marks[] = new Boolean[N];

		for (int i = 0; i < N; i++) {
			S[i] = (byte) (i);
			marks[i] = false;
		}
		marks[N - 1] = true;
		int marked = 1;

		int j = Common.sr.nextInt(N);
		int r = 0;

		while (marked < N) {
			int rN = r % N;
			j = Common.mod(j + S[rN] + key[r % L], N);

			if (marked < N / 2) {
				if (!marks[rN] && !marks[j]) {
					marks[rN] = true;
					marked++;
				}
			} else {
				if ((!marks[rN] && marks[j]) || (!marks[rN] && r == j)) {
					marks[rN] = true;
					marked++;
				}
			}

			Common.swap(S, rN, j);
			Common.swap(marks, rN, j);

			r++;
		}

		return S;
	}

	public static void RC4_SSTmdrop(int N, int L, int D) {
		Byte key[] = Common.newKey(L);
		Byte S[] = KSA_SST(N, key);
		Common.PRGAmdrop(S, N, D);
	}

	public static void main(String[] args) {
		int N = 16;// 16, 64, 256
		int L = 40;// 40, 64, 128
		int D = 0;// 0, 1, 2, 3

		RC4_SSTmdrop(N, L, D);
		// System.out.println(Arrays.asList(x));
		// Common.writeToFile(x, "RC4_SST_N" + N + "_L" + L + "_D" + D);
	}

}
