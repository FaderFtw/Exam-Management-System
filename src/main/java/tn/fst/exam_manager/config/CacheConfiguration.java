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

    private static final String SUFFIX_DEPARTMENTS = ".departments";
    private static final String SUFFIX_AUTHORITIES = ".authorities";
    private static final String SUFFIX_INSTITUTES = ".institutes";
    private static final String SUFFIX_EXAM_SESSIONS = ".examSessions";
    private static final String SUFFIX_CLASSROOMS = ".classrooms";
    private static final String SUFFIX_STUDENT_CLASSES = ".studentClasses";
    private static final String SUFFIX_SESSIONS = ".sessions";
    private static final String SUFFIX_SUPERVISORS = ".supervisors";
    private static final String SUFFIX_MAJORS = ".majors";
    private static final String SUFFIX_TIMETABLES = ".timetables";
    private static final String SUFFIX_PROFESSORS = ".professors";
    private static final String SUFFIX_USERS = ".users";

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
            createCache(cm, tn.fst.exam_manager.domain.User.class.getName() + SUFFIX_AUTHORITIES);
            createCache(cm, tn.fst.exam_manager.domain.Classroom.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Classroom.class.getName() + SUFFIX_DEPARTMENTS);
            createCache(cm, tn.fst.exam_manager.domain.Department.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Department.class.getName() + SUFFIX_INSTITUTES);
            createCache(cm, tn.fst.exam_manager.domain.Department.class.getName() + SUFFIX_EXAM_SESSIONS);
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName() + SUFFIX_CLASSROOMS);
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName() + SUFFIX_STUDENT_CLASSES);
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName() + SUFFIX_SESSIONS);
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName() + SUFFIX_SUPERVISORS);
            createCache(cm, tn.fst.exam_manager.domain.ExamSession.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.ExamSession.class.getName() + SUFFIX_DEPARTMENTS);
            createCache(cm, tn.fst.exam_manager.domain.Institute.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Major.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Major.class.getName() + SUFFIX_DEPARTMENTS);
            createCache(cm, tn.fst.exam_manager.domain.ProfessorDetails.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.ProfessorDetails.class.getName() + SUFFIX_SUPERVISORS);
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName() + SUFFIX_PROFESSORS);
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName() + SUFFIX_EXAM_SESSIONS);
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName() + SUFFIX_DEPARTMENTS);
            createCache(cm, tn.fst.exam_manager.domain.Report.class.getName() + SUFFIX_INSTITUTES);
            createCache(cm, tn.fst.exam_manager.domain.SessionType.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.StudentClass.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.StudentClass.class.getName() + SUFFIX_MAJORS);
            createCache(cm, tn.fst.exam_manager.domain.TeachingSession.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.TeachingSession.class.getName() + SUFFIX_TIMETABLES);
            createCache(cm, tn.fst.exam_manager.domain.TeachingSession.class.getName() + SUFFIX_STUDENT_CLASSES);
            createCache(cm, tn.fst.exam_manager.domain.TeachingSession.class.getName() + SUFFIX_CLASSROOMS);
            createCache(cm, tn.fst.exam_manager.domain.Timetable.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Timetable.class.getName() + SUFFIX_PROFESSORS);
            createCache(cm, tn.fst.exam_manager.domain.Department.class.getName() + SUFFIX_USERS);
            createCache(cm, tn.fst.exam_manager.domain.Institute.class.getName() + SUFFIX_USERS);
            createCache(cm, tn.fst.exam_manager.domain.UserAcademicInfo.class.getName());
            createCache(cm, tn.fst.exam_manager.domain.Department.class.getName() + ".examSessions");
            createCache(cm, tn.fst.exam_manager.domain.Department.class.getName() + ".users");
            createCache(cm, tn.fst.exam_manager.domain.Exam.class.getName() + ".supervisors");
            createCache(cm, tn.fst.exam_manager.domain.ExamSession.class.getName() + ".departments");
            createCache(cm, tn.fst.exam_manager.domain.Institute.class.getName() + ".users");
            createCache(cm, tn.fst.exam_manager.domain.ProfessorDetails.class.getName() + ".supervisedExams");
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
