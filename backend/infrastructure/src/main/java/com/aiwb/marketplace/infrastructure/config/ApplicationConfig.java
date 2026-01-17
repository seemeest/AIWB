package com.aiwb.marketplace.infrastructure.config;

import com.aiwb.marketplace.application.auth.AuthService;
import com.aiwb.marketplace.application.ports.EmailVerificationTokenRepository;
import com.aiwb.marketplace.application.ports.ImageStorage;
import com.aiwb.marketplace.application.ports.LoginAuditEventPublisher;
import com.aiwb.marketplace.application.ports.LoginAuditRepository;
import com.aiwb.marketplace.application.ports.PasswordHasher;
import com.aiwb.marketplace.application.ports.PasswordResetTokenRepository;
import com.aiwb.marketplace.application.ports.RefreshTokenRepository;
import com.aiwb.marketplace.application.ports.TokenService;
import com.aiwb.marketplace.application.ports.UserRepository;
import com.aiwb.marketplace.application.ports.NotificationRepository;
import com.aiwb.marketplace.application.ports.EmailSender;
import com.aiwb.marketplace.application.ports.ProductRepository;
import com.aiwb.marketplace.application.ports.ProductSearchIndex;
import com.aiwb.marketplace.application.ports.OrderRepository;
import com.aiwb.marketplace.application.ports.PaymentService;
import com.aiwb.marketplace.application.ports.DeliveryRepository;
import com.aiwb.marketplace.application.ports.ComplaintRepository;
import com.aiwb.marketplace.application.ports.ModerationActionRepository;
import com.aiwb.marketplace.application.ports.BlockRepository;
import com.aiwb.marketplace.application.ports.AppealRepository;
import com.aiwb.marketplace.application.ports.BlockQueryRepository;
import com.aiwb.marketplace.application.ports.MetricsRepository;
import com.aiwb.marketplace.application.product.ProductService;
import com.aiwb.marketplace.application.order.OrderService;
import com.aiwb.marketplace.application.moderation.ModerationService;
import com.aiwb.marketplace.application.search.SearchService;
import com.aiwb.marketplace.application.notification.NotificationService;
import com.aiwb.marketplace.application.metrics.MetricsService;
import com.aiwb.marketplace.infrastructure.security.BCryptPasswordHasher;
import com.aiwb.marketplace.infrastructure.security.JwtTokenService;
import com.aiwb.marketplace.infrastructure.storage.LocalImageStorage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties({
        JwtProperties.class,
        AuthProperties.class,
        StorageProperties.class,
        SearchProperties.class,
        ModerationProperties.class,
        GeoIpProperties.class,
        KafkaTopicsProperties.class
})
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public PasswordHasher passwordHasher() {
        return new BCryptPasswordHasher();
    }

    @Bean
    public TokenService tokenService(JwtProperties jwtProperties, Clock clock) {
        return new JwtTokenService(jwtProperties, clock);
    }

    @Bean
    public ImageStorage imageStorage(StorageProperties storageProperties) {
        return new LocalImageStorage(storageProperties.rootPath());
    }

    @Bean
    public AuthService authService(UserRepository userRepository,
                                   RefreshTokenRepository refreshTokenRepository,
                                   EmailVerificationTokenRepository verificationTokenRepository,
                                   PasswordResetTokenRepository passwordResetTokenRepository,
                                   LoginAuditRepository loginAuditRepository,
                                   LoginAuditEventPublisher loginAuditEventPublisher,
                                   TokenService tokenService,
                                   PasswordHasher passwordHasher,
                                   Clock clock,
                                   AuthProperties authProperties,
                                   NotificationService notificationService) {
        return new AuthService(
                userRepository,
                refreshTokenRepository,
                verificationTokenRepository,
                passwordResetTokenRepository,
                loginAuditRepository,
                loginAuditEventPublisher,
                tokenService,
                passwordHasher,
                clock,
                authProperties.verificationTokenTtl(),
                authProperties.passwordResetTokenTtl(),
                notificationService
        );
    }

    @Bean
    public NotificationService notificationService(NotificationRepository notificationRepository,
                                                   EmailSender emailSender,
                                                   UserRepository userRepository,
                                                   Clock clock) {
        return new NotificationService(notificationRepository, emailSender, userRepository, clock);
    }

    @Bean
    public MetricsService metricsService(MetricsRepository metricsRepository, Clock clock) {
        return new MetricsService(metricsRepository, clock);
    }

    @Bean
    public ProductService productService(ProductRepository productRepository,
                                         ImageStorage imageStorage,
                                         ProductSearchIndex productSearchIndex,
                                         Clock clock) {
        return new ProductService(productRepository, imageStorage, productSearchIndex, clock);
    }

    @Bean
    public SearchService searchService(ProductSearchIndex productSearchIndex) {
        return new SearchService(productSearchIndex);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository,
                                     PaymentService paymentService,
                                     DeliveryRepository deliveryRepository,
                                     Clock clock,
                                     NotificationService notificationService) {
        return new OrderService(orderRepository, paymentService, deliveryRepository, clock, notificationService);
    }

    @Bean
    public ModerationService moderationService(ComplaintRepository complaintRepository,
                                               ModerationActionRepository actionRepository,
                                               BlockRepository blockRepository,
                                               BlockQueryRepository blockQueryRepository,
                                               AppealRepository appealRepository,
                                               Clock clock,
                                               ModerationProperties moderationProperties,
                                               NotificationService notificationService) {
        return new ModerationService(
                complaintRepository,
                actionRepository,
                blockRepository,
                blockQueryRepository,
                appealRepository,
                clock,
                moderationProperties.appealWindow(),
                notificationService
        );
    }
}
