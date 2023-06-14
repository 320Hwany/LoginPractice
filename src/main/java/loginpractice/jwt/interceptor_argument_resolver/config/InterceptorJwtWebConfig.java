package loginpractice.jwt.interceptor_argument_resolver.config;

import loginpractice.jwt.member_jwt.repository.MemberJwtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InterceptorJwtWebConfig implements WebMvcConfigurer {

    private final MemberJwtRepository memberJwtRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("addJwtInterceptors ");
        registry.addInterceptor(new MemberJwtInterceptor(memberJwtRepository))
                .order(1)
                .addPathPatterns("/jwt/interceptor/**")
                .excludePathPatterns("/jwt/interceptor/signup", "/jwt/interceptor/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberJwtArgumentResolver());
    }
}
