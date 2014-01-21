package org.swordess.test.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.swordess.test.model.TestUnitDescriptor.TestUnitDescriptorBuilder;


public class Descriptors {

	public static List<TestUnitDescriptor> combine(
			Collection<TestCaseDescriptor> testCaseDescriptors) {
		if (null == testCaseDescriptors) {
			return new ArrayList<>();
		}
		
		List<TestUnitDescriptor> combinedTestUnitDescriptors = new ArrayList<>();
		
		Map<String, List<TestCaseDescriptor>> signatureToCaseDescriptors = divideIntoGroupsAccordingToMethodSignature(testCaseDescriptors);
		for (String signature : signatureToCaseDescriptors.keySet()) {
			List<Integer> validECs = new ArrayList<>();
			List<Integer> invalidECs = new ArrayList<>();
			List<Integer> boundaries = new ArrayList<>();
			for (TestCaseDescriptor caseDescriptor : signatureToCaseDescriptors.get(signature)) {
				validECs.addAll(caseDescriptor.getValidECs());
				invalidECs.addAll(caseDescriptor.getInvalidECs());
				boundaries.addAll(caseDescriptor.getBoundaries());
			}
			TestUnitDescriptorBuilder builder = new TestUnitDescriptorBuilder(signature);
			builder.validECs(validECs).invalidECs(invalidECs).boundaries(boundaries);
			combinedTestUnitDescriptors.add(builder.build());
		}
		
		return combinedTestUnitDescriptors;
	}

	public static TestUnitDescriptor select(Collection<TestUnitDescriptor> descriptors, String signature) {
		if (null == signature) {
			throw new IllegalArgumentException("signature should not be null");
		}
		
		if (null != descriptors) {
			for (TestUnitDescriptor descriptor : descriptors) {
				if (signature.equals(descriptor.getMethodSignature())) {
					return descriptor;
				}
			}
		}
		
		return null;
	}
	
	private static Map<String, List<TestCaseDescriptor>> divideIntoGroupsAccordingToMethodSignature(
			Collection<TestCaseDescriptor> testCaseDescriptors) {
		Map<String, List<TestCaseDescriptor>> signatureToCaseDescriptors = new HashMap<>();
		for (TestCaseDescriptor caseDescriptor : testCaseDescriptors) {
			List<TestCaseDescriptor> descriptors = signatureToCaseDescriptors
					.get(caseDescriptor.getMethodSignature());
			if (null == descriptors) {
				descriptors = new ArrayList<>();
				signatureToCaseDescriptors.put(caseDescriptor.getMethodSignature(), descriptors);
			}
			descriptors.add(caseDescriptor);
		}
		return signatureToCaseDescriptors;
	}
	
	private Descriptors() {
	}
	
}
