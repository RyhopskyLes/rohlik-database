package com.rohlik.data.objects;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.rohlik.data.commons.exceptions.WrongCategoryIdException;

import lombok.Data;

@Data
public class Breadcrumb {
private String title;
private String link;
private List<Breadcrumb> children;

public Integer getCategoryId() throws WrongCategoryIdException {
Integer id=null;	
if(!link.isEmpty()&&link.length()>1)	{
int lastSlashIndex = this.link.lastIndexOf('/');
String linkEnd = this.link.substring(lastSlashIndex+1);
int firstHyphenIndex= linkEnd.indexOf('-');
String categoryId = linkEnd.substring(1, firstHyphenIndex);
id= NumberUtils.toInt(categoryId);	} else {throw new WrongCategoryIdException("Root link "+link+" and title "+title+" in breadcrumb.\r\n Probably used wrong categoryId in url"); }
return id;
}

}
