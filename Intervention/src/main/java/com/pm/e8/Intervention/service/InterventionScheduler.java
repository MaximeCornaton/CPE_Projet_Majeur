package com.pm.e8.Intervention.service;

import com.google.common.collect.Lists;
import com.pm.e8.Intervention.model.Intervention;
import com.pm.e8.Intervention.model.Status;
import com.pm.e8.Intervention.repository.InterventionRepository;
import com.project.model.dto.FireDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterventionScheduler {
    private final InterventionRepository iRepo;
    private final FireRestClientService fireRestClientService;

    public InterventionScheduler(InterventionRepository iRepo, FireRestClientService fireRestClientService) {
        this.iRepo = iRepo;
        this.fireRestClientService = fireRestClientService;
    }

    @Scheduled(fixedRate = 5000)
    private void updateIntervention() {
        List<Intervention> InterventionList = Lists.newArrayList(iRepo.findAll());
        List<FireDto> fList = fireRestClientService.getFires();
        for (Intervention i : InterventionList) {
            if (i.getStatus().equals("En cours")) {
                for (FireDto f : fList) {
                    if (f.getId() == i.getIdFire()) {
                        //Rien à faire le feu existe encore
                    }
                    else {
                        i.setStatus(Status.TERMINEE);
                    }
                }
                iRepo.save(i);
            }
        }
    }
}
