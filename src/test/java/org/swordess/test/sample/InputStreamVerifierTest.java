package org.swordess.test.sample;

import static junit.framework.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class InputStreamVerifierTest {

	private Logger mockLog;
	
	@Before
	public void setUp() {
		mockLog = Mockito.mock(Logger.class);
	}
	
	/*
	 * constructor - InputStreamVerifier(byte[])
	 * 
	 * # Equivalent Conditions
	 * External Condition     Valid EC    Invalid EC
	 * ---------------------- ----------- ----------
	 * expected bytes of data non-null(1) null(2)
	 * 
	 * # Boundary Analyze
	 * null bytes(b1)
	 * empty bytes(b2)
	 * bytes with one element(b3)
	 * 
	 * # TestCase
	 * TC1: 1(b3) - bytes with one element
	 * TC2: 2(b1) - null bytes
	 * TC3: 3(b2) - empty bytes
	 */

	/* ******************* cover Valid EC start ******************* */
	
	@Test
	public void instantiateInputStreamVerifier1() {
		new InputStreamVerifier(new byte[] { 1 });
	}
	
	/* ******************* cover Valid EC end ******************* */
	
	/* ******************* cover Invalid EC start ******************* */
	
	@Test(expected = IllegalArgumentException.class)
	public void instantiateInputStreamVerifier2() {
		new InputStreamVerifier(null);
	}
	
	@Test
	public void instantiateInputStreamVerifier3() {
		new InputStreamVerifier(new byte[0]);
	}
	
	/* ******************* cover Invalid EC end ******************* */
	
	/*
	 * InputStreamVerifier#use(InputStream)
	 * 
	 * # Equivalent Conditions
	 * External Condition Valid EC Invalid EC
	 * ------------------ -------- ----------
	 * stream of input    always   N/A
	 * 
	 * # Boundary Analyze
	 * stream carries no data(b1)
	 * stream carries one byte(b2)
	 * stream carries bytes of one same byte(b3)
	 *
	 * # TestCase
	 * TC1: b1 - stream carries no data
	 * TC2: b2 - stream carries one byte
	 * TC3: b3 - stream carries of one same byte
	 * 
	 * # Additional
	 * TC4: change some bytes of the expected data to verify the specification
	 *      mentioned in the constructor
	 */
	
	/* ******************* cover Valid EC start ******************* */
	
	@Test
	public void use1() throws FileNotFoundException {
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("use1.tmp"));
		provider.setLogger(mockLog);
		InputStreamVerifier verifier = new InputStreamVerifier(new byte[0]);
		provider.usedBy(verifier);
		assertTrue(verifier.isMatched());
	}
	
	/* ******************* cover Valid EC end ******************* */
	
	/* ******************* cover Invalid EC start ******************* */
	
	@Test
	public void use2() throws IOException {
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("use2.tmp"));
		provider.setLogger(mockLog);
		InputStreamVerifier verifier = new InputStreamVerifier(new byte[] { 1 });
		FileUtil.write(provider.getPath(), (byte) 1);
		provider.usedBy(verifier);
		assertTrue(verifier.isMatched());
	}
	
	@Test
	public void use3() throws IOException {
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("use3.tmp"));
		provider.setLogger(mockLog);
		InputStreamVerifier verifier = new InputStreamVerifier(new byte[] { 1, 1, 1, 1 });
		FileUtil.write(provider.getPath(), new byte[] { 1, 1, 1, 1 });
		provider.usedBy(verifier);
		assertTrue(verifier.isMatched());
	}
	
	/* ******************* cover Invalid EC end ******************* */
	
	/* ******************* cover additional ******************* */
	
	@Test
	public void use4() throws IOException {
		byte[] expectedData = new byte[] { 1, 2, 3, 4};
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("use4.tmp"));
		provider.setLogger(mockLog);
		InputStreamVerifier verifier = new InputStreamVerifier(expectedData);
		
		FileUtil.write(provider.getPath(), new byte[] {1, 2, 3, 4});
		provider.usedBy(verifier);
		assertTrue(verifier.isMatched());
		
		expectedData[2] = (byte)99;
		provider.usedBy(verifier);
		assertTrue(!verifier.isMatched());
	}
	
}
