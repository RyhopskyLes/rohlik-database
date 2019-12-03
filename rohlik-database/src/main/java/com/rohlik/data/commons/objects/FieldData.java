package com.rohlik.data.commons.objects;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FieldData<T> {
	String fieldName;
	Class<T> fieldtype;
	Optional<T> fieldValue;

}
