package org.swordess.test;

import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.swordess.test.model.Descriptors;
import org.swordess.test.model.TestCaseDescriptor;
import org.swordess.test.model.TestClassDescriptor;
import org.swordess.test.model.TestUnitDescriptor;
import org.swordess.test.util.CollectionUtils;


public class CoverChecker extends BlockJUnit4ClassRunner {

	public CoverChecker(Class<?> clazz) throws InitializationError {
		super(clazz);
	}
	
	@Override
	protected Statement classBlock(RunNotifier notifier) {
		final Statement statement = super.classBlock(notifier);
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				statement.evaluate();
				checkExpectedCasesAndCoveredCases();
			}
		};
	}

	private void checkExpectedCasesAndCoveredCases() throws UncoveredCasesException {
		if (getTestClass().getJavaClass().isAnnotationPresent(TestCaseAnalysis.class)) {
			TestClassDescriptor classDescriptor = new TestClassDescriptor(getTestClass());
			List<TestUnitDescriptor> expectedUnits = classDescriptor.getExpectedTestUnitsDescriptors();
			List<TestCaseDescriptor> coveredCases = classDescriptor.getCoveredTestCaseDescriptors();
			diff(expectedUnits, Descriptors.combine(coveredCases));
		}
	}

	private void diff(List<TestUnitDescriptor> expectedUnits,
			List<TestUnitDescriptor> coveredUnits) throws UncoveredCasesException {
		StringBuilder errorMsg = new StringBuilder();
		for (TestUnitDescriptor expectedUnit : expectedUnits) {
			List<Integer> uncoveredValidECs = expectedUnit.getValidECs();
			List<Integer> uncoveredInvalidECs = expectedUnit.getInvalidECs();
			List<Integer> uncoveredBoundaries = expectedUnit.getBoundaries();
			
			TestUnitDescriptor coveredUnit = Descriptors.select(coveredUnits,
					expectedUnit.getMethodSignature());
			if (null != coveredUnit) {
				uncoveredValidECs.removeAll(coveredUnit.getValidECs());
				uncoveredInvalidECs.removeAll(coveredUnit.getInvalidECs());
				uncoveredBoundaries.removeAll(coveredUnit.getBoundaries());
			}
			
			generateErrorMsg(errorMsg, expectedUnit.getMethodSignature(),
					uncoveredValidECs, uncoveredInvalidECs, uncoveredBoundaries);
		}
		
		if (errorMsg.length() > 0) {
			throw new UncoveredCasesException(errorMsg.toString());
		}
	}
	
	private void generateErrorMsg(StringBuilder errorMsg, String signature,
			List<Integer> uncoveredValidECs, List<Integer> uncoveredInvalidECs,
			List<Integer> uncoveredBoundaries) {
		
		if (CollectionUtils.isNotEmpty(uncoveredValidECs)
				|| CollectionUtils.isNotEmpty(uncoveredInvalidECs)
				|| CollectionUtils.isNotEmpty(uncoveredBoundaries)) {
			errorMsg.append(">>> Uncovered");
			errorMsg.append("\nmethodSignature: " + signature);
			if (!uncoveredValidECs.isEmpty()) {
				errorMsg.append("\nvalidECs: " + uncoveredValidECs);
			}
			if (!uncoveredInvalidECs.isEmpty()) {
				errorMsg.append("\ninvalidECs: " + uncoveredInvalidECs);
			}
			if (!uncoveredBoundaries.isEmpty()) {
				errorMsg.append("\nboundaries: " + uncoveredBoundaries);
			}
		}
	}
	
}
