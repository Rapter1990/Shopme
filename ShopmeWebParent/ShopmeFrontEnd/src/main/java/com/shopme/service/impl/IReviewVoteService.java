package com.shopme.service.impl;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ReviewVote;
import com.shopme.common.entity.VoteResultDTO;
import com.shopme.common.entity.VoteType;

public interface IReviewVoteService {

	public VoteResultDTO undoVote(ReviewVote vote, Integer reviewId, VoteType voteType);
	public VoteResultDTO doVote(Integer reviewId, Customer customer, VoteType voteType);
	
}
