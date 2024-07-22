package com.swyp3.babpool.domain.reject.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentRejectRequest;
import com.swyp3.babpool.domain.reject.dao.RejectRepository;
import com.swyp3.babpool.domain.reject.exception.RejectErrorCode;
import com.swyp3.babpool.domain.reject.exception.RejectException;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RejectServiceImpl implements RejectService{

    private final TsidKeyGenerator tsidKeyGenerator;
    private final RejectRepository rejectRepository;

    @Override
    public void createReject(AppointmentRejectRequest appointmentRejectRequest) {
        appointmentRejectRequest.setRejectId(tsidKeyGenerator.generateTsid());
        int savedRows = rejectRepository.saveRejectData(appointmentRejectRequest);
        if(savedRows != 1){
            throw new RejectException(RejectErrorCode.REJECT_NOT_FOUND, "Reject data save failed");
        }
    }


}
