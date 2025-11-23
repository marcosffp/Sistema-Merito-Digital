package com.projeto.lab.implementacao.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email enviado com sucesso para: {}", to);
        } catch (Exception e) {
            log.error("Erro ao enviar email para {}: {}", to, e.getMessage());
            // Não lança exceção para não interromper o fluxo principal
        }
    }

    public void enviarNotificacaoRecebimentoMoedas(String emailAluno, String nomeAluno, String nomeProfessor, Double valor, String motivo) {
        String subject = "🎉 Você recebeu moedas!";
        String body = String.format(
            "Olá, %s!\n\n" +
            "Você recebeu %.2f moedas de %s.\n\n" +
            "Motivo: %s\n\n" +
            "Acesse o sistema para visualizar seu novo saldo e conferir as vantagens disponíveis!\n\n" +
            "Atenciosamente,\n" +
            "Sistema de Mérito Digital",
            nomeAluno,
            valor,
            nomeProfessor,
            motivo
        );
        sendEmail(emailAluno, subject, body);
    }
}