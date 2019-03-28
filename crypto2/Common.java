package crypto2;

import java.security.SecureRandom;

public class Common {

	final static SecureRandom sr = new SecureRandom();

	static int mod(int n, int m) {
		return ((n % m) + m) % m;
	}

	static Object[] swap(Object[] array, int i, int j) {
		if (i != j) {
			Object tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
		return array;
	}

	public static byte[] asByteArray(String hex) {
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}

		return bts;
	}

	static void PRGAmdrop(Byte S[], int N, int D) {
		int size = 10000;
		byte num[] = new byte[size];

		int i = 0, j = 0;
		int x = 0;
		// int cnt = 0;
		while (!System.out.checkError()) {// && cnt++ < 1000000) {

			int dropCount = D;
			// --------------------------------
			// --------- N = 16 START ---------
			// --------------------------------
			byte tmp1 = 0;
			byte tmp2 = 0;
			while (dropCount-- >= 0) {
				i = (i + 1) % N;
				j = mod(j + S[i], N);
				swap(S, i, j);
			}
			tmp1 = (byte) ((S[mod(S[i] + S[j], N)]) << 4);
			dropCount = D;
			while (dropCount-- >= 0) {
				i = (i + 1) % N;
				j = mod(j + S[i], N);
				swap(S, i, j);
			}
			tmp2 = S[mod(S[i] + S[j], N)];

			num[x] = (byte) (tmp1 | tmp2);

			// --------------------------------
			// --------- N = 16 END -----------
			// --------------------------------

			// --------------------------------
			// --------- N = 256 START --------
			// --------------------------------

			// while (dropCount-- >= 0) {
			// i = (i + 1) % N;
			// j = mod(j + S[i], N);
			// swap(S, i, j);
			// num[x] = S[mod(S[i] + S[j], N)];
			// }

			// --------------------------------
			// --------- N = 256 END ----------
			// --------------------------------

			x++;

			if (x == size) {
				x = 0;
				try {
					System.out.write(num);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	static Byte[] newKey(int L) {
		Byte key[] = new Byte[L];
		byte smallKey[] = new byte[L];
		sr.nextBytes(smallKey);
		for (int i = 0; i < L; i++) {
			key[i] = smallKey[i];
			// key[i] = 1;
		}
		return key;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		if (args.length < 4) {
			System.out.println("usage: gen < 0 rc4 | 1 rc4rs | 2 rc4sst> N L D");
			return;
		}
		int gen = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		int L = Integer.parseInt(args[2]);
		int D = Integer.parseInt(args[3]);
		switch (gen) {
			case 0:
				Zad21_RC4.RC4mdrop(N, N, L, D);
				break;
			case 1:
				Zad22_RC4_RS.RC4_RSmdrop(N, (int) (2 * N * Math.log(N)), L, D);
				break;
			case 2:
				Zad23_RC4_SST.RC4_SSTmdrop(N, L, D);
				break;
		}
		long end = System.currentTimeMillis();
		System.err.println(end - start);
		System.out.println("usage: gen < 0 rc4 | 1 rc4rs | 2 rc4sst> N L D");
	}
}
