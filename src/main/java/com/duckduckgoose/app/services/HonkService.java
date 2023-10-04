package com.duckduckgoose.app.services;

import com.duckduckgoose.app.models.database.Honk;
import com.duckduckgoose.app.models.database.Member;
import com.duckduckgoose.app.models.request.HonkRequest;
import com.duckduckgoose.app.repositories.HonkRepository;
import com.duckduckgoose.app.repositories.MemberRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HonkService {

    private final HonkRepository honkRepository;

    private final MemberRepository memberRepository;

    @Autowired
    public HonkService(HonkRepository honkRepository, MemberRepository memberRepository) {
        this.honkRepository = honkRepository;
        this.memberRepository = memberRepository;
    }

    public void createHonk(Member author, HonkRequest request) throws ValidationException {
        Honk honk = new Honk(author, request.getContent());

        this.honkRepository.save(honk);
    }
  
    public Page<Honk> getHonks(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return honkRepository.findAllByOrderByTimestampDesc(pageable);
        } else {
            return honkRepository.findByContentContainingOrderByTimestampDesc(search, pageable);
        }
    }
    public Page<Honk> getMemberHonks(Member author, String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return honkRepository.findByAuthorOrderByTimestampDesc(author, pageable);
        } else {
            return honkRepository.findByContentContainingAndAuthorOrderByTimestampDesc(search, author, pageable);
        }
    }

}
