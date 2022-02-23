package top.lcywings.pony.common.util;

import top.lcywings.pony.common.exception.CustomException;
import com.google.common.base.Strings;
import top.lcywings.pony.model.common.MailModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;


/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/17
 **/
@Slf4j
@Component
public class MailUtil {
    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Resource
    private TemplateEngine templateEngine;

    /**
     * 发送邮件
     *
     * @param mailModel 邮件相关信息
     * @author yeweiwei
     * @date 2021/11/17 14:18
     */
    public void sendEmail(MailModel mailModel) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            //发送人
            messageHelper.setFrom(fromEmail);
            //发送给谁
            messageHelper.setTo(mailModel.getToMail());
            //标题
            messageHelper.setSubject(mailModel.getTitle());
            //使用模板thymeleaf
            Context context = new Context();
            //定义模板数据
            context.setVariables(mailModel.getParamsMap());
            //获取thymeleaf的html模板,指定模板路径
            String emailContent = templateEngine.process(mailModel.getTemplate(), context);
            messageHelper.setText(emailContent, true);

            if (!Strings.isNullOrEmpty(mailModel.getFilePath())) {
                //附件
                FileSystemResource fileSystemResource = new FileSystemResource(mailModel.getFilePath());
                messageHelper.addAttachment(mailModel.getFileName(), fileSystemResource);
            }
            //发送邮件
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("邮件发送失败，原因：" + e.getMessage(), e);
            throw new CustomException("邮件发送失败,请检查邮箱填写是否正确！");
        }
    }
}
