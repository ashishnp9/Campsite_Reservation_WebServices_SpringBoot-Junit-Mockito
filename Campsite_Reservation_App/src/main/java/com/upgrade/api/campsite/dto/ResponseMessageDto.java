package com.upgrade.api.campsite.dto;

import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * @author Ashish.Patel
 *
 */

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseMessageDto {

	private int responseCode;

	private boolean isError;

	private String responseMessage;

	private String errorDiscription;
	
	private HttpHeaders httpHeaders;
	
	private Object responseObject;

}
