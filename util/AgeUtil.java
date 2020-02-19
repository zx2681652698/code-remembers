package com.ab.dh.api.util;

import tk.mybatis.mapper.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgeUtil {
        /**
         * 根据起保日期计算被保人年龄
         * @param birthDay
         * @param effective
         * @return
         * @throws Exception
         */
		public static int getAge(String birthDay,Date effective) throws Exception {
			Date birDay = DateUtil.SDF_DAY.parse(birthDay);
			Calendar cal = Calendar.getInstance();
			if (cal.before(birthDay)) {
				return 0;
			}
			
			cal.setTime(effective);
			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH);
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
			cal.setTime(birDay);

			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH);
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			int age = yearNow - yearBirth;
			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth) {
						age--;
					}
				} else {
					age--;
				}
			}
			return age;
		}

		public static int getAgeByBirth(Date birthday) {
			// 当前时间
			Calendar curr = Calendar.getInstance();
			// 生日
			Calendar born = Calendar.getInstance();
			born.setTime(birthday);
			// 年龄 = 当前年 - 出生年
			int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
			if (age <= 0) {
				return 0;
			}
			// 如果当前月份小于出生月份: age-1
			// 如果当前月份等于出生月份, 且当前日小于出生日: age-1
			int currMonth = curr.get(Calendar.MONTH);
			int currDay = curr.get(Calendar.DAY_OF_MONTH);
			int bornMonth = born.get(Calendar.MONTH);
			int bornDay = born.get(Calendar.DAY_OF_MONTH);
			if(curr.get(Calendar.YEAR) - born.get(Calendar.YEAR)==0){
				if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay <= bornDay)) {
					age--;
				}
			}else{
				if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay < bornDay)) {
					age--;
				}
			}

			return age < 0 ? 0 : age;
		}

		/**
		 * 
		 * 根据生日判断是否成年人
		 */
		public static  boolean isAdults(String brithday,Date effective) throws Exception {
			int age = getAge(brithday, effective);
            return age >= 18;
        }
		
		/**
		 * 
		 * 根据年龄范围获取一个计算保费所用的生日
		 */
		public static String getBrithdayByFactor(String startdate,String age) {
			if(StringUtil.isEmpty(startdate)){
				startdate=DateUtil.SDF_DAY.format(new Date());
			}
			String birthday ="1991-01-01";
			if (age != null && !"".equals(age)) {
				String ages[] = age.split("-");
				if(ages[0].endsWith("D")){ 
					age=ages[0].substring(0, ages[0].indexOf("D"));
					if ("0".equals(age)) {
						age = "1";
					} 
					birthday = DateUtil.calSDate(startdate, 0 - Integer.parseInt(age), "D");
				}else if(ages[0].endsWith("M")){
					age=ages[0].substring(0, ages[0].indexOf("M"));
					if ("0".equals(age)) {
						age = "1";
					} 
					birthday = DateUtil.calSDate(startdate, 0 - Integer.parseInt(age), "M");
				}else if(ages[0].endsWith("Y")){
					age=ages[0].substring(0, ages[0].indexOf("Y"));
					if ("0".equals(age)) {
						birthday = DateUtil.calSDate(startdate, -1, "M");
					} else{
						birthday = DateUtil.calSDate(startdate, 0 - Integer.parseInt(age), "Y");
					}
				}else{
					birthday = age;
				}
			}
			return birthday;
		}
}
