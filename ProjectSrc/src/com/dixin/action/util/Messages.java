package com.dixin.action.util;

import java.sql.SQLException;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.CleanupFailureDataAccessException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.UncategorizedDataAccessException;

import com.dixin.action.ActionException;
import com.dixin.business.DataException;
import com.dixin.hibernate.BaseJDO;

public class Messages {
	public static String getMessage(Throwable t) {
		if (t instanceof Exception) {
			return getMessage((Exception) t);
		} else if (t instanceof Error) {
			return getMessage((Error) t);
		} else {
			return "错误: " + t.getLocalizedMessage();
		}
	}

	public static String getMessage(Error e) {
		if (e instanceof OutOfMemoryError) {
			return "内存不足";
		} else if (e instanceof StackOverflowError) {
			return "内存堆栈溢出";
		} else {
			return "错误：" + e.getLocalizedMessage();
		}
	}

	public static String getMessage(Exception e) {
		if (e instanceof RuntimeException) {
			return getMessage((RuntimeException) e);
		} else if (e instanceof SQLException) {
			return "访问数据库时发生错误";
		} else {
			return "异常：" + e.getLocalizedMessage();
		}
	}

	public static String getMessage(RuntimeException e) {
		if (e instanceof ActionException) {
			return e.getMessage();
		} else if (e instanceof NumberFormatException) {
			return "数据格式错误";
		} else if (e instanceof DataAccessException) {
			return getMessage((DataAccessException) e);
		} else if (e instanceof DataException) {
			return e.getMessage();
		} else {
			return "运行时异常：" + e.getLocalizedMessage();
		}
	}

	public static String getMessage(DataAccessException e) {
		if (e instanceof UncategorizedDataAccessException) {
			// not append getMessage, because it is really big
			return "访问数据时发生错误。";
		}
		if (e instanceof ConcurrencyFailureException) {
			return "由于并发操作引起的错误，请检查数据并重试。";
		}
		if (e instanceof DataAccessResourceFailureException) {
			return "不能访问数据库服务。";
		}
		if (e instanceof CleanupFailureDataAccessException) {
			return "执行清理操作错误，数据操作可能已成功。";
		}
		if (e instanceof DataIntegrityViolationException) {
			return "操作失败，可能因为该数据与其它数据的关系。数据完整性错误，可能违反了数据之间的关系。";
		}
		if (e instanceof DataRetrievalFailureException) {
			return "数据库返回的数据有误。";
		}
		if (e instanceof PermissionDeniedDataAccessException) {
			return "没有权限访问请求的数据。";
		}
		return "访问数据时发生错误" + e.getLocalizedMessage();
	}

	public static void getMessage(ConstraintViolationException e,
			Class<BaseJDO> clazz, Map<? super String, ? super String> errors) {
		DataIntegrity inte = DataIntegrity.getFromException(e, clazz);
		errors.put(inte.getField(), inte.getIntegrityType().toString());
	}
}
