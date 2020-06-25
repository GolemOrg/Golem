package net.golem.terminal.types;


/**
 * TODO: Finish these :)
 */
public enum EmojiType {

	XBOX_A_BUTTON('\uE000'),
	XBOX_B_BUTTON('\uE001'),

	FOOD_ICON('\uE100'),
	ARMOR_ICON('\uE101'),
	MINECOIN('\uE102'),
	CODE_BUILDER_ICON('\uE103');

	private String unicodeChar;

	EmojiType(char unicodeChar) {
		this.unicodeChar = String.valueOf(unicodeChar);
	}

	@Override
	public String toString() {
		return unicodeChar;
	}
}
