package loginpractice.jwt.member_jwt.repository;

import loginpractice.jwt.member_jwt.domain.MemberJwt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJwtRepository extends JpaRepository<MemberJwt, Long> {

    Optional<MemberJwt> findByName(String name);
}
