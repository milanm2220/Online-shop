package org.web.onlineshop.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportDto 
{
	private Double value;
	private LocalDate date;
}