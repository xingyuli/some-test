package org.swordess.test.model;

import java.util.ArrayList;
import java.util.List;

import org.swordess.test.Cover;

public class TestCaseDescriptor {

	private String methodSignature;
	private List<Integer> validECs = new ArrayList<>();
	private List<Integer> invalidECs = new ArrayList<>();
	private List<Integer> boundaries = new ArrayList<>();
	
	public TestCaseDescriptor(Cover cover) {
		methodSignature = cover.methodSignature();
		if (null != cover.validECs()) {
			for (int validEC : cover.validECs()) {
				validECs.add(validEC);
			}
		}
		if (null != cover.invalidECs()) {
			for (int invalidEC : cover.invalidECs()) {
				invalidECs.add(invalidEC);
			}
		}
		if (null != cover.boundaries()) {
			for (int boundary : cover.boundaries()) {
				boundaries.add(boundary);
			}
		}
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

}
