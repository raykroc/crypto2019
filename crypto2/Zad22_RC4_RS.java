package crypto2;

import java.util.ArrayList;
import java.util.List;

public class Zad22_RC4_RS {

	private static char ithBit(Byte[] key, int i) {
		int bajt = key[i / 8];
		int bit = i % 8;
		String binStr = String.format("%8s", Integer.toBinaryString(bajt & 0xFF)).replace(' ', '0');
		return binStr.charAt(7 - bit); // albo samo (bit)
	}

	private static Byte[] KSA_RS(int N, int T, Byte[] key) {
		int L = key.length * 8;
		Byte S[] = new Byte[N];

		for (int i = 0; i < N; i++) {
			S[i] = (byte) (i);
		}

		for (int r = 0; r < T; r++) {
			List<Byte> top = new ArrayList<Byte>();
			List<Byte> bottom = new ArrayList<Byte>();

			for (int i = 0; i < N; i++) {
				if (ithBit(key, (r * N + i) % L) == '0') {
					top.add(S[i]);
				} else {
					bottom.add(S[i]);
				}
			}

			top.addAll(bottom);
			S = top.toArray(S);
		}

		return S;
	}

	public static void RC4_RSmdrop(int N, int T, int L, int D) {
		Byte key[] = Common.newKey(L);
		// Byte key[] = { 'W', 'i', 'k', 'i' };
		Byte S[] = KSA_RS(N, T, key);
		Common.PRGAmdrop(S, N, D);
	}

	public static void main(String[] args) {
		int N = 256;// 16, 64, 256
		int L = 40;// 40, 64, 128
		int D = 0;// 0, 1, 2, 3

		RC4_RSmdrop(N, N, L, D);
		// System.out.println(Arrays.asList(x));
		// for (byte b : x) {
		// System.out.printf("%02X ", b);
		// }
		// Common.writeToFile(x, "RC4_RS_N" + N + "_L" + L + "_D" + D);
	}

}
