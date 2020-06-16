package net.golem.network.raknet.identifier;

import java.util.ArrayList;

public abstract class Identifier {

	String HEADER = "MCPE";

	String SEPARATOR = ";";

	public abstract ArrayList<Object> getValues();

	public String build() {
		StringBuilder builder = new StringBuilder();
		builder.append(HEADER).append(SEPARATOR);
		ArrayList<Object> values = getValues();
		for(int i = 0; i < values.size(); i++) {
			Object value = values.get(i);
			builder.append(value != null ? value : "");
			if(i + 1 < values.size()) {
				builder.append(SEPARATOR);
			}
		}
		return builder.toString();
	}
}