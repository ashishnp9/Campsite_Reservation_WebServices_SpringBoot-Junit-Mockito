package com.upgrade.api.campsite.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.upgrade.api.campsite.dto.ReservationDto;
import com.upgrade.api.campsite.dto.ResponseMessageDto;
import com.upgrade.api.campsite.model.Reservation;
import com.upgrade.api.campsite.utility.CommonUtils;
import com.upgrade.api.campsite.utility.constants.CommonConstants;

@Service
public class CampsiteServiceImpl implements CampsiteService {

	private static AtomicLong idCounter = new AtomicLong();
	private static ConcurrentHashMap<String, Reservation> reservationDb;

	static {
		createReservationDb();
	}
	
	@Override
	public ResponseMessageDto getAvailableDates() {
		List<String> dates = getDates();
		ResponseMessageDto dto = new ResponseMessageDto();
		dto.setError(false);
		dto.setResponseCode(HttpStatus.OK.value());
		dto.setResponseMessage(CommonConstants.AVAILABLE_DATE_SUCCESS);
		dto.setResponseObject(dates);
		return dto;
	}

	@Override
	public ResponseMessageDto create(ReservationDto dto) {
		
		ResponseMessageDto responseMessageDto = validateData(dto);
		if (!responseMessageDto.isError()) {
			String reservationId = "";
			synchronized (this) {
				reservationId = String.valueOf(idCounter.getAndIncrement());
			}
			Reservation reservation = new Reservation();
			reservation.setEmailAddress(dto.getEmailAddress());
			reservation.setName(dto.getName());
			reservation.setStartDate(java.sql.Date.valueOf(CommonUtils.stringtoDate(dto.getStartDate())));
			reservation.setEndDate(java.sql.Date.valueOf(CommonUtils.stringtoDate(dto.getEndDate())));
			reservation.setReservationId(reservationId);
			reservationDb.put(reservationId, reservation);
			responseMessageDto.setResponseCode(HttpStatus.OK.value());
			responseMessageDto.setResponseMessage(CommonConstants.RESERVATION_SUCCESS+reservationId);
							
		} else {
			responseMessageDto.setResponseCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
			responseMessageDto.setResponseMessage(CommonConstants.VALIDATION_FAIL_MESSAGE);
		}

		return responseMessageDto;
	}

	@Override
	public ResponseMessageDto update(ReservationDto dto) {
		ResponseMessageDto messageDto = new ResponseMessageDto();
		if (reservationDb.containsKey(dto.getReservationId())) {
			messageDto = validateData(dto);
			if (!messageDto.isError()) {

				Reservation reservation = reservationDb.get(dto.getReservationId());
				reservation.setEmailAddress(dto.getEmailAddress());
				reservation.setName(dto.getName());
				reservation.setStartDate(java.sql.Date.valueOf(CommonUtils.stringtoDate(dto.getStartDate())));
				reservation.setEndDate(java.sql.Date.valueOf(CommonUtils.stringtoDate(dto.getEndDate())));
				reservationDb.put(dto.getReservationId(), reservation);
				messageDto.setResponseCode(HttpStatus.OK.value());
				messageDto.setResponseMessage(CommonConstants.UPDATE_RESERVATION_SUCCESS);

			} else {
				messageDto.setResponseCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
				messageDto.setResponseMessage(CommonConstants.VALIDATION_FAIL_MESSAGE);
			}
		} else {
			messageDto.setError(true);
			messageDto.setErrorDiscription(CommonConstants.RESERVATION_ID_NOT_AVAILABLE);
			messageDto.setResponseCode(HttpStatus.NOT_FOUND.value());
			messageDto.setResponseMessage(CommonConstants.RESERVATION_ID_NOT_FOUND);
		}
		return messageDto;
	}

