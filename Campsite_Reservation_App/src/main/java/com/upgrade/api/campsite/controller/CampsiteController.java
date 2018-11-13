package com.upgrade.api.campsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.upgrade.api.campsite.dto.ReservationDto;
import com.upgrade.api.campsite.dto.ResponseMessageDto;
import com.upgrade.api.campsite.service.CampsiteService;
import com.upgrade.api.campsite.utility.constants.CommonConstants;
/**
 * @author Ashish.Patel
 *
 */

@RestController
@RequestMapping(CommonConstants.CAMPSITE)
public class CampsiteController {

	@Autowired
	CampsiteService campsiteService;
	
	@GetMapping("/dates")
	public ResponseEntity<ResponseMessageDto> getAvailableDates() {
		return new ResponseEntity<ResponseMessageDto>(campsiteService.getAvailableDates(), HttpStatus.OK);
	}

	@PostMapping(CommonConstants.RESERVATION)
	public ResponseEntity<ResponseMessageDto> create(@RequestBody ReservationDto dto, UriComponentsBuilder ucBuilder) {

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path(CommonConstants.CAMPSITE+CommonConstants.RESERVATION).buildAndExpand().toUri());
		ResponseMessageDto messageDto = campsiteService.create(dto);
		messageDto.setHttpHeaders(headers);
		if(!messageDto.isError()) {
			return new ResponseEntity<ResponseMessageDto>(messageDto, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<ResponseMessageDto>(messageDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		

	}
	
	@PutMapping(CommonConstants.RESERVATION +"/{reservationId}")
	public ResponseEntity<ResponseMessageDto> update(@PathVariable("reservationId") final String reservationId,@RequestBody ReservationDto dto, UriComponentsBuilder ucBuilder) {

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path(CommonConstants.CAMPSITE+CommonConstants.RESERVATION+"/"+reservationId).buildAndExpand().toUri());
		dto.setReservationId(reservationId);
		ResponseMessageDto messageDto = campsiteService.update(dto);
		messageDto.setHttpHeaders(headers);
		if(!messageDto.isError()) {
			return new ResponseEntity<ResponseMessageDto>(messageDto, HttpStatus.OK);
		}else {
			return new ResponseEntity<ResponseMessageDto>(messageDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	@DeleteMapping(CommonConstants.RESERVATION +"/{reservationId}")
	public ResponseEntity<ResponseMessageDto> cancel(@PathVariable("reservationId") final String reservationId, UriComponentsBuilder ucBuilder) {

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path(CommonConstants.CAMPSITE+CommonConstants.RESERVATION+"/"+reservationId).buildAndExpand().toUri());
		ResponseMessageDto messageDto = campsiteService.cancel(reservationId);
		messageDto.setHttpHeaders(headers);
		if(!messageDto.isError()) {
			return new ResponseEntity<ResponseMessageDto>(messageDto, HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<ResponseMessageDto>(messageDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	

}
