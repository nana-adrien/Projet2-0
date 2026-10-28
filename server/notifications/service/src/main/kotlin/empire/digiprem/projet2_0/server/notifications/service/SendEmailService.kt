package empire.digiprem.projet2_0.server.notifications.service

import empire.digiprem.projet2_0.server.common.event.SendOTPEvent.SendOTPReason
import empire.digiprem.projet2_0.server.notifications.service.utils.DateFormatter.formatInstance
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.time.Instant
import java.time.LocalDateTime

@Service
class SendEmailService(
    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine
) {


    fun sendOtpEmail(contact: String,   otp: String,reason: SendOTPReason,expirationTime: Instant) {

        val mimeMessage: MimeMessage = mailSender.createMimeMessage()

        val helper= MimeMessageHelper(mimeMessage,true,"UTF-8")

        val context= Context().apply {
            setVariable("otpCode", otp)
            setVariable("expirationTime", formatInstance(expirationTime))
        }
        val templateName="auth/${ reason.name.lowercase().replace("_","-")}"
        val htmlContent=templateEngine.process(templateName,context)

        helper.setFrom("empire.digiprem@gmail.com")
        helper.setTo(contact)
        helper.setSubject("Votre code de verification OTP")
        helper.setText(htmlContent,true)

        mailSender.send(mimeMessage)

    }


}