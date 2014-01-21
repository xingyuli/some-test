package org.swordess.test.model;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.swordess.test.Cover;
import org.swordess.test.TestCaseAnalysis;
import org.swordess.test.TestCaseAnalysis.MethodAnalysis;

public class TestClassDescriptor {

	private final TestClass testClass;
	
	// expected test units
	private List<TestUnitDescriptor> expectedDescriptors = new ArrayList<>();
	
	// covered test cases
	private List<TestCaseDescriptor> coveredDescriptors = new ArrayList<>();
	
	public TestClassDescriptor(TestClass testClass) {
		this.testClass = testClass;
		
		TestCaseAnalysis testCaseAnalysis = getAnnotation(TestCaseAnalysis.class);
		if (null == testCaseAnalysis) {
			throw new IllegalStateException(TestCaseAnalysis.class
					+ " not present on target test class "
					+ testClass.getName());
		}
		
		extractExpectedTestUnits();
		extractCoveredTestUnits();
	}
	
	private void extractExpectedTestUnits() {
		for (MethodAnalysis methodAnalysis : getAnnotation(TestCaseAnalysis.class).value()) {
			expectedDescriptors.add(new TestUnitDescriptor(methodAnalysis));
		}
	}
	
	private void extractCoveredTestUnits() {
		for (FrameworkMethod coveredTestCase : listCoveredTestCases()) {
			Cover cover = coveredTestCase.getAnnotation(Cover.class);
			coveredDescriptors.add(new TestCaseDescriptor(cover));
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Annotation> T getAnnotation(Class<T> clazz) {
		for (Annotation anno : testClass.getAnnotations()) {
			if (anno.annotationType() == clazz) {
				return (T) anno;
			}
		}
		return null;
	}
	
	private List<FrameworkMethod> listCoveredTestCases() {
		List<FrameworkMethod> coveredAnnotatedCases = new ArrayList<>();
		for (FrameworkMethod method : testClass.getAnnotatedMethods(Cover.class)) {
			if (null != method.getAnnotation(Test.class) && null == method.getAnnotation(Ignore.class)) {
				coveredAnnotatedCases.add(method);
			}
		}
		return coveredAnnotatedCases;
	}
	
	public List<TestUnitDescriptor> getExpectedTestUnitsDescriptors() {
		return new ArrayList<>(expectedDescriptors);
	}
	
	public List<TestCaseDescriptor> getCoveredTestCaseDescriptors() {
		return new ArrayList<>(coveredDescriptors);
	}
	
}
