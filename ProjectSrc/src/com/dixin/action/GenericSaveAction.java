package com.dixin.action;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.JDBCException;
import org.springframework.dao.DataIntegrityViolationException;

import net.sf.json.JSONObject;

import com.dixin.annotation.Name;
import com.dixin.business.impl.BaseHelper;
import com.dixin.hibernate.BaseJDO;

/**
 * Generic Save action.
 * 
 * @author Luo
 * 
 * @param <T>
 */
public abstract class GenericSaveAction<T extends BaseJDO> extends
		AbstractAction<T> {
	public GenericSaveAction(Class<T> jdoClass,
			Class<? extends BaseHelper<T>> helperClass) {
		super(jdoClass, helperClass);
		localizedName = "保存" + jdoClass.getAnnotation(Name.class).value()
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
		T t = collect(request, result);
		if (t != null) {
			try {
				t = validate(t);
				getHelper().merge(t);
				return true;
			} catch (DataIntegrityViolationException ex) {
				if (parseDataIntegrityViolationException(ex, result)) {
					return false;
				}
				throw ex;
			} catch (JDBCException ex) {
				if (parseDataIntegrityViolationException(ex, result)) {
					return false;
				}
				throw ex;
			}
		}
		return false;
	}

	protected T validate(T t) {
		return t;
	}
}
