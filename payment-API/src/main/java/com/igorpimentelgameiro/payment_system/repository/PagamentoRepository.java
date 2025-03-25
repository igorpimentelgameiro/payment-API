package com.igorpimentelgameiro.payment_system.repository;

import com.igorpimentelgameiro.payment_system.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

}
