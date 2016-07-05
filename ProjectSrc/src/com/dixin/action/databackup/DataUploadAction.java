package com.dixin.action.databackup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.dixin.action.CurrentUser;
import com.dixin.action.IAction;
import com.dixin.servlet.MyServlet;

public class DataUploadAction implements IAction {
	String tmpDir = System.getProperty("java.io.tmpdir");
	static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
	static Logger log = Logger.getLogger(DataUploadAction.class);

	@SuppressWarnings("unchecked")
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// Parse the request
			List<FileItem> items = upload.parseRequest(request);
			boolean schema = false;
			boolean data = false;
			String fileName = tmpDir + "/" + CurrentUser.getCurrentUser()
					+ df.format(new Date());
			File f = new File(fileName);
			for (FileItem item : items) {
				if (!item.isFormField() && "file".equals(item.getFieldName())) {
					item.write(f);
				} else if (item.isFormField()) {
					String fname = item.getFieldName();
					String value = item.getString();
					if ("IMPORT_SCHEMA".equals(fname) && "true".equals(value)) {
						schema = true;
					} else if ("IMPORT_DATA".equals(fname)
							&& "true".equals(value)) {
						data = true;
					}
				}
			}

			if (!f.exists()) {
				response.getWriter().println("上传文件失败，没有接收到文件数据！");
				return;
			}
			Progressive run = new MySQLSource(f, schema, data);
			new Thread(run).start();
			request.getSession().setAttribute("MySQLSource", run);
			response.sendRedirect("/databackup/import.html");
			log.debug("upload success");

		} catch (Exception e) {
			e.printStackTrace();
			log.error("上传文件失败", e);
			String msg = e.getMessage();
			response.getWriter().println(
					"上传文件失败" + (msg == null ? "" : ": " + msg));
		}
	}

	public String getLocalizedName() {
		return "上传数据文件";
	}

	public void setMyServlet(MyServlet s) {
		// TODO Auto-generated method stub
		
	}

}
