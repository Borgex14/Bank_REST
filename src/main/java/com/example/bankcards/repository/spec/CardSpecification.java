package com.example.bankcards.repository.spec;

import com.example.bankcards.dto.request.CardFilterRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CardSpecification {

    public static Specification<Card> withFilters(User user, CardFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("user"), user));

            if (filter != null) {
                if (filter.getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), CardStatus.valueOf(String.valueOf(filter.getStatus()))));
                }
                if (filter.getType() != null) {
                    predicates.add(cb.equal(root.get("type"), CardType.valueOf(String.valueOf(filter.getType()))));
                }
                if (filter.getCurrency() != null) {
                    predicates.add(cb.equal(root.get("currency"), filter.getCurrency()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}