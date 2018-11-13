package com.upgrade.api.campsite.utility;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.upgrade.api.campsite.model.Reservation;

public class CommonUtils {

	public static boolean validateDate(String stringDate) {
		if (stringDate.length() == 0) {
			return false;
		}
		try {
			LocalDate date = LocalDate.parse(stringDate);

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean validateEmailAddress(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	public static boolean validateFullName(String fullName) {
		if (fullName.length() == 0) {
			return false;
		}
		String arr[] = fullName.split(" ");
		if (arr.length < 2 || arr.length > 3) {
			return false;
		}
		return true;
	}

	public static LocalDate parseStringToDate(String stringDate) {
		LocalDate date = LocalDate.parse(stringDate);
		return date;
	}

	public static boolean isValidStartDate(Date date) {

		Date today = java.sql.Date.valueOf(java.time.LocalDate.now());

		// You can reserve the campsite minimum 1 days before the date of arrival
		if (date.equals(today)) {
			return false;
		}

		// You cannot make a reservation in the past
		else if (date.before(today)) {
			return false;
		}

		LocalDate lastAllowedLocalDate = LocalDate.now().plusMonths(1);
		java.util.Date lastAllowedDate = java.sql.Date.valueOf(lastAllowedLocalDate);

		// You can reserve a month maximum in advance
		if (date.after(lastAllowedDate)) {
			return false;
		}

		return true;
	}

	public static boolean compareStartEndDates(Date startDate, Date endDate) {

		// endDate needs to be the same or later than startDate
		if (endDate.before(startDate)) {
			return false;
		}
		long days = endDate.getTime() - startDate.getTime();
		days = TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);

		// Campsite can be reserved for maximum 3 days
		System.out.println("Days = " + days);
		if (days > 3) {

			return false;
		}

		return true;
	}

	public static boolean isValidEndDate(Date date) {

		// You can reserve a month in advance

		LocalDate lastAllowedLocalDate = LocalDate.now().plusMonths(1);
		java.util.Date lastAllowedDate = java.sql.Date.valueOf(lastAllowedLocalDate);

		// You can reserve a month maximum in advance
		if (date.after(lastAllowedDate)) {
			return false;
		}

		// You cannot make a reservation in the past
		Date today = java.sql.Date.valueOf(java.time.LocalDate.now());
		return !(date.before(today));
	}

	public static boolean isValidDateRange(Map<String, Reservation> reservationsDb, Date startDate, Date endDate) {

		System.out.println("reservationsDb ==> " + reservationsDb);

		for (Map.Entry<String, Reservation> entry : reservationsDb.entrySet()) {

			Reservation reservation = entry.getValue();
			if (startDate.after(reservation.getEndDate()) || endDate.before(reservation.getStartDate())) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

}
