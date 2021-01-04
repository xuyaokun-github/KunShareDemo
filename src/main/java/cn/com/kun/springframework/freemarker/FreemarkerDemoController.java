package cn.com.kun.springframework.freemarker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/freemarkerDemo")
@Controller
public class FreemarkerDemoController {

    /**
     * 写法1
     * @param model
     * @return
     */
    @RequestMapping("/sigup2")
    public String sigup2(Model model){
        model.addAttribute("password", "123");
        model.addAttribute("userName", "456");
        return "views/one";
    }

    /**
     * 写法1
     * @param model
     * @return
     */
    @RequestMapping("/sigup3")
    public String sigup3(Model model){
        model.addAttribute("password", "123");
        model.addAttribute("userName", "456");
        return "login";
    }

    /**
     * 写法1
     * @param model
     * @return
     */
    @RequestMapping("/sigup4")
    public String sigup4(Model model){
        model.addAttribute("password", "123");
        model.addAttribute("userName", "456");
        int a = 1/0;
        return "login";
    }

    /**
     * 写法2
     * @return
     */
    @RequestMapping("/sigup")
    public ModelAndView sigup(HttpServletRequest req , ModelAndView  mv){
        mv = mv == null?new ModelAndView():mv;
        mv.addObject("password", "123");
        mv.addObject("userName", "456");
        mv.setViewName("views/one");
        return mv;
    }
}
