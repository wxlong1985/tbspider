package com.taobao.UI;

public class ListItemDataCollection {

	private static ListItemData[] defaultLocations = {
			new ListItemData("Belgium", "near Liege", "/belgium.gif"),
			new ListItemData("Brazil", "near Salvador", "/brazil.gif"),
			new ListItemData("Colombia", "near Bogota", "/colombia.gif"),
			new ListItemData("Indonesia", "main island", "/indonesia.gif")};
			/*new ListItemData("Jamaica", "near Spanish Town",
					"flags/jamaica.gif"),
			new ListItemData("Mozambique", "near Sofala",
					"flags/mozambique.gif"),
			new ListItemData("Philippines", "near Quezon City",
					"flags/philippines.gif"),
			new ListItemData("Sao Tome", "near Santa Cruz", "flags/saotome.gif"),
			new ListItemData("Spain", "near Viana de Bolo", "flags/spain.gif"),
			new ListItemData("Suriname", "near Paramibo", "flags/suriname.gif"),
			new ListItemData("United States", "near Montgomery, Alabama",
					"flags/usa.gif"),
			new ListItemData("United States", "near Needles, California",
					"flags/usa.gif"),
			new ListItemData("United States", "near Dallas, Texas",
					"flags/usa.gif") }*/

	private ListItemData[] locations;
	private int numCountries;

	public ListItemDataCollection(ListItemData[] locations) {
		this.locations = locations;
		this.numCountries = countCountries(locations);
	}

	public ListItemDataCollection() {
		this(defaultLocations);
	}

	public ListItemData[] getLocations() {
		return (locations);
	}

	public int getNumCountries() {
		return (numCountries);
	}

	// Assumes the list is sorted by country name

	private int countCountries(ListItemData[] locations) {
		int n = 0;
		String currentCountry, previousCountry = "None";
		for (int i = 0; i < locations.length; i++) {
			currentCountry = locations[i].getCountry();
			if (!previousCountry.equals(currentCountry))
				n = n + 1;
			currentCountry = previousCountry;
		}
		return (n);
	}
}