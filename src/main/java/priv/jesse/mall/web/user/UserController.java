package priv.jesse.mall.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsingglobal.common.utils.RestTemplateUtils;

import priv.jesse.mall.entity.User;
import priv.jesse.mall.entity.pojo.ResultBean;
import priv.jesse.mall.service.UserService;
import priv.jesse.mall.service.exception.LoginException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    private String contextPath = "http://10.45.52.169:8080";

    /**
     * 打开注册页面
     *
     * @return
     */
    @RequestMapping("/toRegister.html")
    public String toRegister() {
        return "mall/user/register";
    }

    /**
     * 打开登录页面
     *
     * @return
     */
    @RequestMapping("/toLogin.html")
    public String toLogin() {
        return "mall/user/login";
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     */
            @RequestMapping("/login.do")
            public void login(String username,
                    String password,
                    HttpServletRequest request,
                    HttpServletResponse response) throws IOException {
                User user = userService.checkLogin(username, password);
                if (user != null) {
            //登录成功 重定向到首页
            request.getSession().setAttribute("user", user);
            response.sendRedirect("/mall/index.html");
        } else {
            throw new LoginException("登录失败！ 用户名或者密码错误");
        }

    }

    /**
     * 注册
     * @return 
     */
    @RequestMapping("/register.do")
    public ResultBean<Boolean> register(String username,
                         String name,HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        JSONObject obj = new JSONObject();
        obj.put("loginName", username);
        obj.put("userName", name);
        String vurl = contextPath+"/system/user/registerUser";
        String result = RestTemplateUtils.getInstance().post(vurl, obj.toJSONString());
        System.out.println("registerUser11  "+String.valueOf(result));
        JSONObject jobj = JSONObject.parseObject(result);
        boolean status = jobj.getBooleanValue("status");
        ResultBean<Boolean> rb = new ResultBean<>(status);
        rb.setMessage(jobj.getString("message"));
        // 设置密码
        return rb;
    }
    


    /**
     * 登出
     */
    @RequestMapping("/logout.do")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("user");
        response.sendRedirect("/mall/index.html");
    }

    /**
     * 验证用户名是否唯一
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkUsername.do")
    public ResultBean<Boolean> checkUsername(String username){
        List<User> users = userService.findByUsername(username);
        if (users==null||users.isEmpty()){
            return new ResultBean<>(true);
        }
        return new ResultBean<>(false);
    }

    /**
     * 如发生错误 转到这页面
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/error.html")
    public String error(HttpServletResponse response, HttpServletRequest request) {
        return "error";
    }
    
    
    /**
     * 给用户邮箱发送验证码
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendValidateCode.do")
    public ResultBean<Boolean> sendValidateCode( String username,HttpServletRequest request){
    	
    	 JSONObject obj = new JSONObject();
         obj.put("loginName", username);
    	 String vurl = contextPath+"/system/user/generateRegisterCode";
    	String registerResultJSON = RestTemplateUtils.getInstance().post(vurl,obj.toJSONString());
    	obj = JSON.parseObject(registerResultJSON);
    	
    	if( "200".equals(obj.getString("state")) ) {
    		
    		request.getSession().setAttribute("regist_validateCode",obj.getString("data")); 
    		
    		return new ResultBean<>("获取验证码成功！");
    		
    	}else{
    		
    		//获取验证码失败！
    		return new ResultBean<>("获取验证码失败！"+obj.getString("message"));
    	}
        
    }
    
    /**
     * 给用户邮箱发送验证码
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkValidateCode.do")
    public ResultBean<Boolean> checkValidateCode(String validateCode,  HttpServletRequest request){
    	String svalidateCode = (String)request.getSession().getAttribute("regist_validateCode"); 
    	 if (svalidateCode.equals(validateCode)){
             return new ResultBean<>(true);
         }
         return new ResultBean<>(false);
    }
}