package com.igorpimentelgameiro.payment_system.service;

import com.igorpimentelgameiro.payment_system.dto.AtualizarPagamentoDTO;
import com.igorpimentelgameiro.payment_system.dto.DetalhamentoPagamentoDTO;
import com.igorpimentelgameiro.payment_system.dto.PagamentoDTO;
import com.igorpimentelgameiro.payment_system.enums.MetodoPagamento;
import com.igorpimentelgameiro.payment_system.Entity.Pagamento;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;
import com.igorpimentelgameiro.payment_system.repository.PagamentoRepository;
import com.igorpimentelgameiro.payment_system.validation.ValidaPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Stream;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public ResponseEntity<DetalhamentoPagamentoDTO> salvarPagamento(PagamentoDTO pagamentoDTO, UriComponentsBuilder uriComponentsBuilder){
        var pagamento = new Pagamento(pagamentoDTO);
        List<String> erros = Stream.of(
                        new ValidaPagamento(pagamento.getMetodoPagamento() == MetodoPagamento.PIX && pagamentoDTO.numeroCartao() != null,
                                "Pagamento via PIX não necessita de número de cartão"),
                        new ValidaPagamento(pagamento.getMetodoPagamento() == MetodoPagamento.BOLETO && pagamentoDTO.numeroCartao() != null,
                                "Pagamento via BOLETO não necessita de número de cartão"),
                        new ValidaPagamento((pagamento.getMetodoPagamento() == MetodoPagamento.CARTAO_CREDITO ||
                                pagamento.getMetodoPagamento() == MetodoPagamento.CARTAO_DEBITO) && pagamentoDTO.numeroCartao() == null,
                                "Pagamento usando cartão necessita do número do cartão de crédito ou débito"),
                        new ValidaPagamento(pagamento.getValor() == null || pagamentoDTO.valor() <= 0,
                                "Não é possivel fazer um pagamento de R$0,00 Reais")
                )
                .filter(ValidaPagamento::isInvalida)
                .map(ValidaPagamento::getMensagemErro)
                .toList();

        if (!erros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", erros));
        }

        pagamentoRepository.save(pagamento);

        var uri = uriComponentsBuilder.path("/pagamento/detalharPagamento/{id}").buildAndExpand(pagamento.getCodigoPagamento()).toUri();

        return ResponseEntity.created(uri).body(new DetalhamentoPagamentoDTO(pagamento));
    }

    public ResponseEntity<PagamentoDTO>  atualizarStatusPagamento(AtualizarPagamentoDTO atualizarPagamentoDTO){

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
                .toList();

        if (!erros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", erros));
        }

        pagamentoAnterior.atualizarStatus(atualizarPagamentoDTO.statusPagamento());

        return ResponseEntity.ok(new PagamentoDTO(pagamentoAnterior));
    }

    public ResponseEntity<List<DetalhamentoPagamentoDTO>> filtrarPagamento(Integer codigoPagamento, String documento, StatusPagamento statusPagamento){
        var pagamentoFiltrado = pagamentoRepository.findAll().stream()
                .filter(pagamento -> pagamento.getCodigoPagamento() == null || pagamento.getCodigoPagamento().equals(codigoPagamento))
                .filter(pagamento -> documento == null || pagamento.getDocumento().equals(documento))
                .filter(pagamento -> statusPagamento == null || pagamento.getStatusPagamento().equals(statusPagamento))
                .map(DetalhamentoPagamentoDTO::new)
                .toList();
        return ResponseEntity.ok(pagamentoFiltrado);
    }

    public ResponseEntity<DetalhamentoPagamentoDTO> excluirPagamento(Integer codigoDebito){

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

    public ResponseEntity<DetalhamentoPagamentoDTO> detalharPagamento(Integer codigoDebito){
        var pagamento = pagamentoRepository.getReferenceById(Long.valueOf(codigoDebito));
        return ResponseEntity.ok(new DetalhamentoPagamentoDTO(pagamento));
    }
}