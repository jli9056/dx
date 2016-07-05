package com.dixin.action;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.JDBCException;
import org.springframework.dao.DataIntegrityViolationException;

import net.sf.json.JSONObject;

import com.dixin.annotation.Name;
import com.dixin.business.impl.BaseHelper;
import com.dixin.hibernate.BaseJDO;

/**
 * Add JDO action
 * 
 * @author Luo
 * 
 * @param <T>
 */
public abstract class GenericAddAction<T extends BaseJDO> extends
		AbstractAction<T> {
	public GenericAddAction(Class<T> jdoClass,
			Class<? extends BaseHelper<T>> helperClass) {
		super(jdoClass, helperClass);
		localizedName = "增加" + jdoClass.getAnnotation(Name.class).value()
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
		T product = collect(request, result);
		if (product == null) {
			return false;
		}
		try {
			validate(product);
			getHelper().save(product);
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

	protected void validate(T t) {
	};
}
