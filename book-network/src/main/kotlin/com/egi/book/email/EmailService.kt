package com.egi.book.email

import jakarta.mail.MessagingException
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessageHelper.*
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.nio.charset.StandardCharsets.*

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine,
    @Value("\${application.account.email}")
    private val email: String,
) {
    @Async
    @Throws(MessagingException::class)
    fun sendEmail(
        to: String,
        username: String,
        emailTemplate: EmailTemplateName,
        confirmationUrl: String,
        activationCode: String,
        subject: String,
    ) {
        val templateName =
            EmailTemplateName.ACTIVATE_ACCOUNT.templateName

        val mimeMessage = mailSender.createMimeMessage()
        val helper =
            MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name(),
            )
        val properties = HashMap<String, Any>()
        properties["username"] = username
        properties["confirmationUrl"] = confirmationUrl
        properties["activation_code"] = activationCode

        val context = Context()
        context.setVariables(properties)

        helper.setFrom(email)
        helper.setTo(to)
        helper.setSubject(subject)

        val template = templateEngine.process(templateName, context)

        helper.setText(template, true)
        mailSender.send(mimeMessage)
    }
}
