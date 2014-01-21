package org.swordess.test.sample;

import java.io.IOException;
import java.io.InputStream;


public class InputStreamVerifier implements InputStreamProvider.InputStreamUser {

	private byte[] expectedData;
	private boolean match;
	
	/**
	 * Constructs a verifier with the given bytes.
	 * <p>
	 * NOTE: Any changes to the <code>expectedData</code> array will impact this
	 * verifier as the verifier simply references the same array.
	 * 
	 * @throws IllegalArgumentException
	 *             if expectedData is not valid
	 * @param expectedData
	 *            expected bytes of data, should not be null
	 */
	public InputStreamVerifier(byte[] expectedData) {
		if (null == expectedData) {
			throw new IllegalArgumentException("expected data should be null");
		}
		this.expectedData = expectedData;
	}
	
	@Override
	public void use(InputStream in) throws IOException {
		byte[] buf = new byte[512];
		int offset = 0;
		int bytesRead = -1;
		while (-1 != (bytesRead = in.read(buf, 0, buf.length))) {
			if (!ArrayUtils.equals(expectedData, offset, buf, 0, bytesRead)) {
				match = false;
				return;
			}
			offset += bytesRead;
		}
		match = true;
	}
	
	public boolean isMatched() {
		return match;
	}
	
}
