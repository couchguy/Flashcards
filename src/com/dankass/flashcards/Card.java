package com.dankass.flashcards;

public class Card {

	private long id;
	private String title;
	private String front;
	private String back;
	//private int setOfCards;
	
	public Card() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	//public int getSetOfCards() {
	//	return setOfCards;
	//}

	//public void setSetOfCards(int set) {
	//	this.setOfCards = set;
	//}

	@Override
	public String toString() {
		return title;
	}

}
