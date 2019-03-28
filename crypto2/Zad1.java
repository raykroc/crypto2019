package crypto2;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;

public class Zad1 {
	static SecureRandom sr = new SecureRandom();

	private static int mod(int n, int m) {
		return ((n % m) + m) % m;
	}

	private static Byte[] KSA(int N, int T, Byte[] key) {
		int L = key.length;
		Byte S[] = new Byte[N];

		for (int i = 0; i < N; i++) {
			S[i] = (byte) (i);
		}
		int j = 0;

		for (int i = 0; i < T; i++) {
			j = (j + S[mod(i, N)] + key[mod(i, L)]) % N;
			byte tmp = S[mod(i, N)];
			S[mod(i, N)] = S[mod(j, N)];
			S[mod(j, N)] = tmp;
		}

		return S;
	}

	private static Byte[] PRGA(Byte S[], int N, int numOfBytes) {
		Byte ret[] = new Byte[numOfBytes];

		int i = 0, j = 0;
		for (int x = 0; x < numOfBytes; x++) {
			i = mod(i + 1, N);
			j = mod(j + S[i], N);

			byte tmp = S[i];
			S[i] = S[j];
			S[j] = tmp;

			ret[x] = S[mod(S[i] + S[j], N)];
		}
		return ret;
	}

	public static Byte[] RC4(int N, int T, int numOfBytes) {
		Byte key[] = new Byte[keyLen];
		for (int i = 0; i < keyLen; i++) {
			key[i] = (byte) sr.nextInt(Byte.MAX_VALUE);
		}
		Byte S[] = KSA(N, T, key);
		return PRGA(S, N, numOfBytes);
	}

	public static Byte[] drop(Byte in[], int N) {
		if (N < in.length) {
			Byte out[] = new Byte[in.length - N];
			for (int i = 0; i < out.length; i++) {
				out[i] = in[i + N];
			}
			return out;
		}
		return null;
	}

	public static Byte[] mdrop(Byte in[], int N) {
		if (N < in.length) {
			Byte out[] = new Byte[in.length / (N + 1)];
			for (int i = 0; i < out.length; i++) {
				out[i] = in[(N + 1) * i];
			}
			return out;
		}
		return null;
	}

	final static int keyLen = 64; // 40, 64, 128

	public static void main(String[] args) {
		int num = 1_000;
		int N = 256; // 16, 64, 256
		int D = 0; // 0, 1, 2, 3
		// Byte x[] = drop(RC4(N, N, num + k + N), k + N);
		// Byte x[] = mdrop(RC4(N, N, num * (1 + D) + N), D);
		Byte x[] = RC4(N, Zad1.keyLen + N, num);

		try {
			PrintWriter writer = new PrintWriter("RC4_23_N" + N + "_K" + keyLen + "_D" + D + ".txt", "UTF-8");

			writer.println("type: d");
			writer.println("count: " + num / 4);
			writer.println("numbit: 4");
			for (int i = 0; i < num; i += 4) {
				writer.println(((x[i] & 0xFF) << 24) | ((x[i + 1] & 0xFF) << 16) | ((x[i + 2] & 0xFF) << 8) | (x[i + 3] & 0xFF));
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("lipa");
		}

		// System.out.println(Arrays.toString(x));
	}

	/*
	 * dieharder -a -g 202 -f plik
	 *
	 * plik:
	 *
	 * type: d count: 10000 numbit: 4
	 */

}
