package com.rohlik.data.kosik.objects;

import java.util.Map.Entry;
import java.util.Optional;
import org.jsoup.nodes.Element;

import com.google.gson.JsonObject;

public class LinkAndName {
	private Optional<Element> element;
	private Optional<JsonObject> object;
	private Optional<String> link = Optional.empty();
	private Optional<String> name = Optional.empty();

	public LinkAndName() {
	}

	public LinkAndName(Element element) {
		this.element = Optional.ofNullable(element);
		this.link = this.element.isPresent() ? this.element.map(anchor -> anchor.attr("href")) : Optional.empty();
		this.name = this.element.isPresent() ? this.element.map(anchor -> anchor.text()) : Optional.empty();
	}

	public LinkAndName(JsonObject object) {
		this.object = Optional.ofNullable(object);
		this.link = this.object.isPresent()
				? this.object.map(theObject -> theObject.get("url")).map(urlElement -> urlElement.getAsString())
				: Optional.empty();
		this.name = this.object.isPresent()
				? this.object.map(theObject -> theObject.get("name")).map(nameElement -> nameElement.getAsString())
				: Optional.empty();
	}

	public LinkAndName(Optional<JsonObject> object, Integer one) {
		this.object = object;
		this.link = this.object.map(theObject -> theObject.get("url")).map(urlElement -> urlElement.getAsString());
		this.name = this.object.map(theObject -> theObject.get("name")).map(nameElement -> nameElement.getAsString());
	}
	
	public LinkAndName(Optional<Element> element) {
		this.element = element;
		this.link = this.element.map(anchor -> anchor.attr("href"));
		this.name = this.element.map(anchor -> anchor.text());
		
	}

	public Optional<String> getLink() {
		return this.link;
	}

	public Optional<String> getName() {
		return this.name;
	}

	public Optional<Entry<String, String>> toEntry() {
		return getLink().isPresent() && getName().isPresent()
				? Optional.of(new MapEntry<String, String>(getLink().get(), getName().get()))
				: Optional.empty();
	}

	@Override
	public String toString() {
		return "LinkAndName [link=" + this.getLink() + "name=" + this.getName() + "]";
	}

}
