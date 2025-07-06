package jonatasSantos.royalLux.core.application.usecases.payment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.CustomerServiceRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.PaymentRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.contracts.usecases.payment.PaymentGetUseCase;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentGetUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.payment.PaymentGetUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Payment;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentGetUseCaseImpl implements PaymentGetUseCase {

    @PersistenceContext
    private EntityManager entityManager;
    private final PaymentRepository paymentRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final UserRepository userRepository;

    public PaymentGetUseCaseImpl(PaymentRepository paymentRepository, CustomerServiceRepository customerServiceRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.customerServiceRepository = customerServiceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PaymentGetUseCaseOutputDto> execute(User user, PaymentGetUseCaseInputDto input, Integer page, Integer size, Boolean ascending) {
        var userLogged = this.userRepository.findById(String.valueOf(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Seu usuário é inexistente"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Payment> query = cb.createQuery(Payment.class);
        Root<Payment> root = query.from(Payment.class);

        List<Predicate> predicates = new ArrayList<>();

        if (input.id() != null)
            predicates.add(cb.equal(root.get("id"), input.id()));

        if (input.customerServiceId() != null) {
            var customerServiceFound = customerServiceRepository.findById(input.customerServiceId().toString()).orElse(null);

            if (customerServiceFound != null) {
                predicates.add(cb.equal(root.get("customerService"), customerServiceFound));
            } else {
                predicates.add(cb.isNull(root.get("customerService")));
            }
        }

        if (input.createdByUserId() != null) {
            var userFound = userRepository.findById(input.createdByUserId().toString()).orElse(null);

            if (userFound != null) {
                predicates.add(cb.equal(root.get("createdByUser"), userFound));
            } else {
                predicates.add(cb.isNull(root.get("createdByUser")));
            }
        }

        if (input.status() != null)
            predicates.add(cb.like(root.get("status"), "%" + input.status() + "%"));

        if (input.method() != null)
            predicates.add(cb.like(root.get("method"), "%" + input.method() + "%"));

        if (input.description() != null)
            predicates.add(cb.like(root.get("description"), "%" + input.description() + "%"));

        if (input.transactionId() != null)
            predicates.add(cb.like(root.get("transactionId"), "%" + input.transactionId() + "%"));

        if (input.paymentToken() != null)
            predicates.add(cb.like(root.get("paymentToken"), "%" + input.paymentToken() + "%"));

        if (input.paymentUrl() != null)
            predicates.add(cb.like(root.get("paymentUrl"), "%" + input.paymentUrl() + "%"));

        if (input.payerName() != null)
            predicates.add(cb.like(root.get("payerName"), "%" + input.payerName() + "%"));

        query.where(predicates.toArray(new Predicate[0]));

        if (Boolean.TRUE.equals(ascending)) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<Payment> typedQuery = entityManager.createQuery(query);

        int setPage = (page != null && page >= 0) ? page : 0;
        Integer setSize = (size != null && size > 0) ? size : null;

        if (setSize != null) {
            typedQuery.setFirstResult(setPage * setSize);
            typedQuery.setMaxResults(setSize);
        }

        var payments = typedQuery.getResultList();

        if (userLogged.getRole().equals(UserRole.CLIENT)) {
            payments = payments.stream()
                    .filter(paymentFound -> {
                        var customerService = this.customerServiceRepository
                                .findById(String.valueOf(paymentFound.getCustomerService().getId()));

                        return customerService
                                .map(customerServiceFound ->
                                        customerServiceFound.getClient().getUser().getId() == userLogged.getId()
                                ).orElse(false);
                    })
                    .toList();
        }

        if(!userLogged.getRole().equals(UserRole.ADMIN))
            payments.forEach(payment -> {
                payment.setPaymentToken(null);
                payment.setPaymentUrl(null);
                payment.setTransactionId(null);
            });

        return payments.stream()
                .map(paymentFound -> new PaymentGetUseCaseOutputDto(
                        paymentFound.getId(),
                        paymentFound.getCustomerService().getId(),
                        paymentFound.getCreatedByUser().getId(),
                        paymentFound.getStatus(),
                        paymentFound.getMethod(),
                        paymentFound.getDescription(),
                        paymentFound.getTransactionId(),
                        paymentFound.getPaymentToken(),
                        paymentFound.getPaymentUrl(),
                        paymentFound.getPayerName(),
                        paymentFound.getCreatedAt(),
                        paymentFound.getUpdatedAt()
                )).toList();
    }
}
