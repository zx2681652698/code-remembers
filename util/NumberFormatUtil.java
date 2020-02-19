package com.ab.dh.api.util;

import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberFormatUtil {
	public  static BigDecimal formatComma2BigDecimal(Object obj) {
		String val = String.valueOf(obj);
		if (val == null)
			return new BigDecimal("0.00");

		val = val.replaceAll(",", "");
		if (!isNumber(val))
			return new BigDecimal("0.00");

		BigDecimal decimal = new BigDecimal(val);
		decimal = decimal.setScale(2, RoundingMode.HALF_UP);

		return decimal;

	}
	public  static String formatCommaAnd2Point(Object obj) {
		BigDecimal decimal = formatComma2BigDecimal(obj);

		DecimalFormat df = new DecimalFormat("#,###.00");
		String decimalStr = df.format(decimal).equals(".00")?"0.00":df.format(decimal);
		if(decimalStr.startsWith(".")){
			decimalStr = "0"+decimalStr;
		}
		return decimalStr;
	}
	public  static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
            return value.contains(".");
        } catch (NumberFormatException e) {
			return false;
		}
	}
	public  static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	public  static boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}
	
	/**
	 * 
	 * 如果字符串为小数，并且小数位全部为0时返回整数部分
	 * @return
	 */
	public  static String strToIntegerNumber(String value) {
		if(StringUtil.isNotEmpty(value)&&isNumber(value)){
			String[] valueArray = value.split("\\.");
			if(valueArray.length==2){
				String integerPart = valueArray[0];
				BigDecimal integerPartB = new BigDecimal(integerPart);
				BigDecimal decimalPartB = new BigDecimal(value).subtract(integerPartB);
				if(decimalPartB.compareTo(new BigDecimal("0.00"))==0){////结果是:-1 小于,0 等于,1 大于
					return integerPart;
				}
			}
		}
		return value;
	}
	
	public static void main(String[] args) {
		System.out.println(strToIntegerNumber("625.00"));
	}
	
	/**
	 * 判断传入字符串是否为整数，并且将10000的整数倍的数字改成xx万;
	 * 传入不为整数时返回原字符串
	 * @param value
	 * @return
	 */
	public static String formatNumber_10000(String value){
		if(StringUtil.isNotEmpty(value)){
			if(isInteger(value)){
				int temp = Integer.parseInt(value);
				int temp_yu = temp%10000;
				if(temp_yu == 0){
					return temp/10000+"万";
				}
			}
		}
		return value;
	}
}
