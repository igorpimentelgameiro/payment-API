package com.igorpimentelgameiro.payment_system.controller;

import com.igorpimentelgameiro.payment_system.dto.AtualizarPagamentoDTO;
import com.igorpimentelgameiro.payment_system.dto.PagamentoDTO;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;
import com.igorpimentelgameiro.payment_system.dto.DetalhamentoPagamentoDTO;
import com.igorpimentelgameiro.payment_system.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/criarPagamento")
    @Transactional
    @Operation(summary = "cria um novo pagamento", description = "O numero do cartão só deverá ser informado se o pagamento for pelo método de cartão de crédito ou débito")
    public ResponseEntity<DetalhamentoPagamentoDTO> novoPagamento(@RequestBody @Valid PagamentoDTO pagamentoDTO, UriComponentsBuilder uriComponentsBuilder) {
        return pagamentoService.salvarPagamento(pagamentoDTO, uriComponentsBuilder);

    }

    @GetMapping("/listaPagamento")
    @Operation(summary = "Realiza o filtro dos pagamentos por parâmetros")
    public ResponseEntity<List<DetalhamentoPagamentoDTO>> listarPagamentos(
            @RequestParam(required = false) Integer codigoDebito,
            @RequestParam(required = false) String cpfCnpj,
            @RequestParam(required = false) StatusPagamento status) {
        return pagamentoService.filtrarPagamento(codigoDebito, cpfCnpj, status);
    }

    @PutMapping("atualizarPagamento")
    @Transactional
    @Operation(summary = "Atualiza o status de um pagamento")
    public ResponseEntity<PagamentoDTO> atualizarStatus(@RequestBody @Valid AtualizarPagamentoDTO atualizarPagamentoDTO) {
        return pagamentoService.atualizarStatusPagamento(atualizarPagamentoDTO);
    }

    @DeleteMapping("/desativarPagamento")
    @Transactional
    @Operation(summary = "Inativa um pagamento")
    public ResponseEntity<DetalhamentoPagamentoDTO> excluirPagamento(@RequestParam Integer codigoDebito) {
        return pagamentoService.excluirPagamento(codigoDebito);
    }

    @GetMapping("/detalharPagamento")
    @Operation(summary = "Detalha um pagamento")
    public ResponseEntity<DetalhamentoPagamentoDTO> detalharPagamento(@RequestParam Integer codigoPagamento) {
        return pagamentoService.detalharPagamento(codigoPagamento);
    }
}