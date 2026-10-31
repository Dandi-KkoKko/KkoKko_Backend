package com.kkokko.onna.dto;

import com.kkokko.onna.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagResDto {

	private Long id;
	private String name;

	public static TagResDto from(Tag tag) {
		return new TagResDto(tag.getId(), tag.getName());
	}
}
