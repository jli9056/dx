package com.dixin.action;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.util.ResultUtil;
import com.dixin.annotation.Name;
import com.dixin.business.DataException;
import com.dixin.business.impl.BaseHelper;
import com.dixin.hibernate.BaseJDO;

/**
 * Generic delete action.
 * 
 * @author Luo
 * 
 * @param <T>
 */
public abstract class GenericDeleteAction<T extends BaseJDO> extends
		AbstractAction<T> {
	public GenericDeleteAction(Class<T> jdoClass,
			Class<? extends BaseHelper<T>> helperClass) {
		super(jdoClass, helperClass);
		localizedName = "删除" + jdoClass.getAnnotation(Name.class).value()
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
			throw new ActionException("没有指定要删除的数据");
		}
		int deleted = 0;
		int undeleted = 0;
		try {
			for (String id : sid) {
				boolean success = getHelper().deleteById(Integer.parseInt(id));
				if (success) {
					deleted++;
				} else {
					undeleted++;
				}
			}
		} catch (DataException e) {
			result.accumulate(ResultUtil.MSG, e.getMessage());
			return false;
		}
		if (undeleted > 0) {
			if (sid.length > 1) {
				result.accumulate(ResultUtil.MSG, deleted + "个删除成功。"
						+ undeleted + "个删除失败，可能这些记录被其它数据引用，不能删除。");
			} else {
				result.accumulate(ResultUtil.MSG, "删除失败，可能该数据被其它数据引用，不能删除。");
			}
			return false;
		}
		return true;
	}
}
