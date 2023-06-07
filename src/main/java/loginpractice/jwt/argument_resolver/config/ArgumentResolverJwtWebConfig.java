package loginpractice.jwt.argument_resolver.config;

import loginpractice.jwt.member_jwt.repository.MemberJwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ArgumentResolverJwtWebConfig implements WebMvcConfigurer {

    private final MemberJwtRepository memberJwtRepository;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberJwtArgumentResolver(memberJwtRepository));
    }
}
