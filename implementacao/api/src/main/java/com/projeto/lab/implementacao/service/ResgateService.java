package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.dto.CupomResponse;
import com.projeto.lab.implementacao.dto.ResgateRequest;
import com.projeto.lab.implementacao.dto.ResgateResponse;
import com.projeto.lab.implementacao.dto.VantagemResponse;
import com.projeto.lab.implementacao.exception.ResgateException;
import com.projeto.lab.implementacao.mapper.ResgateMapper;
import com.projeto.lab.implementacao.mapper.VantagemMapper;
import com.projeto.lab.implementacao.model.Resgate;
import com.projeto.lab.implementacao.model.Aluno;
import com.projeto.lab.implementacao.model.Distribuicao;
import com.projeto.lab.implementacao.model.Participante;
import com.projeto.lab.implementacao.model.Vantagem;
import com.projeto.lab.implementacao.repository.DistribuicaoRepository;
import com.projeto.lab.implementacao.repository.ResgateRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class ResgateService {
    private final ResgateRepository resgateRepository;
    private final AlunoService alunoService;
    private final VantagemService vantagemService;
    private final ResgateMapper resgateMapper;
    private final VantagemMapper vantagemMapper;
    private final ParticipanteService participanteService;
    private final EmailService emailService;
    private final DistribuicaoRepository distribuicaoRepository;

    @Transactional
    public ResgateResponse cadastrarResgate(ResgateRequest dto) {
        Vantagem vantagem = vantagemService.buscarPorId(dto.vantagemId());
        if (!participanteService.temSaldoSuficiente(dto.alunoId(), vantagem.getCusto())) {
            throw new ResgateException("Saldo insuficiente para realizar o resgate");

        }
        participanteService.atualizarSaldo(dto.alunoId(), -Math.abs(vantagem.getCusto()));
        Aluno aluno = alunoService.buscarPorId(dto.alunoId());
        Resgate resgate = new Resgate();
        resgate.setCupom(gerarCupomAleatorio());
        resgate.setCodigo(String.valueOf(System.currentTimeMillis()));
        resgate.setPagador(aluno);
        resgate.setData(LocalDateTime.now());
        resgate.setValor(vantagem.getCusto());
        resgate.setPagador(aluno);
        resgate.setVantagem(vantagem);
        resgate.setUtilizado(false);
        vantagem.setEstoque(vantagem.getEstoque() - 1);
        if (vantagem.getEstoque() <= 0) {
            vantagem.setDisponivel(false);
        }
        Resgate salvo = resgateRepository.save(resgate);
        emailService.sendEmail(aluno.getEmail(), "Confirmação de Resgate",
                "Você resgatou a vantagem: " + vantagem.getNome() + "\nCupom: " + resgate.getCupom());
        emailService.sendEmail(vantagem.getEmpresa().getEmail(), "Notificação de Resgate", "A vantagem "
                + vantagem.getNome() + " foi resgatada por " + aluno.getNome() + "\nCupom: " + resgate.getCupom());
        vantagemService.salvarVantagem(vantagem);

        return resgateMapper.toResponse(salvo);
    }

    public String validarCupomResgate(CupomResponse cupom) {
        Resgate resgate = buscarResgatePorCupom(cupom.cupomParceiro());

        if (!resgate.getCupom().equals(cupom.cupomAluno())) {
            processarCupomInvalido(resgate);
            return "Cupom inválido. O resgate foi cancelado e o valor reembolsado ao aluno.";
        }

        return "Cupom válido para a vantagem: " + resgate.getVantagem().getNome();
    }

    private Resgate buscarResgatePorCupom(String cupomParceiro) {
        return resgateRepository.findByCupom(cupomParceiro)
                .orElseThrow(() -> new ResgateException("Cupom inválido"));
    }

    private void processarCupomInvalido(Resgate resgate) {
        resgate.setUtilizado(true);
        resgateRepository.save(resgate);

        Vantagem vantagem = atualizarEstoqueVantagem(resgate.getVantagem());
        Participante participanteAluno = participanteService.buscarPorId(resgate.getPagador().getId());

        reembolsarParticipante(participanteAluno, resgate, vantagem);
        enviarEmailReembolso(participanteAluno, vantagem, resgate.getValor());
    }

    private Vantagem atualizarEstoqueVantagem(Vantagem vantagem) {
        vantagem.setEstoque(vantagem.getEstoque() + 1);
        vantagem.setDisponivel(true);
        return vantagemService.salvarVantagem(vantagem);
    }

    private void reembolsarParticipante(Participante participante, Resgate resgate, Vantagem vantagem) {
        participanteService.atualizarSaldo(participante.getId(), resgate.getValor());

        Distribuicao distribuicao = new Distribuicao();
        distribuicao.setCodigo(UUID.randomUUID().toString());
        distribuicao.setData(LocalDateTime.now());
        distribuicao.setValor(resgate.getValor());
        distribuicao.setMotivo("Reembolso por cupom inválido no resgate da vantagem: " + vantagem.getNome());
        distribuicao.setRecebedor(participante);

        distribuicaoRepository.save(distribuicao);
    }

    private void enviarEmailReembolso(Participante participante, Vantagem vantagem, double valor) {
        String assunto = "Distribuição de Moedas";
        String mensagem = "Reembolso por cupom inválido no resgate da vantagem: " + vantagem.getNome() + "\nValor: "
                + valor;
        emailService.sendEmail(participante.getEmail(), assunto, mensagem);
    }

    private String gerarCupomAleatorio() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder cupom = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(caracteres.length());
            cupom.append(caracteres.charAt(index));
        }

        return cupom.toString();
    }

    public List<ResgateResponse> listarResgatesPorAluno(Long alunoId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);

        List<Resgate> resgates = aluno.getTransacoesComoPagador().stream()
                .filter(transacao -> transacao instanceof Resgate)
                .map(transacao -> (Resgate) transacao)
                .collect(Collectors.toList());

        return resgates.stream()
                .map(resgateMapper::toResponse)
                .collect(Collectors.toList());
    }

    public VantagemResponse mostrarDetalhesVantagemPorResgate(Long resgateId) {
        Resgate resgate = resgateRepository.findById(resgateId)
                .orElseThrow(() -> new ResgateException("Resgate não encontrado"));

        Vantagem vantagem = resgate.getVantagem();
        if (vantagem == null) {
            throw new ResgateException("Nenhuma vantagem associada a este resgate");
        }

        return vantagemMapper.toResponse(vantagem);
    }
}
