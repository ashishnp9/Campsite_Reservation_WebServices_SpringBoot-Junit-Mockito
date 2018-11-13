package com.upgrade.api.campsite.dto;

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
public class ReservationDto {

	private String name;
    private String emailAddress;
    private String startDate;
    private String endDate;
    private String reservationId;
	
	
}
