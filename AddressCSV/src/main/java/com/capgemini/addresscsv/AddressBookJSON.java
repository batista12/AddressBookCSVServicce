package com.capgemini.addressbookcsv;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;

/**
 * @author ASUS
 *
 */
public class AddressBookJSONS {
	private static String HOME = "C:\\Users\\ASUS\\eclipse-workspace\\AddressBookCSV\\src\\main\\java\\com\\capgemini\\addressbookcsv\\Address JSON contacts details";
	private HashMap<String, List<Contact>> addMap;

	public AddressBookJSONS() {
		addMap = new HashMap<String, List<Contact>>();
		readDataFromAddressBook();
	}

	/**
	 * 
	 */
	public void readDataFromAddressBook() {
		try {
			Files.walk(Paths.get(HOME)).filter(Files::isRegularFile).forEach(file -> {
				List<Contact> contactList = new ArrayList<Contact>();
				try {
					Reader reader = Files.newBufferedReader(file.toAbsolutePath());
					contactList.addAll(Arrays.asList(new Gson().fromJson(reader, Contact[].class)));
					String fileName = file.toAbsolutePath().toString().replace(HOME + "\\", "");
					addMap.put(fileName, contactList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * @param bName
	 * @return
	 */
	public boolean addAddressBook(String bName) {
		Path addressBooks = Paths.get(HOME + "/" + bName + ".json");
		if (Files.notExists(Paths.get(HOME + "/" + bName + ".json"))) {
			try {
				Files.createFile(addressBooks);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * @param contactObj
	 * @param addressbName
	 */
	public void writeContactToAddressBook(Contact contactObj, String addressbName) {
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(HOME + "\\" + addressbName + ".json"));
			List<Contact> contactList = addMap.get(addressbName);
			if (contactList == null) {
				contactList = new ArrayList<Contact>();
			}
			contactList.add(contactObj);
			new Gson().toJson(contactList, writer);
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void print() {
		addMap.entrySet().stream().map(entry -> entry.getValue()).forEach(System.out::println);
	}

	public HashMap<String, List<Contact>> getaddMap() {
		return addMap;
	}

	public void setaddMap(HashMap<String, List<Contact>> addMap) {
		this.addMap = addMap;
	}
}
