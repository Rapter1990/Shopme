package com.shopme.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.shopme.setting.CurrencySettingBag;

public class FormatCurrencyUtil {

	public static String formatCurrency(float amount, CurrencySettingBag settings) {
		String symbol = settings.getSymbol();
		String symbolPosition = settings.getSymbolPosition();
		String decimalPointType = settings.getDecimalPointType();
		String thousandPointType = settings.getThousandPointType();
		int decimalDigits = settings.getDecimalDigits();

		String pattern = symbolPosition.equals("Before price") ? symbol : "";
		pattern += "###,###";

		if (decimalDigits > 0) {
			pattern += ".";
			for (int count = 1; count <= decimalDigits; count++) pattern += "#";
		}

		pattern += symbolPosition.equals("After price") ? symbol : "";

		char thousandSeparator = thousandPointType.equals("POINT") ? '.' : ',';
		char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';

		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
		decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
		decimalFormatSymbols.setGroupingSeparator(thousandSeparator);

		DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);

		return formatter.format(amount);
	}
}
