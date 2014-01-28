package org.swordess.test.sample;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.swordess.test.Boundary;
import org.swordess.test.Cover;
import org.swordess.test.CoverChecker;
import org.swordess.test.EquivalentCondition;
import org.swordess.test.EquivalentCondition.Condition;
import org.swordess.test.TestCaseAnalysis;
import org.swordess.test.TestCaseAnalysis.MethodAnalysis;
import org.swordess.test.sample.InputStreamProvider.InputStreamUser;

@TestCaseAnalysis({
	@MethodAnalysis(
		signature = InputStreamProviderTest.SIGNATURE_INPUT_STREAM_PROVIDER,
		equivalentConditions = {
			@EquivalentCondition(
				name    = "path of the file",
				valid   = @Condition(nbr = 1, desc = "non-empty string"),
				invalid = {
					@Condition(nbr = 2, desc = "null"),
					@Condition(nbr = 3, desc = "''"),
					@Condition(nbr = 4, desc = "' '"),
				}
			),
			@EquivalentCondition(
				name    = "whether file exist",
				valid   = @Condition(nbr = 5, desc = "yes"),
				invalid = @Condition(nbr = 6, desc = "no")
			)
		},
		boundaries = {
			@Boundary(nbr = 1, desc = "null"),
			@Boundary(nbr = 2, desc = "''"),
			@Boundary(nbr = 3, desc = "' '"),
		}
	),
	@MethodAnalysis(
		signature = InputStreamProviderTest.SIGNATURE_USED_BY,
		equivalentConditions = {
			@EquivalentCondition(
				name    = "user of stream",
				valid   = @Condition(nbr = 1, desc = "non-null"),
				invalid = @Condition(nbr = 2, desc = "null")
			)
		},
		boundaries = {
			@Boundary(nbr = 1, desc = "user of null"),
			@Boundary(nbr = 2, desc = "user closed the stream")
		}
	),
	@MethodAnalysis(
		signature = InputStreamProviderTest.SIGNATURE_USED_BY_COLLECTION,
		equivalentConditions = {
			@EquivalentCondition(
				name    = "users of stream",
				valid   = @Condition(nbr = 1, desc = "non-null"),
				invalid = @Condition(nbr = 2, desc = "null")
			)
		},
		boundaries = {
			@Boundary(nbr = 1, desc = "users of null"),
			@Boundary(nbr = 2, desc = "users with empty elements"),
			@Boundary(nbr = 3, desc = "users with null elements")
		}
	)
})
@RunWith(CoverChecker.class)
public class InputStreamProviderTest {

	static final String SIGNATURE_INPUT_STREAM_PROVIDER = "InputStreamProvider(String)";
	static final String SIGNATURE_USED_BY = "usedBy(InputStreamUser)";
	static final String SIGNATURE_USED_BY_COLLECTION = "usedBy(Collection<InputStreamUser>)";
	
	private static int ACTUAL_INVOKE_NUMBER = 0;
	
	private Logger mockLog;
	
	@Before
	public void setUp() {
		mockLog = Mockito.mock(Logger.class);
	}
	
	@Cover(
		methodSignature = SIGNATURE_INPUT_STREAM_PROVIDER,
		conditions = "StringUtils.isBlank(path)", invalidECs = 2, boundaries = 1)
	@Test(expected = IllegalArgumentException.class)
	public void testConditionInputStreamProvider1() throws FileNotFoundException {
		new InputStreamProvider(null);
	}
	
	@Cover(
		methodSignature = SIGNATURE_INPUT_STREAM_PROVIDER,
		conditions = "!new File(path).exist()", invalidECs = 6)
	@Test(expected = FileNotFoundException.class)
	public void testConditionInputStreamProvider2() throws FileNotFoundException {
		new InputStreamProvider(FileUtil.ensureNonExistence("tc5.tmp"));
	}
	
