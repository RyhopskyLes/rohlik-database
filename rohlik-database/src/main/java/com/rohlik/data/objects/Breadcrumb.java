package com.rohlik.data.objects;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import lombok.Data;

@Data
public class Breadcrumb {
private String title;
private String link;
private List<Breadcrumb> children;

public Integer getCategoryId() {
int lastSlashIndex = this.link.lastIndexOf('/');
String linkEnd = this.link.substring(lastSlashIndex+1);
int firstHyphenIndex= linkEnd.indexOf('-');
String categoryId = linkEnd.substring(1, firstHyphenIndex);
return NumberUtils.toInt(categoryId);	
}

}
