package crypto3;

import static org.junit.Assert.assertEquals;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.Cipher;

import org.junit.Test;

import crypto3.Zad1.Algorithm;

public class Zad1Test {

	private void encryptAndDecrypt() throws GeneralSecurityException {
		Zad1.cipher = Cipher.getInstance(Zad1.ALGORITHM.getString());
		SecureRandom sr = new SecureRandom();

		for (int i = 0; i < 1000; i++) {
			String text = "kornel" + System.currentTimeMillis() + "mirkowski" + sr.nextLong();
			byte[] m = text.getBytes();
			byte[] c = Zad1.encrypt(m);
			byte[] d = Zad1.decrypt(c);
			String decrypted = new String(d);
			assertEquals(text, decrypted);
		}
	}

	@Test
	public void TestCBC() throws GeneralSecurityException {
		Zad1.ALGORITHM = Algorithm.CBC;
		encryptAndDecrypt();
	}

	@Test
	public void TestCTR() throws GeneralSecurityException {
		Zad1.ALGORITHM = Algorithm.CTR;
		encryptAndDecrypt();
	}

	@Test
	public void TestECB() throws GeneralSecurityException {
		Zad1.ALGORITHM = Algorithm.ECB;
		encryptAndDecrypt();
	}

	@Test
	public void TestOFB() throws GeneralSecurityException {
		Zad1.ALGORITHM = Algorithm.OFB;
		encryptAndDecrypt();
	}
}
