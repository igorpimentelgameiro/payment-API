package com.igorpimentelgameiro.payment_system.service;

import com.igorpimentelgameiro.payment_system.entity.Pagamento;
import com.igorpimentelgameiro.payment_system.dto.AtualizarPagamentoDTO;
import com.igorpimentelgameiro.payment_system.dto.DetalhamentoPagamentoDTO;
import com.igorpimentelgameiro.payment_system.dto.PagamentoDTO;
import com.igorpimentelgameiro.payment_system.enums.MetodoPagamento;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;
import com.igorpimentelgameiro.payment_system.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.igorpimentelgameiro.payment_system.util.PagamentoMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PagamentoServiceTest {

    @Mock
    PagamentoRepository pagamentoRepository;

    @InjectMocks
    PagamentoService pagamentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvarPagamento_Sucesso() {

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(new Pagamento());

        ResponseEntity<DetalhamentoPagamentoDTO> result = pagamentoService.salvarPagamento(getPagDTOMock_Sucesso());


        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void testSalvarPagamento_Falha_Pix() {

        PagamentoDTO pagamentoDTO = getPagDTOMock_Falha_Pix();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.salvarPagamento(pagamentoDTO);
        });


        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testSalvarPagamento_Falha_Boleto() {

        PagamentoDTO pagamentoDTO = getPagDTOMock_Falha_Boleto(); // Mock onde numeroCartao não é null


        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.salvarPagamento(pagamentoDTO);
        });


        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());


        assertTrue(exception.getMessage().contains("Pagamento via BOLETO não necessita de número de cartão"));
    }


    @Test
    void testSalvarPagamento_Falha_Cartao_Credito() {
        PagamentoDTO pagamentoDTO = getPagDTOMock_Falha_Cartao_Credito();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.salvarPagamento(pagamentoDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testSalvarPagamento_Falha_Cartao_Debito() {
        PagamentoDTO pagamentoDTO = getPagDTOMock_Falha_Cartao_Debito();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.salvarPagamento(pagamentoDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testAtualizarStatusPagamento_Sucesso() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1L);
        pagamentoAnterior.setDocumento("123");
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PENDENTE_PROCESSAMENTO);

        when(pagamentoRepository.getReferenceById(1L)).thenReturn(pagamentoAnterior);

        AtualizarPagamentoDTO atualizarPagamentoDTO = getAtualizarPagDTOMock_Sucesso();

        ResponseEntity<PagamentoDTO> result = pagamentoService.atualizarStatusPagamento(atualizarPagamentoDTO);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAtualizarStatusPagamento_Falha_caso1() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1L); // Mudando para Long
        pagamentoAnterior.setDocumento("123");
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_SUCESSO);

        when(pagamentoRepository.getReferenceById(1L)).thenReturn(pagamentoAnterior);

        AtualizarPagamentoDTO atualizarPagamentoDTO = getAtualizarPagDTOMock_Sucesso();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.atualizarStatusPagamento(atualizarPagamentoDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testFiltrarPagamento() {
        when(pagamentoRepository.findAll()).thenReturn(List.of(new Pagamento()));

        ResponseEntity<List<DetalhamentoPagamentoDTO>> result = pagamentoService.filtrarPagamento(null, null, null);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testExcluirPagamento_Sucesso() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1L);
        pagamentoAnterior.setDocumento("123");
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PENDENTE_PROCESSAMENTO);

        when(pagamentoRepository.getReferenceById(1L)).thenReturn(pagamentoAnterior);

        ResponseEntity<DetalhamentoPagamentoDTO> result = pagamentoService.excluirPagamento(1);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDetalharPagamento() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1L);
        pagamentoAnterior.setDocumento("123");
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_SUCESSO);

        when(pagamentoRepository.getReferenceById(1L)).thenReturn(pagamentoAnterior);

        ResponseEntity<DetalhamentoPagamentoDTO> result = pagamentoService.detalharPagamento(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
