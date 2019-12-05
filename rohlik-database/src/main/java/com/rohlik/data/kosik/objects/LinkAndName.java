package com.rohlik.data.kosik.objects;

import java.util.Optional;
import org.jsoup.nodes.Element;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LinkAndName {
	private Element element;
	private JsonObject object;
	private String link;
	private String name;

	protected LinkAndName() {
	}

	protected LinkAndName(Element element) {
		this.element = element;
		this.link = this.element!=null ? this.element.attr("href") : null;
		this.name = this.element!=null ? this.element.text() : null;		
	}

	protected LinkAndName(JsonObject object) {
		this.object = object;
		this.link = this.object!=null && !this.object.isJsonNull()
				? Optional.ofNullable(this.object).map(theObject -> theObject.get("url")).map(JsonElement::getAsString).orElseGet(()->null)
				: null;
		this.name = this.object!=null && !this.object.isJsonNull()
				? Optional.ofNullable(this.object).map(theObject -> theObject.get("name")).map(JsonElement::getAsString).orElseGet(()->null)
				: null;
	}
		
	protected LinkAndName(Optional<Element> element) {
		this.element = element.orElseGet(()->null);
		this.link = this.element!=null ? this.element.attr("href") : null;
		this.name = this.element!=null ? this.element.text() : null;
		
	}

	public Optional<String> getLink() {
		return Optional.ofNullable(this.link);
	}

	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

	
	@Override
	public String toString() {
		return "LinkAndName [link=" + this.getLink() + "name=" + this.getName() + "]";
	}

}
