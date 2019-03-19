package crypto2;

import java.io.PrintWriter;
import java.security.SecureRandom;

public class Common {

	final static SecureRandom sr = new SecureRandom();

	static Byte[] mdrop(Byte in[], int N) {
		Byte out[] = new Byte[in.length / (N + 1)];
		for (int i = 0; i < out.length; i++) {
			out[i] = in[(N + 1) * i];
		}
		return out;
	}

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

	static Byte[] PRGA(Byte S[], int N, int numOfBytes) {
		Byte ret[] = new Byte[numOfBytes];

		int i = 0, j = 0;
		for (int x = 0; x < numOfBytes; x++) {
			i = (i + 1) % N;
			j = mod(j + S[i], N);

			swap(S, i, j);
			ret[x] = S[mod(S[i] + S[j], N)];
		}
		return ret;
	}

	static Byte[] newKey(int L) {
		Byte key[] = new Byte[L];
		byte smallKey[] = new byte[L];
		Common.sr.nextBytes(smallKey);
		for (int i = 0; i < L; i++) {
			key[i] = smallKey[i];
			// key[i] = 1;
		}
		return key;
	}

	static void writeToFile(Byte[] x, String filename) {
		// System.out.println(Arrays.asList(x));

		try {
			PrintWriter writer = new PrintWriter("out/" + filename + ".txt", "UTF-8");

			// PrintStream writer = System.out;
			writer.println("type: d");
			writer.println("count: " + num / 4);
			writer.println("numbit: 4");
			for (int i = 0; i < num; i += 4) {
				writer.println(((x[i] & 0xFF) << 24) | ((x[i + 1] & 0xFF) << 16) | ((x[i + 2] & 0xFF) << 8) | (x[i + 3] & 0xFF));
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("lipa");
		}

	}

	final static int num = 4000_000;

	public static void main(String[] args) {

		int[] Ns = { 16, 64, 256 };
		int[] Ls = { 40, 64, 128 };
		int[] Ds = { 0, 1, 2, 3 };

		for (int N : Ns) {
			for (int L : Ls) {
				for (int D : Ds) {
					String suffix = "_N" + N + "_L" + L + "_D" + D;
					System.out.println(suffix);

					Byte x[] = Common.mdrop(Zad21_RC4.RC4(N, N, L, Common.num * (1 + D) + N), D);
					Common.writeToFile(x, "RC4/RC4" + suffix);

					Byte y[] = Common.mdrop(Zad22_RC4_RS.RC4_RS(N, N, L, Common.num * (1 + D) + N), D);
					Common.writeToFile(y, "RS/RS" + suffix);

					Byte z[] = Common.mdrop(Zad23_RC4_SST.RC4_SST(N, N, L, Common.num * (1 + D) + N), D);
					Common.writeToFile(z, "SST/SST" + suffix);
				}
			}
		}

		/*
		 * dieharder -a -g 202 -f plik
		 *
		 * plik:
		 *
		 * type: d count: 10000 numbit: 4
		 */

	}
}
