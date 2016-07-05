package com.dixin.action;

import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;

import com.dixin.action.util.ResultUtil;
import com.dixin.annotation.Name;
import com.dixin.business.impl.BaseHelper;
import com.dixin.hibernate.BaseJDO;

/**
 * Generic Search Action.
 * 
 * @author Luo
 * 
 * @param <T>
 */
public abstract class GenericLoadAction<T extends BaseJDO> extends
		AbstractAction<T> {
	public GenericLoadAction(Class<T> jdoClass,
			Class<? extends BaseHelper<T>> helperClass) {
		super(jdoClass, helperClass);
		localizedName = "加载" + jdoClass.getAnnotation(Name.class).value()
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
		String sid = request.getParameter("id");
		if (sid == null) {
			throw new ActionException("没有指定要加载的数据");
		}
		T t = getHelper().findById(Integer.parseInt(sid));
		result.accumulate(ResultUtil.DATA, t);
		return true;
	}
}
