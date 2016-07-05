package com.dixin.action.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dixin.action.IAction;
import com.dixin.business.impl.ProductHelper;
import com.dixin.servlet.MyServlet;

public class ExportProductAction implements IAction {
	public static Logger log = Logger.getLogger(ExportProductAction.class);

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException,
	 *             ServletException
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ProductHelper helper = new ProductHelper();
		File f = File.createTempFile("prod", "data");
		if (log.isDebugEnabled())
			log.debug("Temp File:" + f.getAbsolutePath());
		helper.export(f);
		FileInputStream in = new FileInputStream(f);
		OutputStream out = response.getOutputStream();
		try {
			response.setContentType("binary/data");
			response.setContentLength((int) f.length());
			response.setHeader("Content-Disposition",
					"attachment; filename=\"product.data\"");

			byte[] buf = new byte[1024 * 4];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
			response.reset();
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("导出时发生错误:" + ex.getMessage());
		}
	}

	public String getLocalizedName() {
		return "导出产品信息";
	}

	public void setMyServlet(MyServlet s) {
		// TODO Auto-generated method stub
		
	}

}
