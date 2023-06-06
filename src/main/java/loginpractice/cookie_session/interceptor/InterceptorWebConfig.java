package loginpractice.cookie_session.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class InterceptorWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("addInterceptors");
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/interceptor/**")
                .excludePathPatterns("/interceptor/signup", "/interceptor/login");
    }
}
