package cn.com.kun.web.login.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * 	启动类需要加一句：	System.setProperty("java.awt.headless", "false");
 */
//@RestController
//@RequestMapping("/sse")
public class SSEController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSEController.class);
	
	// 服务端推送 返回结果必须以 data: 开始
	public static final String SSE_RETURN_START = "data:" ;
	
	// // 服务端推送 返回结果必须以 \n\n 结束
	public static final String SSE_RETURN_END = "\n\n" ;
	
	// 服务端推送SSE技术 内容格式必须是  text/event-stream
	@GetMapping(value = {"","/"}, produces = "text/event-stream;charset=UTF-8")
	public String index() {
		String capture = CaptureScreenUtils.capture();
		// 返回的内容 必须以 data: 开头，以 \n\n 结尾，不然前端JS处始终不会进入到 onmessage 方法内
		return SSE_RETURN_START + capture + SSE_RETURN_END ;
	}
	
	
	
	
	
	@GetMapping("/index")
	public SseEmitter sse() {
		// 获取屏幕截图，每个1秒获取一次，返回前端界面进行图片展示
		String capture = CaptureScreenUtils.capture();
		SseEmitter emitter = new SseEmitter(1L) ;
		try {
			emitter.send(SseEmitter.event()
					.reconnectTime(1)
					.data(capture));
		} catch (IOException e) {
			LOGGER.error("服务端推送服务发生异常", e);
		}
		
		return emitter;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
