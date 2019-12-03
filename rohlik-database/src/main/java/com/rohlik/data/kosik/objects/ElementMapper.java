package com.rohlik.data.kosik.objects;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ElementMapper {

	public Optional<String> toName(Optional<Element> element) {
		return element.map(elem -> elem.select(".product-box__info__name")).map(elem -> elem.text());
	}

	public Optional<Integer> toIdProduct(Optional<Element> element) {
		return element.map(elem -> elem.attr("data-product-id")).filter(id -> !id.isEmpty())
				.map(id -> Integer.parseInt(id));
	}

	public Optional<Double> toOrigPrice(Optional<Element> element) {
		return element.map(elem -> elem.select(".price__old-price")).map(price -> price.text())
				.filter(price -> !price.isEmpty())
				.map(price -> price.replace("Kč", "").replace(",", ".").trim().replace(" ", ""))
				.map(price -> Double.parseDouble(price));
	}

	public Optional<Double> toActualPrice(Optional<Element> element) {
		return element.map(elem -> elem.select(".price__actual-price")).map(price -> price.attr("content"))
				.filter(price -> !price.isEmpty()).map(price -> Double.parseDouble(price));
	}

	public Optional<String> toProductPath(Optional<Element> element) {
		return element.map(elem -> elem.select("a")).map(a -> a.first()).map(a -> a.attr("href"));
	}

	public Optional<String> toImageSrc(Optional<Element> element) {
		return element.map(elem -> elem.select(".product-box__head")).map(head -> head.select("img"))
				.map(img -> img.first()).map(img -> img.attr("data-srcset"));
	}

	public Optional<String> toAmountProduct(Optional<Element> element) {
		return element
				.map(elem -> elem.select(
						".product-box__quantity.a-product-label.a-product-label--squashed.a-product-label--grey"))
				.map(label -> label.text());
	}

	public Optional<String> toUnitPrice(Optional<Element> element) {
		return element.map(elem -> elem.select(".price__unit-per-price")).map(span -> span.text());
	}

	public Optional<Boolean> inStock(Optional<Element> element) {
		return element.map(elem -> elem.select(".amount__add")).map(div -> div.first().text())
				.map(text -> text.equals("Do košíku"));
	}
}
