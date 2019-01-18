package com.mvc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class JavaShellUtil {

	// public static void main(String[] args) {
	//// String shell = "/users/oap00/exploit/jar/oapp_batch.sh";
	//// String shell = "sh /users/oap00/exploit/script/ZOAPP_BW_I010C";
	// String shell;
	//// shell = "sh /users/oap00/exploit/script/ZOAPP_BW_I010C";
	// shell = "sh /users/oap00/exploit/script/ZOAPP_BW_I010P";
	//// shell = "sh /users/oap00/exploit/script/ZOAPP_BW_I011C";
	//// shell = "sh /users/oap00/exploit/script/ZOAPP_BW_I011P";
	// try {
	// executeShellSh(shell);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static int executeShellSh(String shellCommand) throws IOException {
		// LogUtil.outSystem.out.println("Shell Command : " + shellCommand);
		System.out.println("Shell Command : " + shellCommand);
		int success = 0;
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		// 格式化日期时间，记录日志时使用
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");

		try {
			stringBuffer.append(dateFormat.format(new Date())).append("Run Shell Command Start ").append(shellCommand).append(" \r\n");
			Process pid = null;
			String[] cmd = { "/bin/sh", "-c", shellCommand };
			// 执行Shell命令
			pid = Runtime.getRuntime().exec(cmd);
			if (pid != null) {
				stringBuffer.append("pid:").append(pid.toString()).append("\r\n");
				// bufferedReader用于读取Shell的输出内容
				bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
				pid.waitFor();
			} else {
				stringBuffer.append("not find pid\r\n");
			}
			stringBuffer.append(dateFormat.format(new Date())).append("Run Shell Command End \r\n  Message:\r\n");
			String line = null;
			// 读取Shell的输出内容，并添加到stringBuffer中
			while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line).append("\r\n");
			}
		} catch (Exception ioe) {
			stringBuffer.append("Run Shell Command Exception:\r\n").append(ioe.getMessage()).append("\r\n");
		} finally {
			if (bufferedReader != null) {
				// 将Shell的执行情况输出到日志文件中
				// LogUtil.outSystem.out.println(stringBuffer.toString());
				System.out.println(stringBuffer.toString());
			}
			success = 1;
		}
		return success;
	}

	private static String IP;
	private static String name;
	private static String pwd;

	private static Connection getConn() {
//		IP = Env.getInstance().getProperty("HOSTIP");
//		name = Env.getInstance().getProperty("HOSTname");
//		pwd = Env.getInstance().getProperty("HOSTpwd");
		Connection con = new Connection(IP);
		try {
			con.connect();
			boolean flag = con.authenticateWithPassword(name, pwd);
			if(flag){
				System.out.println("服务区登陆成功");
			}else{
				System.out.println("服务区登陆失败");
			}
		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
		}
		return con;
	}

	// public static void main(String[] args) {
	// try {
	// // exec("sh /users/oap00/exploit/script/ZOAPP_BW_I011P");
	// exec("sh /users/oap00/exploit/script/zz.sh");
	// // exec("ls -l");
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static boolean exec(String command) throws InterruptedException {
		System.out.println("command: " + command);
		boolean rtn = false;
		try {
			Connection conn = getConn();
			Session sess = conn.openSession();
			sess.execCommand(command);
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			InputStream stderr = new StreamGobbler(sess.getStderr());
			BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println("GanyMedUtil out&gt; " + line);
			}
			while (true) {
				line = stderrReader.readLine();
				if (line == null)
					break;
				System.out.println("GanyMedUtil out&gt; " + line);
			}
			/* Show exit status, if available (otherwise "null") */
			System.out.println("ExitCode: " + sess.getExitStatus() + " " + IP + ":" + command);
			br.close();
			stderrReader.close();
			sess.close();
			conn.close();
			rtn = new Integer(0).equals(sess.getExitStatus());
			return rtn;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(2);
			return rtn;
		}
	}

}
