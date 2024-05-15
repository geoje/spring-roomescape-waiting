package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberPassword;
import roomescape.global.JwtManager;
import roomescape.repository.JpaMemberRepository;
import roomescape.service.dto.member.MemberCreateRequest;
import roomescape.service.dto.member.MemberLoginRequest;
import roomescape.service.dto.member.MemberResponse;
import roomescape.service.exception.UnauthorizedEmailException;
import roomescape.service.exception.UnauthorizedPasswordException;

import java.util.List;

@Service
public class MemberService {

    private final JpaMemberRepository memberRepository;
    private final JwtManager jwtManager;

    public MemberService(JpaMemberRepository memberRepository, JwtManager jwtManager) {
        this.memberRepository = memberRepository;
        this.jwtManager = jwtManager;
    }

    public void signup(MemberCreateRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입되어 있는 이메일 주소입니다.");
        }
        memberRepository.save(request.toMember());
    }

    public String login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedEmailException("이메일이 존재하지 않습니다."));

        MemberPassword requestPassword = new MemberPassword(request.getPassword());
        if (member.isMismatchedPassword(requestPassword)) {
            throw new UnauthorizedPasswordException("비밀번호가 올바르지 않습니다.");
        }
        return jwtManager.generateToken(member);
    }

    public List<MemberResponse> findAllMemberNames() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::new)
                .toList();
    }
}