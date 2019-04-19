package crypto3;

import java.security.GeneralSecurityException;
import java.util.List;

public class Zad2 {
	static byte[] challenge(byte[] m1, byte[] m2) throws GeneralSecurityException {
		return Zad1.challenge(m1, m2);
	}

	static List<byte[]> oracle(List<byte[]> messages) throws GeneralSecurityException {
		return Zad1.oracle(messages);
	}

	static void print(byte[] bytes) {
		for (byte b : bytes) {
			System.out.printf("%3d.", b + 128);
		}
		System.out.println();
	}

	static byte[] xor(byte[] arr1, byte[] arr2) {
		int len = Math.min(arr1.length, arr2.length);
		byte[] arr3 = new byte[len];
		for (int i = 0; i < len; i++) {
			arr3[i] = (byte) (arr1[i] ^ arr2[i]);
		}
		return arr3;
	}

	static boolean equal(byte[] arr1, byte[] arr2) {
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}

	static boolean test() throws GeneralSecurityException {
		String m1 = "123456789012345";
		String m2 = "111111111111111";
		byte[] mb1 = m1.getBytes();
		byte[] mb2 = m2.getBytes();
		byte[] cbb = Zad1.challenge(mb1, mb2);
		byte[] iv1 = Zad1.lastIvec().getIV();
		byte[] iv2 = Zad1.predictIvec().getIV();
		byte[] xor = xor(xor(iv1, iv2), mb1);
		byte[] cbx = Zad1.encrypt(xor);

		// System.out.println();
		// System.out.print("mb1 ");
		// print(mb1);
		// System.out.print("mb2 ");
		// print(mb2);
		// System.out.print("cbb ");
		// print(cbb);
		// System.out.print("iv1 ");
		// print(iv1);
		// System.out.print("iv2 ");
		// print(iv2);
		// System.out.print("xor ");
		// print(xor);
		// System.out.print("cbx ");
		// print(cbx);

		boolean b = equal(cbb, cbx);
		// System.out.println("Distinguisher guess b = " + (b ? 1 : 2));
		return (b == Zad1.b);
	}

	public static void main(String[] args) {
		int failed = 0;
		int limit = 100_000;
		try {
			for (int i = 0; i < limit; i++) {
				if (!test()) {
					failed++;
					// throw new GeneralSecurityException("NIE ZGADLEM!!");
				}
			}
		} catch (GeneralSecurityException e) {
			System.out.println(e.getMessage());
		}
		if (failed == 0) {
			System.out.println("All " + limit + " experiments were passed");
		} else {
			System.out.println(failed + " out of " + limit + " experiments failed.");
		}
	}
}
