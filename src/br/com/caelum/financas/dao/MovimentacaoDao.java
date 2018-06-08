package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.caelum.financas.exception.ValorInvalidoException;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

@Stateless
public class MovimentacaoDao {
	
	@Inject
    private EntityManager manager;

	public void adiciona(Movimentacao movimentacao) {
		this.manager.joinTransaction();
		this.manager.persist(movimentacao);
		
		if(movimentacao.getValor().compareTo(BigDecimal.ZERO) < 0) {
			throw new ValorInvalidoException ("Movimentacao negativa");
		}
	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> listaComCategorias() {
		return this.manager.createQuery("select distinct m from Movimentacao m left join fetch m.categorias", Movimentacao.class).getResultList();
	}
	
	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}
	
	public List<Movimentacao> listaTodasMovimentacoes(Conta conta){
		String jpql = "select m from Movimentacao m " + "where m.conta = :conta order by m.valor desc";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("conta", conta);
		return query.getResultList();
		
	}
	
	public List<Movimentacao> listaPorValorETipo(BigDecimal valor,TipoMovimentacao tipo){
		String jpql = "select m from Movimentacao m " + "where m.valor <= :valor and m.tipoMovimentacao = :tipo";
		
		Query query = this.manager.createQuery(jpql);
		query.setParameter("valor", valor);
		query.setParameter("tipo", tipo);
		
		query.setHint("org.hibernate.cacheable", "true");
		
		return query.getResultList();
	}
	
	public BigDecimal calculaTotalMovimentacao(Conta conta,TipoMovimentacao tipo){
		String jpql = "select sum(m.valor) from Movimentacao m where m.conta = :conta and m.tipoMovimentacao = :tipo";
		return manager.createQuery(jpql, BigDecimal.class)
		    .setParameter("conta", conta)
		    .setParameter("tipo", tipo)
		    .getSingleResult();
	}
	
	public List<Movimentacao> buscaTodasMovimentacoesDaConta(String titular){
		String jpql = "select m from Movimentacao m where m.conta.titular like :titular";
		return manager.createQuery(jpql, Movimentacao.class)
				.setParameter("titular", titular)
				.getResultList();
	}
	
	public List<ValorPorMesEAno> listaMesesComMovimentacoes(Conta conta, TipoMovimentacao tipo){
		String jpql = "select new br.com.caelum.financas.modelo.ValorPorMesEAno(month(m.data), "
				+ "year(m.data), sum(m.valor)) "
				+ "from Movimentacao m where m.conta = :conta and m.tipoMovimentacao = :tipo "
				+ "group by year(m.data), month(m.data) "
				+ "order by sum(m.valor) desc";
				
		return manager.createQuery(jpql, ValorPorMesEAno.class)
				.setParameter("conta", conta)
				.setParameter("tipo", tipo)
				.getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.joinTransaction();
		this.manager.remove(movimentacaoParaRemover);
	}

}
