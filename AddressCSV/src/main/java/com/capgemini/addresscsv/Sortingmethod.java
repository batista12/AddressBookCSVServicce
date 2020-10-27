package com.capgemini.addressbookcsv;

import java.util.Comparator;

public class SortByChoice implements Comparator<Contact> {
	public enum choice {
		NAME, CITY, STATE, ZIP
	}

	public choice sortingBy;

	public SortByChoice(choice sortingBy) {
		this.sortingBy = sortingBy;
	}

	@Override
	public int compare(Contact obj1, Contact obj2) {
		switch (sortingBy) {
		case NAME:
			return (obj1.getFirstName() + obj1.getLastName()).compareTo(obj2.getFirstName() + obj2.getLastName());
		case CITY:
			return (obj1.getCity().compareTo(obj2.getCity()));
		case STATE:
			return (obj1.getState().compareTo(obj2.getState()));
		case ZIP:
			return (obj1.getZip().compareTo(obj2.getZip()));
		default:
			System.out.println("Invalid choice");
		}
		return 0;
	}
}
