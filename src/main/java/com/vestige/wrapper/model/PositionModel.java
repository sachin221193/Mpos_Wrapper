package com.vestige.wrapper.model;

/**
 * 
 * @author shashank.madeshiya
 *
 */
public class PositionModel {

	private String currentPosition;
	private String previousPosition;
	private String nextPosition;

	public String getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}

	public String getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(String previousPosition) {
		this.previousPosition = previousPosition;
	}

	public String getNextPosition() {
		return nextPosition;
	}

	public void setNextPosition(String nextPosition) {
		this.nextPosition = nextPosition;
	}

	@Override
	public String toString() {
		return "POsitionModel [currentPosition=" + currentPosition + ", previousPosition=" + previousPosition
				+ ", nextPosition=" + nextPosition + "]";
	}

}
