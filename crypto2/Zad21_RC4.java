package crypto2;

import java.util.Arrays;

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

	public static Byte[] RC4(int N, int T, int L, int numOfBytes) {
		Byte key[] = Common.newKey(L);
		Byte S[] = KSA(N, T, key);
		return Common.PRGA(S, N, numOfBytes);
	}

	public static void main(String[] args) {
		int N = 256;// 16, 64, 256
		int L = 40;// 40, 64, 128
		int D = 0;// 0, 1, 2, 3

		Byte x[] = Common.mdrop(RC4(N, N, L, Common.num * (1 + D) + N), D);
		System.out.println(Arrays.asList(x));
		// Common.writeToFile(x, "RC4_N" + N + "_L" + L + "_D" + D);
	}

}
