package cn.com.kun.utils;

import cn.com.kun.common.vo.ResultVo;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * 执行Linux命令工具类
 *
 * @author xuyaokun
 * @date 2018/9/20 16:13
 */
public class ProcessUtil {


    private final static Logger log = LoggerFactory.getLogger(ProcessUtil.class);

    /**
     * 执行本机上的Linux命令
     *
     * @param command
     * @return
     */
    public static ResultVo execCmd(String... command) {

        String tmpCommand = "";
        for (String s : command) {
            tmpCommand += s + " ";
        }
        log.info("execCmd:" + tmpCommand);

        Map resultMap = new HashMap();
        try {
            Process p = command.length == 1 ? Runtime.getRuntime().exec(command[0]) : Runtime.getRuntime().exec(command);

            //获取结果值
            int exitValue = p.waitFor();

            String tmp;
            String result = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (null != (tmp = br.readLine())) {
                result += tmp + "\n";
            }
            br.close();

            String error = "";
            BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while (null != (tmp = er.readLine())) {
                error += tmp + "\n";
            }
            er.close();
            resultMap.put("result", result);//命令输出内容
            resultMap.put("error", error);
            resultMap.put("exitValue", exitValue);//命令执行返回码

            if(exitValue != 0){
                return ResultVo.valueOfError("执行脚本失败！", resultMap);
            }else{
                return ResultVo.valueOfSuccess(resultMap);
            }

        } catch (Exception e) {
            log.error("执行脚本异常", e);
            return ResultVo.valueOfError("执行脚本失败！", 1);
        }
    }

    /**
     * 执行远程的Linux命令
     * （用户名密码验证方式）
     *
     * @param host
     * @param port
     * @param user
     * @param password
     * @param command
     * @return
     */
    public static ResultVo execRemoteCmd(String host, String user, String password, String command, int... port)  {

        Map resultMap = new HashMap();

        JSch jsch = new JSch();
        com.jcraft.jsch.Session session = null;
        ChannelExec channelExec = null;
        try {

            session = jsch.getSession(user, host, port.length > 0 ? port[0]:22);
            session.setConfig("StrictHostKeyChecking", "no");
            //java.util.Properties config = new java.util.Properties();
            //config.put("StrictHostKeyChecking", "no");

            session.setPassword(password);
            session.connect();

            channelExec = (ChannelExec) session.openChannel("exec");
            InputStream in = channelExec.getInputStream();
            InputStream errorIn = channelExec.getExtInputStream();
            channelExec.setCommand(command);
//            channelExec.setErrStream(System.err);
            channelExec.connect();

            //执行结果
            //String returnStr = IOUtils.toString(in, "UTF-8");
            String tmp;
            String result = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while (null != (tmp = br.readLine())) {
                result += tmp + "\n";
            }
            br.close();

            //错误的输出结果
            String error = "";
            BufferedReader er = new BufferedReader(new InputStreamReader(errorIn));
            while (null != (tmp = er.readLine())) {
                result += tmp + "\n";
            }
            er.close();

            // 得到returnCode  0表示执行成功
//            System.out.println("channelExec.isClosed：" + channelExec.isClosed());
//            System.out.println("channelExec.getExitStatus：" + channelExec.getExitStatus());

            resultMap.put("result", result);
            resultMap.put("error", error);
            resultMap.put("exitValue", channelExec.getExitStatus());

            if(channelExec.getExitStatus() != 0){
                log.error("execRemoteCommand return:" + JSONObject.toJSON(resultMap).toString());
                return ResultVo.valueOfError("执行脚本失败！", resultMap);
            }else{
                return ResultVo.valueOfSuccess(resultMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("error", "执行远程命令出现异常");
            return ResultVo.valueOfError("执行脚本失败！", resultMap);
        } finally {
            if(channelExec!=null){
                channelExec.disconnect();
            }
            if(session!=null&&session.isConnected()){
                session.disconnect();
            }
        }

    }


    /**
     * 执行远程Linux命令
     * （使用密钥验证方式--公钥必须添加好，否则无权限执行）
     *
     * @param host
     * @param port
     * @param user
     * @param priKeyPath
     * @param command
     * @return
     */
    public static ResultVo execRemoteCmdByPrikey(String host, int port, String user, String priKeyPath, String command)  {

        Map resultMap = new HashMap();

        JSch jsch = new JSch();
        com.jcraft.jsch.Session session = null;
        ChannelExec channelExec = null;
        try {
            jsch.addIdentity(priKeyPath);
            session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelExec = (ChannelExec) session.openChannel("exec");
            InputStream in = channelExec.getInputStream();
            InputStream errorIn = channelExec.getExtInputStream();
            channelExec.setCommand(command);
            channelExec.connect();

            //执行结果
            String tmp;
            String result = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while (null != (tmp = br.readLine())) {
                result += tmp + "\n";
            }
            br.close();

            //错误的输出结果
            String error = "";
            BufferedReader er = new BufferedReader(new InputStreamReader(errorIn));
            while (null != (tmp = er.readLine())) {
                result += tmp + "\n";
            }
            er.close();

            resultMap.put("result", result);
            resultMap.put("error", error);
            resultMap.put("exitValue", channelExec.getExitStatus());

            if(channelExec.getExitStatus() != 0){
                log.debug("execCmd:" + command);
                log.error("execRemoteCommand return:" + JSONObject.toJSON(resultMap).toString());
                return ResultVo.valueOfError("执行脚本失败！", resultMap);
            }else{
                return ResultVo.valueOfSuccess(resultMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("error", "执行远程命令出现异常");
            return ResultVo.valueOfError("执行脚本失败！", 1);
        } finally {
            if(channelExec!=null){
                channelExec.disconnect();
            }
            if(session!=null&&session.isConnected()){
                session.disconnect();
            }
        }

    }
}
