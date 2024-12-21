package com.traveladvisor.paymentserver.service.domain.usecase.command;

import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.common.domain.vo.Money;
import com.traveladvisor.paymentserver.service.domain.dto.command.CreatePointEntryCommand;
import com.traveladvisor.paymentserver.service.domain.entity.PointEntry;
import com.traveladvisor.paymentserver.service.domain.exception.PaymentApplicationServiceException;
import com.traveladvisor.paymentserver.service.domain.mapper.PointEntryMapper;
import com.traveladvisor.paymentserver.service.domain.port.output.repository.PointEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreatePointEntryCommandHandler {

    private final PointEntryRepository pointEntryRepository;
    private final PointEntryMapper pointEntryMapper;

    @Transactional
    public void createPointEntry(CreatePointEntryCommand createPointEntryCommand) {
        // 이미 생성된, 중복되는 포인트 계좌가 존재하는지 확인합니다.
        validatePointEntryIsNotExists(createPointEntryCommand.memberId());

        // 새로운 포인트 계좌 도메인 객체를 생성합니다.
        PointEntry pointEntry = createPointEntry(createPointEntryCommand.memberId());

        // 생성한 포인트 계좌를 저장합니다.
        savePointEntry(pointEntry);
    }

    /**
     * 포인트 계좌 도메인 객체를 생성합니다.
     *
     * @param memberId
     * @return
     */
    private PointEntry createPointEntry(UUID memberId) {
        return pointEntryMapper.toPointEntry(memberId);
    }

    /**
     * 포인트 계좌가 이미 생성돼 있는지 확인합니다.
     *
     * @param memberId 회원 ID
     */
    private void validatePointEntryIsNotExists(UUID memberId) {
        pointEntryRepository.findById(new MemberId(memberId))
                .ifPresent(pointEntry -> {
                    throw new PaymentApplicationServiceException("이미 생성된 포인트 계좌가 존재합니다.");
                });
    }

    /**
     * 포인트 계좌를 저장합니다.
     *
     * @param pointEntry
     * @return 데이터베이스에 저장된 pointEntry
     */
    private PointEntry savePointEntry(PointEntry pointEntry) {
        PointEntry savedPointEntry = pointEntryRepository.save(pointEntry);
        log.info("포인트 계좌가 데이터베이스에 생성됐습니다. 생성된 포인트 계좌 ID: {}", savedPointEntry.getId().getValue());

        return savedPointEntry;
    }

    /**
     * 포인트 계좌에 포인트를 적립합니다.
     *
     * @param money
     * @param pointEntry
     */
    private void addPointEntry(Money money, PointEntry pointEntry) {
        pointEntry.addPointAmount(money);
    }

}
