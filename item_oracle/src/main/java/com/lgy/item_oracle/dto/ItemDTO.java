package com.lgy.item_oracle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDTO {
	private String name;
	private int price;
	private String description;
}
