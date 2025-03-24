package com.igorpimentelgameiro.payment_system.repository;

import com.igorpimentelgameiro.payment_system.Entity.Pagamento;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

}
