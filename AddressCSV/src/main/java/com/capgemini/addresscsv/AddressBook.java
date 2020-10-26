import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AddressBookMain {
	Scanner sc = new Scanner(System.in);
	AddressBookCSVService addressBookCSVService = new AddressBookCSVService();
	private static List<Contact> addressList = new ArrayList<Contact>();
	private static HashMap<String, List<Contact>> addressBookMap = new HashMap<String, List<Contact>>();
	HashMap<Contact, String> personCityMap = new HashMap<Contact, String>();
	HashMap<Contact, String> personStateMap = new HashMap<Contact, String>();
	private String addressListName;
	
	public void setAddressListName(String listName) {
		this.addressListName = listName;
	}
	public boolean addContact(Contact contactObj) {
		boolean isPresent = addressList.stream().anyMatch(obj -> obj.equals(contactObj));
		if (isPresent == false) {
			addressList.add(contactObj);
			new AddressBookCSVService().writeContactToAddressBook(contactObj, addressListName);
			System.out.println("Contact added");
			return true;
		} else {
			System.out.println("Contact already present. Duplication not allowed");
			return false;
		}
	}
	public boolean editDetails(String firstName, String lastName) {
		Contact editObj;
		boolean contactFound = false;
		for (int i = 0; i < addressList.size(); i++) {
			editObj = (Contact) addressList.get(i);
			if ((editObj.getFirstName().equals(firstName)) && (editObj.getLastName().equals(lastName))) {
				System.out.println("Enter new Address:");
				editObj.setAddress(sc.nextLine());
				System.out.println("Enter new City");
				editObj.setCity(sc.nextLine());
				System.out.println("Enter new State");
				editObj.setState(sc.nextLine());
				System.out.println("Enter new Zip");
				editObj.setZip(sc.nextLine());
				System.out.println("Enter new Phone no");
				editObj.setPhoneNo(sc.nextLine());
				System.out.println("Enter new Email");
				editObj.setEmail(sc.nextLine());
				contactFound = true;
				break;
			}
		}
		return contactFound;
	}
	public boolean removeDetails(String firstName, String lastName) {
		Contact removeObj;
		boolean contactFound = false;
		for (int i = 0; i < addressList.size(); i++) {
			removeObj = (Contact) addressList.get(i);
			if ((removeObj.getFirstName().equals(firstName)) && (removeObj.getLastName().equals(lastName))) {
				addressList.remove(i);
				contactFound = true;
				break;
			}
		}
		return contactFound;
	}
	public void addAddressList(String listName) {
		List<Contact> newAddressList = new LinkedList<Contact>();
		addressBookMap.put(listName, newAddressList);
		boolean isAddressBookAdded = new AddressBookCSVService().addAddressBook(listName);
		if (isAddressBookAdded)
			System.out.println("Address book added");
		else
			System.out.println("Address book not added. Might already be present");
		addressListName = listName;
	}
	public static boolean checkForDuplicate(String first, String last) {
		if (addressList.stream().anyMatch(obj -> (obj.getFirstName() + obj.getFirstName()).equals(first + last))) {
			System.out.println("This contact already exists, try again!!");
			return true;
		} else
			return false;
	}
	private void searchPersonAcrossCityState(String searchPerson, int searchChoice, String cityOrState) {
		for (Map.Entry<String, List<Contact>> entry : addressBookMap.entrySet()) {
			List<Contact> list = entry.getValue();
			if (searchChoice == 1)
				list.stream().filter(
						obj -> ((obj.getCity().equals(cityOrState)) && (obj.getFirstName().equals(searchPerson))))
						.forEach(System.out::println);
			else if (searchChoice == 2)
				list.stream().filter(
						obj -> ((obj.getState().equals(cityOrState)) && (obj.getFirstName().equals(searchPerson))))
						.forEach(System.out::println);
		}
	}
	private void addToDictionary(boolean contactIsAdded, Contact contactObj) {
		if (contactIsAdded == true) {
			personCityMap.put(contactObj, contactObj.getCity());
			personStateMap.put(contactObj, contactObj.getState());
		}
	}

	private void viewPersonsByCityState(String cityOrState, int searchChoice) {
		for (Map.Entry<String, List<Contact>> entry : addressBookMap.entrySet()) {
			List<Contact> list = entry.getValue();
			if (searchChoice == 1)
				list.stream().filter(obj -> obj.getCity().equals(cityOrState)).forEach(System.out::println);
			else if (searchChoice == 2)
				list.stream().filter(obj -> obj.getState().equals(cityOrState)).forEach(System.out::println);
		}
	}
	private long getCountByCityState(String cityOrState, int searchChoice) {
		long count = 0;
		for (Map.Entry<String, List<Contact>> entry : addressBookMap.entrySet()) {
			List<Contact> list = entry.getValue();
			if (searchChoice == 1)
				count += list.stream().filter(obj -> obj.getCity().equals(cityOrState)).count();
			else if (searchChoice == 2)
				count += list.stream().filter(obj -> obj.getState().equals(cityOrState)).count();
		}
		return count;
	}
	private List<Contact> sortAddressBookByName(List<Contact> sortList) {
		SortByChoice sortByChoice = new SortByChoice(SortByChoice.choice.NAME);
		Collections.sort(sortList, sortByChoice);
		return sortList;
	}
	private List<Contact> sortAddressBookByChoice(int sortChoice, List<Contact> sortList) {
		SortByChoice sortByChoice = null;
		switch (sortChoice) {
		case 1:
			sortByChoice = new SortByChoice(SortByChoice.choice.CITY);
			break;
		case 2:
			sortByChoice = new SortByChoice(SortByChoice.choice.STATE);
			break;
		case 3:
			sortByChoice = new SortByChoice(SortByChoice.choice.ZIP);
			break;
		default:
			System.out.println("Invalid Choice");
		}
		Collections.sort(sortList, sortByChoice);
		return sortList;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		AddressBookMain addressObj = new AddressBookMain();
		int choice = 0;

		while (choice != 12) {
			if (addressObj.addressBookMap.isEmpty()) {
				System.out.println("Please add an address book to begin");
				System.out.println("Enter the name of address book that u want to add:");
				String listName = sc.nextLine();
				addressObj.addAddressList(listName);
			}

			System.out.println("Enter the name of the address book you want to access");
			String listName = sc.nextLine();
			if (addressObj.addressBookMap.containsKey(listName)) {
				addressObj.addressList = addressObj.addressBookMap.get(listName);
				addressObj.setAddressListName(listName);
			}

			else {
				System.out.println("Address list with name " + listName + " not present. Please add it first.");
			}

			System.out.println(
					"Enter a choice: \n 1)Add a new contact \n 2)Edit a contact \n 3)Delete Contact \n 4)Add Address Book \n 5)View current Address Book Contacts"
							+ " \n 6)Search person in a city or state across the multiple Address Books \n 7)View persons by city or state \n "
							+ "8)Get count of contact persons by city or state \n 9)Sort entries by name in current address book"
							+ "\n 10)Sort entries in current address book by city, state or zip \n 11)View all contacts from file \n 12)Exit");
			choice = Integer.parseInt(sc.nextLine());
			switch (choice) {
			case 1: {
				System.out.println("Add Person Details:");
				System.out.println("First Name:");
				String firstName = sc.nextLine();
				System.out.println("Last Name:");
				String lastName = sc.nextLine();
				if (checkForDuplicate(firstName, lastName))
					continue;
				System.out.println("Address:");
				String address = sc.nextLine();
				System.out.println("City:");
				String city = sc.nextLine();
				System.out.println("State:");
				String state = sc.nextLine();
				System.out.println("Zip:");
				String zip = sc.nextLine();
				System.out.println("Phone no:");
				String phoneNo = sc.nextLine();
				System.out.println("Email");
				String email = sc.nextLine();

				Contact contactObj = new Contact(firstName, lastName, address, city, state, zip, phoneNo, email);
				boolean contactIsAdded = addressObj.addContact(contactObj);
				addressObj.addToDictionary(contactIsAdded, contactObj);
				break;
			}
			case 2: {
				System.out.println(
						"Enter first name, press Enter key, and then enter last name of person to edit details:");
				String firstName = sc.nextLine();
				String lastName = sc.nextLine();
				boolean contactFound = addressObj.editDetails(firstName, lastName);
				if (contactFound == true)
					System.out.println("Details successfully edit");
				else
					System.out.println("Contact not found");
				break;
			}
			case 3: {
				System.out.println(
						"Enter first name, press Enter key, and then enter last name of person to delete data");
				String firstName = sc.nextLine();
				String lastName = sc.nextLine();
				boolean contactFound = addressObj.removeDetails(firstName, lastName);
				if (contactFound == true)
					System.out.println("Details successfully deleted");
				else
					System.out.println("Contact not found");
				break;
			}
			case 4: {
				System.out.println("Enter the name of address book that u want to add:");
				listName = sc.nextLine();
				addressObj.addAddressList(listName);
				break;
			}
			case 5: {
				System.out.println(" " + addressObj.addressList);
				break;
			}
			case 6: {
				System.out.println("Enter first name of person to search");
				String searchPerson = sc.nextLine();
				System.out.println("Enter the name of city or state");
				String cityOrState = sc.nextLine();
				System.out.println("Enter 1 if you entered name of a city \nEnter 2 if you entered name of a state");
				int searchChoice = Integer.parseInt(sc.nextLine());
				addressObj.searchPersonAcrossCityState(searchPerson, searchChoice, cityOrState);
				break;
			}
			case 7: {
				System.out.println("Enter the name of city or state");
				String cityOrState = sc.nextLine();
				System.out.println("Enter 1 if you entered name of a city \nEnter 2 if you entered name of a state");
				int searchChoice = Integer.parseInt(sc.nextLine());
				addressObj.viewPersonsByCityState(cityOrState, searchChoice);
				break;
			}
			case 8: {
				System.out.println("Enter the name of city or state");
				String cityOrState = sc.nextLine();
				System.out.println("Enter 1 if you entered name of a city \nEnter 2 if you entered name of a state");
				int searchChoice = Integer.parseInt(sc.nextLine());
				System.out.println("Total persons in " + cityOrState + " = "
						+ addressObj.getCountByCityState(cityOrState, searchChoice));
				break;
			}
			case 9: {
				List<Contact> sortedEntriesList = addressObj.sortAddressBookByName(addressObj.addressList);
				System.out.println("Entries sorted in current address book. Sorted Address Book Entries:");
				System.out.println(sortedEntriesList);
				break;
			}
			case 10: {
				System.out.println("Enter 1 to sort by city \nEnter 2 to sort by state \nEnter 3 to sort by zipcode");
				int sortChoice = Integer.parseInt(sc.nextLine());
				List<Contact> sortedEntriesList = addressObj.sortAddressBookByChoice(sortChoice,
						addressObj.addressList);
				System.out.println(sortedEntriesList);
				break;
			}
			case 11: {
				addressObj.addressBookCSVService.print();
				break;
			}
			case 12: {
				System.out.println("Thank you for using the application");
			}
			}
		}
	}
}
