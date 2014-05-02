package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	private static final DateFormat df = new SimpleDateFormat(
			"hh:mm:ss dd/MM/yyyy ");
	private static final DateFormat dfsql = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss ");

	@Override
	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder(1000);
		builder.append("[").append(record.getLevel()).append("] ");
		builder.append(formatMessage(record));
		builder.append("    [" + record.getSourceMethodName()).append("] - ");
		builder.append(df.format(new Date(record.getMillis()))).append(" - ");
		builder.append("[").append(record.getSourceClassName()).append(".")
				.append(record.getSourceMethodName()).append("]");
	//	System.out.println("called for :" + formatMessage(record));
		builder.append("\n");

		java.sql.Date sdate = new java.sql.Date(record.getMillis());

		try {
			String sql = "insert into log(level,message,method,tarih) values ('"
					+ record.getLevel()
					+ "','"
					+ formatMessage(record)
					+ "','"
					+ record.getSourceClassName()
					+ "."
					+ record.getSourceMethodName()
					+ "','"
					+ dfsql.format(sdate) + "')";
			Jdbc.query(sql);
			Jdbc.close("logformatter");

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return builder.toString();
	}

}