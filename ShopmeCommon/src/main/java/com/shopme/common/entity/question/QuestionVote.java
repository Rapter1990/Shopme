package com.shopme.common.entity.question;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.entity.Customer;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "questions_votes")
@Getter
@Setter
public class QuestionVote implements Serializable{

	public static final int VOTE_UP_POINT = 1;
	public static final int VOTE_DOWN_POINT = -1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	private int votes;
	
	@Transient
	public boolean isUpvoted() {
		return this.votes == VOTE_UP_POINT;
	}

	@Transient
	public boolean isDownvoted() {
		return this.votes == VOTE_DOWN_POINT;
	}
	
	public void voteUp() {
		this.votes = VOTE_UP_POINT;
	}

	public void voteDown() {
		this.votes = VOTE_DOWN_POINT;
	}
}