	@Override
	public ResponseMessageDto cancel(String reservationId) {
		ResponseMessageDto messageDto = new ResponseMessageDto();
		if (reservationDb.containsKey(reservationId)) {
			reservationDb.remove(reservationId);
			messageDto.setError(false);
			messageDto.setResponseCode(HttpStatus.NO_CONTENT.value());
			messageDto.setResponseMessage(CommonConstants.RESERVATION_DELETED_SUCCESS);
		} else {
			messageDto.setError(true);
			messageDto.setErrorDiscription(CommonConstants.RESERVATION_ID_NOT_AVAILABLE);
			messageDto.setResponseCode(HttpStatus.NOT_FOUND.value());
			messageDto.setResponseMessage(CommonConstants.RESERVATION_ID_NOT_FOUND);
		}
		return messageDto;
	}
	
	private ResponseMessageDto validateData(ReservationDto dto) {
		ResponseMessageDto rDto = new ResponseMessageDto();
		Date startDateFormatted = null, endDateFormatted = null;
		rDto.setError(true);
		if (CommonUtils.validateDate(dto.getStartDate()) && CommonUtils.validateDate(dto.getEndDate())) {
			startDateFormatted = java.sql.Date.valueOf(CommonUtils.stringtoDate(dto.getStartDate()));
			endDateFormatted = java.sql.Date.valueOf(CommonUtils.stringtoDate(dto.getEndDate()));
			if (CommonUtils.isValidStartDate(startDateFormatted)) {
				if (CommonUtils.isValidEndDate(endDateFormatted)) {
					if (CommonUtils.compareStartEndDate(startDateFormatted, endDateFormatted)) {
						if (CommonUtils.validateEmail(dto.getEmailAddress())) {
							if (CommonUtils.nameValidation(dto.getName())) {
								if (CommonUtils.isValidDateRange(reservationDb, startDateFormatted, endDateFormatted)) {
									rDto.setError(false);

								} else {
									rDto.setErrorDiscription(CommonConstants.TIMEFRAME_RESERVED);
								}
							} else {
								rDto.setErrorDiscription(CommonConstants.NAME_MESSAGE);
							}
						} else {
							rDto.setErrorDiscription(CommonConstants.VALID_EMAIL);
						}

					} else {
						rDto.setErrorDiscription(CommonConstants.START_END_DATE_COMBINATION);
					}
				} else {
					rDto.setErrorDiscription(CommonConstants.END_DATE_INVALID);
				}
			} else {
				rDto.setErrorDiscription(CommonConstants.START_DATE_INVALID);
			}

		} else {
			rDto.setErrorDiscription(CommonConstants.START_END_DATE_FORMAT_INVALID);
		}

		return rDto;
	}
	
	private static void createReservationDb() {

		reservationDb = new ConcurrentHashMap<>();
	}
	
	private List<String> getDates() {
		List<String> dates = new ArrayList<>();
		Set<Date> reservedDates = new HashSet<>();
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		
		for (Map.Entry<String, Reservation> entry : reservationDb.entrySet()) {
			Reservation reservation = entry.getValue();

			startDate.setTime(reservation.getStartDate());
			endDate.setTime(reservation.getEndDate());
			while (!startDate.after(endDate)) {
				Date date = startDate.getTime();
				reservedDates.add(date);
				startDate.add(Calendar.DATE, 1);
			}
		}
		
		LocalDate localToday_date = java.time.LocalDate.now();
		localToday_date = localToday_date.plusDays(1);
		Date today = java.sql.Date.valueOf(localToday_date);
		LocalDate lastAllowedLocalDate = LocalDate.now().plusMonths(1);
		Date lastAllowedDate = java.sql.Date.valueOf(lastAllowedLocalDate);
		Calendar startDate1 = Calendar.getInstance();
		Calendar endDate1 = Calendar.getInstance();
		startDate1.setTime(today);
		endDate1.setTime(lastAllowedDate);
		while (!startDate1.after(endDate1)) {
			Date date = startDate1.getTime();
			if (!reservedDates.contains(date)) {
				String available_date = date.toString();
				available_date = available_date.substring(4, 10);
				dates.add("[ "+available_date+" ]");
			}
			startDate1.add(Calendar.DATE, 1);
		}
		return dates;
	}

	

}
