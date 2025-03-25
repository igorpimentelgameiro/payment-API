package com.igorpimentelgameiro.payment_system.service;

import com.igorpimentelgameiro.payment_system.dto.AtualizarPagamentoDTO;
import com.igorpimentelgameiro.payment_system.dto.DetalhamentoPagamentoDTO;
import com.igorpimentelgameiro.payment_system.dto.PagamentoDTO;
import com.igorpimentelgameiro.payment_system.entity.Pagamento;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;
import com.igorpimentelgameiro.payment_system.repository.PagamentoRepository;
import com.igorpimentelgameiro.payment_system.util.MapperPagamentoPagamentoDto;
import com.igorpimentelgameiro.payment_system.validation.ValidaPagamento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;

    }

    public ResponseEntity<DetalhamentoPagamentoDTO> salvarPagamento(PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = new Pagamento(pagamentoDTO);

        List<String> erros = new ArrayList<>();

        switch (pagamento.getMetodoPagamento()) {
            case PIX:
                if (pagamentoDTO.getNumeroCartao() != null) {
                    erros.add("Pagamento via PIX não necessita de número de cartão");
                }
                break;

            case BOLETO:
                if (pagamentoDTO.getNumeroCartao() != null) {
                    erros.add("Pagamento via BOLETO não necessita de número de cartão");
                }
                break;

            case CARTAO_CREDITO:
            case CARTAO_DEBITO:
                if (pagamentoDTO.getNumeroCartao() == null) {
                    erros.add("Pagamento usando cartão necessita do número do cartão de crédito ou débito");
                }
                break;

            default:
                break;
        }

        if (pagamentoDTO.getValorPagamento() == null || pagamentoDTO.getValorPagamento() <= 0) {
            erros.add("Não é possível fazer um pagamento de R$0,00 Reais");
        }

        if (!erros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", erros));
        }

        pagamentoRepository.save(pagamento);

        DetalhamentoPagamentoDTO detalhamentoPagamentoDTO = new DetalhamentoPagamentoDTO(pagamento);

        return new ResponseEntity<>(detalhamentoPagamentoDTO, HttpStatus.CREATED);
    }


    public ResponseEntity<PagamentoDTO> atualizarStatusPagamento(AtualizarPagamentoDTO atualizarPagamentoDTO) {

        var pagamentoAnterior = pagamentoRepository.getReferenceById(Long.valueOf(atualizarPagamentoDTO.codigoPagamento()));

        List<String> erros = Stream.of(
                        new ValidaPagamento(pagamentoAnterior.getStatusPagamento().equals(StatusPagamento.PENDENTE_PROCESSAMENTO)
                                && atualizarPagamentoDTO.statusPagamento().equals(StatusPagamento.PENDENTE_PROCESSAMENTO),
                                "Não é possível atualizar o pagamento para o mesmo status"),

                        new ValidaPagamento(pagamentoAnterior.getStatusPagamento().equals(StatusPagamento.PROCESSADO_SUCESSO),
                                "Não é possível atualizar um pagamento que está processado com sucesso"),

                        new ValidaPagamento(pagamentoAnterior.getStatusPagamento().equals(StatusPagamento.PROCESSADO_FALHA)
                                && !atualizarPagamentoDTO.statusPagamento().equals(StatusPagamento.PENDENTE_PROCESSAMENTO),
                                "Só é possível atualizar um pagamento com falha para o status pendente de processamento")
                )
                .filter(ValidaPagamento::isInvalida)
                .map(ValidaPagamento::getMensagemErro)
                .collect(Collectors.toList());

        if (!erros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", erros));
        }

        pagamentoAnterior.atualizarStatus(atualizarPagamentoDTO.statusPagamento());

        PagamentoDTO pagamentoNovo = MapperPagamentoPagamentoDto.toDTO(pagamentoAnterior);

        return ResponseEntity.ok(pagamentoNovo);
    }

    public ResponseEntity<List<DetalhamentoPagamentoDTO>> filtrarPagamento(Integer codigoPagamento, String documento, StatusPagamento statusPagamento) {
        List<DetalhamentoPagamentoDTO> pagamentosFiltrados = pagamentoRepository.findAll().stream()

                .filter(pagamento -> (codigoPagamento == null || pagamento.getCodigoPagamento().equals(codigoPagamento.longValue())))

                .filter(pagamento -> documento == null || Objects.equals(String.valueOf(pagamento.getDocumento()), documento))

                .filter(pagamento -> statusPagamento == null || pagamento.getStatusPagamento().equals(statusPagamento))

                .map(DetalhamentoPagamentoDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pagamentosFiltrados);
    }


    public ResponseEntity<DetalhamentoPagamentoDTO> excluirPagamento(Integer codigoDebito) {

        var pagamento = pagamentoRepository.getReferenceById(Long.valueOf(codigoDebito));

        var inativaPagamento = Stream.of(
                        new ValidaPagamento(!pagamento.getStatusPagamento().equals(StatusPagamento.PENDENTE_PROCESSAMENTO),
                                "Não é possivel excluir um pagamento que não está pendente de processamento")
                )
                .filter(ValidaPagamento::isInvalida)
                .map(ValidaPagamento::getMensagemErro)
                .toList();

        if (!inativaPagamento.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", inativaPagamento));
        }

        pagamento.setAtivo(false);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<DetalhamentoPagamentoDTO> detalharPagamento(Integer codigoDebito) {
        var pagamento = pagamentoRepository.getReferenceById(Long.valueOf(codigoDebito));
        return ResponseEntity.ok(new DetalhamentoPagamentoDTO(pagamento));
    }
}