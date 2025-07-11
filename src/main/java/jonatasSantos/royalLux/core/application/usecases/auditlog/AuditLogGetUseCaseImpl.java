package jonatasSantos.royalLux.core.application.usecases.auditlog;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.AuditLogRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.auditlog.AuditLogGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.auditlog.AuditLogGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.auditlog.AuditLogGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.application.models.dtos.customerservice.CustomerServiceGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.AuditLog;
import jonatasSantos.royalLux.core.domain.entities.CustomerService;
import jonatasSantos.royalLux.core.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuditLogGetUseCaseImpl implements AuditLogGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogGetUseCaseImpl(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AuditLogGetUseCaseOutputDto> execute(User user, AuditLogGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuditLog> query = cb.createQuery(AuditLog.class);
        Root<AuditLog> root = query.from(AuditLog.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.userId() != null) {
            var userFound = userRepository.findById(input.userId().toString()).orElse(null);

            if (userFound != null) {
                predicates.add(cb.equal(root.get("user"), userFound));
            } else {
                predicates.add(cb.isNull(root.get("user")));
            }
        }

        if (input.origin() != null)
            predicates.add(cb.like(root.get("origin"), "%" + input.origin() + "%"));

        if (input.method() != null)
            predicates.add(cb.like(root.get("method"), "%" + input.method() + "%"));

        if (input.parameters() != null)
            predicates.add(cb.like(root.get("parameters"), "%" + input.parameters() + "%"));

        if (input.result() != null)
            predicates.add(cb.like(root.get("result"), "%" + input.result() + "%"));

        if (input.description() != null)
            predicates.add(cb.like(root.get("description"), "%" + input.description() + "%"));

        if (input.stackTrace() != null)
            predicates.add(cb.like(root.get("stackTrace"), "%" + input.stackTrace() + "%"));

        if (input.status() != null)
            predicates.add(cb.like(root.get("status"), "%" + input.status() + "%"));

        // startTime

        if (input.year() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("createdAt"), cb.literal("YYYY")),
                    input.year().toString()
            ));
        }
        if (input.month() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("createdAt"), cb.literal("MM")),
                    String.format("%02d", input.month())
            ));
        }
        if (input.day() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("createdAt"), cb.literal("DD")),
                    String.format("%02d", input.day())
            ));
        }
        if (input.hour() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("createdAt"), cb.literal("HH24")),
                    String.format("%02d", input.hour())
            ));
        }
        if (input.minute() != null) {
            predicates.add(cb.equal(
                    cb.function("TO_CHAR", String.class, root.get("createdAt"), cb.literal("MI")),
                    String.format("%02d", input.minute())
            ));
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<AuditLog> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var auditLogs = typedQuery.getResultList();

        return auditLogs.stream()
                .map(auditLogFound -> new AuditLogGetUseCaseOutputDto(
                        auditLogFound.getId(),
                        auditLogFound.getUserId(),
                        auditLogFound.getOrigin(),
                        auditLogFound.getMethod(),
                        auditLogFound.getParameters(),
                        auditLogFound.getResult(),
                        auditLogFound.getDescription(),
                        auditLogFound.getStackTrace(),
                        auditLogFound.getStatus(),
                        auditLogFound.getCreatedAt()
                )).toList();
    }
}
