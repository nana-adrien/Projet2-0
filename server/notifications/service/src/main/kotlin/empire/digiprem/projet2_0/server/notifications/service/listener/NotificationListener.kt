package empire.digiprem.projet2_0.server.notifications.service.listener

import empire.digiprem.projet2_0.server.common.event.SendNotificationEvent
import empire.digiprem.projet2_0.server.common.event.SendOTPEvent
import empire.digiprem.projet2_0.server.notifications.service.NotificationsService
import empire.digiprem.projet2_0.server.notifications.service.SendEmailService
import empire.digiprem.projet2_0.server.notifications.service.SendSMSService
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class NotificationListener(
  private val  sendEmailService: SendEmailService,
  private val  sendSMSService: SendSMSService,
    private val notificationService: NotificationsService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener(condition ="#event.isEmail")
    fun sendOTPByEmail(event: SendOTPEvent){
        sendEmailService.sendOtpEmail(
            contact = event.contact,
            reason = event.reason,
            otp = event.otp,
            expirationTime = event.expiresAt
        )
    }
    @EventListener(condition ="!#event.isEmail")
    fun sendOTPBySMS(event: SendOTPEvent){
        sendSMSService.sendOtpSms(
            toPhoneNumber = event.contact,
            reason = event.reason,
            otp = event.otp,
            expirationTime = event.expiresAt
        )
    }
    @EventListener
    fun sendNotification(event: SendNotificationEvent){
        notificationService.saveNotification(
            userId=event.userId,
            title=event.title,
            body=event.body,
            data=event.data
        )
    }

}