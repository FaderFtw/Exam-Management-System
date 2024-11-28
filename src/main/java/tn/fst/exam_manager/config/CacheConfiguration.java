package tn.fst.exam_manager.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, tn.fst.exam_manager.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, tn.fst.exam_manager.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, tn.fst.exam_manager.domain.User.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Authority.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.User.class.getName() + ".authorities");
            createCache(cm, tn.fst.exam_manager.domain.Classroom.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Classroom.class.getName() + ".departments");
            createCache(cm, tn.fst.exam_manager.domain.Department.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Department.class.getName() + ".institutes");
            createCache(cm, tn.fst.exam_manager.domain.Department.class.getName() + ".examSessions");
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName() + ".classrooms");
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName() + ".studentClasses");
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName() + ".sessions");
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName() + ".supervisors");
            createCache(cm, tn.fst.exam_manager.domain.ExamSession.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.ExamSession.class.getName() + ".departments");
            createCache(cm, tn.fst.exam_manager.domain.Institute.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Major.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Major.class.getName() + ".departments");
            createCache(cm, tn.fst.exam_manager.domain.ProfessorDetails.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.ProfessorDetails.class.getName() + ".supervisedExams");
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName() + ".professors");
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName() + ".examSessions");
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName() + ".departments");
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName() + ".institutes");
            createCache(cm, tn.fst.exam_manager.domain.SessionType.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.StudentClass.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.StudentClass.class.getName() + ".majors");
            createCache(cm, tn.fst.exam_manager.domain.TeachingSession.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.TeachingSession.class.getName() + ".timetables");
            createCache(cm, tn.fst.exam_manager.domain.TeachingSession.class.getName() + ".studentClasses");
            createCache(cm, tn.fst.exam_manager.domain.TeachingSession.class.getName() + ".classrooms");
            createCache(cm, tn.fst.exam_manager.domain.Timetable.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Timetable.class.getName() + ".professors");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
