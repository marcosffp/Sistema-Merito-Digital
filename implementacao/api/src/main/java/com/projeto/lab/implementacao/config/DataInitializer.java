package com.projeto.lab.implementacao.config;

import com.projeto.lab.implementacao.model.Instituicao;
import com.projeto.lab.implementacao.model.Professor;
import com.projeto.lab.implementacao.repository.InstituicaoRepository;
import com.projeto.lab.implementacao.repository.ProfessorRepository;
import com.projeto.lab.implementacao.service.ProfessorService;

import lombok.AllArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class DataInitializer {
    private final PasswordEncoder passwordEncoder;
    private final ProfessorService professorService;

    @Bean
    CommandLineRunner initializeInstituicoesEProfessores(InstituicaoRepository instituicaoRepository,
            ProfessorRepository professorRepository) {
        return args -> {
            if (instituicaoRepository.count() == 0) {
                List<Instituicao> instituicoes = new ArrayList<>();

                Instituicao puc = new Instituicao();
                puc.setNome("PUC Minas");
                puc.setCnpj("12.345.678/0001-90");
                puc.setEndereco("Av. Dom José Gaspar, 500");
                instituicoes.add(puc);

                Instituicao ufmg = new Instituicao();
                ufmg.setNome("UFMG");
                ufmg.setCnpj("98.765.432/0001-10");
                ufmg.setEndereco("Av. Pres. Antônio Carlos, 6627");
                instituicoes.add(ufmg);

                Instituicao unibh = new Instituicao();
                unibh.setNome("UNI-BH");
                unibh.setCnpj("11.222.333/0001-44");
                unibh.setEndereco("Rua dos Goitacazes, 1159");
                instituicoes.add(unibh);

                instituicaoRepository.saveAll(instituicoes);
                System.out.println("Instituições iniciais cadastradas com sucesso!");
            }

            if (professorRepository.count() == 0) {
                List<Instituicao> instituicoes = instituicaoRepository.findAll();

                if (!instituicoes.isEmpty()) {
                    Instituicao puc = instituicoes.get(0);
                    Instituicao ufmg = instituicoes.size() > 1 ? instituicoes.get(1) : puc;
                    Instituicao uniBh = instituicoes.size() > 2 ? instituicoes.get(2) : puc;

                    List<Professor> professores = new ArrayList<>();

                    Professor prof1 = new Professor();
                    prof1.setEmail("professor.silva@pucminas.br");
                    prof1.setSenha(passwordEncoder.encode("senha123"));
                    prof1.setNome("Carlos Silva");
                    prof1.setCpf("123.456.789-00");
                    prof1.setInstituicao(puc);
                    prof1.setDepartamento("Departamento de Computação");
                    prof1.setSaldoMoedas(0.0);
                    professores.add(prof1);

                    Professor prof2 = new Professor();
                    prof2.setEmail("maria.santos@ufmg.br");
                    prof2.setSenha(passwordEncoder.encode("senha456"));
                    prof2.setNome("Maria Santos");
                    prof2.setCpf("987.654.321-00");
                    prof2.setInstituicao(ufmg);
                    prof2.setDepartamento("Departamento de Matemática");
                    prof2.setSaldoMoedas(0.0);
                    professores.add(prof2);

                    Professor prof3 = new Professor();
                    prof3.setEmail("joao.oliveira@unibh.br");
                    prof3.setSenha(passwordEncoder.encode("senha789"));
                    prof3.setNome("João Oliveira");
                    prof3.setCpf("456.789.123-00");
                    prof3.setInstituicao(uniBh);
                    prof3.setDepartamento("Departamento de Engenharia");
                    prof3.setSaldoMoedas(0.0);
                    professores.add(prof3);

                    Professor prof4 = new Professor();
                    prof4.setEmail("ana.costa@pucminas.br");
                    prof4.setSenha(passwordEncoder.encode("senha321"));
                    prof4.setNome("Ana Costa");
                    prof4.setCpf("111.222.333-44");
                    prof4.setInstituicao(puc);
                    prof4.setDepartamento("Departamento de Física");
                    prof4.setSaldoMoedas(0.0);
                    professores.add(prof4);

                    professorRepository.saveAll(professores);
                    System.out.println("Professores iniciais cadastrados com sucesso!");
                    professorService.saldosInicializar();
                }
            }

            professorService.atualizarSaldoSemestral();
        };
    }
}