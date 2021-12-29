package com.shopme.common.entity;

public class VoteResultDTO {

	private boolean successful;
	private String message;
	private int voteCount;
	
	public VoteResultDTO(boolean successful, String message, int voteCount) {
		super();
		this.successful = successful;
		this.message = message;
		this.voteCount = voteCount;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	
	public static VoteResultDTO fail(String message) {
		return new VoteResultDTO(false, message, 0);
	}
	
	public static VoteResultDTO success(String message, int voteCount) {
		return new VoteResultDTO(true, message, voteCount);
	}
}
