package com.upgrade.api.campsite.utility;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.upgrade.api.campsite.model.Reservation;

/**
 * @author Ashish.Patel
 *
 */

public class CommonUtils {

	public static boolean validateEmail(String emailAddress) {
		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

		Pattern pattern = Pattern.compile(regex);
		if (emailAddress == null)
			return false;
		return pattern.matcher(emailAddress).matches();
	}

	public static LocalDate stringtoDate(String dateInString) {
		LocalDate date = LocalDate.parse(dateInString);
		return date;
	}

	public static boolean isValidStartDate(Date date) {

		if (date.equals(java.sql.Date.valueOf(java.time.LocalDate.now()))) {
			return false;
		}

		else if (date.before(java.sql.Date.valueOf(java.time.LocalDate.now()))) {
			return false;
		}

		java.util.Date lastAllowedDate = java.sql.Date.valueOf(LocalDate.now().plusMonths(1));

		if (date.after(lastAllowedDate)) {
			return false;
		}

		return true;
	}

	public static boolean compareStartEndDate(Date startDate, Date endDate) {

		if (endDate.before(startDate)) {
			return false;
		}
		long days = endDate.getTime() - startDate.getTime();
		days = TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);

		if (days > 3) {

			return false;
		}

		return true;
	}

	public static boolean isValidEndDate(Date date) {

		java.util.Date lastAllowedDate = java.sql.Date.valueOf(LocalDate.now().plusMonths(1));

		if (date.after(lastAllowedDate)) {
			return false;
		}

		return !(date.before(java.sql.Date.valueOf(java.time.LocalDate.now())));
	}

	public static boolean validateDate(String stringDate) {
		if (stringDate.length() == 0) {
			return false;
		}
		try {
			LocalDate.parse(stringDate);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isValidDateRange(Map<String, Reservation> reservationsDb, Date startDate, Date endDate) {
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

	public static boolean nameValidation(String name) {
		if (name.length() == 0) {
			return false;
		}
		String nameArr[] = name.split(" ");
		if (nameArr.length < 2 || nameArr.length > 3) {
			return false;
		}
		return true;
	}
}
