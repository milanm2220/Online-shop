package org.web.onlineshop.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web.onlineshop.dto.CartDto;
import org.web.onlineshop.dto.ItemDto;
import org.web.onlineshop.dto.ReportDto;
import org.web.onlineshop.model.Cart;
import org.web.onlineshop.model.Item;
import org.web.onlineshop.service.CartService;
import org.web.onlineshop.util.Constants;
import org.web.onlineshop.util.OrderStatus;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/carts")
public class CartController 
{		
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ItemDto>> getCartItems(@PathVariable Long id)
	{
		try
		{
			Cart cart = this.cartService.findById(id);
			List<Item> items = new ArrayList<>(cart.getItems());
			List<ItemDto> itemDtos = new ArrayList<>();
			items.stream().forEach(item ->
			{
				itemDtos.add(modelMapper.map(item, ItemDto.class));
			});
			return new ResponseEntity<>(itemDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/ordered", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CartDto>> getOrdersInOrderedState()
	{
		try
		{
			List<Cart> orders = this.cartService.findByState(OrderStatus.ORDERED);
			List<CartDto> orderDtos = new ArrayList<>();
			orders.stream().forEach(order ->
			{
				orderDtos.add(modelMapper.map(order, CartDto.class));					
			});
			return new ResponseEntity<>(orderDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/delivered", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CartDto>> getDeliveredOrdersInPeriod(@RequestParam(value = "startDate") String startDateString, @RequestParam(value = "endDate") String endDateString)
	{
		LocalDate startDate = LocalDate.parse(startDateString);
		LocalDate endDate = LocalDate.parse(endDateString);
		
		if (startDate.isAfter(endDate))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try
		{
			List<Cart> orders = this.cartService.findByState(OrderStatus.DELIVERED);
			List<CartDto> orderDtos = new ArrayList<>();
			orders.stream().forEach(order ->
			{
				if ((order.getTimestamp().toLocalDate().isAfter(startDate) || order.getTimestamp().toLocalDate().isEqual(startDate)) &&
					(order.getTimestamp().toLocalDate().isBefore(endDate) || order.getTimestamp().toLocalDate().isEqual(endDate)))
				{					
					orderDtos.add(modelMapper.map(order, CartDto.class));					
				}
			});
			return new ResponseEntity<>(orderDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/canceled", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ReportDto>> getNumberOfCanceledOrdersInPeriod(@RequestParam(value = "startDate") String startDateString, @RequestParam(value = "endDate") String endDateString)
	{
		LocalDate startDate = LocalDate.parse(startDateString);
		LocalDate endDate = LocalDate.parse(endDateString);

		if (startDate.isAfter(endDate))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try
		{
			List<Cart> orders = this.cartService.findByState(OrderStatus.CANCELED);
			List<ReportDto> reportDtos = new ArrayList<>();
			while (!startDate.isAfter(endDate))
			{
				int numOfCanceledOrders = 0;
				for (Cart order : orders)
				{
					if (order.getTimestamp().toLocalDate().isEqual(startDate))
					{
						numOfCanceledOrders++;
					}
				}
				ReportDto reportDto = new ReportDto((double)numOfCanceledOrders, startDate);
				reportDtos.add(reportDto);
				startDate = startDate.plusDays(1);
			}
			return new ResponseEntity<>(reportDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/income", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ReportDto>> getIncomeInPeriod(@RequestParam(value = "startDate") String startDateString, @RequestParam(value = "endDate") String endDateString)
	{
		LocalDate startDate = LocalDate.parse(startDateString);
		LocalDate endDate = LocalDate.parse(endDateString);
		
		if (startDate.isAfter(endDate))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try
		{
			List<Cart> orders = this.cartService.findByState(OrderStatus.DELIVERED);
			List<ReportDto> incomeReportDtos = new ArrayList<>();
			while (!startDate.isAfter(endDate))
			{
				double income = 0.0;
				for (Cart order : orders)
				{
					if (order.getTimestamp().toLocalDate().isEqual(startDate))
					{
						income += order.getTotalPrice();
					}
				}
				ReportDto incomeReportDto = new ReportDto(income, startDate);
				incomeReportDtos.add(incomeReportDto);
				startDate = startDate.plusDays(1);
			}
			return new ResponseEntity<>(incomeReportDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
}