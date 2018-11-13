package com.upgrade.api.campsite.service;

import com.upgrade.api.campsite.dto.ReservationDto;
import com.upgrade.api.campsite.dto.ResponseMessageDto;


/**
 * @author Ashish.Patel
 *
 */

public interface CampsiteService {

	public ResponseMessageDto create(ReservationDto dto);
	public ResponseMessageDto update(ReservationDto dto);
	public ResponseMessageDto cancel(String reservationId);
	public ResponseMessageDto getAvailableDates();
}
