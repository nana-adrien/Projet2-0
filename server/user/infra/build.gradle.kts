plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    // Exposé en api : les repositories de ce module implémentent JpaRepository, donc tout module
    // qui dépend de :user:infra (même en implementation) a besoin de ce type sur son classpath de
    // compilation pour résoudre les méthodes héritées (findById, save, etc.).
    api(libs.spring.boot.starter.data.jpa)
}
