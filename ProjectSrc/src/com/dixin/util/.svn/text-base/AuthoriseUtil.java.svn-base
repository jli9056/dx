/**
 * 
 */
package com.dixin.util;

import java.io.File;

import com.dixin.business.DataException;

/**
 * @author Jason
 * 
 */
public class AuthoriseUtil {
	/**
	 * 
	 * @return
	 * @throws Throwable
	 */
	public static void authorize() {
		try {
			RegistryHelper helper = RegistryHelper.getDixinRegistry();
			String location = helper.get("location");
			if (location == null || !new File(location).exists()) {
				throw new DataException("此软件为非法安装，请购买正版软件！");
			}
			/*Date now = new Date();
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if (now.after(df.parse("2010-01-01"))) {
					int loginTimes = 0;
					String today = df.format(now);
					String encodedKey = new String(Base64.encodeBase64(today
							.getBytes()));
					String times = helper.get(encodedKey);
					if (times == null) {
						loginTimes++;
					} else {
						try {
							loginTimes = Integer.parseInt(times);
						} catch (NumberFormatException ex) {
						}
						loginTimes++;
					}
					helper.putInt(encodedKey, loginTimes);
					if (loginTimes > 3) {
						throw new DataException(
								"软件试用期截止至2010年1月1日，试用期之后每天只能登录3次！");
					}
				}
			} catch (ParseException e) {
			}*/
		} catch (Throwable ex) {
			if (ex instanceof DataException) {
				throw (DataException) ex;
			}
		}
	}

}
