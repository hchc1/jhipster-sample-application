package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cliente;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ClienteRepositoryWithBagRelationshipsImpl implements ClienteRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Cliente> fetchBagRelationships(Optional<Cliente> cliente) {
        return cliente.map(this::fetchTags);
    }

    @Override
    public Page<Cliente> fetchBagRelationships(Page<Cliente> clientes) {
        return new PageImpl<>(fetchBagRelationships(clientes.getContent()), clientes.getPageable(), clientes.getTotalElements());
    }

    @Override
    public List<Cliente> fetchBagRelationships(List<Cliente> clientes) {
        return Optional.of(clientes).map(this::fetchTags).orElse(Collections.emptyList());
    }

    Cliente fetchTags(Cliente result) {
        return entityManager
            .createQuery("select cliente from Cliente cliente left join fetch cliente.tags where cliente is :cliente", Cliente.class)
            .setParameter("cliente", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Cliente> fetchTags(List<Cliente> clientes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, clientes.size()).forEach(index -> order.put(clientes.get(index).getId(), index));
        List<Cliente> result = entityManager
            .createQuery(
                "select distinct cliente from Cliente cliente left join fetch cliente.tags where cliente in :clientes",
                Cliente.class
            )
            .setParameter("clientes", clientes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
