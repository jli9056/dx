package com.dixin.action.databackup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dixin.action.IAction;
import com.dixin.servlet.MyServlet;

public class DataDownloadAction implements IAction {

	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String fileName = request.getParameter("filename");
		MySQLDump run = (MySQLDump) request.getSession().getAttribute(fileName);
		File f = run.getFile();
		FileInputStream in = new FileInputStream(f);
		byte[] buf = new byte[1024 * 10];
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName + ".data");
		long size = in.getChannel().size();
		response.setHeader("Content-Length", String.valueOf(size));
		OutputStream out = response.getOutputStream();
		int count = in.read(buf);
		while (count >= 0) {
			out.write(buf, 0, count);
			count = in.read(buf);
		}
		out.flush();
		out.close();
	}

	public String getLocalizedName() {
		return "下载备份数据";
	}

	public void setMyServlet(MyServlet s) {
		// TODO Auto-generated method stub
		
	}
}
