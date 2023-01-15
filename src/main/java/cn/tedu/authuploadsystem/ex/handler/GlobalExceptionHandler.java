package cn.tedu.authuploadsystem.ex.handler;

import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.web.JsonResult;
import cn.tedu.authuploadsystem.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.StringJoiner;

/**
 * 全局异常处理器
 * 该类会使当前项目中,任何标注@RequestMapping处理请求的方法对于ServiceException都应该
 * 是抛出的且各控制器类中都不必关心如何处理ServiceException，会由该类中的相关方法进行处理！
 *
 * @Author java.@Wqy
 * @Version 0.0.1
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler(){
        log.debug("创建全局异常处理器对象：GlobalExceptionHandler");
    }

    /**
     * 自定义异常处理----已存在的异常
     * @param e 自定义的异常类
     * @return 返回JSON对象,该对象封装了状态信息和状态描述
     */
    @ExceptionHandler// 此注解会使SpringMVC框架进行统一捕获相同类型的异常
    public JsonResult<Void> handlerServiceException(ServiceException e){ //★接收"手动抛出"该异常时传入的枚举属性和异常反馈信息
        log.debug("开始统一处理ServiceException----已经存在的异常");
        return JsonResult.fail(e);
    }

    /**
     * 自定义异常处理----检查不通过的异常
     * @param e BindException
     * @return  返回JSON对象,该对象封装了状态信息和状态描述
     */
    @ExceptionHandler
    public JsonResult<Void> handlerBindException(BindException e){// 因为该异常的信息不是我们自己定的,而状态信息是我们定的,故调用两参的fail()
        log.debug("开始处理BindException----检查不通过的异常");

        //"快速失败"(一次仅获取一次异常)
        String defaultMessage = e.getFieldError().getDefaultMessage();//Spring框架提供的API,直接获取某一个错误信息

        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST,defaultMessage);//调用JsonResult中的fail()方法,转传入入自定义的枚举属性和处理后的提示错误信息
    }

    /**
     * 自定义异常处理----未封装的请求参数异常
     * @param e ConstraintViolationException
     * @return  返回JSON对象,该对象封装了状态信息和状态描述
     */
    @ExceptionHandler
    public JsonResult<Void> handleConstViolationException(ConstraintViolationException e){// 因为该异常的信息不是我们自己定的,而状态信息是我们定的,故调用两参的fail()
        log.debug("开始处理ConstraintViolationException----未封装的请求参数异常");
        StringJoiner stringJoiner = new StringJoiner(". ");
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();//该异常返回的信息为Set集合
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {// 遍历Set集合
            stringJoiner.add(constraintViolation.getMessage());// 将获取的信息拿到并添加到StringJoiner
        }
        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST,stringJoiner.toString());
    }

    /**
     * 全局异常处理
     * @param e 全局的异常类
     * @return 返回异常处理反馈的信息
     */
//    @ExceptionHandler
//    public String handlerServiceException(Throwable e){
//        log.debug("这是一个Throwable异常,将统一处理");
//        return e.getMessage();
//    }
}
