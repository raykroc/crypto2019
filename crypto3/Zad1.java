package crypto3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Zad1 {
	private static interface MyInterface {
		byte[] doSomething(byte[] param1) throws GeneralSecurityException;
	}

	static enum Algorithm {
		CBC, OFB, ECB, CTR;

		public String getString() {
			return "AES/" + this.name() + "/PKCS5Padding";

		}

		public boolean hasIvec() {
			if (this.name().equals("ECB")) {
				return false;
			} else {
				return true;
			}
		}
	}

	static SecretKey secretKey;
	static Algorithm ALGORITHM = Algorithm.CBC;
	static Cipher cipher;
	static byte[] ivec = new byte[16];
	static int cnt = 14;
	static SecureRandom sr = new SecureRandom();
	static boolean b;

	static {
		// ALGORITHM = Algorithm.CBC;
		// ALGORITHM = Algorithm.OFB;
		// ALGORITHM = Algorithm.ECB;
		// ALGORITHM = Algorithm.CTR;
		sr.nextBytes(ivec);
		try {
			getKey();
			cipher = Cipher.getInstance(ALGORITHM.getString());
			// System.out.println(cipher.getAlgorithm());
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	static IvParameterSpec genIvec() {
		ivec[cnt]++;
		if (ivec[cnt] == 0) {
			cnt--;
			if (cnt < 0) {
				cnt = 14;
			} else {
				ivec[cnt]++;
			}
		}
		return new IvParameterSpec(ivec);
	}

	static IvParameterSpec predictIvec() {
		byte[] copy = ivec.clone();
		int copyCnt = cnt;

		copy[copyCnt]++;
		if (copy[copyCnt] == 0) {
			copyCnt--;
			if (copyCnt < 0) {
				copyCnt = 15;
			} else {
				copy[copyCnt]++;
			}
		}

		return new IvParameterSpec(copy);
	}

	static IvParameterSpec lastIvec() {
		return new IvParameterSpec(ivec);
	}

	static byte[] encrypt(byte[] plainText) throws GeneralSecurityException {
		AlgorithmParameterSpec params = null;
		if (ALGORITHM.hasIvec()) {
			params = genIvec();
		}
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
		return cipher.doFinal(plainText);
	}

	static byte[] testEncrypt(byte[] plainText) throws GeneralSecurityException {
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, lastIvec());
		return cipher.doFinal(plainText);
	}

	static byte[] decrypt(byte[] cipherText) throws GeneralSecurityException {
		AlgorithmParameterSpec params = null;
		if (ALGORITHM.hasIvec()) {
			params = lastIvec();
		}
		cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
		return cipher.doFinal(cipherText);
	}

	static void getKey() throws IOException, GeneralSecurityException {
		String fileName = "keystore.jceks";

		char[] password = "kornel".toCharArray();
		String alias = "128key";

		KeyStore ks = KeyStore.getInstance("JCEKS");
		try (FileInputStream fis = new FileInputStream(fileName)) {
			ks.load(fis, password);
			secretKey = (SecretKey) ks.getKey(alias, password);
		}
	}

	static byte[] challenge(byte[] m1, byte[] m2) throws GeneralSecurityException {
		int len1 = ((m1.length / 16) + 1) * 16;
		int len2 = ((m2.length / 16) + 1) * 16;
		if (len1 != len2) {
			throw new GeneralSecurityException("Different length of inputs");
		}

		byte[] cipher;
		b = sr.nextBoolean();
		if (b) {
			cipher = encrypt(m1);
		} else {
			cipher = encrypt(m2);
		}
		// System.out.println("Oracle challenge b = " + (b ? 1 : 2));
		return cipher;
	}

	static List<byte[]> oracle(List<byte[]> messages) throws GeneralSecurityException {
		List<byte[]> ciphers = new ArrayList<>();
		for (int i = 0; i < messages.size(); i++) {
			ciphers.add(encrypt(messages.get(i)));
		}

		return ciphers;
	}

	static void encryptFiles(List<String> filenames) {
		encryptOrDecryptFiles(filenames, (x) -> encrypt(x), "encrypted/");
	}

	static void decryptFiles(List<String> filenames) {
		encryptOrDecryptFiles(filenames, (x) -> decrypt(x), "decrypted/");
	}

	static void encryptOrDecryptFiles(List<String> filenames, MyInterface oracle, String prefix) {
		for (String filename : filenames) {
			System.out.println("Start encryption of " + filename);
			byte[] message;
			try {
				message = Files.readAllBytes(Paths.get(filename));
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			byte[] encrypted;
			try {
				// encrypted = encrypt(message);
				encrypted = oracle.doSomething(message);
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
				return;
			}

			String[] tmp = filename.split("/");
			String outFileName = prefix + tmp[tmp.length - 1];
			try (OutputStream writer = new FileOutputStream(outFileName)) {
				writer.write(encrypted);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			System.out.println(filename + " encrypted to " + outFileName);
		}
	}

	static void test() throws GeneralSecurityException {
		int limit = 5;
		List<byte[]> messages = new ArrayList<>(limit);
		for (int i = 0; i < limit; i++) {
			messages.add(("Kornel " + i).getBytes());
		}
		List<byte[]> ciphers = oracle(messages);
		for (int i = 0; i < limit; i++) {
			System.out.print("c" + i + ": ");
			Zad2.print(ciphers.get(i));
		}
		System.out.println();

		String m1 = "Kornel";
		String m2 = "Mirkowski";
		byte[] cb = challenge(m1.getBytes(), m2.getBytes());
		System.out.print("cb: ");
		Zad2.print(cb);
	}

	public static void main(String[] args) throws Exception {
		// Zad1.test();
		List<String> list1 = new ArrayList<String>();
		list1.add("in1.txt");
		list1.add("in2.txt");
		list1.add("in3.txt");
		encryptFiles(list1);

		List<String> list2 = new ArrayList<String>();
		list2.add("encrypted/in1.txt");
		list2.add("encrypted/in2.txt");
		list2.add("encrypted/in3.txt");
		decryptFiles(list2);
	}

}