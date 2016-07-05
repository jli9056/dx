package com.dixin.action;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.util.ResultUtil;
import com.dixin.annotation.Name;
import com.dixin.business.impl.BaseHelper;
import com.dixin.hibernate.BaseJDO;

/**
 * Generic delete action.
 * 
 * @author Luo
 * 
 * @param <T>
 */
public abstract class GenericRestoreAction<T extends BaseJDO> extends
		AbstractAction<T> {
	public GenericRestoreAction(Class<T> jdoClass,
			Class<? extends BaseHelper<T>> helperClass) {
		super(jdoClass, helperClass);
		localizedName = "恢复" + jdoClass.getAnnotation(Name.class).value()
				+ "信息";
	}

	private final String localizedName;

	public String getLocalizedName() {
		return localizedName;
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String[] sid = request.getParameterValues("id");
		if (sid == null) {
			throw new ActionException("没有指定要恢复的数据");
		}
		int deleted = 0;
		int undeleted = 0;
		for (String id : sid) {
			boolean success = getHelper().restoreById(Integer.parseInt(id));
			if (success) {
				deleted++;
			} else {
				undeleted++;
			}
		}
		if (undeleted > 0) {
			if (sid.length > 1) {
				result.accumulate(ResultUtil.MSG, deleted + "个恢复成功。"
						+ undeleted + "个恢复失败，可能这些记录被其它数据引用，不能恢复。");
			} else {
				result.accumulate(ResultUtil.MSG, "恢复失败，可能该数据被其它数据引用，不能恢复。");
			}
			return false;
		}
		return true;
	}
}
