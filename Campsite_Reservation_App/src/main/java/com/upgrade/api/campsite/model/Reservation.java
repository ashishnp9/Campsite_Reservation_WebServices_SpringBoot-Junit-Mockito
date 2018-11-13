package com.upgrade.api.campsite.model;

import java.util.Date;

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
public class Reservation {

	private String name;
    private String emailAddress;
    private Date startDate;
    private Date endDate;
    private String reservationId;
	
	
}