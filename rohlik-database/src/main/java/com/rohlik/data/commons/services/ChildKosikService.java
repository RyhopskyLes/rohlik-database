package com.rohlik.data.commons.services;

import java.util.Set;

import com.rohlik.data.kosik.entities.ChildKosik;

public interface ChildKosikService {
public void addMissingEquiCategoryNameToAllChildren();
public Set<ChildKosik> findChildrenWithDeadEquiIds();
}
