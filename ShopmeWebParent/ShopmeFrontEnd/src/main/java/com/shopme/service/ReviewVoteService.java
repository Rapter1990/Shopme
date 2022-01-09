package com.shopme.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.ReviewVote;
import com.shopme.common.entity.VoteResultDTO;
import com.shopme.common.entity.VoteType;
import com.shopme.repository.ReviewRepository;
import com.shopme.repository.ReviewVoteRepository;
import com.shopme.service.impl.IReviewVoteService;

@Service
@Transactional
public class ReviewVoteService implements IReviewVoteService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReviewVoteService.class);
 
	private ReviewRepository reviewRepo;
	
	private ReviewVoteRepository voteRepo;
	
	@Autowired
	public ReviewVoteService(ReviewRepository reviewRepo, ReviewVoteRepository voteRepo) {
		super();
		this.reviewRepo = reviewRepo;
		this.voteRepo = voteRepo;
	}

	@Override
	public VoteResultDTO undoVote(ReviewVote vote, Integer reviewId, VoteType voteType) {
		// TODO Auto-generated method stub
		
		LOGGER.info("ReviewVoteService | undoVote is called");
		
		LOGGER.info("ReviewVoteService | undoVote | vote : " + vote.toString());
		LOGGER.info("ReviewVoteService | undoVote | reviewId : " + reviewId);
		LOGGER.info("ReviewVoteService | undoVote | voteType : " + voteType);
		
		voteRepo.delete(vote);
		reviewRepo.updateVoteCount(reviewId);
		Integer voteCount = reviewRepo.getVoteCount(reviewId);
		
		LOGGER.info("ReviewVoteService | undoVote | voteCount : " + voteCount);

		return VoteResultDTO.success("You have unvoted " + voteType + " that review.", voteCount);
	}

	@Override
	public VoteResultDTO doVote(Integer reviewId, Customer customer, VoteType voteType) {
		// TODO Auto-generated method stub
		
		LOGGER.info("ReviewVoteService | doVote is called");
		
		LOGGER.info("ReviewVoteService | doVote | customer : " + customer.getFullName());
		LOGGER.info("ReviewVoteService | doVote | voteType : " + voteType);
		
		Review review = null;

		try {
			review = reviewRepo.findById(reviewId).get();
			LOGGER.info("ReviewVoteService | doVote | review : " + review.toString());
			
		} catch (NoSuchElementException ex) {
			LOGGER.info("ReviewVoteService | doVote | ex : " + ex.toString());
			return VoteResultDTO.fail("The review ID " + reviewId + " no longer exists.");
		}
		
		ReviewVote vote = voteRepo.findByReviewAndCustomer(reviewId, customer.getId());
		
		if (vote != null) {
			if (vote.isUpvoted() && voteType.equals(VoteType.UP) || 
					vote.isDownvoted() && voteType.equals(VoteType.DOWN)) {
				LOGGER.info("ReviewVoteService | doVote | vote != null | undoVote ");
				return undoVote(vote, reviewId, voteType);
			} else if (vote.isUpvoted() && voteType.equals(VoteType.DOWN)) {
				LOGGER.info("ReviewVoteService | doVote | vote != null | voteDown ");
				vote.voteDown();
			} else if (vote.isDownvoted() && voteType.equals(VoteType.UP)) {
				LOGGER.info("ReviewVoteService | doVote | vote != null | voteUp ");
				vote.voteUp();
			}
			
		}else {
			
			LOGGER.info("ReviewVoteService | doVote | vote == null");
			
			vote = new ReviewVote();
			vote.setCustomer(customer);
			vote.setReview(review);
			
			LOGGER.info("ReviewVoteService | doVote | vote : " + vote.toString());

			if (voteType.equals(VoteType.UP)) {
				LOGGER.info("ReviewVoteService | doVote | voteType : " + voteType);
				vote.voteUp();
			} else {
				LOGGER.info("ReviewVoteService | doVote | voteType : " + voteType);
				vote.voteDown();
			}
		}
		
		voteRepo.save(vote);
		reviewRepo.updateVoteCount(reviewId);
		Integer voteCount = reviewRepo.getVoteCount(reviewId);
		
		LOGGER.info("ReviewVoteService | doVote | voteCount : " + voteCount);
		
		return VoteResultDTO.success("You have successfully voted " + voteType + " that review.", 
				voteCount);
	}
	
	public void markReviewsVotedForProductByCustomer(List<Review> listReviews, Integer productId,
			Integer customerId) {
		
		LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer is called");
		
		LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | listReviews : " + listReviews.toString());
		LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | productId : " + productId);
		LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | customerId : " + customerId);
		
		
		List<ReviewVote> listVotes = voteRepo.findByProductAndCustomer(productId, customerId);
		
		LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | listVotes : " + listVotes.toString());

		for (ReviewVote vote : listVotes) {
			Review votedReview = vote.getReview();
			
			LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | votedReview : " + votedReview.toString());

			LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | "
					+ "listReviews.contains(votedReview) : " + (listReviews.contains(votedReview)));
			
			if (listReviews.contains(votedReview)) {
				int index = listReviews.indexOf(votedReview);
				
				LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | index : " + index);
				
				Review review = listReviews.get(index);
				
				LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | review : " + review.toString());

				if (vote.isUpvoted()) {
					LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | vote.isUpvoted() : " + (vote.isUpvoted()));
					review.setUpvotedByCurrentCustomer(true);
				} else if (vote.isDownvoted()) {
					LOGGER.info("ReviewVoteService | markReviewsVotedForProductByCustomer | vote.isDownvoted() : " + (vote.isDownvoted()));
					review.setDownvotedByCurrentCustomer(true);
				}
			}
		}
	}

}
