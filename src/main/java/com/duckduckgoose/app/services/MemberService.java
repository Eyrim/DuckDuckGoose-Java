package com.duckduckgoose.app.services;

import com.duckduckgoose.app.models.database.Member;
import com.duckduckgoose.app.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Page<Member> getMembers(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return memberRepository.findAll(pageable);
        } else {
            return memberRepository.findByUsernameContaining(search, pageable);
        }
    }

    public Page<Member> getFollowedMembers(Member followerMember, String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return memberRepository.findByFollowerMembersContaining(followerMember, pageable);
        } else {
            return memberRepository.findByUsernameContainingAndFollowerMembersContaining(search, followerMember, pageable);
        }
    }

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public void addFollower(Member followerMember, Member followedMember) {
        followerMember.getFollowedMembers().add(followedMember);
        memberRepository.save(followerMember);
    }

    public void removeFollower(Member followerMember, Member followedMember) {
        followerMember.getFollowedMembers().remove(followedMember);
        memberRepository.save(followerMember);
    }
}
