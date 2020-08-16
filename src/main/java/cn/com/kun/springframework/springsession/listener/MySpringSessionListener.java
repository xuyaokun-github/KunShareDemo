package cn.com.kun.springframework.springsession.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.stereotype.Component;

@Component
public class MySpringSessionListener implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof SessionDeletedEvent){
            System.out.println("触发了SessionDeletedEvent事件");
        }
    }
}
