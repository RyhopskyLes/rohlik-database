package com.rohlik.data.kosik.interfaces;

import java.util.List;
import java.util.function.Function;

import com.rohlik.data.kosik.objects.NavigationItem;

public interface NavigationBuilder {
public NavigationItem buildItem(String uri);
public List<NavigationItem> buildLevel(String uri);
public Function<String, List<NavigationItem>> buildNavigationLevel();
}