	@Cover(methodSignature = SIGNATURE_INPUT_STREAM_PROVIDER, validECs = {1,5})
	@Test
	public void testECInputStreamProvider1() throws FileNotFoundException {
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("tc1.tmp"));
		assertNotNull(provider);
	}
	
	@Cover(methodSignature = SIGNATURE_INPUT_STREAM_PROVIDER, invalidECs = 3, boundaries = 2)
	@Test(expected = IllegalArgumentException.class)
	public void testECInputStreamProvider2() throws FileNotFoundException {
		new InputStreamProvider("");
	}
	
	@Cover(methodSignature = SIGNATURE_INPUT_STREAM_PROVIDER, invalidECs = 4, boundaries = 3)
	@Test(expected = IllegalArgumentException.class)
	public void testECInputStreamProvider3() throws FileNotFoundException {
		new InputStreamProvider(" ");
	}
	
	@Cover(methodSignature = SIGNATURE_USED_BY, validECs = 1)
	@Test
	public void testECUsedBy1() throws FileNotFoundException {
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("usedBy1.tmp"));
		provider.setLogger(mockLog);
		provider.usedBy(new InputStreamUser() {
			@Override
			public void use(InputStream in) throws IOException {
				// do nothing and let the stream provide to close
			}
		});
	}
	
	@Cover(methodSignature = SIGNATURE_USED_BY, invalidECs = 2, boundaries = 1)
	@Test(expected = NullPointerException.class)
	public void testECUsedBy2() throws FileNotFoundException {
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("usedBy2.tmp"));
		provider.setLogger(mockLog);
		provider.usedBy((InputStreamUser)null);
	}
	
	@Cover(methodSignature = SIGNATURE_USED_BY, boundaries = 2)
	@Test
	public void testBoundaryUsedBy1() throws FileNotFoundException {
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("usedBy3.tmp"));
		provider.setLogger(mockLog);
		provider.usedBy(new InputStreamUser() {
			@Override
			public void use(InputStream in) throws IOException {
				// close the stream in the client code
				in.close();
			}
		});
	}

	@Cover(methodSignature = SIGNATURE_USED_BY_COLLECTION, validECs = 1)
	@Test
	public void testECUsedBySeveralUsers1() throws FileNotFoundException {
		Collection<RecordableStreamUser> users = new ArrayList<>();
		users.add(new RecordableStreamUser());
		users.add(new RecordableStreamUser());
		users.add(new RecordableStreamUser());
		users.add(new RecordableStreamUser());
		
		// set up the expected invoke number for each stream user
		int iterationOrder = 0;
		for (RecordableStreamUser user : users) {
			user.setExpectedInvokeNumber(++iterationOrder);
		}
		
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("usedBySeveralUsers1.tmp"));
		provider.setLogger(mockLog);
		provider.usedBy(users);
		
		// verify the invoke number for each stream user
		for (RecordableStreamUser user : users) {
			assertEquals(user.getExpectedInvokeNumber(), user.getActualInvokeNumber());
		}
	}
	
	@Cover(methodSignature = SIGNATURE_USED_BY_COLLECTION, invalidECs = 2, boundaries = 1)
	@Test(expected = NullPointerException.class)
	public void testECUsedBySeveralUsers2() throws FileNotFoundException {
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("usedBySeveralUsers2.tmp"));
		provider.setLogger(mockLog);
		provider.usedBy((Collection<InputStreamUser>)null);
	}
	
	@Cover(methodSignature = SIGNATURE_USED_BY_COLLECTION, boundaries = 2)
	@Test
	public void testBoundaryUsedBySeveralUsers1() throws FileNotFoundException {
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("usedBySeveralUsers3.tmp"));
		provider.setLogger(mockLog);
		provider.usedBy(Collections.<InputStreamUser>emptySet());
	}
	
	@Cover(methodSignature = SIGNATURE_USED_BY_COLLECTION, boundaries = 3)
	@Test
	public void testBoundaryUsedBySeveralUsers2() throws FileNotFoundException {
		List<InputStreamUser> usersWithNullElements = new ArrayList<>(16);
		Collections.fill(usersWithNullElements, null);
		
		InputStreamProvider provider = new InputStreamProvider(FileUtil.ensureExistence("usedBySeveralUsers4.tmp"));
		provider.setLogger(mockLog);
		provider.usedBy(usersWithNullElements);
	}
	
	private static class RecordableStreamUser implements InputStreamUser {

		private int expectedInvokeNumber;
		private int actualInvokeNumber;

		public void use(InputStream in) throws IOException {
			// do nothing but remember the actual invoke number
			actualInvokeNumber = ++ACTUAL_INVOKE_NUMBER;
		}

		void setExpectedInvokeNumber(int expectedInvokeNumber) {
			this.expectedInvokeNumber = expectedInvokeNumber;
		}

		int getExpectedInvokeNumber() {
			return expectedInvokeNumber;
		}

		int getActualInvokeNumber() {
			return actualInvokeNumber;
		}
		
	}
	
}
