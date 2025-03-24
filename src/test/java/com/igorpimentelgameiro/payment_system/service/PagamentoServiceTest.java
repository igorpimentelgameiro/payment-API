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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.igorpimentelgameiro.payment_system.util.PagamentoMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        ResponseEntity<DetalhamentoPagamentoDTO> result = pagamentoService.salvarPagamento(getPagDTOMock_Sucesso(), UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void testSalvarPagamento_Falha_Pix() {
        // Preparando os dados antes da lambda
        PagamentoDTO pagamentoDTO = getPagDTOMock_Falha_Pix();

        // Preparando o UriComponentsBuilder antes da lambda
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // Verificando a exceção gerada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            // Chamada única ao serviço que pode gerar a exceção
            pagamentoService.salvarPagamento(pagamentoDTO, uriBuilder);
        });

        // Verificando se a exceção gerada é a esperada (BAD_REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


    @Test
    void testSalvarPagamento_Falha_Boleto() {
        // Preparando os dados antes da lambda
        PagamentoDTO pagamentoDTO = getPagDTOMock_Falha_Boleto();

        // Preparando o UriComponentsBuilder antes da lambda
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // Verificando a exceção gerada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            // Chamando o método que pode lançar a exceção
            pagamentoService.salvarPagamento(pagamentoDTO, uriBuilder);
        });

        // Verificando se a exceção gerada é a esperada (BAD_REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


    @Test
    void testSalvarPagamento_Falha_Cartao_Credito() {
        // Preparando os dados antes da lambda
        PagamentoDTO pagamentoDTO = getPagDTOMock_Falha_Cartao_Credito();

        // Preparando o UriComponentsBuilder antes da lambda
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // Verificando a exceção gerada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            // Chamando o método que pode lançar a exceção
            pagamentoService.salvarPagamento(pagamentoDTO, uriBuilder);
        });

        // Verificando se a exceção gerada é a esperada (BAD_REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


    @Test
    void testSalvarPagamento_Falha_Cartao_Debito() {
        // Preparando os dados antes da lambda
        PagamentoDTO pagamentoDTO = getPagDTOMock_Falha_Cartao_Debito();

        // Preparando o UriComponentsBuilder antes da lambda
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // Verificando a exceção gerada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            // Chamando o método que pode lançar a exceção
            pagamentoService.salvarPagamento(pagamentoDTO, uriBuilder);
        });

        // Verificando se a exceção gerada é a esperada (BAD_REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


    @Test
    void testAtualizarStatusPagamento_Sucesso() {
        // Preparando os dados antes da lambda
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValor(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PENDENTE_PROCESSAMENTO);

        // Configuração do mock do repositório
        when(pagamentoRepository.getReferenceById(Long.valueOf(getAtualizarPagDTOMock_Sucesso().codigoPagamento())))
                .thenReturn(pagamentoAnterior);

        // Preparando o objeto para o método
        AtualizarPagamentoDTO atualizarPagamentoDTO = getAtualizarPagDTOMock_Sucesso();

        // Verificando o retorno
        ResponseEntity<PagamentoDTO> result = pagamentoService.atualizarStatusPagamento(atualizarPagamentoDTO);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    void testAtualizarStatusPagamento_Falha_caso1() {
        // Preparando os dados antes da lambda
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValor(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_SUCESSO);

        // Configuração do mock do repositório
        when(pagamentoRepository.getReferenceById(Long.valueOf(getAtualizarPagDTOMock_Falha_Caso1().codigoPagamento())))
                .thenReturn(pagamentoAnterior);

        // Preparando o objeto para o método
        AtualizarPagamentoDTO atualizarPagamentoDTO = getAtualizarPagDTOMock_Sucesso();

        // Verificando a exceção gerada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.atualizarStatusPagamento(atualizarPagamentoDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


    @Test
    void testAtualizarStatusPagamento_Falha_caso2() {
        // Preparando os dados antes da lambda
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValor(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_FALHA);

        // Configuração do mock do repositório
        when(pagamentoRepository.getReferenceById(Long.valueOf(getAtualizarPagDTOMock_Sucesso().codigoPagamento())))
                .thenReturn(pagamentoAnterior);

        // Preparando o objeto para o método
        AtualizarPagamentoDTO atualizarPagamentoDTO = getAtualizarPagDTOMock_Sucesso();

        // Verificando a exceção gerada
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
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValor(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PENDENTE_PROCESSAMENTO);

        when(pagamentoRepository.getReferenceById(Long.valueOf(pagamentoAnterior.getCodigoPagamento()))).thenReturn(pagamentoAnterior);

        ResponseEntity<DetalhamentoPagamentoDTO> result = pagamentoService.excluirPagamento(1);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testExcluirPagamento_Falha() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValor(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_SUCESSO);

        when(pagamentoRepository.getReferenceById(Long.valueOf(pagamentoAnterior.getCodigoPagamento()))).thenReturn(pagamentoAnterior);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.excluirPagamento(1);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testDetalharPagamento() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValor(100.00);
        pagamentoAnterior.setMetodoPagamento(MetodoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_SUCESSO);

        when(pagamentoRepository.getReferenceById(Long.valueOf(pagamentoAnterior.getCodigoPagamento()))).thenReturn(pagamentoAnterior);

        ResponseEntity<DetalhamentoPagamentoDTO> result = pagamentoService.detalharPagamento(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        //
    }
}