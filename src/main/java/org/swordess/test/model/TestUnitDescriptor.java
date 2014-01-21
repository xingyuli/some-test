package org.swordess.test.model;

import java.util.ArrayList;
import java.util.List;

import org.swordess.test.Boundary;
import org.swordess.test.Builder;
import org.swordess.test.EquivalentCondition;
import org.swordess.test.EquivalentCondition.Condition;
import org.swordess.test.TestCaseAnalysis.MethodAnalysis;

public class TestUnitDescriptor {

	private final MethodAnalysis methodAnalysis;
	
	private String methodSignature;
	private List<Integer> validECs;
	private List<Integer> invalidECs;
	private List<Integer> boundaries;
	
	private TestUnitDescriptor() {
		this(null);
	}
	
	public TestUnitDescriptor(MethodAnalysis methodAnalysis) {
		this.methodAnalysis = methodAnalysis;
		
		if (null != methodAnalysis) {
			methodSignature = methodAnalysis.signature();
			if (null != methodAnalysis.equivalentConditions()) {
				extractNbrFromEquivalentConditions();
			}
			if (null != methodAnalysis.boundaries()) {
				extractNbrFromBoundaries();
			}
		}
	}
	
	private void extractNbrFromEquivalentConditions() {
		for (EquivalentCondition ec : methodAnalysis.equivalentConditions()) {
			if (null != ec.valid()) {
				validECs = new ArrayList<>();
				for (Condition condition : ec.valid()) {
					validECs.add(condition.nbr());
				}
			}
			if (null != ec.invalid()) {
				invalidECs = new ArrayList<>();
				for (Condition condition : ec.invalid()) {
					invalidECs.add(condition.nbr());
				}
			}
		}
	}
	
	private void extractNbrFromBoundaries() {
		boundaries = new ArrayList<>();
		for (Boundary boundary : methodAnalysis.boundaries()) {
			boundaries.add(boundary.nbr());
		}
	}

	private void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}

	private void setValidECs(List<Integer> validECs) {
		this.validECs = validECs;
	}

	private void setInvalidECs(List<Integer> invalidECs) {
		this.invalidECs = invalidECs;
	}

	private void setBoundaries(List<Integer> boundaries) {
		this.boundaries = boundaries;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public List<Integer> getValidECs() {
		return new ArrayList<>(validECs);
	}

	public List<Integer> getInvalidECs() {
		return new ArrayList<>(invalidECs);
	}

	public List<Integer> getBoundaries() {
		return new ArrayList<>(boundaries);
	}
	
	public static class TestUnitDescriptorBuilder implements Builder<TestUnitDescriptor> {
		
		private final String methodSignature;

		private List<Integer> validECs;
		private List<Integer> invalidECs;
		private List<Integer> boundaries;
		
		TestUnitDescriptorBuilder(String methodSignature) {
			this.methodSignature = methodSignature;
		}

		TestUnitDescriptorBuilder validECs(List<Integer> validECs) {
			this.validECs = validECs;
			return this;
		}

		TestUnitDescriptorBuilder invalidECs(List<Integer> invalidECs) {
			this.invalidECs = invalidECs;
			return this;
		}

		TestUnitDescriptorBuilder boundaries(List<Integer> boundaries) {
			this.boundaries = boundaries;
			return this;
		}

		@Override
		public TestUnitDescriptor build() {
			if (null == validECs) {
				validECs = new ArrayList<>();
			}
			if (null == invalidECs) {
				invalidECs = new ArrayList<>();
			}
			if (null == boundaries) {
				boundaries = new ArrayList<>();
			}
			
			TestUnitDescriptor descriptor = new TestUnitDescriptor();
			descriptor.setMethodSignature(methodSignature);
			descriptor.setValidECs(validECs);
			descriptor.setInvalidECs(invalidECs);
			descriptor.setBoundaries(boundaries);
			return descriptor;
		}
		
	}
	
}
